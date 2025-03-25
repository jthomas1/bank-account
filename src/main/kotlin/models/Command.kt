package org.example.models

import java.math.BigDecimal
import java.util.UUID

sealed interface Command

data class NewAccount(
    val firstName: String,
    val lastName: String
) : Command

data class Deposit(val amount: BigDecimal, val accountNumber: UUID) : Command

data class Withdraw(val amount: BigDecimal, val accountNumber: UUID) : Command

data class Balance(val accountNumber: UUID) : Command

data object Quit : Command
data object Help : Command
data object CommandNotFound : Command

object CommandParser {
    fun fromString(str: String): Command {
        val parts = str.split(" ")
        return when (parts[0].lowercase()) {
            "newaccount" -> NewAccount(firstName = parts[1], lastName = parts[2])
            "deposit" -> Deposit(amount = BigDecimal(parts[1]), accountNumber = UUID.fromString(parts[2]))
            "withdraw" -> Withdraw(amount = BigDecimal(parts[1]), accountNumber = UUID.fromString(parts[2]))
            "balance" -> Balance(accountNumber = UUID.fromString(parts[1]))
            "quit" -> Quit
            "help" -> Help
            else -> CommandNotFound
        }
    }
}
