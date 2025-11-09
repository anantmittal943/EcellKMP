package com.anantmittal.ecellkmp.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.presentation.login_screen.components.LoginEcellLogo
import com.anantmittal.ecellkmp.presentation.login_screen.components.LoginFormTextField
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.White
import com.anantmittal.ecellkmp.utility.presentation.components.LoadingIndicator
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.img
import ecellkmp.composeapp.generated.resources.leaguespartansemireg
import ecellkmp.composeapp.generated.resources.overlockreg
import ecellkmp.composeapp.generated.resources.sm_email
import ecellkmp.composeapp.generated.resources.sm_password
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
//    onLoginClick: (loginModel: LoginModel) -> Unit,
    onSignupClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
//                is LoginAction.OnLoginClick -> onLoginClick(action.loginModel)
                is LoginAction.OnSignupClick -> onSignupClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAccentDark)
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .systemGesturesPadding()
                .displayCutoutPadding()
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login",
                    color = White,
                    fontSize = 42.sp,
                    fontFamily = FontFamily(Font(Res.font.overlockreg)),
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "Let's Sign You In",
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(Res.font.overlockreg))
                )
            }

            Image(
                painter = painterResource(Res.drawable.img),
                contentDescription = "login here"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginFormTextField(
                value = state.email,
                onValueChange = {
                    onAction(LoginAction.OnEmailChange(it))
                },
                onUnfocus = { onAction(LoginAction.OnEmailFocusLost) },
                modifier = Modifier,
                error = state.emailError,
                label = { Text("Email") },
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_email), contentDescription = null, Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            LoginFormTextField(
                value = state.password,
                onValueChange = {
                    onAction(LoginAction.OnPasswordChange(it))
                },
                onUnfocus = { onAction(LoginAction.OnPasswordFocusLost) },
                modifier = Modifier,
                error = state.passwordError,
                label = { Text("Password") },
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_password), contentDescription = null, Modifier.size(18.dp)) },
                trailingIcon = {
                    val image = if (state.isVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (state.isVisible) "Hide password" else "Show password"

                    IconButton(onClick = { onAction(LoginAction.OnVisibilityChange(!state.isVisible)) }) {
                        Icon(imageVector = image, description)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (state.isVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Text(
                text = "Forgot Password?",
                color = White,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
                    .padding(end = 8.dp)
                    .clickable(
                        enabled = true,
                        onClick = {},
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onAction(
                        LoginAction.OnLoginClick(
                            LoginModel(
                                email = state.email,
                                password = state.password
                            )
                        )
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorAccent),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                content = {
                    Text(
                        text = "Login",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(Res.font.leaguespartansemireg))
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = buildAnnotatedString {
                    append("Don't Have An Account? ")
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorAccent
                        )
                    ) {
                        append("Signup")
                    }
                },
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = White,
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        onAction(LoginAction.OnSignupClick)
                    }
                )
            )
        }

        LoginEcellLogo(
            modifier = Modifier
        )
    }

        if (state.isLoading) {
            LoadingIndicator()
        }
    }
}
