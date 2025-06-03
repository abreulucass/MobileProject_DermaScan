package br.com.abreulucas.mobileproject2.common.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream

fun getPathFromUri(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        tempFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}