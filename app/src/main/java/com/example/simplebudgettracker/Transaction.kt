package com.example.simplebudgettracker

import org.json.JSONObject

data class Transaction(
    val description: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("description", description)
        jsonObject.put("amount", amount)
        jsonObject.put("timestamp", timestamp)
        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject): Transaction {
            val description = jsonObject.getString("description")
            val amount = jsonObject.getDouble("amount")
            val timestamp = jsonObject.getLong("timestamp")
            return Transaction(description, amount, timestamp)
        }
    }
}
