package com.example.simplebudgettracker

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.*

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("budget_tracker_prefs", Context.MODE_PRIVATE)
    private val _transactions = mutableStateListOf<Transaction>()
    val transactions: List<Transaction> = _transactions

    init {
        loadTransactions()
    }

    fun addTransaction(description: String, amount: Double) {
        val transaction = Transaction(description, amount)
        _transactions.add(transaction)
        saveTransactions()
    }

    fun removeTransaction(transaction: Transaction) {
        _transactions.remove(transaction)
        saveTransactions()
    }

    private fun saveTransactions() {
        viewModelScope.launch {
            val jsonArray = JSONArray()
            _transactions.forEach {
                val jsonObject = it.toJson()
                jsonArray.put(jsonObject)
            }
            sharedPreferences.edit().putString("transactions", jsonArray.toString()).apply()
        }
    }

    private fun loadTransactions() {
        val jsonString = sharedPreferences.getString("transactions", null) ?: return
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val transaction = Transaction.fromJson(jsonObject)
            _transactions.add(transaction)
        }
    }

    fun calculateDailySpending(): Double {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        return transactions.filter {
            val transactionDate = Calendar.getInstance()
            transactionDate.timeInMillis = it.timestamp
            val transactionDay = transactionDate.get(Calendar.DAY_OF_YEAR)
            val transactionYear = transactionDate.get(Calendar.YEAR)
            transactionDay == currentDay && transactionYear == currentYear
        }.sumOf { it.amount }
    }

    fun calculateWeeklySpending(): Double {
        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        return transactions.filter {
            val transactionDate = Calendar.getInstance()
            transactionDate.timeInMillis = it.timestamp
            val transactionWeek = transactionDate.get(Calendar.WEEK_OF_YEAR)
            val transactionYear = transactionDate.get(Calendar.YEAR)
            transactionWeek == currentWeek && transactionYear == currentYear
        }.sumOf { it.amount }
    }

    fun calculateMonthlySpending(): Double {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        return transactions.filter {
            val transactionDate = Calendar.getInstance()
            transactionDate.timeInMillis = it.timestamp
            val transactionMonth = transactionDate.get(Calendar.MONTH)
            val transactionYear = transactionDate.get(Calendar.YEAR)
            transactionMonth == currentMonth && transactionYear == currentYear
        }.sumOf { it.amount }
    }
}
