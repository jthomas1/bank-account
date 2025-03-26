package org.example.models

import java.math.BigDecimal
import java.util.UUID

abstract class CommandFactory {
    abstract fun withValidatedArgs(args: List<String>): Result<Command>
}

sealed interface Command

data class NewAccount(
    val firstName: String, val lastName: String
) : Command {
    companion object Factory : CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 2) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "NewAccount"))
            }
            val (firstName, lastName) = args
            return Result.success(NewAccount(firstName = firstName, lastName = lastName))
        }
    }
}

private fun parseBigDecimal(value: String): Result<BigDecimal> {
    return  runCatching { BigDecimal(value) }
}

private fun parseUUID(value: String): Result<UUID> {
    return runCatching { UUID.fromString(value) }
}

data class Deposit(val amount: BigDecimal, val accountNumber: UUID) : Command {
    companion object Factory : CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 2) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "Deposit"))
            }
            val (amount, accountNumber) = args
            val decimalAmount = parseBigDecimal(amount).getOrNull()
                ?: return Result.failure(CommandParser.InvalidArgumentError("Amount", amount))
            val accountUUID = parseUUID(accountNumber).getOrNull()
                ?: return Result.failure(CommandParser.InvalidArgumentError("Account number", accountNumber))
            return Result.success(Deposit(amount = decimalAmount, accountNumber = accountUUID))
        }
    }
}

data class Withdraw(val amount: BigDecimal, val accountNumber: UUID) : Command {
    companion object Factory : CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 2) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "Withdraw"))
            }
            val (amount, accountNumber) = args
            val decimalAmount = parseBigDecimal(amount).getOrNull()
                ?: return Result.failure(CommandParser.InvalidArgumentError("Amount", amount))
            val accountUUID = parseUUID(accountNumber).getOrNull()
                ?: return Result.failure(CommandParser.InvalidArgumentError("Account number", accountNumber))
            return Result.success(Withdraw(amount = decimalAmount, accountNumber = accountUUID))
        }
    }
}

data class Balance(val accountNumber: UUID) : Command {
    companion object Factory : CommandFactory() {
        override fun withValidatedArgs(args: List<String>): Result<Command> {
            if (args.size != 1) {
                return Result.failure(CommandParser.MissingArgumentsError(commandName = "Balance"))
            }
            val accountNumber = args.first()
            val accountUUID = parseUUID(accountNumber).getOrNull()
                ?: return Result.failure(CommandParser.InvalidArgumentError("Account number", accountNumber))
            return Result.success(Balance(accountNumber = accountUUID))
        }
    }
}

data object Quit : Command
data object Help : Command

object CommandParser {
    data class MissingArgumentsError(val commandName: String) : Error(
        "Incorrect number of arguments for command '$commandName'"
    )

    data class InvalidArgumentError(val argumentName: String, val invalidValue: String) :
        Error("Invalid value: $invalidValue for argument: $argumentName")

    data class UnknownCommandError(val commandString: String) : Error(
        "Unknown command '$commandString'"
    )

    fun fromString(str: String): Result<Command> {
        val parts = str.split(" ")
        val name = parts.first()
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
