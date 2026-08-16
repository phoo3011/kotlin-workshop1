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
import kotlin.test.*

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server root returns 200
        assertEquals(HttpStatusCode.OK, client.get("/").status)
    }

    @Test
    fun `test task endpoints`() = testApplication {
        configure()

        val createResponse = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"content":"Learn Ktor","isDone":false}""")
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        assertTrue(createResponse.bodyAsText().contains("Learn Ktor"))

        val getAllResponse = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, getAllResponse.status)
        assertTrue(getAllResponse.bodyAsText().contains("Learn Ktor"))

        val getByIdResponse = client.get("/tasks/1")
        assertEquals(HttpStatusCode.OK, getByIdResponse.status)
        assertTrue(getByIdResponse.bodyAsText().contains("Learn Ktor"))

        val updateResponse = client.put("/tasks/1") {
            contentType(ContentType.Application.Json)
            setBody("""{"id":1,"content":"Finish Ktor workshop","isDone":true}""")
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        assertTrue(updateResponse.bodyAsText().contains("Finish Ktor workshop"))

        assertEquals(HttpStatusCode.NoContent, client.delete("/tasks/1").status)
        assertEquals(HttpStatusCode.NotFound, client.get("/tasks/1").status)
    }

}
