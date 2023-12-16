package com.crysure.reflvy

import com.crysure.reflvy.model.OpenAIRequestModel
import com.crysure.reflvy.model.OpenAIResponseModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
object OpenAIAPIClient {
    private const val BASE_URL = "https://api.openai.com/v1/"
    interface OpenAIAPIService {
        @Headers("Authorization: Bearer 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")
        @POST("chat/completions")
        fun getCompletion(@Body requestModel: OpenAIRequestModel): Call<OpenAIResponseModel>
    }
    fun create(): OpenAIAPIService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(OpenAIAPIService::class.java)
    }
}