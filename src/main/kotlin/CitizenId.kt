package org.example

fun validateCitizenId(citizenId: String): Boolean {
    val normalizedId = citizenId.map { char ->
        when (char) {
            in '๐'..'๙' -> (char - '๐' + '0'.code).toChar()
            else -> char
        }
    }.joinToString("")

    if (normalizedId.length != 13) return false

    if (!normalizedId.all { it in '0'..'9' }) return false

    var sum = 0
    for (i in 0 until 12) {
        val digit = normalizedId[i].digitToInt()
        sum += digit * (13 - i)
    }

    val mod = sum % 11
    val expectedChecksum = (11 - mod) % 10
    val actualChecksum = normalizedId[12].digitToInt()

    return expectedChecksum == actualChecksum
}