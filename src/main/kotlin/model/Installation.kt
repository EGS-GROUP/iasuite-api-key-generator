package com.egssmart.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime


object Installations : IntIdTable() {
    val apiKey = reference(
        name     = "api_key_id",
        refColumn = ApiKeys.id,                   // ← la PK de la tabla
        onDelete  = ReferenceOption.CASCADE,
        onUpdate  = ReferenceOption.NO_ACTION,
        fkName    = null
    )
    val started = datetime("started")
    val finished = datetime("finished").nullable()
}
