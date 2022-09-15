package me.kofesst.spring.souvenirstore.model

import java.sql.Time

data class PickupPoint(
    val id: Long = 0,
    val address: String = "",
    val startTime: Time = Time.valueOf("00:00:00"),
    val endTime: Time = Time.valueOf("23:59:59"),
)
