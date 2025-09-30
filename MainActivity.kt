package com.app.facebookloginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btn_f: ImageView
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_f = findViewById(R.id.img_facebook)
        FacebookSdk.sdkInitialize(this@MainActivity)
        callbackManager = CallbackManager.Factory.create()

        btn_f.setOnClickListener(this)
        }


    fun onFacebookClicked() {
            LoginManager.getInstance().logInWithReadPermissions(this,listOf("public_profile", "email"))
            //Callback registration
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("TAG","Sucess Login")
                    getUserProfile(result?.accessToken!!, result.accessToken?.userId)
                }

                override fun onCancel() {
                    showMessage("Login Cancelled")
                }

                override fun onError(error: FacebookException?) {
                    showMessage(error!!.message.toString())
                }

            })
        }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)

    }

      fun getUserProfile(token: AccessToken, userId: String?) {
        val parameters = Bundle()
        parameters.putString("fields","id,first_name,middle_name, last_name,name,picture,email")
        GraphRequest(token,"/$userId/",parameters,HttpMethod.GET,GraphRequest.Callback {
            response ->
                val jsonObject = response.jsonObject
            // Facebook Access Token
            // You can see Access Token only in Debug mode.
            // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.

            if(BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true)
                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
            }
            //Facebook id
            if(jsonObject.has("id")) {
                val facebookid = jsonObject.getString("id")
                Log.i("Facebook Id: ", facebookid.toString())
            }
            else {
                Log.i("Facebook Id: ","Not Exists")
            }
            // Facebook First Name
            if (jsonObject.has("first_name")) {
                val facebookfirstname = jsonObject.getString("first_name")
                Log.i("Facebook First Name",facebookfirstname)
            }
            else {
                Log.i("Facebook First Name: ","Not exists")
            }
            // Facebook Middle Name
            if (jsonObject.has("middle_name")) {
                val facebookMiddleName = jsonObject.getString("middle_name")
                Log.i("Facebook Middle Name: ", facebookMiddleName)
            } else {
                Log.i("Facebook Middle Name: ", "Not exists")
            }

            // Facebook Last Name
            if (jsonObject.has("last_name")) {
                val facebookLastName = jsonObject.getString("last_name")
                Log.i("Facebook Last Name: ", facebookLastName)
            } else {
                Log.i("Facebook Last Name: ", "Not exists")
            }
            // Facebook Name
            if (jsonObject.has("name")) {
                val facebookName = jsonObject.getString("name")
                Log.i("Facebook Name: ", facebookName)
            } else {
                Log.i("Facebook Name: ", "Not exists")
            }
            //Facebook Profile Pic URL
            if (jsonObject.has("picture")){
                val facebookPictureObject = jsonObject.getJSONObject("picture")
                if (facebookPictureObject.has("data")){
                    val facebookDataObject = facebookPictureObject.getJSONObject("data")
                    if (facebookDataObject.has("url")) {
                        val facebookProfilePicURL = facebookDataObject.getString("url")
                        Log.i("Facebook Profile Pic Url :",facebookProfilePicURL)
                    }
                }
            }else{
                Log.i("Facebook Profile Pic URL: ", "Not exists")
            }


            // Facebook Email
            if (jsonObject.has("email")) {
                val facebookEmail = jsonObject.getString("email")
                Log.i("Facebook Email: ", facebookEmail)
            } else {
                Log.i("Facebook Email: ", "Not exists")
            }

        }).executeAsync()

          val intent = Intent(this@MainActivity,ProfileActivity::class.java)
          startActivity(intent)

      }

    private fun showMessage(s: String) {

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.img_facebook -> {
                onFacebookClicked()
            }
        }
    }

}