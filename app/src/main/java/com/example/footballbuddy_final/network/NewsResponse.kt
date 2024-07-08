package com.example.footballbuddy_final.network

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
