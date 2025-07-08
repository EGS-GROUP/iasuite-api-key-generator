package com.egssmart.config

import com.egssmart.model.ApiKeys
import com.egssmart.model.Installations
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(appConfig: ApplicationConfig) {
        val cfg = appConfig.config("database")

        val db = Database.connect(
            url      = cfg.property("url").getString(),
            driver   = cfg.property("driver").getString(),
            user     = cfg.property("user").getString(),
            password = cfg.property("password").getString()
        )
        transaction(db) {
            SchemaUtils.create(ApiKeys, Installations)
        }
    }
}
