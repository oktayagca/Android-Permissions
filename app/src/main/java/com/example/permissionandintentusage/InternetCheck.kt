package com.example.permissionandintentusage

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun hasInternetConnection(activity: Activity):Boolean{
    val connectivityManager =activity.application.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
        val activeNetwork = connectivityManager.activeNetwork ?:return false
        val  capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?:return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else ->false
        }
    }
    else{
        connectivityManager.activeNetworkInfo?.run {
            return when(type){
                ConnectivityManager.TYPE_WIFI ->true
                ConnectivityManager.TYPE_MOBILE ->true
                ConnectivityManager.TYPE_ETHERNET ->true
                else ->false
            }
        }
    }
    return false
}