package com.egssmart.util

import at.favre.lib.crypto.bcrypt.BCrypt
import java.security.SecureRandom
import java.util.Base64

object TokenGenerator {
    private val rnd = SecureRandom()
    fun newKey(): String = ByteArray(24).also(rnd::nextBytes)
        .let { Base64.getUrlEncoder().withoutPadding().encodeToString(it) }
}

fun hashToken(token: String): String = BCrypt.withDefaults().hashToString(12, token.toCharArray())
