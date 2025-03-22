package org.example.models

import java.util.*

data class Customer(val id: UUID = UUID.randomUUID(), val firstName: String, val lastName: String)