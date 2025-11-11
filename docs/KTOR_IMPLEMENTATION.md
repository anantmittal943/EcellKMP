# Implementing Ktor Data Source

## Overview

This guide explains how to implement the REST API data source using Ktor client.

## Step 1: Add Ktor Dependencies

Add to `gradle/libs.versions.toml`:

```toml
[versions]
ktor = "2.3.7"

[libraries]
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-android = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
```

Add to `composeApp/build.gradle.kts`:

```kotlin
commonMain.dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
}

androidMain.dependencies {
    implementation(libs.ktor.client.android)
}

iosMain.dependencies {
    implementation(libs.ktor.client.darwin)
}
```

## Step 2: Create Ktor Client Module

Create `KtorClientModule.kt`:

```kotlin
package com.anantmittal.ecellkmp.di

import com.anantmittal.ecellkmp.utility.domain.AppConfig
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }

            install(HttpTimeout) {
                requestTimeoutMillis = AppConfig.ApiConfig.TIMEOUT_SECONDS * 1000
                connectTimeoutMillis = AppConfig.ApiConfig.TIMEOUT_SECONDS * 1000
            }

            defaultRequest {
                url(AppConfig.ApiConfig.BASE_URL)
            }
        }
    }
}
```

## Step 3: Implement KtorRemoteEcellDataSource

```kotlin
package com.anantmittal.ecellkmp.data.network.datasource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.domain.Variables
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class KtorRemoteEcellDataSource(
    private val httpClient: HttpClient
) : RemoteEcellDataSource {

    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Ktor: Creating account for email: ${accountDTO.email}")

            val response = httpClient.post(AppConfig.ApiConfig.Endpoints.CREATE_ACCOUNT) {
                contentType(ContentType.Application.Json)
                setBody(accountDTO)
            }

            when (response.status) {
                HttpStatusCode.Created, HttpStatusCode.OK -> {
                    AppLogger.d(Variables.TAG, "Ktor: Account created successfully")
                    Result.Success(Unit)
                }
                HttpStatusCode.Conflict -> {
                    AppLogger.e(Variables.TAG, "Ktor: Account already exists")
                    Result.Error(DataError.Remote.CONFLICT)
                }
                HttpStatusCode.BadRequest -> {
                    AppLogger.e(Variables.TAG, "Ktor: Bad request")
                    Result.Error(DataError.Remote.BAD_REQUEST)
                }
                HttpStatusCode.Unauthorized -> {
                    AppLogger.e(Variables.TAG, "Ktor: Unauthorized")
                    Result.Error(DataError.Remote.UNAUTHORIZED)
                }
                HttpStatusCode.RequestTimeout -> {
                    AppLogger.e(Variables.TAG, "Ktor: Request timeout")
                    Result.Error(DataError.Remote.REQUEST_TIMEOUT)
                }
                HttpStatusCode.TooManyRequests -> {
                    AppLogger.e(Variables.TAG, "Ktor: Too many requests")
                    Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
                }
                in HttpStatusCode.BadRequest.value..HttpStatusCode.InternalServerError.value -> {
                    AppLogger.e(Variables.TAG, "Ktor: Client error ${response.status}")
                    Result.Error(DataError.Remote.CLIENT_ERROR)
                }
                in HttpStatusCode.InternalServerError.value..599 -> {
                    AppLogger.e(Variables.TAG, "Ktor: Server error ${response.status}")
                    Result.Error(DataError.Remote.SERVER_ERROR)
                }
                else -> {
                    AppLogger.e(Variables.TAG, "Ktor: Unknown error ${response.status}")
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            AppLogger.e(Variables.TAG, "Ktor: Exception creating account: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.Remote.NO_INTERNET)
        }
    }

    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Ktor: Getting account for email: $email")

            val response = httpClient.get(AppConfig.ApiConfig.Endpoints.GET_ACCOUNT) {
                parameter("email", email)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val accountDTO = response.body<AccountDTO>()
                    AppLogger.d(Variables.TAG, "Ktor: Account retrieved successfully")
                    Result.Success(accountDTO)
                }
                HttpStatusCode.NotFound -> {
                    AppLogger.e(Variables.TAG, "Ktor: Account not found")
                    Result.Error(DataError.Remote.DOCUMENT_NOT_FOUND)
                }
                HttpStatusCode.Unauthorized -> {
                    AppLogger.e(Variables.TAG, "Ktor: Unauthorized")
                    Result.Error(DataError.Remote.UNAUTHORIZED)
                }
                HttpStatusCode.RequestTimeout -> {
                    AppLogger.e(Variables.TAG, "Ktor: Request timeout")
                    Result.Error(DataError.Remote.REQUEST_TIMEOUT)
                }
                in HttpStatusCode.BadRequest.value..HttpStatusCode.InternalServerError.value -> {
                    AppLogger.e(Variables.TAG, "Ktor: Client error ${response.status}")
                    Result.Error(DataError.Remote.CLIENT_ERROR)
                }
                in HttpStatusCode.InternalServerError.value..599 -> {
                    AppLogger.e(Variables.TAG, "Ktor: Server error ${response.status}")
                    Result.Error(DataError.Remote.SERVER_ERROR)
                }
                else -> {
                    AppLogger.e(Variables.TAG, "Ktor: Unknown error ${response.status}")
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            AppLogger.e(Variables.TAG, "Ktor: Exception getting account: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.Remote.NO_INTERNET)
        }
    }

    override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Ktor: Getting team members")

            val response = httpClient.get(AppConfig.ApiConfig.Endpoints.GET_TEAM_MEMBERS)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val teamMembers = response.body<List<AccountDTO>>()
                    AppLogger.d(Variables.TAG, "Ktor: Retrieved ${teamMembers.size} team members")
                    Result.Success(teamMembers)
                }
                HttpStatusCode.Unauthorized -> {
                    AppLogger.e(Variables.TAG, "Ktor: Unauthorized")
                    Result.Error(DataError.Remote.UNAUTHORIZED)
                }
                HttpStatusCode.RequestTimeout -> {
                    AppLogger.e(Variables.TAG, "Ktor: Request timeout")
                    Result.Error(DataError.Remote.REQUEST_TIMEOUT)
                }
                in HttpStatusCode.BadRequest.value..HttpStatusCode.InternalServerError.value -> {
                    AppLogger.e(Variables.TAG, "Ktor: Client error ${response.status}")
                    Result.Error(DataError.Remote.CLIENT_ERROR)
                }
                in HttpStatusCode.InternalServerError.value..599 -> {
                    AppLogger.e(Variables.TAG, "Ktor: Server error ${response.status}")
                    Result.Error(DataError.Remote.SERVER_ERROR)
                }
                else -> {
                    AppLogger.e(Variables.TAG, "Ktor: Unknown error ${response.status}")
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            AppLogger.e(Variables.TAG, "Ktor: Exception getting team members: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.Remote.NO_INTERNET)
        }
    }
}
```

## Step 4: Update Modules

```kotlin
// Add networkModule to your app initialization
val sharedModule = module {
    includes(networkModule)

    // ... rest of your modules

    single<RemoteEcellDataSource>(DataSourceQualifiers.KTOR) {
        KtorRemoteEcellDataSource(get()) // Inject HttpClient
    }
}
```

## Step 5: Switch to Ktor Implementation

To switch from Firebase to Ktor:

```kotlin
// In Modules.kt, change:
single<RemoteEcellDataSource> { get(DataSourceQualifiers.FIREBASE) }

// To:
single<RemoteEcellDataSource> { get(DataSourceQualifiers.KTOR) }
```

## Testing

Create test implementation:

```kotlin
class TestRemoteEcellDataSource : RemoteEcellDataSource {
    private val testAccounts = mutableListOf<AccountDTO>()

    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        testAccounts.add(accountDTO)
        return Result.Success(Unit)
    }

    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        val account = testAccounts.find { it.email == email }
        return if (account != null) {
            Result.Success(account)
        } else {
            Result.Error(DataError.Remote.DOCUMENT_NOT_FOUND)
        }
    }

    override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
        return Result.Success(testAccounts)
    }
}
```

## Benefits

✅ **Clean Separation** - Auth and data operations are separate
✅ **Easy Testing** - Mock implementations for testing
✅ **Flexible** - Switch between Firebase and API without code changes
✅ **Scalable** - Add new data sources easily
✅ **Type Safe** - Compile-time safety with Koin qualifiers

