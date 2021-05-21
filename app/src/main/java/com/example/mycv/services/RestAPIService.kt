package com.example.mycv.services

import com.example.mycv.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestAPIService {
    fun createUserAccount(userData: UserModel, onResult: (UserModel?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(ApiRequests::class.java)
        retrofit.signUp(userData).enqueue(
            object : Callback<UserModel> {
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}