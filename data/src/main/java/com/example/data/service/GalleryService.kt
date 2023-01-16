package com.example.data.service

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.example.domain.core.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GalleryService @Inject constructor(private val context: Context) {

    fun getGalleryList(perPage: Int, start: Int): Flow<Result<List<Uri>>> = callbackFlow {
        send(Result.Loading())

        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )

        var cursor: Cursor? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val selectionBundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_OFFSET, start)
                putInt(ContentResolver.QUERY_ARG_LIMIT, perPage)
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED)
                )
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, null)
                putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, null)

            }
            cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selectionBundle,
                null
            )
        } else {
            cursor = context.contentResolver
                .query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                    "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC LIMIT $perPage OFFSET $start"
                )
        }

        Timber.tag("start->").d("end cursor: ")

//        val imageList = mutableListOf<Bitmap>()
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
//                Timber.tag("start->").d("imag : ${imageUrlList[0]}")
            }
            trySend(Result.Success(imageUrlList.toList()))
        }

        awaitClose {
            channel.close()
        }
    }


    fun galleryList (perPage: Int, start: Int): List<Uri> {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )

        var cursor: Cursor? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val selectionBundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_OFFSET, start)
                putInt(ContentResolver.QUERY_ARG_LIMIT, perPage)
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED)
                )
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, null)
                putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, null)

            }
            cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selectionBundle,
                null
            )
        } else {
            cursor = context.contentResolver
                .query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                    "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC LIMIT $perPage OFFSET $start"
                )
        }

        Timber.tag("start->").d("end cursor: ")

//        val imageList = mutableListOf<Bitmap>()
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
//                Timber.tag("start->").d("imag : ${imageUrlList[0]}")
            }
            return imageUrlList.toList()
        }
    }
}