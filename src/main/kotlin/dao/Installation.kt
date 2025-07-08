package com.egssmart.dao

import com.egssmart.model.ApiKey
import com.egssmart.model.Installations
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Installation(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Installation>(Installations)

    var apiKey  by ApiKey referencedOn Installations.apiKey
    var started by Installations.started
    var finished by Installations.finished
}
