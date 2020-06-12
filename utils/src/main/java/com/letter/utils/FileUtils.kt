package com.letter.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream


object FileUtils {

    fun createOrExistDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdir()
    }

    fun createOrExistDir(path: String): Boolean = createOrExistDir(File(path))

    fun saveBitmapToExternalFilesDir(context: Context,
                                     path: String,
                                     name: String,
                                     bitmap: Bitmap?,
                                     format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
                                     quality: Int = 100): File? {
        if (bitmap != null && createOrExistDir(context.getExternalFilesDir(path))) {
            val file = File(context.getExternalFilesDir(path), name)
            val os = FileOutputStream(file)
            bitmap.compress(format, quality, os)
            os.flush()
            return file
        }
        return null
    }
}