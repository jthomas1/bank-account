package org.example.services

import org.example.db.AccountRepository
import org.example.models.Account
import org.example.models.Customer
import org.example.models.Transaction
import org.example.models.TransactionType
import java.math.BigDecimal
import java.util.*


class AccountService(private val storage: AccountRepository) {
    fun create(firstName: String, lastName: String): Account {
        val customer = Customer(firstName = firstName, lastName = lastName)
        return storage.create(customer)
    }

    fun deposit(accountId: UUID, amount: BigDecimal): Account {
        val account = storage.read(accountId) ?: throw IllegalArgumentException("Account not found.")
        val transaction = Transaction(amount = amount, type = TransactionType.DEPOSIT)
        return storage.update(account.copy(transactions = account.transactions + transaction))
    }

    fun withdraw(accountId: UUID, amount: BigDecimal): Account {
        val account = storage.read(accountId) ?: throw IllegalArgumentException("Account not found.")
        if (account.balance < amount) {
            throw IllegalArgumentException("Insufficient funds.")
        }
        val transaction = Transaction(amount = amount, type = TransactionType.WITHDRAWAL)
        return storage.update(account.copy(transactions = account.transactions + transaction))
    }

    fun getAccount(accountId: UUID): Account? {
        return storage.read(accountId)
    }
}