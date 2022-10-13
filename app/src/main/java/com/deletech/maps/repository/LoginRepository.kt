package com.deletech.maps.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.deletech.maps.R
import com.deletech.maps.custom.Resource
import com.deletech.maps.models.Profile
import com.deletech.maps.models.Token
import com.deletech.maps.networks.NetworkUtils
import com.deletech.maps.networks.RequestService
import com.deletech.maps.storage.MapsDatabase
import com.deletech.maps.storage.PreferenceManager
import com.deletech.maps.storage.daos.TokenDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class LoginRepository (application: Application){
    val signInObservable = MutableLiveData<Resource<Token>>()
    private val db:MapsDatabase
    private val context: Context
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    init {
        context =application.applicationContext
        db =MapsDatabase.getDatabase(application)!!

    }
    fun signIn(){
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            executeSignIn()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun executeSignIn(){
        val loginInfo = Profile(
            username = "tom",
            password = "tom"
 )
        GlobalScope.launch(context= Dispatchers.Main){
            val call= RequestService.getService("").login(loginInfo)
            call.enqueue(object: retrofit2.Callback<Token> {
                override fun onFailure(call:retrofit2. Call<Token>?,t:Throwable?){
                    setIsError(t.toString())
                }
                override fun onResponse(call: retrofit2.Call<Token>?, response: retrofit2.Response<Token>?) {
                    if(response!=null){
                        if (response.isSuccessful){
                            if (response.body()!!.accessToken!=null){
                                preferenceManager.saveToken(response.body()!!.accessToken.toString())
                                setIsSuccesful(response.body()!!)
                            }
                            else{
                                setIsError("Error Loading Data")

                            }

                        }
                        else{
                            setIsError(response.toString())
                        }

                    }
                    else{
                        setIsError("Error Loggin In")
                    }
                }
            })

        }
    }

    private fun setIsLoading(){
        signInObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccesful(parameters: Token){
        signInObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        signInObservable.postValue(Resource.error(message, null))
    }
}