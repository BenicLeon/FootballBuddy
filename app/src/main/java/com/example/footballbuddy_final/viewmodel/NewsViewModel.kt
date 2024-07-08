package com.example.footballbuddy_final.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballbuddy_final.network.Article
import com.example.footballbuddy_final.network.NewsApi
import kotlinx.coroutines.launch


class NewsViewModel : ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    fun fetchNews(clubName: String) {
        viewModelScope.launch {



            val response = NewsApi.retrofitService.getNews("$clubName football")
            if (response.isSuccessful) {
                _articles.value = response.body()?.articles ?: emptyList()
                Log.d("NewsViewModel", "Fetched news successfully")
            } else {
                Log.e("NewsViewModel", "Failed to fetch news: ${response.errorBody()}")


            }
        }
    }

}
