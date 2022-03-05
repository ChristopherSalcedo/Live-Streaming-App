package com.example.caption
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        setup()
        lenguagefun()
    }
    private fun lenguagefun(){
        var i : Int = 0
        //val lista = arrayOf("-------","English", "Espanol")
        val lista = resources.getStringArray(R.array.strarr)
        val spinner = findViewById<Spinner>(R.id.spinner_sample)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        spinner.adapter = aa
        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                if (p2 == 1){
                    setLocate("en")
                    finish();
                    startActivity(getIntent());
                } else if(p2 == 2 ){
                    setLocate("es")
                    finish();
                    startActivity(getIntent());
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun setLocate(languageToLoad:String?){ //change language
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
    private fun loadLocate(){ //set language in shared pref an charge setlocate function
        val sharedpref = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedpref.getString("My_Lang","")
        setLocate(language)
    }
    private fun setup(){
        title = "Auth"
        val email = findViewById<EditText>(R.id.txtemail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val sendButton = findViewById<Button>(R.id.button)
        val regButton = findViewById<Button>(R.id.button2)
        sendButton.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                    } else {
                        error()
                    }

                }
            }
        }

        regButton.setOnClickListener {
            val homeIntent = Intent(this,Register::class.java)
            startActivity(homeIntent)

        }

    }
    private fun showHome(email:String,provider:ProviderType){
        val homeIntent = Intent(this,Home::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }

    private fun error() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
}