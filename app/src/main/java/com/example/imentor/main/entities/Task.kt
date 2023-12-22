package com.example.imentor.main.entities

data class Task(
    val name: String? = null,
    var taskID: String? = null,
    val explantation: String? = null,
    val startDateTime: String? = null,
    val endDateTime: String? = null,
    val interval: String? = null,
    val type: String? = null,
    var complated: Boolean? = null,
    var subTasks: String? = null,
    var counters: List<Counter>? = null,
    ) {
}