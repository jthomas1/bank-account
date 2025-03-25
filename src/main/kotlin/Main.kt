package org.example

import org.example.db.AccountRepository
import org.example.services.AccountService

fun main() {
    REPL(AccountService(AccountRepository()))
}