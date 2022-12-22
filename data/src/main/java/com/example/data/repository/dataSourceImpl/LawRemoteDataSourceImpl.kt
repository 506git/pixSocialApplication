package com.example.data.repository.dataSourceImpl


import com.example.data.model.LawMaker
import com.example.data.repository.dataSource.LawMakerRemoteSource
import com.example.data.service.LawMakerService
import com.example.domain.model.Test
import javax.inject.Inject

//
//class LawMakerRemoteSourceImpl @Inject constructor(
//    private val lawMakerService: LawMakerService
//) : LawMakerRemoteSource {
//
//    override suspend fun getLawMaker(): Flow<PagingData<LawMaker>> {
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            remoteMediator = LawRemoteDataSource(lawMakerService)
//
//        )
//
//    }
//}
//
class LawMakerRemoteSourceImpl @Inject constructor(
    private val githubService: LawMakerService
) : LawMakerRemoteSource {

    override suspend fun getLawMaker(): List<LawMaker> {
        return githubService.getLawMaker()
    }
}