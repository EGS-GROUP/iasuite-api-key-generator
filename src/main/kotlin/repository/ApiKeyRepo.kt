package com.egssmart.repository

import com.egssmart.model.ApiKey
import com.egssmart.model.ApiKeys
import com.egssmart.util.TokenGenerator
import org.jetbrains.exposed.sql.transactions.transaction

object ApiKeyRepo {
    fun create(customer: String, maxUses: Int = 2): ApiKey = transaction {
        ApiKey.new {
            token = TokenGenerator.newKey()
            this.customer = customer
            this.maxUses = maxUses
        }
    }

    fun find(token: String): ApiKey? = transaction {
        ApiKey.find { ApiKeys.token eq token }.firstOrNull()
    }

    fun incrementUsage(apiKey: ApiKey) = transaction {
        apiKey.used = apiKey.used + 1
    }
}
