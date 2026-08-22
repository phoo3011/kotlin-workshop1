package com.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    val repository = IssueRepository()
    val service = IssueService(repository)

    configureIssueRoutes(service)
}
