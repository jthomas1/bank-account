package org.example.models

import java.math.BigDecimal
import java.util.*

enum class TransactionType {
    DEPOSIT, WITHDRAWAL
}

data class Transaction(val id: UUID = UUID.randomUUID(), val amount: BigDecimal, val type: TransactionType)
