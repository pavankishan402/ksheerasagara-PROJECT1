package com.example.ksheerasagara.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ksheerasagara.ui.screens.AISuggestionScreen
import com.example.ksheerasagara.ui.screens.AddMilkEntryScreen
import com.example.ksheerasagara.ui.screens.CowManagementScreen
import com.example.ksheerasagara.ui.screens.DailyPassbookScreen
import com.example.ksheerasagara.ui.screens.ExpenseScreen
import com.example.ksheerasagara.ui.screens.HomeScreen
import com.example.ksheerasagara.ui.screens.LoginScreen
import com.example.ksheerasagara.ui.screens.MonthlyPassbookScreen
import com.example.ksheerasagara.ui.screens.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // SPLASH SCREEN
        composable("splash") {

            SplashScreen(navController)
        }

        // LOGIN SCREEN
        composable("login") {

            LoginScreen(

                onLoginSuccess = {

                    navController.navigate("home") {

                        popUpTo("login") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // HOME SCREEN
        composable("home") {

            HomeScreen(

                onAddMilkClick = {
                    navController.navigate("add_milk")
                },

                onDailyPassbookClick = {
                    navController.navigate("daily_passbook")
                },

                onMonthlyPassbookClick = {
                    navController.navigate("monthly_passbook")
                },

                onExpenseClick = {
                    navController.navigate("expense")
                },

                onProfitClick = {
                    navController.navigate("cows")
                },

                onAISuggestionClick = {
                    navController.navigate("ai_suggestion")
                },

                onLogoutClick = {

                    navController.navigate("login") {

                        popUpTo(0)

                        launchSingleTop = true
                    }
                }
            )
        }

        // ADD MILK SCREEN
        composable("add_milk") {

            AddMilkEntryScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // DAILY PASSBOOK SCREEN
        composable("daily_passbook") {

            DailyPassbookScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // MONTHLY PASSBOOK SCREEN
        composable("monthly_passbook") {

            MonthlyPassbookScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // EXPENSE SCREEN
        composable("expense") {

            ExpenseScreen(

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // COW MANAGEMENT SCREEN
        composable("cows") {

            CowManagementScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onHomeClick = {
                    navController.navigate("home")
                },

                onExpenseClick = {
                    navController.navigate("expense")
                },

                onAIClick = {
                    navController.navigate("ai_suggestion")
                }
            )
        }

        // AI SUGGESTION SCREEN
        composable("ai_suggestion") {

            AISuggestionScreen(

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}