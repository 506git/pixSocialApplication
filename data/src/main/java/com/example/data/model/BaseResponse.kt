package com.example.data.model

abstract class BaseResponse<M> {
    abstract fun mapper(): M
}