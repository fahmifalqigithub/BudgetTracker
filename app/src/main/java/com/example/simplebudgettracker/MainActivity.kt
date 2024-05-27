package com.example.simplebudgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplebudgettracker.ui.theme.SimpleBudgetTrackerTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleBudgetTrackerTheme {
                MainScreen()
            }
        }
    }
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") { MainMenu(navController) }
        composable("budget_tracker") { BudgetTrackerApp() }
        composable("report") { ReportScreen(viewModel = viewModel()) }
    }
}

@Composable
fun MainMenu(navController: NavHostController) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("budget_tracker") },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(8.dp)
                .clickable(interactionSource = interactionSource,
                    indication = rememberRipple(),
                    onClick = { navController.navigate("budget_tracker") }
                )
        ) {
            Text("Budget Tracker")
        }

        Button(
            onClick = { navController.navigate("report") },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(8.dp)
                .clickable(interactionSource = interactionSource,
                    indication = rememberRipple(),
                    onClick = { navController.navigate("report") }
                )
        ) {
            Text("Report")
        }
    }
}


fun detectTapGestures(onPress: Any, onRelease: Any) {

}

@Composable
fun BudgetTrackerApp(viewModel: BudgetViewModel = viewModel()) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                if (description.isNotBlank() && amount.isNotBlank()) {
                    viewModel.addTransaction(description, amount.toDouble())
                    description = ""
                    amount = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Add")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TransactionList(transactions = viewModel.transactions, onDelete = { viewModel.removeTransaction(it) })
    }
}

@Composable
fun ReportScreen(viewModel: BudgetViewModel) {
    val weeklySpending = viewModel.calculateWeeklySpending()
    val monthlySpending = viewModel.calculateMonthlySpending()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Weekly Report", style = MaterialTheme.typography.headlineLarge)
        Text("Total Weekly Spending: ${formatCurrency(weeklySpending)}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Monthly Report", style = MaterialTheme.typography.headlineLarge)
        Text("Total Monthly Spending: ${formatCurrency(monthlySpending)}")
    }
}
@Composable
fun TransactionList(transactions: List<Transaction>, onDelete: (Transaction) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(transactions) { transaction ->
            TransactionItem(transaction = transaction, onDelete = onDelete)
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onDelete: (Transaction) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(1.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = transaction.description,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Left
            )
            Text(
                text = formatCurrency(transaction.amount),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { onDelete(transaction) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}