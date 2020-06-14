package com.example.flightmobileapp
//import android.support.v8.1.app.AppCompatActivity


import Api
import android.graphics.BitmapFactory
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_game.*
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.*


class GameActivity : AppCompatActivity() {
    private var aileron : Double = 0.0
    private var elevator : Double = 0.0
    private var throttle: Double = 0.0
    private var rudder: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:52686")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val I = response.body()?.byteStream()
                val B = BitmapFactory.decodeStream(I)
                runOnUiThread {
                    SimulatorView.setImageBitmap(B)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                print("basa")
            }
        })
        setContentView(R.layout.activity_game)
        //val joystick = findViewById(R.id.joystick) as JoystickView
        joystick.setOnMoveListener { angle, strength ->
            val rad = toRadians(angle + 0.0)
            aileron = kotlin.math.cos(rad)
            elevator = kotlin.math.sin(rad)
            aileron = (aileron * strength) / 100
            elevator = (elevator * strength) / 100
            // send to server
        }
    }
}