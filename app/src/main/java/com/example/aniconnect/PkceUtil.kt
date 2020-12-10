package com.example.aniconnect

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class PkceUtil {
    companion object{
        fun genCodeVerifier(numBytes: Int): String { // generate a string of 32 secure random bytes
            val codeVerifier = ByteArray(numBytes)
            SecureRandom().nextBytes(codeVerifier)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier)
        }
        fun genCodeChallenge(codeVerifier: String) : String {
            val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
            val message = MessageDigest.getInstance("SHA-256")
            message.update(bytes,0,bytes.size)
            val digest: ByteArray = message.digest()
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
        }
    }

}