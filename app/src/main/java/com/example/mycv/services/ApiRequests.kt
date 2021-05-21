package com.example.mycv.services

import com.example.mycv.models.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiRequests {
    @POST("sign-up")
    fun signUp(@Body userData: UserModel): Call<UserModel>
}