package io.github.andreypfau.kotlinx.encoding.hex

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class HexTest {
    @Test
    fun testHex() {
        testHex("", byteArrayOf())
        testHex("0001020304050607", byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7))
        testHex("08090a0b0c0d0e0f", byteArrayOf(8, 9, 10, 11, 12, 13, 14, 15))
        testHex("f0f1f2f3f4f5f6f7", ubyteArrayOf(0xf0u, 0xf1u, 0xf2u, 0xf3u, 0xf4u, 0xf5u, 0xf6u, 0xf7u).asByteArray())
        testHex("f8f9fafbfcfdfeff", ubyteArrayOf(0xf8u, 0xf9u, 0xfau, 0xfbu, 0xfcu, 0xfdu, 0xfeu, 0xffu).asByteArray())
        testHex("67", byteArrayOf('g'.code.toByte()))
        testHex("e3a1", byteArrayOf(0xE3.toByte(), 0xA1.toByte()))
    }

    private fun testHex(enc: String, dec: ByteArray) {
        try {
            assertEquals(enc, dec.encodeHex())
            assertContentEquals(dec, enc.lowercase().decodeHex())
            assertContentEquals(dec, enc.uppercase().decodeHex())
        } catch (e: Throwable) {
            throw RuntimeException("Failed test: $enc - ${dec.contentToString()}", e)
        }
    }
}
