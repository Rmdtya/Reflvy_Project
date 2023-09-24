package com.example.reflvy.model

import com.google.gson.annotations.SerializedName
data class OpenAIResponseModel(
    @SerializedName("id") val id: String,
    @SerializedName("object") val objectType: String,
    @SerializedName("created") val createdTimestamp: Long,
    @SerializedName("model") val modelVersion: String,
    @SerializedName("choices") val choices: Array<OpenAIChoice>,
    @SerializedName("usage") val usageInfo: UsageAI
)
data class OpenAIChoice(
    @SerializedName("message") val responseMessage: ResponseMessage,
    @SerializedName("finish_reason") val finishReason: String
)
data class ResponseMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class UsageAI(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)
