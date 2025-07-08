package com.egssmart.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object ApiKeys : IntIdTable() {
    val token = varchar("token", 64).uniqueIndex()
    val customer = varchar("customer", 128)
    val maxUses = integer("max_uses")
    val used = integer("used").default(0)
}

class ApiKey(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApiKey>(ApiKeys)

    var token by ApiKeys.token
    var customer by ApiKeys.customer
    var maxUses by ApiKeys.maxUses
    var used by ApiKeys.used
    val remaining get() = maxUses - used
}
