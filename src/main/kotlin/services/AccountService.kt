package org.example.services

import org.example.db.AccountRepository
import org.example.models.Account
import org.example.models.Customer
import org.example.models.Transaction
import org.example.models.TransactionType
import java.math.BigDecimal
import java.util.*


data class AccountNotFound(val accountId: UUID) : Error("Account with id $accountId not found")
data class InsufficientFunds(val accountId: UUID, val requestedAmount: BigDecimal, val actualAmount: BigDecimal) :
    Error("Insufficient funds in account $accountId. Requested amount: $requestedAmount, actual amount: $actualAmount")

class AccountService(private val storage: AccountRepository) {
    fun create(firstName: String, lastName: String): Account {
        val customer = Customer(firstName = firstName, lastName = lastName)
        return storage.create(customer)
    }

    fun getAccount(accountId: UUID): Account? {
        return storage.read(accountId)
    }

    fun deposit(accountId: UUID, amount: BigDecimal): Result<Account> {
        val account = getAccount(accountId) ?: return Result.failure(AccountNotFound(accountId))
        val transaction = Transaction(amount = amount, type = TransactionType.DEPOSIT)
        return Result.success(storage.update(account.copy(transactions = account.transactions + transaction)))
    }

    fun withdraw(accountId: UUID, amount: BigDecimal): Result<Account> {
        val account = getAccount(accountId) ?: return Result.failure(AccountNotFound(accountId))
        if (account.balance < amount) {
            return Result.failure(InsufficientFunds(accountId, amount, account.balance))
        }
        val transaction = Transaction(amount = amount, type = TransactionType.WITHDRAWAL)
        return Result.success(storage.update(account.copy(transactions = account.transactions + transaction)))
    }
}
