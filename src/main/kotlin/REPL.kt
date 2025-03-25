package org.example

import org.example.models.*
import org.example.services.AccountService
import kotlin.system.exitProcess

const val bankTeller = "👔"
const val bank = "🏦"


fun help() {
    println("Available commands:")
    println(
        """
        🪪 NewAccount <firstname> <lastname> - Creates a new account for a customer
        🏦 Deposit <amount> <account number> - Deposits money to the account
        💸 Withdraw <amount> <account number> - Withdraws money from the account
        💹 Balance <account number> - Shows the balance of the account
        👋 Quit - Exits the application
        ⁉️ Help - Shows this help message
    """.trimIndent()
    )
}

class REPL(private val service: AccountService) {
    init {
        println("$bank Welcome to Big Banking Application!")
        prompt()
    }

    private fun prompt(message: String? = "How can I help you?") {
        println("\n$bankTeller $message")
        print("> ")
        when (val command = CommandParser.fromString(readln())) {
            is NewAccount -> {
                val account = service.create(command.firstName, command.lastName)
                println("$bankTeller Account created: ${account.id}")
                prompt()
            }

            is Deposit -> {
                service.deposit(command.accountNumber, amount = command.amount)
                println("$bankTeller Deposited ${command.amount} to account ${command.accountNumber}")
                prompt()
            }

            is Withdraw -> {
                service.withdraw(command.accountNumber, amount = command.amount)
                println("$bankTeller Withdrawn ${command.amount} from account ${command.accountNumber}")
                prompt()
            }

            is Balance -> {
                service.getAccount(command.accountNumber)?.let {
                    println("$bankTeller Balance: ${it.balance}")
                    prompt()
                } ?: {
                    println(
                        "Account ${command.accountNumber} not found. Please create an account before trying to access it."
                    )
                    prompt()
                }

            }

            is Help -> {
                help()
                prompt()
            }

            is Quit -> {
                println("Bye!")
                exitProcess(0)
            }

            else -> {
                prompt("I'm sorry, we don't offer that service. Can I help you with something else?")
            }
        }
    }
}