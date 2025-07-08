package com.egssmart.routes

import com.egssmart.dao.Installation
import com.egssmart.dto.CompleteInstallationResponse
import com.egssmart.repository.ApiKeyRepo
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import com.egssmart.dto.CreateKeyRequest
import com.egssmart.dto.KeyResponse
import com.egssmart.dto.ValidateResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.authRoutes() {

    /* 1) Emitir clave */
    post("/key") {
        val req = call.receive<CreateKeyRequest>()
        val key = ApiKeyRepo.create(req.customer, req.maxUses ?: 2)
        call.respond(HttpStatusCode.Created, KeyResponse(key.token, key.maxUses))
    }

    /* 2) Validar */
    get("/validate") {
        val apiToken = call.request.header("x-api-token")
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        val key = ApiKeyRepo.find(apiToken)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        call.respond(
            ValidateResponse(
                customer  = key.customer,
                valid     = key.remaining > 0,
                remaining = key.remaining
            )
        )
    }

    /* 3) Registrar inicio */
    post("/register-installation") {
        val apiToken = call.request.header("x-api-token")
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        val key = ApiKeyRepo.find(apiToken)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        transaction {
            Installation.new {
                this.apiKey = key
                started = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            }
        }
        call.respond(mapOf("success" to true))
    }

            /* 4) Completar instalación */
            post("/complete-installation") {
                val apiToken = call.request.header("x-api-token")
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val key = ApiKeyRepo.find(apiToken)
                    ?: return@post call.respond(HttpStatusCode.NotFound)

                if (key.remaining <= 0)
                    return@post call.respond(
                        HttpStatusCode.Forbidden,
                        mapOf("error" to "No remaining uses")
                    )

                ApiKeyRepo.incrementUsage(key)

                call.respond(
                    CompleteInstallationResponse(
                        success = true,
                        remaining_uses = key.remaining
                    )
                )
            }

}