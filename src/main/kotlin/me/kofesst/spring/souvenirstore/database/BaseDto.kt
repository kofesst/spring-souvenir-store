package me.kofesst.spring.souvenirstore.database

interface BaseDto<Model : Any> {
    fun toModel(): Model
}