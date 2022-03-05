package com.example.caption

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import java.util.*

enum class ProviderType{
    BASIC
}
class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email?:"",provider?:"")
    }
    private fun setLocate(languageToLoad:String?){
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",languageToLoad)
        editor.apply()
    }
    private fun loadLocate(){
        val sharedpref = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedpref.getString("My_Lang","")
        setLocate(language)
    }
    private fun setup(email:String,provider:String){
        title = "Inicio"
        val em = findViewById<TextView>(R.id.lblemail)
        val pro = findViewById<TextView>(R.id.lblprovider)
        val btn = findViewById<TextView>(R.id.btnlogout)
        em.text = email
        pro.text = provider
        btn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}