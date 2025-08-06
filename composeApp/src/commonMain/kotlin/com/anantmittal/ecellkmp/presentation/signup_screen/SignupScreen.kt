package com.anantmittal.ecellkmp.presentation.signup_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.presentation.login_screen.components.LoginEcellLogo
import com.anantmittal.ecellkmp.presentation.login_screen.components.LoginFormTextField
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.ColorAccentDark
import com.anantmittal.ecellkmp.utility.presentation.White
import ecellkmp.composeapp.generated.resources.Res
import ecellkmp.composeapp.generated.resources.leaguespartansemireg
import ecellkmp.composeapp.generated.resources.overlockreg
import ecellkmp.composeapp.generated.resources.signup_2
import ecellkmp.composeapp.generated.resources.sm_confmPassword
import ecellkmp.composeapp.generated.resources.sm_email
import ecellkmp.composeapp.generated.resources.sm_library_id
import ecellkmp.composeapp.generated.resources.sm_password
import ecellkmp.composeapp.generated.resources.sm_user
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupScreenRoot(
    viewModel: SignupViewModel = koinViewModel(),
//    onSignupClick: (signupModel: SignupModel) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SignupScreen(
        state = state,
        onAction = { action ->
            /*when (action) {
                is SignupAction.OnSignupClick -> onSignupClick(action.signupModel)
                else -> Unit
            }*/
            viewModel.onAction(action)
        }
    )
}


@Composable
private fun SignupScreen(
    state: SignupState,
    onAction: (SignupAction) -> Unit
) {
    Column(
        modifier = Modifier
            .background(ColorAccentDark)
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
                    text = "Sign Up",
                    color = White,
                    fontSize = 42.sp,
                    fontFamily = FontFamily(Font(Res.font.overlockreg)),
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Let's Get Started",
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(Res.font.overlockreg))
                )
            }

            Image(
                painter = painterResource(Res.drawable.signup_2),
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
            verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.Top)
        ) {
            LoginFormTextField(
                value = state.name,
                onValueChange = {
                    onAction(SignupAction.OnNameChange(it))
                },
                modifier = Modifier,
                label = { Text("Name") },
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_user), contentDescription = null, Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            LoginFormTextField(
                value = state.email,
                onValueChange = {
                    onAction(SignupAction.OnEmailChange(it))
                },
                modifier = Modifier,
                label = { Text("Email") },
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_email), contentDescription = null, Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            LoginFormTextField(
                value = state.kietLibId,
                onValueChange = {
                    onAction(SignupAction.OnKietLibIdChange(it))
                },
                modifier = Modifier,
                label = { Text("KIET Library ID") },
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_library_id), contentDescription = null, Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            LoginFormTextField(
                value = state.password,
                onValueChange = {
                    onAction(SignupAction.OnPasswordChange(it))
                },
                modifier = Modifier,
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_password), contentDescription = null, Modifier.size(18.dp)) },
                visualTransformation = if (state.isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (state.isVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (state.isVisible) "Hide password" else "Show password"

                    IconButton(onClick = { onAction(SignupAction.OnVisibilityChange(!state.isVisible)) }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            LoginFormTextField(
                value = state.cnfmPassword,
                onValueChange = {
                    onAction(SignupAction.OnCnfmPasswordChange(it))
                },
                modifier = Modifier,
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = { Icon(painter = painterResource(Res.drawable.sm_confmPassword), contentDescription = null, Modifier.size(18.dp)) },
                visualTransformation = if (state.isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (state.isVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (state.isVisible) "Hide password" else "Show password"

                    IconButton(onClick = { onAction(SignupAction.OnVisibilityChange(!state.isVisible)) }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            Button(
                onClick = {
                    onAction(
                        SignupAction.OnSignupClick(
                            SignupModel(
                                name = state.name,
                                email = state.email,
                                kietLibId = state.kietLibId,
                                cnfmPassword = state.cnfmPassword
                            )
                        )
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorAccent),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text(
                        text = "Sign Up",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(Res.font.leaguespartansemireg))
                    )
                }
            )
        }

        LoginEcellLogo(
            modifier = Modifier
        )
    }
}
