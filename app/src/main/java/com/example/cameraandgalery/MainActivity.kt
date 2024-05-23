package com.example.cameraandgalery

import android.content.ContentValues.TAG
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cameraandgalery.adapter.GalleryAdapter
import com.example.cameraandgalery.databinding.ActivityMainBinding
import com.example.cameraandgalery.db.MyDbHelper
import com.example.cameraandgalery.model.Gallery
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Throws

class MainActivity : AppCompatActivity() {
    lateinit var myDbHelper: MyDbHelper
    lateinit var galleryAdapter: GalleryAdapter
    private var absolutPath = ""
    lateinit var photoUri: Uri
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        myDbHelper = MyDbHelper(this)
        binding.imageView.setOnClickListener {
            getImageContent.launch("image/*")
        }
        binding.cameraView.setOnClickListener {
            val imageFile = createImageFile()
            photoUri = FileProvider.getUriForFile(
                this@MainActivity,
                "com.example.cameraandgalery",
                imageFile
            )
            getTakeImageContent.launch(photoUri)
        }
        galleryAdapter = GalleryAdapter(myDbHelper.showPhoto())
        binding.rv.adapter = galleryAdapter

        binding.btnSave.setOnClickListener {
            myDbHelper.addPhoto(
                Gallery(
                    1,
                    binding.edtName.text.toString(),
                    absolutPath
                )

            )
            galleryAdapter = GalleryAdapter(myDbHelper.showPhoto())
            binding.rv.adapter = galleryAdapter
            binding.edtName.text.clear()
            absolutPath = ""
            binding.imageView.setImageResource(R.drawable.ic_launcher_foreground)
            binding.cameraView.setImageResource(R.drawable.ic_launcher_foreground)

        }

    }
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        it?: return@registerForActivityResult
        binding.imageView.setImageURI(it)
        val format = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val inputStream = contentResolver?.openInputStream(it)
        val file = File(filesDir, "${format}.jpg")
        val fileOutputStream = FileOutputStream(file)
        inputStream?.copyTo(fileOutputStream)
        fileOutputStream.close()
        inputStream?.close()
        absolutPath = file.absolutePath
    }

    private fun createImageFile(): File {
        val format = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${format}", ".jpg", externalFilesDir).apply {
            absolutPath = absolutePath
        }
    }
    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            val format = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(Date())
            if (it) {
                binding.cameraView.setImageURI(photoUri)
                val inputStream = contentResolver?.openInputStream(photoUri)
                val file = File(filesDir, "IMG_$format.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
            }
        }

}