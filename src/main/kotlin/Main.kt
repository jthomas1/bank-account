package org.example

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    CLI().subcommands(NewAccount()).main(args)
}