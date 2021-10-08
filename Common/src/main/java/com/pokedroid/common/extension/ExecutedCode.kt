package com.pokedroid.common.extension

/**
 * @author panicDev
 * @created 08/06/18.
 * @project pergikulinerAndroid.
 */
data class ExecutedCode(val executed: Boolean)

infix fun ExecutedCode.otherwise(lambda: () -> Unit) {

    if (!executed) {
        lambda()
    }

}