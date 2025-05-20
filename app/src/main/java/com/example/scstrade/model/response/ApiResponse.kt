package com.example.scstrade.model.response

data class ApiResponse<T>(
    val isSuccess: Boolean,
    val statusCode: Int,
    val message: String,
    val data: T?,
    val status: Int
)
