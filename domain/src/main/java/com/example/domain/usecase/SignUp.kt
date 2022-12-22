package com.example.domain.usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class SignUp (private  val repository : AppRepository){
    suspend operator fun invoke(email: String, password: String): Flow<Result<Unit>> {
        return repository.signUp(email, password)
    }
}