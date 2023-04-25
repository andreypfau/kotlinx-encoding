package io.github.andreypfau.kotlinx.encoding.binary

import kotlin.jvm.JvmStatic

public object Binary {

    /**
     * Read short signed 16-bit integer in the network byte order (Big Endian)
     */
    @JvmStatic
    public fun ByteArray.loadShortAt(offset: Int): Short =
        (((get(offset).toInt() and 0xFF) shl 8) or
                (get(offset + 1).toInt() and 0xFF)).toShort()

    /**
     * Write short signed 16-bit integer in the network byte order (Big Endian)
     */
    @JvmStatic
    public fun ByteArray.storeShortAt(offset: Int, value: Short) {
        set(offset, value.toByte())
        set(offset + 1, (value.toInt() ushr 8).toByte())
    }

    /**
     * Read short unsigned 16bit integer in the network byte order (Big Endian)
     */
    public inline fun ByteArray.loadUShortAt(offset: Int): UShort =
        loadShortAt(offset).toUShort()

    /**
     * Write short unsigned 16bit integer in the network byte order (Big Endian)
     */
    public inline fun ByteArray.storeUShortAt(offset: Int, value: UShort) {
        storeShortAt(offset, value.toShort())
    }

    /**
     * Read regular signed 32-bit integer in the network byte order (Big Endian)
     */
    @JvmStatic
    public fun ByteArray.loadIntAt(offset: Int): Int =
        (get(offset).toInt() shl 24) or
                ((get(offset + 1).toInt() and 0xFF) shl 16) or
                ((get(offset + 2).toInt() and 0xFF) shl 8) or
                ((get(offset + 3).toInt() and 0xFF))

    /**
     * Write regular signed 32-bit integer in the network byte order (Big Endian)
     */
    @JvmStatic
    public fun ByteArray.storeIntAt(offset: Int, value: Int) {
        set(offset, (value ushr 24).toByte())
        set(offset + 1, (value ushr 16).toByte())
        set(offset + 2, (value ushr 8).toByte())
        set(offset + 3, value.toByte())
    }

    /**
     * Read regular unsigned 32-bit integer in the network byte order (Big Endian)
     */
    public inline fun ByteArray.loadUIntAt(offset: Int): UInt = loadIntAt(offset).toUInt()

    /**
     * Write regular unsigned 32-bit integer in the network byte order (Big Endian)
     */
    public inline fun ByteArray.storeUIntAt(offset: Int, value: UInt) {
        storeIntAt(offset, value.toInt())
    }

    /**
     * Read long signed 64-bit integer in the network byte order (Big Endian)
     */
    @JvmStatic
    public fun ByteArray.loadLongAt(offset: Int): Long =
        (get(offset).toLong() shl 56) or
                ((get(offset + 1).toLong() and 0xFF) shl 48) or
                ((get(offset + 2).toLong() and 0xFF) shl 40) or
                ((get(offset + 3).toLong() and 0xFF) shl 32) or
                ((get(offset + 4).toLong() and 0xFF) shl 24) or
                ((get(offset + 5).toLong() and 0xFF) shl 16) or
                ((get(offset + 6).toLong() and 0xFF) shl 8) or
                ((get(offset + 7).toLong() and 0xFF))

    /**
     * Write long signed 64-bit integer in the network byte order (Big Endian)
     */
    @JvmStatic
    public fun ByteArray.storeLongAt(offset: Int, value: Long) {
        set(offset, (value ushr 56).toByte())
        set(offset + 1, (value ushr 48).toByte())
        set(offset + 2, (value ushr 40).toByte())
        set(offset + 3, (value ushr 32).toByte())
        set(offset + 4, (value ushr 24).toByte())
        set(offset + 5, (value ushr 16).toByte())
        set(offset + 6, (value ushr 8).toByte())
        set(offset + 7, value.toByte())
    }

    /**
     * Read long unsigned 64-bit integer in the network byte order (Big Endian)
     */
    public inline fun ByteArray.loadULongAt(offset: Int): ULong = loadLongAt(offset).toULong()

    /**
     * Write long unsigned 64-bit integer in the network byte order (Big Endian)
     */
    public inline fun ByteArray.storeULongAt(offset: Int, value: ULong) {
        storeLongAt(offset, value.toLong())
    }
}
