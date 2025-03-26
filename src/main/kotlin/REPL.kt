package org.example

import org.example.models.*
import org.example.services.AccountService
import kotlin.system.exitProcess

const val bankTeller = "👔"
const val bank = "🏦"

private fun tellerSay(message: String) = println("\n$bankTeller $message")

fun help() {
    tellerSay("Available commands:")
    println(
        """
        🪪 NewAccount <firstname> <lastname> - Creates a new account for a customer
        🏦 Deposit <amount> <account number> - Deposits money to the account
        💸 Withdraw <amount> <account number> - Withdraws money from the account
        💹 Balance <account number> - Shows the balance of the account
        👋 Quit - Exits the application
        ❓ Help - Shows this help message
    """.trimIndent()
    )
}

class REPL(private val service: AccountService) {
    init {
        println("$bank Welcome to Big Banking Application!")
        prompt()
    }

    private fun prompt(message: String = "How can I help you?") {
        tellerSay(message)
        print("> ")
        CommandParser.fromString(readln()).fold<Unit, Command>(
            onSuccess = { command ->
                when (command) {
                    is NewAccount -> {
                        val account = service.create(command.firstName, command.lastName)
                        tellerSay("Account created: ${account.id}")
                        prompt()
                    }

                    is Deposit -> {
                        service.deposit(command.accountNumber, amount = command.amount).fold(
                            onSuccess = { tellerSay("Deposited £${command.amount} to account ${command.accountNumber}") },
                            onFailure = { tellerSay(it.message ?: "") }
                        )

                        prompt()
                    }

                    is Withdraw -> {
                        service.withdraw(command.accountNumber, amount = command.amount).fold(
                            onSuccess = { tellerSay("Withdrawn £${command.amount} from account ${command.accountNumber}") },
                            onFailure = { tellerSay(it.message ?: "") }
                        )

                        prompt()
                    }

                    is Balance -> {
                        service.getAccount(command.accountNumber)?.let {
                            tellerSay("Balance: £${it.balance}")
                            prompt()
                        } ?: {
                            tellerSay(
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
                        tellerSay("Bye!")
                        exitProcess(0)
                    }
                }
            },
            onFailure = { error ->
                tellerSay(error.message ?: "")
                prompt()
            }
        )
    }
}
