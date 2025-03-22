package db

import org.example.db.AccountRepository
import org.example.models.Customer
import org.example.models.Transaction
import org.example.models.TransactionType
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import kotlin.test.Test

class AccountRepositoryTest {
    @Test
    fun create() {
        val repo = AccountRepository()
        val customer = Customer(firstName = "James", lastName = "Thomas")
        val account = repo.create(customer)
        assertEquals(account.customer, customer)
    }

    @Test
    fun read() {
        val repo = AccountRepository()
        val customer = Customer(firstName = "James", lastName = "Thomas")
        val account = repo.create(customer)
        val readAccount = repo.read(account.id)
        assertEquals(readAccount, account)
    }

    @Test
    fun update() {
        val repo = AccountRepository()
        val customer = Customer(firstName = "James", lastName = "Thomas")
        val account = repo.create(customer)
        val transaction = Transaction(amount = BigDecimal("3.50"), type = TransactionType.DEPOSIT)
        val updatedAccount = repo.update(account.copy(transactions = account.transactions + transaction))
        assertEquals(updatedAccount.transactions.size, 1)
        assertEquals(updatedAccount.transactions.first(), transaction)
    }
}