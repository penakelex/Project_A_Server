package org.l11_3.database.extensions

inline fun <reified Type> Array<Type>.minus(other: Type): Array<Type> {
    if (isEmpty()) return this
    var index = 0
    return Array(size - 1) {
        if (get(index) != other) this@minus[index++]
        else {
            index++
            this@minus[index++]
        }
    }
}