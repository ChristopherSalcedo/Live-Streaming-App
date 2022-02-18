package com.example.caption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC
}
class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email?:"",provider?:"")
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