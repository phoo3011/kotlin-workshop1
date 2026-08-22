package com.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.configureIssueRoutes(service: IssueService) {
    routing {
        get("/") {
            call.respondText("Simple Issue Tracker API")
        }

        get("/issues") {
            val statusText = call.request.queryParameters["status"]
            val priorityText = call.request.queryParameters["priority"]
            val status = statusText?.toIssueStatus()
            val priority = priorityText?.toIssuePriority()

            if (statusText != null && status == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid issue status"))
                return@get
            }
            if (priorityText != null && priority == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid issue priority"))
                return@get
            }

            call.respond(HttpStatusCode.OK, service.getAll(status, priority))
        }

        get("/issues/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid issue id"))
                return@get
            }

            val issue = service.getById(id)
            if (issue == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Issue not found"))
            } else {
                call.respond(HttpStatusCode.OK, issue)
            }
        }

        post("/issues") {
            try {
                val request = call.receive<CreateIssueRequest>()
                call.respond(HttpStatusCode.Created, service.create(request))
            } catch (exception: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(exception.message ?: "Invalid issue"))
            }
        }

        put("/issues/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid issue id"))
                return@put
            }

            try {
                val request = call.receive<UpdateIssueRequest>()
                val issue = service.update(id, request)
                if (issue == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Issue not found"))
                } else {
                    call.respond(HttpStatusCode.OK, issue)
                }
            } catch (exception: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(exception.message ?: "Invalid issue"))
            }
        }

        put("/issues/{id}/status") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid issue id"))
                return@put
            }

            val request = call.receive<UpdateIssueStatusRequest>()
            val issue = service.changeStatus(id, request.status)
            if (issue == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Issue not found"))
            } else {
                call.respond(HttpStatusCode.OK, issue)
            }
        }

        delete("/issues/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid issue id"))
                return@delete
            }

            if (service.delete(id)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Issue not found"))
            }
        }
    }
}

private fun String.toIssueStatus(): IssueStatus? =
    enumValues<IssueStatus>().find { status -> status.name == uppercase() }

private fun String.toIssuePriority(): IssuePriority? =
    enumValues<IssuePriority>().find { priority -> priority.name == uppercase() }
