package com.egssmart.routes

import com.egssmart.repository.ApiKeyRepo
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.nio.file.Files
import java.nio.file.Path
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("FileRoutes")

fun Route.fileRoutes(staticDir: Path) {

    /* Directorio raíz absoluto y normalizado  */
    val root = staticDir.toAbsolutePath().normalize()

    /**
     *  GET /file/{path...}
     *
     *  Ejemplo:
     *     /file/docker/traefik/traefik-stack.yml
     *     └────────────  path capturado ────────────────┘
     */
    get("/file/{path...}") {

        /* 1. Autenticación -------------------------------------------------- */
        val token = call.request.headers["x-api-token"]
            ?: return@get call.respond(HttpStatusCode.Unauthorized, "Missing token")

        val key = ApiKeyRepo.find(token)
            ?: return@get call.respond(HttpStatusCode.Unauthorized, "Invalid token")

        if (key.remaining <= 0)
            return@get call.respond(HttpStatusCode.Forbidden, "Quota exceeded (${key.maxUses})")

        /* 2. Reconstruir la ruta pedida ------------------------------------- */
        val segments = call.parameters.getAll("path")     // <— ahora es "path"
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid path")

        val relativePath = segments.joinToString("/")     // "docker/traefik/traefik-stack.yml"
        val requested    = root.resolve(relativePath).normalize()

        /* 3. Seguridad: impedir escapes fuera de staticDir ------------------ */
        if (!requested.startsWith(root) || !Files.exists(requested)) {
            log.warn("404 for {} (resolved to {})", relativePath, requested)
            return@get call.respond(HttpStatusCode.NotFound)
        }

        /* 4. Servir el archivo --------------------------------------------- */
        log.info("Serving {}", requested)
        call.respondFile(requested.toFile())
    }
}

