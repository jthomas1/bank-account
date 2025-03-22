package org.example

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class NewAccount: CliktCommand(name = "NewAccount") {
    val firstName: String by argument()
    val lastName: String by argument()

    override fun run() {
        echo("Hello $firstName $lastName")
    }
}

class CLI: CliktCommand() {
    override fun run() = Unit
}