package com.example

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IssueApiTest {
    @Test
    fun `root returns api name`() = testApplication {
        application {
            module()
        }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Simple Issue Tracker API"))
    }

    @Test
    fun `api supports issue crud status change and filtering`() = testApplication {
        application {
            module()
        }

        val createResponse = client.post("/issues") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                  "title": "Login fails",
                  "description": "Users cannot log in",
                  "priority": "HIGH"
                }
                """.trimIndent(),
            )
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        assertTrue(createResponse.bodyAsText().contains("OPEN"))

        val getResponse = client.get("/issues/1")
        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertTrue(getResponse.bodyAsText().contains("Login fails"))

        val updateResponse = client.put("/issues/1") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                  "title": "Login fails on mobile",
                  "description": "Mobile users cannot log in",
                  "status": "IN_PROGRESS",
                  "priority": "MEDIUM"
                }
                """.trimIndent(),
            )
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        assertTrue(updateResponse.bodyAsText().contains("Login fails on mobile"))

        val statusResponse = client.put("/issues/1/status") {
            contentType(ContentType.Application.Json)
            setBody("""{"status":"CLOSED"}""")
        }
        assertEquals(HttpStatusCode.OK, statusResponse.status)
        assertTrue(statusResponse.bodyAsText().contains("CLOSED"))

        val matchingFilterResponse = client.get("/issues?status=CLOSED&priority=MEDIUM")
        assertEquals(HttpStatusCode.OK, matchingFilterResponse.status)
        assertTrue(matchingFilterResponse.bodyAsText().contains("Login fails on mobile"))

        val nonMatchingFilterResponse = client.get("/issues?status=OPEN&priority=HIGH")
        assertEquals(HttpStatusCode.OK, nonMatchingFilterResponse.status)
        assertFalse(nonMatchingFilterResponse.bodyAsText().contains("Login fails on mobile"))

        val deleteResponse = client.delete("/issues/1")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)
        assertEquals(HttpStatusCode.NotFound, client.get("/issues/1").status)
    }

    @Test
    fun `invalid filter returns bad request`() = testApplication {
        application {
            module()
        }

        val response = client.get("/issues?status=WAITING")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `blank issue title returns bad request`() = testApplication {
        application {
            module()
        }

        val response = client.post("/issues") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                  "title": " ",
                  "description": "Valid description",
                  "priority": "LOW"
                }
                """.trimIndent(),
            )
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
