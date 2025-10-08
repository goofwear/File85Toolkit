package com.goofwear.file85toolkit

import org.tukaani.xz.LZMAInputStream
import org.tukaani.xz.LZMAOutputStream
import org.tukaani.xz.LZMA2Options
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64

object Codecs {
    fun lzmaCompress(input: ByteArray): ByteArray {
        val baos = ByteArrayOutputStream()
        LZMAOutputStream(baos, LZMA2Options(), -1).use { it.write(input) }
        return baos.toByteArray()
    }

    fun lzmaDecompress(input: ByteArray): ByteArray {
        val bais = ByteArrayInputStream(input)
        val lzmaIn = LZMAInputStream(bais)
        return lzmaIn.readBytes()
    }

    fun base85Encode(input: ByteArray): ByteArray {
        return Base64.getEncoder().encode(input) // simulating Base85
    }

    fun base85Decode(input: ByteArray): ByteArray {
        return Base64.getDecoder().decode(input)
    }
}
