package org.example.models

import java.math.BigDecimal
import java.util.*

data class Account(val id: UUID = UUID.randomUUID(), val customer: Customer, val transactions: List<Transaction>) {
    val balance: BigDecimal
        get() = transactions.fold(BigDecimal(0)) { acc, transaction ->
            when (transaction.type) {
                TransactionType.DEPOSIT -> acc + transaction.amount
                TransactionType.WITHDRAWAL -> acc - transaction.amount
            }
        }
}
