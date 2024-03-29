package com.reezkyillma.projectandroid

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.HashMap
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    internal lateinit var username: EditText
    internal lateinit var fullname: EditText
    internal lateinit var email: EditText
    internal lateinit var password: EditText
    internal lateinit var register: Button
    internal lateinit var txt_login: TextView

    internal lateinit var auth: FirebaseAuth
    internal lateinit var reference: DatabaseReference
    internal lateinit var pd: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        fullname = findViewById(R.id.fullname)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register)
        txt_login = findViewById(R.id.txt_login)

        auth = FirebaseAuth.getInstance()

        txt_login.setOnClickListener { startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)) }

        fun emailValidation():Boolean{
            val email = email.text.toString()
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }
        register.setOnClickListener {


            val str_username = username.text.toString()
            val str_fullname = fullname.text.toString()
            val str_email = email.text.toString()
            val str_password = password.text.toString()
            val isEmailValid = emailValidation()
            println("EMAIL IS VALID ? " + isEmailValid)

            if (TextUtils.isEmpty(str_fullname)) {
                Toast.makeText(this@RegisterActivity, "Full Name are required!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(str_username)) {
                Toast.makeText(this@RegisterActivity, "Username are required!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(str_email)) {
                Toast.makeText(this@RegisterActivity, "Email are required!", Toast.LENGTH_SHORT).show()
            } else if(isEmailValid != true){
                Toast.makeText(this@RegisterActivity, "Please input your email Correctly", Toast.LENGTH_SHORT).show()
            }else if (TextUtils.isEmpty(str_password)) {
                Toast.makeText(this@RegisterActivity, "Password are required!", Toast.LENGTH_SHORT).show()
            } else if (str_password.length < 6) {
                Toast.makeText(this@RegisterActivity, "Password must have 6 characters!", Toast.LENGTH_SHORT).show()
            } else {
                pd = ProgressDialog(this@RegisterActivity)
                pd.setMessage("Please wait...")
                pd.show()
                register(str_username, str_fullname, str_email, str_password)
            }
        }
    }

    fun register(username: String, fullname: String, email: String, password: String) {
        println("USERNAME : " + username)
        println("FULLNAME :" + fullname)
        println("EMAIL :" +  email)
        println("PASSWORD : " + password)
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@RegisterActivity) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val userID = firebaseUser!!.uid

                        reference = FirebaseDatabase.getInstance().reference.child("Users").child(userID)
                        val map = HashMap<String, Any>()
                        map["id"] = userID
                        map["username"] = username.toLowerCase()
                        map["fullname"] = fullname
                        map["imageurl"] = "https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49"
                        map["bio"] = ""

                        reference.setValue(map).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                pd.dismiss()
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                    } else {
                        pd.dismiss()
                        Toast.makeText(this@RegisterActivity, "Email and password already registered !", Toast.LENGTH_SHORT).show()
                    }
                }


    }
}
