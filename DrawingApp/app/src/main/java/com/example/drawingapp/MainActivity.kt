package com.example.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var drawingView : DrawView? = null

    private var sizeSelector : ImageButton? = null

    private var mImageButtonCurrentPaint : ImageButton? = null

    val openGallaryLaucnher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK && result.data!=null){
                val imageView = findViewById<ImageView>(R.id.bg_imageview)
                imageView.setImageURI(result.data?.data)
            }
        }

    val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (!isGranted){
                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this, "It seems you denied the permission", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // this means our permission has been granted
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGallaryLaucnher.launch(pickIntent)

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // get the drawable view
        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())
        // get the size selector button
        sizeSelector = findViewById(R.id.size_btn)
        sizeSelector!!.setOnClickListener{
            showSizeChooserDialog()
        }
        // Get the linear layout
        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.color_layout)
        //set the default color
        mImageButtonCurrentPaint = linearLayoutPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.color_button_selected)
        )
        // get and set the gallery open button
        val galleryButton = findViewById<ImageButton>(R.id.gallery_btn)
        galleryButton.setOnClickListener(){
            showStoragePermission()
        }
        // get and set the save open button
        val saveButton = findViewById<ImageButton>(R.id.save_btn)
        galleryButton.setOnClickListener(){
            if(isReadStorageallowed()){
                lifecycleScope.launch{
                    val flLayout: FrameLayout = findViewById(R.id.fl_drawing_view_container)
                    saveBitmapFile(getImageFromBitmap(flLayout))
                }
            }
        }
    }

    private fun isReadStorageallowed() : Boolean{
        val result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun showStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
          this,
          Manifest.permission.READ_EXTERNAL_STORAGE
        )){
            showRationaleDialog("Kids Drawing App", "It seems that you already denied" +
                    "permission for reading storage.")
        }else{
            requestPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))
        }
    }

    private fun showSizeChooserDialog(){
        val sizeDialog = Dialog(this)
        sizeDialog.setContentView(R.layout.size_chooser_dialogue)
        sizeDialog.setTitle("Brush Size:")
        // Bind the dialog's popup screens
       sizeDialog.findViewById<ImageButton>(R.id.small).setOnClickListener{
            drawingView?.setSizeForBrush(10.toFloat())
            sizeDialog.dismiss()
        }
        sizeDialog.findViewById<ImageButton>(R.id.medium).setOnClickListener{
            drawingView?.setSizeForBrush(20.toFloat())
            sizeDialog.dismiss()
        }
        sizeDialog.findViewById<ImageButton>(R.id.large).setOnClickListener{
            drawingView?.setSizeForBrush(30.toFloat())
            sizeDialog.dismiss()
        }
        sizeDialog.show()
    }

    fun paintClicked(view: View){
        if(mImageButtonCurrentPaint !== view){
            // Only executes if we press a button that is not already pressed
            val cimageButton = view as ImageButton
            val color = cimageButton.tag.toString()
            drawingView?.setColor(color)
            cimageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.color_button_selected)
            )
            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.color_button)
            )
            mImageButtonCurrentPaint = view
        }
    }

    private fun showRationaleDialog(title: String, message: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Cancel"){dialog,_ ->
            dialog.dismiss()
        }
        builder.create().show()

    }

    private fun getImageFromBitmap(view : View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null){
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private suspend fun saveBitmapFile(mBitmap: Bitmap?){
        var result = ""
        withContext(Dispatchers.IO){
            if(mBitmap != null){
                try{
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG,90,bytes)

                    val f = File(externalCacheDir?.absoluteFile.toString()
                    + File.separator + "DrawingApp" + System.currentTimeMillis()/1000 + ".png")

                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()

                    result = f.absolutePath

                    runOnUiThread{
                        if (result.isNotEmpty()){
                            Toast.makeText(this@MainActivity, "It seems you denied the permission", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Something went wrong ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (e : Exception){
                    result = ""
                    e.printStackTrace()
                }
            }
        }

    }
}