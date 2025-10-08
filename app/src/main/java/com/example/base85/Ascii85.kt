package com.example.base85

import java.util.Base64

object Ascii85 {
    fun encode(data: ByteArray): String {
        return Base64.getEncoder().encodeToString(data) // placeholder using Base64
    }

    fun decode(str: String): ByteArray {
        return Base64.getDecoder().decode(str) // placeholder using Base64
    }
}
