package com.example.simplebudgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplebudgettracker.ui.theme.SimpleBudgetTrackerTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.simplebudgettracker.ui.theme.SettingsScreen
import kotlinx.coroutines.launch

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

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") { MainMenu(navController) }
        composable("budget_tracker") { BudgetTrackerApp() }
        composable("settings") { SettingsScreen(navController) }
    }
}

@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("budget_tracker") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Budget Tracker")
        }
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Settings")
        }
    }
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
fun TransactionList(transactions: List<Transaction>, onDelete: (Transaction) -> Unit) {
    Column {
        transactions.forEach { transaction ->
            TransactionItem(transaction = transaction, onDelete = onDelete)
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onDelete: (Transaction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(transaction.description)
        Text("${transaction.amount}")
        IconButton(onClick = { onDelete(transaction) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
