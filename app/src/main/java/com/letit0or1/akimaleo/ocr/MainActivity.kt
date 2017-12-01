package com.letit0or1.akimaleo.ocr

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.method.KeyListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import it.sephiroth.android.library.tooltip.Tooltip
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    /**
     * For preventing and allowing editText editing
     **/
    private lateinit var keyListener: KeyListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        take_photo.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this@MainActivity)
        }

        keyListener = data.keyListener
        setEditable(false)
        set_editable.setOnClickListener {
            changeEditable()
        }

        copy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("ocr", data.text)
            clipboard.primaryClip = clip
            Toast.makeText(this@MainActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideSoftwareKeyboard() {
        val view = this.currentFocus
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun changeEditable() {
        setEditable(check.visibility == View.INVISIBLE)
    }

    private fun setEditable(setEditable: Boolean) {
        if (setEditable) {
            check.visibility = View.VISIBLE
            data.isCursorVisible = true
            data.keyListener = keyListener
        } else {
            check.visibility = View.INVISIBLE
            data.keyListener = null
            data.isCursorVisible = false
            hideSoftwareKeyboard()
        }
    }

    private fun showCopyTooltip() {
        Tooltip.make(this,
                Tooltip.Builder(100)
                        .anchor(copy, Tooltip.Gravity.TOP)
                        .closePolicy(Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 2000)
                        .activateDelay(800)
                        .showDelay(300)
                        .text("Copy text")
                        .maxWidth(500)
                        .withArrow(true)
                        .withOverlay(true)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build()
        ).show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(dataIntent)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val q = com.letit0or1.akimaleo.opticalcharacterrecognizer.OpticalCharacterRecognizer.extractText(bitmap, this)
                data.setText(q)
                showCopyTooltip()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
