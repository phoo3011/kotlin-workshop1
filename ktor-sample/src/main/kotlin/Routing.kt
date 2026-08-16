package com.example

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, Arm!")
        }

        get("/tasks") {
            call.respond(TaskRepository.getAll())
        }

        get("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                return@get
            }

            val task = TaskRepository.getById(id)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            } else {
                call.respond(HttpStatusCode.OK, task)
            }
        }

        post("/tasks") {
            val taskRequest = call.receive<TaskRequest>()
            val task = TaskRepository.add(taskRequest)
            call.respond(HttpStatusCode.Created, task)
        }

        put("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                return@put
            }

            val updatedTask = call.receive<Task>()
            val task = TaskRepository.update(id, updatedTask)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            } else {
                call.respond(HttpStatusCode.OK, task)
            }
        }

        delete("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                return@delete
            }

            if (TaskRepository.delete(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
    }
}
