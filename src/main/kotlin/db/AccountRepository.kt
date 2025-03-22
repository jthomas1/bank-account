package org.example.db

import org.example.models.Account
import org.example.models.Customer
import java.util.UUID

class AccountRepository {
    // pretend we have an account database table.
    private val accounts = mutableMapOf<UUID, Account>()

    fun create(customer: Customer): Account {
        val id = UUID.randomUUID()
        val account = Account(id, customer = customer, transactions = listOf())
        accounts[id] = account
        return account
    }

    fun read(id: UUID): Account? {
        return accounts[id]
    }

    fun update(account: Account): Account {
        accounts.replace(account.id, account)
        return account
    }
}