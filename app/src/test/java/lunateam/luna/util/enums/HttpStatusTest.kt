package lunateam.luna.util.enums

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class HttpStatusTest {
    @Test
    fun shouldHaveTheCorrectKeys() {
        val actualKeys = HttpStatus.entries.map { it.name }
        val expectedKeys = listOf(
            "OK",
            "CREATED",
            "NO_CONTENT",
            "BAD_REQUEST",
            "UNAUTHORIZED",
            "FORBIDDEN",
            "NOT_FOUND",
            "CONFLICT",
            "INTERNAL_SERVER_ERROR",
            "SERVICE_UNAVAILABLE"
        )

        assertEquals(expectedKeys, actualKeys)
    }

    @Test
    fun shouldHaveTheCorrectValues() {
        val actualValues = HttpStatus.entries.map { it.code }
        val expectedValues = listOf(
            200,
            201,
            204,
            400,
            401,
            403,
            404,
            409,
            500,
            503,
        )

        assertEquals(expectedValues, actualValues)
    }

    @Test
    fun shouldHaveTheCorrectCode() {
        assertEquals(200, HttpStatus.OK.code)
        assertEquals(201, HttpStatus.CREATED.code)
        assertEquals(204, HttpStatus.NO_CONTENT.code)
        assertEquals(400, HttpStatus.BAD_REQUEST.code)
        assertEquals(401, HttpStatus.UNAUTHORIZED.code)
        assertEquals(403, HttpStatus.FORBIDDEN.code)
        assertEquals(404, HttpStatus.NOT_FOUND.code)
        assertEquals(409, HttpStatus.CONFLICT.code)
        assertEquals(500, HttpStatus.INTERNAL_SERVER_ERROR.code)
        assertEquals(503, HttpStatus.SERVICE_UNAVAILABLE.code)
    }

    @Test
    fun shouldReturnTheCorrectMessage() {
        assertEquals("OK", HttpStatus.message(200))
        assertEquals("CREATED", HttpStatus.message(201))
        assertEquals("NO_CONTENT", HttpStatus.message(204))
        assertEquals("BAD_REQUEST", HttpStatus.message(400))
        assertEquals("UNAUTHORIZED", HttpStatus.message(401))
        assertEquals("FORBIDDEN", HttpStatus.message(403))
        assertEquals("NOT_FOUND", HttpStatus.message(404))
        assertEquals("CONFLICT", HttpStatus.message(409))
        assertEquals("INTERNAL_SERVER_ERROR", HttpStatus.message(500))
        assertEquals("SERVICE_UNAVAILABLE", HttpStatus.message(503))
    }
}
