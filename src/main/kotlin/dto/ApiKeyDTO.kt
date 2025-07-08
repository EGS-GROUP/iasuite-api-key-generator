package com.egssmart.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateKeyRequest(
    val customer: String,
    val maxUses: Int? = null
)

@Serializable
data class KeyResponse(
    val key: String,
    val maxUses: Int
)

@Serializable
data class ValidateResponse(
    val customer: String,
    val valid: Boolean,
    val remaining: Int
)

@Serializable
data class CompleteInstallationResponse(
    val success: Boolean,
    val remaining_uses: Int
)