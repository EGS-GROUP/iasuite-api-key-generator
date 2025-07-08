val kotlin_version: String by project
val logback_version: String by project
val expose_version: String by project
val postgresql_version: String by project
val bcrypt_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "com.egssmart"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")

    // Exposed + PostgreSQL
    implementation("org.jetbrains.exposed:exposed-core:$expose_version")
    implementation("org.jetbrains.exposed:exposed-dao:$expose_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$expose_version")
    implementation("org.postgresql:postgresql:$postgresql_version")

    // BCrypt
    implementation("at.favre.lib:bcrypt:$bcrypt_version")

    //datetime
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$expose_version")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
