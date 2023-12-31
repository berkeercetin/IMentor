package com.example.imentor.main.entities

import java.io.Serializable

data class Task(
    val name: String? = null,
    var taskID: String? = null,
    val explanation: String? = null,
    val startDateTime: String? = null,
    val endDateTime: String? = null,
    val type: String? = null,
    var complated: Boolean? = false,
    ): Serializable {
}