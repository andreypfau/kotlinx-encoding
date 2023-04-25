package io.github.andreypfau.kotlinx.encoding.base64

private val BASE64 =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".encodeToByteArray()
private val BASE64_URL =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".encodeToByteArray()

public open class Base64 private constructor(
    internal val isUrlSafe: Boolean
){
    private val map = if (isUrlSafe) BASE64_URL else BASE64

    public fun encode(source: ByteArray): String {
        val length = (source.size + 2) / 3 * 4
        val out = ByteArray(length)
        var index = 0
        val end = source.size - source.size % 3
        var i = 0
        while (i < end) {
            val b0 = source[i++].toInt()
            val b1 = source[i++].toInt()
            val b2 = source[i++].toInt()
            out[index++] = map[(b0 and 0xff shr 2)]
            out[index++] = map[(b0 and 0x03 shl 4) or (b1 and 0xff shr 4)]
            out[index++] = map[(b1 and 0x0f shl 2) or (b2 and 0xff shr 6)]
            out[index++] = map[(b2 and 0x3f)]
        }
        when (source.size - end) {
            1 -> {
                val b0 = source[i].toInt()
                out[index++] = map[b0 and 0xff shr 2]
                out[index++] = map[b0 and 0x03 shl 4]
                out[index++] = '='.code.toByte()
                out[index] = '='.code.toByte()
            }

            2 -> {
                val b0 = source[i++].toInt()
                val b1 = source[i].toInt()
                out[index++] = map[(b0 and 0xff shr 2)]
                out[index++] = map[(b0 and 0x03 shl 4) or (b1 and 0xff shr 4)]
                out[index++] = map[(b1 and 0x0f shl 2)]
                out[index] = '='.code.toByte()
            }
        }
        return out.decodeToString()
    }

    public fun decode(source: String): ByteArray {
        // Ignore trailing '=' padding and whitespace from the input.
        var limit = source.length
        while (limit > 0) {
            val c = source[limit - 1]
            if (c != '=' && c != '\n' && c != '\r' && c != ' ' && c != '\t') {
                break
            }
            limit--
        }

        // If the input includes whitespace, this output array will be longer than necessary.
        val out = ByteArray((limit * 6L / 8L).toInt())
        var outCount = 0
        var inCount = 0

        var word = 0
        for (pos in 0 until limit) {
            val c = source[pos]

            val bits: Int
            if (c in 'A'..'Z') {
                // char ASCII value
                //  A    65    0
                //  Z    90    25 (ASCII - 65)
                bits = c.code - 65
            } else if (c in 'a'..'z') {
                // char ASCII value
                //  a    97    26
                //  z    122   51 (ASCII - 71)
                bits = c.code - 71
            } else if (c in '0'..'9') {
                // char ASCII value
                //  0    48    52
                //  9    57    61 (ASCII + 4)
                bits = c.code + 4
            } else if (c == '+' || c == '-') {
                bits = 62
            } else if (c == '/' || c == '_') {
                bits = 63
            } else if (c == '\n' || c == '\r' || c == ' ' || c == '\t') {
                continue
            } else {
                throw IllegalArgumentException("Illegal char: '$c'")
            }

            // Append this char's 6 bits to the word.
            word = word shl 6 or bits

            // For every 4 chars of input, we accumulate 24 bits of output. Emit 3 bytes.
            inCount++
            if (inCount % 4 == 0) {
                out[outCount++] = (word shr 16).toByte()
                out[outCount++] = (word shr 8).toByte()
                out[outCount++] = word.toByte()
            }
        }

        val lastWordChars = inCount % 4
        when (lastWordChars) {
            1 -> {
                // We read 1 char followed by "===". But 6 bits is a truncated byte! Fail.
                throw IllegalArgumentException()
            }

            2 -> {
                // We read 2 chars followed by "==". Emit 1 byte with 8 of those 12 bits.
                word = word shl 12
                out[outCount++] = (word shr 16).toByte()
            }

            3 -> {
                // We read 3 chars, followed by "=". Emit 2 bytes for 16 of those 18 bits.
                word = word shl 6
                out[outCount++] = (word shr 16).toByte()
                out[outCount++] = (word shr 8).toByte()
            }
        }

        // If we sized our out array perfectly, we're done.
        if (outCount == out.size) return out

        // Copy the decoded bytes to a new, right-sized array.
        return out.copyOf(outCount)
    }

    public companion object Default : Base64(isUrlSafe = false) {
        public val UrlSafe: Base64 = Base64(isUrlSafe = true)
    }
}

public inline fun String.decodeBase64(): ByteArray = Base64.decode(this)
public inline fun ByteArray.encodeBase64(): String = Base64.encode(this)

public inline fun base64(value: String): ByteArray = Base64.decode(value)
public inline fun base64(value: ByteArray): String = Base64.encode(value)

public inline fun String.decodeBase64Url(): ByteArray = Base64.UrlSafe.decode(this)
public inline fun ByteArray.encodeBase64Url(): String = Base64.UrlSafe.encode(this)

public inline fun base64url(value: String): ByteArray = Base64.UrlSafe.decode(value)
public inline fun base64url(value: ByteArray): String = Base64.UrlSafe.encode(value)
