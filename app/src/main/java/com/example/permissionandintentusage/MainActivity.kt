package com.example.permissionandintentusage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissionandintentusage.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
private lateinit var permissionLauncher: ActivityResultLauncher<String>
var selectedBitmap : Bitmap? = null
private var binding: ActivityMainBinding? = null

class MainActivity : AppCompatActivity() {

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
            if(!it.value){
                //do any thing if need
                Snackbar.make(binding!!.root,"Permission is needed",Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)
        //registerLauncher()
        binding!!.button.setOnClickListener{
//            checkSelfPermission(context = this,activity = this,view,android.Manifest.permission.READ_EXTERNAL_STORAGE,"Gallery")
            //resultLauncher.launch(neededRunTimePermission)

        }
    }

    fun checkSelfPermission (context: Context, activity: Activity, view: View, permission:String, permissionName: String){
        val snackBar = Snackbar.make(view,"Permission needed for $permissionName", Snackbar.LENGTH_INDEFINITE)
        if(ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                //rational
                snackBar.setAction("Give Permission"){
                    //request permission
                    permissionLauncher.launch(permission)
                }
                snackBar.show()
            }
            else{
                //request permission
                permissionLauncher.launch(permission)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    val imageData = intentFromResult.data
                    //imageView.setImageURI(imageData)
                    if(imageData != null){
                        try {
                            if(Build.VERSION.SDK_INT >= 28){
                                val source = ImageDecoder.createSource(this@MainActivity.contentResolver,imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding!!.imageView.setImageBitmap(selectedBitmap)
                            }
                            else{
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                binding!!.imageView.setImageBitmap(selectedBitmap)
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this,"Permission Needed",Toast.LENGTH_LONG).show()
            }
        }
    }

}