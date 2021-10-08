package com.pokedroid.core

object ShortURL {

    private const val ALPHABET = "VUd5ugM8WTrSPKQXZ1BhOoHkenxDjf4ARNItp26s9v7EqmywbL0aiF3JzlcGYC"
    private const val BASE = ALPHABET.length

    @Suppress("NAME_SHADOWING")
    fun encode(num: Int): String {
        var num = num
        val str = StringBuilder()
        while (num > 0) {
            str.insert(0, ALPHABET[num % BASE])
            num /= BASE
        }
        return str.toString()
    }

    fun decode(str: String): Int {
        var num = 0
        for (element in str) {
            num = num * BASE + ALPHABET.indexOf(element)
        }
        return num
    }

}