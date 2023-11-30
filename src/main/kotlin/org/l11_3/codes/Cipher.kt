package org.l11_3.codes

import kotlin.math.pow

private const val ZERO = 0
private const val ONE = 1
private const val NINE = 9
private const val CONSTANT = 31
private const val ZERO_CHARACTER = '0'
private const val ALPHABET = "123456789"


//Hash
fun Any.cipher(): String = buildString {
    (if (this@cipher is String) this@cipher else this@cipher.toString()).forEach { character ->
        append("${character.code.minus(CONSTANT).toCode()}$ZERO")
    }
}


private fun Int.toCode(): String = buildString {
    var number = this@toCode
    while (number > ZERO) {
        insert(ZERO, ALPHABET[number.rem(NINE)])
        number = number.div(NINE)
    }
}.reversed()

//UnHash
fun String.unCipher(): String {
    if (isEmpty()) return this
    val indexes: List<Int> = getZeroes()
    return buildString {
        append(this@unCipher.getCharacter(ZERO, indexes[ZERO]))
        for (i in ZERO until indexes.lastIndex) append(
            this@unCipher.getCharacter(indexes[i].plus(ONE), indexes[i.plus(ONE)])
        )
    }

}

private fun String.toCharFromCode(): Char {
    var code = ZERO
    var power = ZERO
    for (digit in this) code = code.plus(digit.digitToInt().minus(ONE).times(NINE.toDouble().pow(power++).toInt()))
    return code.plus(CONSTANT).toChar()
}

private fun String.getCharacter(
    startIndex: Int, endIndex: Int
): Char = substring(startIndex, endIndex).toCharFromCode()

private fun String.getZeroes(): List<Int> = buildList {
    for ((index, character) in this@getZeroes.withIndex()) if (character == ZERO_CHARACTER) add(index)
}