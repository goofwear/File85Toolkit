package com.example.base85

import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.io.OutputStream

object SafUtils {
    fun readFile(context: Context, uri: Uri): ByteArray {
        val input: InputStream? = context.contentResolver.openInputStream(uri)
        return input?.readBytes() ?: ByteArray(0)
    }

    fun writeFile(context: Context, uri: Uri, data: ByteArray) {
        val output: OutputStream? = context.contentResolver.openOutputStream(uri)
        output?.write(data)
        output?.close()
    }
}
