import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.example.validateCitizenId

class ValidateCitizenIdTest {

    @Test
    fun `valid 13 digit id returns true`() {
        // Arrange
        val id = "1234567890121"
        // Act
        val result = validateCitizenId(id)
        // Assert
        assertTrue(result)
    }

    @Test
    fun `id with wrong length returns false`() {
        assertFalse(validateCitizenId("12345"))
        assertFalse(validateCitizenId("12345678901234"))
        assertFalse(validateCitizenId(""))
    }

    @Test
    fun `id containing non digit characters returns false`() {
        assertFalse(validateCitizenId("123456789012A"))
        assertFalse(validateCitizenId("ABCDEFGHIJKLM"))
        assertFalse(validateCitizenId("1234567 8901"))
    }

    @Test
    fun `id with checksum validation`() {
        assertFalse(validateCitizenId("1101700185207"))
        assertFalse(validateCitizenId("1234567890129"))

        assertTrue(validateCitizenId("3509900547250"))
        assertTrue(validateCitizenId("1234567890121"))
    }

    @Test
    fun `id with Thai number returns true`() {
        assertTrue(validateCitizenId("๑๒๓๔๕๖๗๘๙๐๑๒๑"))
    }
}