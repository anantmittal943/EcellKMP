package com.anantmittal.ecellkmp.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.presentation.login_screen.LoginScreenRoot
import com.anantmittal.ecellkmp.presentation.login_screen.LoginViewModel
import com.anantmittal.ecellkmp.presentation.signup_screen.SignupScreenRoot
import com.anantmittal.ecellkmp.presentation.signup_screen.SignupViewModel
import com.anantmittal.ecellkmp.presentation.splash_screen.SplashScreen
import com.anantmittal.ecellkmp.utility.presentation.animations.CrossFadeTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
) {
    var showSplash by remember { mutableStateOf(true) }
    val navController = rememberNavController()
    val ecellRepository: EcellRepository = koinInject()
    val currentUser by ecellRepository.currentUser.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
    }

    MaterialTheme {
        CrossFadeTransition(
            showFirst = showSplash,
            animationDurationMs = 1500,
            firstScreen = { SplashScreen() },
            secondScreen = {
                NavHost(
                    navController = navController,
                    startDestination = if (currentUser == null) Route.AuthNavGraph else Route.NormalNavGraph
                ) {
                    // Unauthenticated Navigation Graph
                    navigation<Route.AuthNavGraph>(
                        startDestination = Route.Login
                    ) {
                        composable<Route.Login> {
                            val viewModel = koinViewModel<LoginViewModel>()
                            LoginScreenRoot(
                                viewModel = viewModel,
//                                onLoginClick = { loginModel -> },
                                onSignupClick = {
                                    navController.navigate(Route.Signup)
                                },
                            )
                        }
                        composable<Route.Signup> {
                            val viewModel = koinViewModel<SignupViewModel>()
                            SignupScreenRoot(
                                viewModel = viewModel,
//                                onSignupClick = { signupModel -> },
                            )
                        }
                    }
                    // Authenticated Navigation Graph
                    navigation<Route.NormalNavGraph>(
                        startDestination = Route.Home
                    ) {
                        composable<Route.Home> {}
                    }
                }
            }
        )
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}