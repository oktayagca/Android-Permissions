package com.example.permissionandintentusage

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.permissionandintentusage.databinding.ActivityMainForMultiplePermissionBinding
import com.google.android.material.snackbar.Snackbar

class MainActivityForMultiplePermission:AppCompatActivity() {
    private var binding:ActivityMainForMultiplePermissionBinding? = null
    private val tag = "MainActivityFor"
    private val  neededRunTimePermission = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.READ_CONTACTS,
    )

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissions->
        permissions.entries.forEach{
            Log.d(tag,"registerForActivityResult: ${it.key} = ${it.value}")

            //if any permission is not granted
            if(! it.value){
                //do any thing if need
                Snackbar.make(binding!!.root,"Permission is needed",Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMainForMultiplePermissionBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)
        binding!!.button.setOnClickListener{
            resultLauncher.launch(neededRunTimePermission)
        }
    }
}