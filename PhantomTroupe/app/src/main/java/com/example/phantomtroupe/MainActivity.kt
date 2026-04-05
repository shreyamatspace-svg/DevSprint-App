package com.example.phantomtroupe

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val btnFindJob = findViewById<Button>(R.id.btnFindJob)
        val recruiterImage = findViewById<ImageView>(R.id.recruiterImage)
        val jobResult = findViewById<TextView>(R.id.jobResult)
        val adviceResult = findViewById<TextView>(R.id.adviceResult)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnFindJob.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isEmpty()) return@setOnClickListener

            // Show recruiter
            recruiterImage.visibility = View.VISIBLE
            Picasso.get().load("https://robohash.org/$name?set=set3").into(recruiterImage)

            // 1. Get Random Career
            apiService.getCareers().enqueue(object : Callback<CareerResponse> {
                override fun onResponse(call: Call<CareerResponse>, response: Response<CareerResponse>) {
                    val products = response.body()?.products
                    if (!products.isNullOrEmpty()) {
                        val randomJob = products[Random.nextInt(products.size)].title
                        jobResult.text = "Congratulations $name!\nYour new career is: $randomJob"
                    }
                }
                override fun onFailure(call: Call<CareerResponse>, t: Throwable) {}
            })

            // 2. Get Random Advice
            apiService.getAdvice().enqueue(object : Callback<AdviceResponse> {
                override fun onResponse(call: Call<AdviceResponse>, response: Response<AdviceResponse>) {
                    adviceResult.text = "Expert Advice: \"${response.body()?.slip?.advice}\""
                }
                override fun onFailure(call: Call<AdviceResponse>, t: Throwable) {}
            })
        }
    }
}