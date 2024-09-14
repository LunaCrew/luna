package lunateam.luna.util.enums

enum class HttpStatus(val code: Int) {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    companion object {
        /**
         * Get the message for the given code.
         * @param code The HTTP status code.
         */
        infix fun message(code: Int): String = entries.find { it.code == code }.toString()
    }
}
