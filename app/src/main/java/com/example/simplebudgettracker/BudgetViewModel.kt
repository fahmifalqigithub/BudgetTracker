package com.example.simplebudgettracker

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {
    private val _transactions = mutableStateListOf<Transaction>()
    val transactions: List<Transaction> = _transactions

    fun addTransaction(description: String, amount: Double) {
        _transactions.add(Transaction(description, amount))
    }

    fun removeTransaction(transaction: Transaction) {
        _transactions.remove(transaction)
    }
}
