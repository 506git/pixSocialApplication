package com.example.data.repository.dataSource

import com.example.data.model.LawMaker

//interface LawLocalDataSource {
//    fun getMoviesFromDB(movieId : Int): Flow<LawMaker>
//}

interface LawMakerRemoteSource {
    suspend fun getLawMaker(): List<LawMaker>
}