package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.data.mapper.RoomChatMapper
import com.example.data.model.*
import com.example.data.repository.dataSource.GalleryDataSource
import com.example.data.repository.dataSource.TestRemoteDataSource
import com.example.data.service.PushService
import com.example.domain.core.Result
import com.example.domain.model.RoomChat
import com.example.domain.model.RoomInfo
import com.example.domain.repository.AppRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val TestRemoteSource: TestRemoteDataSource,
    private val context: Context,
    private val pushService: PushService,
    private val galleryDataSource: GalleryDataSource
) : AppRepository {
    override suspend fun signUp(email: String, password: String): Flow<Result<Unit>> =
        callbackFlow {
            send(Result.Loading())
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                if (it.user != null) {
                    trySend(Result.Success(Unit))
                }
            }.addOnFailureListener {
                trySend(Result.Error(it))
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun signIn(email: String, password: String): Flow<Result<Unit>> =
        callbackFlow {
            send(Result.Loading())
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                if (it.user != null) {
                    trySend(Result.Success(Unit))
                }
            }.addOnFailureListener {
                trySend(Result.Error(it))
            }
            awaitClose {
                channel.close()
            }
        }


    override suspend fun signInWithCredential(idToken: String): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
            if (it.user != null) {
                it.user!!.getIdToken(true).addOnSuccessListener { result ->
                    val token = result.token.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            TestRemoteSource.googleLogin(token)
                        }.onSuccess {
                            trySend(Result.Success(Unit))
                        }.onFailure { e ->
                            trySend(Result.Error(Exception(e)))
                        }
                    }
                }
            }
        }.addOnFailureListener {
            trySend(Result.Error(it))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun googleAutoLogIn(): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())
        val user = auth.currentUser

        if (user != null) {
            runCatching {
                TestRemoteSource.getUserInfo(user.uid)
            }.onSuccess {
                trySend(Result.Success(Unit))
            }.onFailure { e ->
                trySend(Result.Error(Exception(e)))
            }
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun googleSignIn(): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())
    }

    override suspend fun updateUserProfileToFirebaseDb(userName: String): Flow<Result<Unit>> =
        callbackFlow {
            send(Result.Loading())
            val currentUserUid = auth.currentUser//?.uid.toString()

            val databaseReference =
                firebaseDatabase.getReference("userInfo").child(currentUserUid?.uid.toString())
            databaseReference
                .setValue(
                    UserProfile(
                        name = currentUserUid?.displayName,
                        imageUrl = currentUserUid?.photoUrl.toString(),
                        email = currentUserUid?.email.toString()
                    )
                )
                .addOnSuccessListener {
                    trySend(Result.Success(Unit))
                }
                .addOnFailureListener {
                    trySend(Result.Error(it))
                }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun initUserProfileInfo(): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())
        val currentUserUid = auth.currentUser//?.uid.toString()
        val databaseReference =
            firebaseDatabase.getReference("userInfo").child(currentUserUid?.uid.toString())
//        Timber.d("test ==> ${getSharedPreferences("FCM_USER_TOKEN")}")
        databaseReference
            .setValue(
                UserProfile(
                    name = currentUserUid?.displayName,
                    imageUrl = currentUserUid?.photoUrl.toString(),
                    email = currentUserUid?.email.toString(),
                    fcmToken = "" //추후에 수정하자...
                )
            )
            .addOnSuccessListener {
                trySend(Result.Success(Unit))
            }
            .addOnFailureListener {
                trySend(Result.Error(it))
            }
        awaitClose {
            channel.close()
        }
    }

//    private fun getSharedPreferences(key: String) : String = sharedPref.getString(key, "").toString()


    override suspend fun addUserId(userId: String): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())
        val currentUser = auth.currentUser
        val currentUserUid = currentUser?.uid.toString()//?.uid.toString()

        val databaseReference = firebaseDatabase.getReference("userInfo")
//            .child((userId))
        databaseReference.get().addOnSuccessListener {
            val checkUser = it.children.find { it.child("email").value.toString() == userId }
            if (checkUser?.exists() == true) {
                checkUser.let {
                    val otherUserId = checkUser.key.toString()
                    val list = swapId(currentUserUid, otherUserId)
                    val tempCurrentUserId = list.first
                    val tempOtherUserId = list.second
//                    val userEmail = checkUser.child("email").value.toString()
//                    val databaseRef = firebaseDatabase.getReference("userRoomInfo").child(currentUserUid).child("@make@$currentUserUid@${checkUser.key}")
                    val databaseRef = firebaseDatabase.getReference("userRoomInfo")
                    val databaseMy = databaseRef.child(currentUserUid)
                        .child("@make@$tempCurrentUserId@${tempOtherUserId}")
                    val databaseOther = databaseRef.child(otherUserId)
                        .child("@make@$tempCurrentUserId@${tempOtherUserId}")
                    databaseMy.setValue(
                        UserRoomInfo(
                            room_img = checkUser.child("imageUrl").value.toString(),
                            room_title = checkUser.child("email").value.toString(),
                            room_name = checkUser.child("name").value.toString(),
                            room_id = "@make@$tempCurrentUserId@${tempOtherUserId}"
                        )
                    )
                    databaseOther.setValue(
                        UserRoomInfo(
                            room_img = currentUser?.photoUrl.toString(),
                            room_title = currentUser?.email.toString(),
                            room_name = currentUser?.displayName.toString(),
                            room_id = "@make@$tempCurrentUserId@${tempOtherUserId}"
                        )
                    )
                    firebaseDatabase.getReference("roomInfo")
                        .child("@make@$tempCurrentUserId@${tempOtherUserId}").setValue(
                        ""
                    )
                }
                trySend(Result.Success(Unit))
            } else trySend(Result.Error(NullPointerException()))

        }.addOnFailureListener {
            trySend(Result.Error(it))
        }

        awaitClose {
            channel.close()
        }
    }

    override fun updateUserFcmToken(token: String) {
        val currentUserUid = auth.currentUser//?.uid.toString()
        val databaseReference =
            firebaseDatabase.getReference("userInfo").child(currentUserUid?.uid.toString())
        val updateFcm = HashMap<String, Any>()
        updateFcm["fcmToken"] = token
        databaseReference.updateChildren(updateFcm)

    }

    override fun getRoomChat(roomId: String): Flow<Result<List<RoomChat>>> = callbackFlow {
        send(Result.Loading())
        val databaseReference = firebaseDatabase.getReference("roomInfo").child(roomId)
        val currentUserUid = auth.currentUser?.uid.toString()//?.uid.toString()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userRoomChatList = listOf<RoomChat>()

                snapshot.children.forEach { snapshotChild ->
                    val firebaseUserItem =
                        snapshotChild.getValue(com.example.data.model.RoomChat::class.java)
                    val firebaseItem = RoomChatMapper.mapperToRoomChat(firebaseUserItem!!)
                    firebaseItem.messageSender =
                        if (firebaseItem.messageId == currentUserUid) "me" else "you"
                    firebaseItem.messageKey = snapshotChild.key.toString()
                    firebaseItem.let {
                        userRoomChatList = userRoomChatList + it
                    }

                }
                trySend(Result.Success(userRoomChatList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.Error(Exception(error.message)))
            }
        })

        awaitClose {
            channel.close()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendChat(message: String, roomId: String): Flow<Result<Unit>> =
        callbackFlow {
            send(Result.Loading())
            val currentUser = auth.currentUser
            val databaseReference = firebaseDatabase.getReference("roomInfo")
            databaseReference.child(roomId).push().setValue(
                com.example.data.model.RoomChat(
                    message_id = currentUser?.uid.toString(),
                    message = message,
                    message_date = simpleDateFormat(LocalDateTime.now()),
                    message_type = "text",
                    message_sender_image = currentUser?.photoUrl.toString(),
                    message_sender_name = currentUser?.displayName.toString()
                )
            )

            var strlist = roomId.trim().split("@")
            strlist = strlist.filterNot { str ->
                str == currentUser?.uid.toString().trim()
            }.filterNot { str ->
                str == "make"
            }
            if (strlist.size > 1) {
                val databaseUserReference = firebaseDatabase.getReference("userInfo")
                var sendPushToken = ""
                databaseUserReference.child(strlist[1]).get().addOnSuccessListener {
                    sendPushToken = it.child("fcmToken").value.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            pushService.sendPush(
                                SendDTO(currentUser?.displayName.toString(), message, sendPushToken)
                            )
                        } catch (e: HttpException) {
                            trySend(Result.Error(e))
                        }

                        trySend(Result.Success(Unit))
                    }
                }
            } else trySend(Result.Success(Unit))


            awaitClose {
                channel.close()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun sendImage(message: String, roomId: String): Flow<Result<Unit>> =
        callbackFlow {
            send(Result.Loading())
            val currentUser = auth.currentUser
            val databaseReference = firebaseDatabase.getReference("roomInfo")
            uploadImage(message).collect() {
                when (it) {
                    is Result.Success -> {
                        databaseReference.child(roomId).push().setValue(
                            com.example.data.model.RoomChat(
                                message_id = currentUser?.uid.toString(),
                                message = it.data,
                                message_date = simpleDateFormat(LocalDateTime.now()),
                                message_type = "photo",
                                message_sender_image = currentUser?.photoUrl.toString(),
                                message_sender_name = currentUser?.displayName.toString()
                            )
                        )

                        var strlist = roomId.trim().split("@")
                        strlist = strlist.filterNot { str ->
                            str == currentUser?.uid.toString().trim()
                        }.filterNot { str ->
                            str == "make"
                        }
                        if (strlist.size > 1) {
                            val databaseUserReference = firebaseDatabase.getReference("userInfo")
                            var sendPushToken = ""
                            databaseUserReference.child(strlist[1]).get().addOnSuccessListener {
                                sendPushToken = it.child("fcmToken").value.toString()
                                CoroutineScope(Dispatchers.IO).launch {
                                    pushService.sendPush(
                                        SendDTO(
                                            currentUser?.displayName.toString(),
                                            "사진이 전송되었습니다.",
                                            sendPushToken
                                        )
                                    )
                                    trySend(Result.Success(Unit))
                                }
                            }
                        } else {
                            trySend(Result.Success(Unit))
                        }
                    }
                }
            }
//        databaseReference.child(roomId).push().setValue(
//            com.example.data.model.RoomChat(
//                message_id = currentUser?.uid.toString(),
//                message = message,
//                message_date = simpleDateFormat(LocalDateTime.now()),
//                message_type = "photo",
//                message_sender_image = currentUser?.photoUrl.toString(),
//                message_sender_name = currentUser?.displayName.toString()
//            )
//        )

//        var strlist = roomId.trim().split("@")
//        Timber.d("test start@ ==> $strlist")
//        strlist = strlist.filterNot { str ->
//            str == currentUser?.uid.toString().trim()
//        }.filterNot { str ->
//            str == "make"
//        }
//        if (strlist.size > 1) {
//            Timber.d("test end@ ==> ${strlist[1]}")
//        } else Timber.d("test end@ ==> it.me")

            trySend(Result.Success(Unit))

            awaitClose {
                channel.close()
            }
        }

    override fun getGalleryList(): Flow<Result<List<Uri>>> = callbackFlow {
        send(Result.Loading())
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )
        val cursor = context.contentResolver
            .query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
            )
        val imageList = mutableListOf<Bitmap>()
        val imageUrlList = mutableListOf<Uri>()
        cursor.use { imageCursor ->
            while (imageCursor!!.moveToNext() /*&& imageList.size <= 5*/) {
                val latestImageUri = imageCursor.getString(1)
                val imageFile = File(latestImageUri)
                if (imageFile.exists()) {
                    imageUrlList.add(Uri.parse("file://$latestImageUri"))
//                    imageList.add(BitmapFactory.decodeFile(latestImageUri))
//                camera_imgView_album.setImageBitmap(bitmap)
                }

            }
            trySend(Result.Success(imageUrlList.toList()))
        }

        awaitClose {
            channel.close()
        }
    }

    override fun galleryList() = galleryDataSource.getGalleryData()

    override suspend fun removeChat(messageId: String, roomId: String): Flow<Result<Unit>> =
        callbackFlow {
            send(Result.Loading())
            try {
                val databaseReference =
                    firebaseDatabase.getReference("roomInfo").child(roomId).child(messageId)
                databaseReference.removeValue()
                trySend(Result.Success(Unit))
            } catch (e: Exception) {
                trySend(Result.Error(e))
            }

            awaitClose {
                channel.close()
            }
        }

    override fun getTestData() = TestRemoteSource.getTestData()
//    override suspend fun getImageList(): Flow<Result<Unit>> = callbackFlow {
//        send(Result.Loading())
//
//        awaitClose {
//            channel.close()
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun simpleDateFormat(date: LocalDateTime) =
        date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

//    fun test(){
//        val currentUserUid = auth.currentUser?.uid.toString()
//        val projection = arrayOf(
//            MediaStore.Images.ImageColumns._ID,
//            MediaStore.Images.ImageColumns.DATA,
//            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.ImageColumns.DATE_TAKEN,
//            MediaStore.Images.ImageColumns.MIME_TYPE
//        )
//        val cursor = context.contentResolver
//            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
//                "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC")
//        val imageList = mutableListOf<Bitmap>()
//        val latestImageUriList = mutableListOf<Uri>()
//        cursor.use { imageCursor ->
//            while (imageCursor!!.moveToNext() && imageList.size <= 5) {
//                val latestImageUri = imageCursor.getString(1)
//                val imageFile = File(latestImageUri)
//                latestImageUriList.add(Uri.parse("file://$latestImageUri"))
//
//                if (imageFile.exists()) {
//                    imageList.add(BitmapFactory.decodeFile(latestImageUri))
////                camera_imgView_album.setImageBitmap(bitmap)
//                }
//            }
//        }
//        val imageName = "$currentUserUid/${UUID.randomUUID()}.jpg"
//        val storageRef = firebaseStorage.reference.child(imageName)
//
//        storageRef.putFile(latestImageUriList[2]).addOnCompleteListener { task ->
//            if(task.isSuccessful){
//                storageRef.downloadUrl.addOnSuccessListener {
//                    Timber.tag("TEST--->").d("TESTSUCCES${it}")
//                }
//            }else{
//                Timber.tag("TEST--->").d("TESTEXCEPTION")
//            }
//        }
//    }

    fun uploadImage(path: String): Flow<Result<String>> = callbackFlow {
        send(Result.Loading())
        val currentUserUid = auth.currentUser?.uid.toString()

        val imageName = "$currentUserUid/${UUID.randomUUID()}.jpg"
        val storageRef = firebaseStorage.reference.child(imageName)

        storageRef.putFile(Uri.parse(path)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener {
                    trySend(Result.Success(it.toString()))
                }
            } else {
                trySend(Result.Error(task.exception))
            }
        }


        awaitClose {
            channel.close()
        }
    }

    fun swapId(value1: String, value2: String): Pair<String, String> {
        var tamp: String? = null
        var str1 = value1
        var str2 = value2

        if (str1 < str2) {
            tamp = str1
            str1 = str2
            str2 = tamp
            return Pair(str1, str2)
        } else return Pair(str1, str2)
    }


    override fun getRoomInfo(): Flow<Result<List<RoomInfo>>> = callbackFlow {
        send(Result.Loading())
        val currentUserUid = auth.currentUser?.uid.toString()
//        test()
        val databaseReference = firebaseDatabase.getReference("userRoomInfo").child(currentUserUid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userRoomList = listOf<RoomInfo>()
                snapshot.children.forEach { snapshotChild ->
                    val firebaseUserItem = snapshotChild.getValue(RoomInfo::class.java)
                    firebaseUserItem?.let {
                        userRoomList = userRoomList + it
                    }
                }
                trySend(Result.Success(userRoomList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.Error(Exception(error.message)))
            }
        })

        awaitClose {
            channel.close()
        }

    }

    override fun signOut() {
        auth.signOut()
    }


}