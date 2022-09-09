package me.kofesst.spring.souvenirstore.util

import me.kofesst.spring.souvenirstore.database.BaseDto

fun <Model : Any, Dto : BaseDto<Model>> Iterable<Dto>.asModels() =
    this.map { it.toModel() }