package org.example.models

import java.math.BigDecimal
import java.util.UUID

abstract class CommandFactory {
    abstract fun withValidatedArgs(args: List<String>): Result<Command>
}

sealed interface Command

data class NewAccount(
    val firstName: String,
    val lastName: String
) : Command {
    companion object Factory: CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 2) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "NewAccount"))
            }
            return Result.success(NewAccount(firstName = args[0], lastName = args[1]))
        }
    }
}

data class Deposit(val amount: BigDecimal, val accountNumber: UUID) : Command {
    companion object Factory: CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 2) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "Deposit"))
            }
            return Result.success(Deposit(amount = BigDecimal(args[0]), accountNumber = UUID.fromString(args[1])))
        }
    }
}

data class Withdraw(val amount: BigDecimal, val accountNumber: UUID) : Command {
    companion object Factory: CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 2) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "Withdraw"))
            }
            return Result.success(Withdraw(amount = BigDecimal(args[0]), accountNumber = UUID.fromString(args[1])))
        }
    }
}

data class Balance(val accountNumber: UUID) : Command {
    companion object Factory: CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 1) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "Balance"))
            }
            return Result.success(Balance(accountNumber = UUID.fromString(args[0])))
        }
    }
}

data object Quit : Command
data object Help : Command

object CommandParser {
    data class MissingArgumentsError(val commandName: String) :
        Error(
            "Not enough arguments for command '$commandName'"
        )

    data class UnknownCommandError(val commandString: String) :
        Error(
            "Unknown command '$commandString'"
        )

    private fun parseArgs(args: List<String>, numExpected: Int, name: String)  {
        if (args.size != numExpected) {
            throw MissingArgumentsError(commandName = name)
        }
    }

    fun fromString(str: String): Result<Command> {
        val parts = str.split(" ")
        val name = parts[0]
        val args = parts.drop(1)
        return when (name.lowercase()) {
            "newaccount" -> NewAccount.withValidatedArgs(args)
            "deposit" -> Deposit.withValidatedArgs(args)
            "withdraw" -> Withdraw.withValidatedArgs(args)
            "balance" -> Balance.withValidatedArgs(args)
            "quit" -> Result.success(Quit)
            "help" -> Result.success(Help)
            else -> Result.failure(UnknownCommandError(commandString = str))
        }
    }
}
