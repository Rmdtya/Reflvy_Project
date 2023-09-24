package com.example.reflvy.model

import com.google.gson.annotations.SerializedName

data class MessageAI(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class OpenAIRequestModel(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<MessageAI>,
    @SerializedName("temperature") val temperature: Float
)

