package com.anantmittal.ecellkmp.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.presentation.account_screen.AccountScreenRoot
import com.anantmittal.ecellkmp.presentation.account_screen.AccountViewModel
import com.anantmittal.ecellkmp.presentation.bottom_navigation.BottomNavItemState
import com.anantmittal.ecellkmp.presentation.bottom_navigation.BottomNavigationBar
import com.anantmittal.ecellkmp.presentation.home_screen.HomeScreenRoot
import com.anantmittal.ecellkmp.presentation.home_screen.HomeViewModel
import com.anantmittal.ecellkmp.presentation.login_screen.LoginScreenRoot
import com.anantmittal.ecellkmp.presentation.login_screen.LoginViewModel
import com.anantmittal.ecellkmp.presentation.signup_screen.SignupScreenRoot
import com.anantmittal.ecellkmp.presentation.signup_screen.SignupViewModel
import com.anantmittal.ecellkmp.presentation.splash_screen.SplashScreen
import com.anantmittal.ecellkmp.presentation.team_detail_screen.TeamDetailScreenRoot
import com.anantmittal.ecellkmp.presentation.team_detail_screen.TeamDetailViewModel
import com.anantmittal.ecellkmp.presentation.team_shared.TeamSharedViewModel
import com.anantmittal.ecellkmp.utility.presentation.animations.CrossFadeTransition
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
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

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentGraphRoute = navBackStackEntry?.destination?.parent?.route
                val showBottomBar =
                    currentGraphRoute == Route.NormalNavGraph.path || currentGraphRoute == Route.TeamNavGraph.path // || currentGraphRoute == Route.AuthNavGraph.path

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(
                                navController = navController,
                                items = when (currentGraphRoute) {
                                    Route.NormalNavGraph.path -> {
                                        listOf(
                                            BottomNavItemState("Home", Icons.Default.Home, Route.Home),
                                            BottomNavItemState("Account", Icons.Default.AccountCircle, Route.Account)
                                        )
                                    }

                                    Route.TeamNavGraph.path -> {
                                        listOf(
                                            BottomNavItemState("Home", Icons.Default.Home, Route.Home),
                                            BottomNavItemState("Meetings", Icons.Default.DateRange, Route.Meetings),
                                            BottomNavItemState("Account", Icons.Default.AccountCircle, Route.Account)
                                        )
                                    }

//                                    Route.AuthNavGraph.path -> {
//                                        listOf(
//                                            BottomNavItemState("Home", Icons.Default.Home, Route.Home),
//                                            BottomNavItemState("Login", Icons.Default.Login, Route.Login)
//                                        )
//                                    }

                                    else -> {
                                        emptyList()
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = if (currentUser == null) {
                            Route.AuthNavGraph
                        } else {
                            Route.NormalNavGraph
                        }
                        // TODO: Add condition for checking TeamUser
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
                            composable<Route.Home> {
                                val viewModel = koinViewModel<HomeViewModel>()
                                HomeScreenRoot(
                                    viewModel = viewModel,
                                    onTeamMemberClick = {}
                                )
                            }
                        }
                        // Authenticated Normal Navigation Graph
                        navigation<Route.NormalNavGraph>(
                            startDestination = Route.Home
                        ) {
                            composable<Route.Home> {
                                val viewModel = koinViewModel<HomeViewModel>()
                                val teamSharedViewModel = it.sharedKoinViewModel<TeamSharedViewModel>(navController)
                                LaunchedEffect(true) {
                                    teamSharedViewModel.selectMember(null)
                                }
                                HomeScreenRoot(
                                    viewModel = viewModel,
                                    onTeamMemberClick = { profile ->
                                        teamSharedViewModel.selectMember((profile))
                                        navController.navigate(Route.TeamDetail(profile.email))
                                    }

                                )
                            }
                            composable<Route.Account> {
                                val viewModel = koinViewModel<AccountViewModel>()
                                AccountScreenRoot(
                                    viewModel = viewModel
                                )
                            }
                            composable<Route.TeamDetail> {
                                val viewModel = koinViewModel<TeamDetailViewModel>()
                                TeamDetailScreenRoot(
                                    viewModel = viewModel,
                                    onBackClick = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }
                        // Authenticated Team Navigation Graph
                        navigation<Route.TeamNavGraph>(
                            startDestination = Route.Home
                        ) {
                            composable<Route.Home> {
                                val viewModel = koinViewModel<HomeViewModel>()
                                HomeScreenRoot(
                                    viewModel = viewModel,
                                    onTeamMemberClick = {}
                                )
                            }
                            composable<Route.Account> {
                                val viewModel = koinViewModel<AccountViewModel>()
                                AccountScreenRoot(
                                    viewModel = viewModel
                                )
                            }
                            composable<Route.Meetings> {}
                            composable<Route.TeamDetail> {}
                            composable<Route.CreateMeeting> {}
                        }
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