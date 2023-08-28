package com.example.reflvy.data

data class VideoItem(
    val snippet: Snippet,
    val statistics: Statistics,
    val contentDetails: ContentDetails
)

data class Snippet(
    val title: String,
    val channelTitle: String,
    val publishedAt: String,
    val thumbnails: Thumbnails // Assuming Thumbnails is a class containing different thumbnail types
)

data class Statistics(
    val viewCount: String
)

data class ContentDetails(
    val duration: String
)

data class Thumbnails(
    val high: ThumbnailType,
    // Add other thumbnail types if available (medium, high, etc.)
)

data class ThumbnailType(
    val url: String
)

data class VideoResponse(val items: List<VideoItem>)
