package com.example.phantomtroupe

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // Fetches products - we will pick a random one from the list
    @GET("https://dummyjson.com/products")
    fun getCareers(): Call<CareerResponse>

    @GET("https://api.adviceslip.com/advice")
    fun getAdvice(): Call<AdviceResponse>
}