package com.app.facebookloginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.login.LoginManager

class ProfileActivity : AppCompatActivity() {
    lateinit var logout: Button
    lateinit var profilepic: ImageView
    lateinit var profilename: TextView
    lateinit var id: TextView
    lateinit var firstname : TextView
    lateinit var lastname: TextView
    lateinit var middlename: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        logout = findViewById(R.id.btn_logout)
        profilepic = findViewById(R.id.iv_profile)
        profilename = findViewById(R.id.tv_profile)
        id = findViewById(R.id.tv_id)
        firstname = findViewById(R.id.tv_firstname)
        middlename = findViewById(R.id.tv_middlename)
        lastname = findViewById(R.id.tv_lastname)

        logout.setOnClickListener {
            disconnectFromFacebook()
        }

    }

    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return // already logged out
        }

        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            object : GraphRequest.Callback {
                override fun onCompleted(graphResponse: GraphResponse) {
                    LoginManager.getInstance().logOut()
                }
            }).executeAsync()
    }


}
