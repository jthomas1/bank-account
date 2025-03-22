package models

import org.example.models.Account
import org.example.models.Customer
import org.example.models.Transaction
import org.example.models.TransactionType
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal

class AccountTest {

    @Test
    fun getBalanceDefault() {
        val customer = Customer(firstName = "James", lastName = "Thomas")
        val account = Account(customer = customer, transactions = listOf())
        assertEquals(BigDecimal(0), account.balance)
    }

    @Test
    fun getBalance() {
        val customer = Customer(firstName = "James", lastName = "Thomas")
        val transactions = listOf(
            Transaction(amount = BigDecimal(10), type = TransactionType.DEPOSIT),
            Transaction(amount = BigDecimal(5), type = TransactionType.WITHDRAWAL)
        )
        val account = Account(customer = customer, transactions = transactions)
        assertEquals(BigDecimal(5), account.balance)
    }

}