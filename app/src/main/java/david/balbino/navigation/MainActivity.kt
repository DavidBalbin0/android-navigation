package david.balbino.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import david.balbino.navigation.screens.LoginScreen
import david.balbino.navigation.screens.MenuScreen
import david.balbino.navigation.screens.OrdersScreen
import david.balbino.navigation.screens.ProfileScreen
import david.balbino.navigation.ui.theme.NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navigation controller to manage transitions between screens
                    val navController = rememberNavController()

                    // Define the NavHost component to host the app screens
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        // Configure enter transition animation
                        enterTransition = {
                            fadeIn(
                                animationSpec = tween (
                                    300, easing = LinearEasing
                                )
                            ) + slideIntoContainer(
                                animationSpec = tween (300, easing = LinearEasing),
                                towards = AnimatedContentTransitionScope.SlideDirection.Start
                            )
                        },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = AnimatedContentTransitionScope.SlideDirection.Start
                            )
                        }
                    ) {
                        // Define the screens available in the app
                        composable(route = "login") { LoginScreen(navController) }
                        composable(route = "menu") { MenuScreen(navController) }

                        composable(
                            route = "orders?number={number}", // Defines a composable screen with a route containing a dynamic parameter named "number"
                            arguments = listOf(
                                // Defines an optional argument named "number" to pass order information
                                navArgument(name = "number") {
                                    defaultValue = "no value" // Sets a default value if the argument is not provided
                                }
                            ),
                        ) {
                            val number = it.arguments?.getString("number") // Retrieves the value of the "number" argument from the incoming navigation arguments
                            OrdersScreen(navController, number!!) // Passes the retrieved order number to the OrdersScreen composable
                        }

                        composable(
                            route = "profile/{name}/{age}", // Defines a composable screen with a route containing two dynamic parameters: "name" and "age"
                            arguments = listOf(
                                // Defines an argument for the user's name, with a default value of "no name"
                                navArgument("name") {
                                    type = NavType.StringType // Specifies the argument type as String
                                    defaultValue = "no name" // Sets a default value if the argument is not provided
                                },
                                // Defines an argument for the user's age, with a default value of 0
                                navArgument("age") {
                                    type = NavType.IntType // Specifies the argument type as Int
                                    defaultValue = 0 // Sets a default value if the argument is not provided
                                }
                            ),
                        ) {
                            // Retrieves the values of the "name" and "age" arguments from the incoming navigation arguments
                            val name = it.arguments?.getString("name")
                            val age = it.arguments?.getInt("age")

                            // Passes the retrieved name and age to the ProfileScreen composable
                            ProfileScreen(navController, name!!, age!!)
                        }


                    }

                }
            }
        }
    }
}
