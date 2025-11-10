# Error Handling Guide

## Overview

The E-Cell KMP app uses a comprehensive error handling system based on the `DataError` sealed interface. This guide explains how to use and handle errors throughout the
application.

## DataError Structure

```kotlin
sealed interface DataError : Error {
    enum class Remote : DataError { ... }
    enum class Local : DataError { ... }
}
```

### Remote Errors (`DataError.Remote`)

Remote errors occur during network operations, API calls, and Firebase services.

#### Network Errors

- `NO_INTERNET` - No internet connection available
- `REQUEST_TIMEOUT` - Request took too long to complete
- `TOO_MANY_REQUESTS` - Rate limiting (too many requests)

#### Server Errors

- `SERVER_ERROR` - Server returned 5xx error
- `CLIENT_ERROR` - Server returned 4xx error

#### Authentication Errors

- `UNAUTHORIZED` - User not authenticated or session expired
- `FORBIDDEN` - User lacks permission
- `AUTH_FAILED` - Firebase Authentication failed
- `EMAIL_ALREADY_EXISTS` - Email already registered
- `INVALID_CREDENTIALS` - Invalid email or password
- `USER_NOT_FOUND` - User account not found

#### Firestore Errors

- `FIRESTORE_ERROR` - Firestore operation failed
- `DOCUMENT_NOT_FOUND` - Document not found
- `NO_DOCUMENTS_FOUND` - Query returned no results
- `DUPLICATE_ENTRY` - Duplicate entry (email, library_id, etc.)

#### Data Errors

- `SERIALIZATION_ERROR` - Failed to parse response
- `INVALID_DATA` - Response data is invalid
- `MISSING_FIELD` - Required field missing

#### Job Cancellation

- `OPERATION_CANCELLED` - Coroutine was cancelled

#### General

- `UNKNOWN` - Unknown or unhandled error

### Local Errors (`DataError.Local`)

Local errors occur during local database operations and device storage access.

#### Database Errors

- `DATABASE_ERROR` - SQLite operation failed
- `INSERT_FAILED` - Failed to insert data
- `UPDATE_FAILED` - Failed to update data
- `DELETE_FAILED` - Failed to delete data
- `NOT_FOUND` - Query returned no results
- `NULL_RESULT` - Query result is null
- `CONSTRAINT_VIOLATION` - Database constraint violation

#### Storage Errors

- `DISK_FULL` - Device storage is full
- `INSUFFICIENT_SPACE` - Not enough space
- `WRITE_FAILED` - Failed to write to storage
- `READ_FAILED` - Failed to read from storage
- `FILE_NOT_FOUND` - File not found
- `PERMISSION_DENIED` - No storage permission

#### Cache Errors

- `CACHE_ERROR` - Cache operation failed
- `CACHE_CORRUPTED` - Cache is corrupted
- `CACHE_EXPIRED` - Cache has expired

#### Data Integrity

- `DATA_CORRUPTED` - Data is corrupted
- `MAPPING_ERROR` - Failed to map data
- `VALIDATION_ERROR` - Data validation failed

#### General

- `UNKNOWN` - Unknown or unhandled error

## Extension Functions

### `toErrorMessage()`

Converts a `DataError` to a user-friendly message:

```kotlin
val error: DataError.Remote = DataError.Remote.NO_INTERNET
val message = error.toErrorMessage()
// Returns: "No internet connection. Please check your network."
```

### `isRecoverable()`

Checks if an error is recoverable (can retry):

```kotlin
val error: DataError.Remote = DataError.Remote.REQUEST_TIMEOUT
if (error.isRecoverable()) {
    // Show retry button
}
```

**Recoverable errors:**

- `NO_INTERNET`
- `REQUEST_TIMEOUT`
- `SERVER_ERROR`
- `OPERATION_CANCELLED`
- `CACHE_ERROR`
- `CACHE_EXPIRED`

### `requiresAuth()`

Checks if an error requires re-authentication:

```kotlin
val error: DataError.Remote = DataError.Remote.UNAUTHORIZED
if (error.requiresAuth()) {
    // Navigate to login screen
}
```

**Auth-required errors:**

- `UNAUTHORIZED`
- `AUTH_FAILED`
- `USER_NOT_FOUND`

## Usage Examples

### In Repository Layer

```kotlin
override suspend fun loadAccount(email: String): Result<AccountModel, DataError.Remote> {
    return try {
        when (val result = firebaseAuth.getAccount(email)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(DataError.Remote.FIRESTORE_ERROR)
        }
    } catch (e: FirebaseAuthException) {
        Result.Error(DataError.Remote.AUTH_FAILED)
    } catch (e: FirebaseFirestoreException) {
        Result.Error(DataError.Remote.FIRESTORE_ERROR)
    } catch (e: Exception) {
        Result.Error(DataError.Remote.UNKNOWN)
    }
}
```

### In ViewModel Layer

```kotlin
fun onAction(action: Action.LoadData) {
    viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        when (val result = repository.loadData()) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        data = result.data,
                        isLoading = false,
                        error = null
                    )
                }
            }
            is Result.Error -> {
                val errorMessage = result.error.toErrorMessage()
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = UiText.DynamicString(errorMessage)
                    )
                }

                // Handle specific errors
                when {
                    result.error.requiresAuth() -> {
                        // Navigate to login
                    }
                    result.error.isRecoverable() -> {
                        // Show retry button
                    }
                }
            }
        }
    }
}
```

### In UI Layer

```kotlin
@Composable
fun Screen(state: ScreenState, onAction: (Action) -> Unit) {
    when {
        state.isLoading -> LoadingIndicator()

        state.error != null -> {
            ErrorMessage(
                message = state.error.asString(),
                onRetry = { onAction(Action.Retry) }
            )
        }

        state.data != null -> {
            DataDisplay(state.data)
        }
    }
}
```

## Best Practices

### 1. Always Use Specific Errors

❌ **Bad:**

```kotlin
catch(e: Exception) {
    Result.Error(DataError.Remote.UNKNOWN)
}
```

✅ **Good:**

```kotlin
catch(e: FirebaseAuthException) {
    Result.Error(DataError.Remote.AUTH_FAILED)
} catch (e: FirebaseFirestoreException) {
    Result.Error(DataError.Remote.FIRESTORE_ERROR)
} catch (e: Exception) {
    Result.Error(DataError.Remote.UNKNOWN)
}
```

### 2. Log Errors for Debugging

```kotlin
catch(e: Exception) {
    e.printStackTrace()
    AppLogger.e(TAG, "Error loading account: ${e.message}")
    Result.Error(DataError.Remote.UNKNOWN)
}
```

### 3. Provide User-Friendly Messages

```kotlin
val message = when (error) {
    DataError.Remote.NO_INTERNET -> "Please check your internet connection"
    DataError.Remote.AUTH_FAILED -> "Login failed. Please try again"
    else -> error.toErrorMessage()
}
```

### 4. Handle Recoverable Errors

```kotlin
if (error.isRecoverable()) {
    _state.update {
        it.copy(
            showRetryButton = true,
            error = UiText.DynamicString(error.toErrorMessage())
        )
    }
}
```

### 5. Handle Auth Errors

```kotlin
if (error.requiresAuth()) {
    navigationController.navigate(Route.Login)
}
```

## Error Categories Summary

| Category | Error Type                        | User Action             |
|----------|-----------------------------------|-------------------------|
| Network  | `NO_INTERNET`, `REQUEST_TIMEOUT`  | Check connection, Retry |
| Auth     | `UNAUTHORIZED`, `AUTH_FAILED`     | Login again             |
| Data     | `NOT_FOUND`, `INVALID_DATA`       | Refresh data            |
| Storage  | `DISK_FULL`, `INSUFFICIENT_SPACE` | Free up space           |
| System   | `OPERATION_CANCELLED`, `UNKNOWN`  | Retry operation         |

## Testing Error Handling

### Unit Tests

```kotlin
@Test
fun `test error handling for no internet`() = runTest {
        // Given
        val error = DataError.Remote.NO_INTERNET

        // Then
        assertTrue(error.isRecoverable())
        assertFalse(error.requiresAuth())
        assertEquals("No internet connection. Please check your network.", error.toErrorMessage())
    }
```

### UI Tests

```kotlin
@Test
fun `test error message displayed`() {
    composeTestRule.setContent {
        Screen(
            state = ScreenState(error = UiText.DynamicString("Error message")),
            onAction = {}
        )
    }

    composeTestRule.onNodeWithText("Error message").assertIsDisplayed()
}
```

## Conclusion

The DataError system provides:

- ✅ Type-safe error handling
- ✅ Clear error categories
- ✅ User-friendly messages
- ✅ Recoverable error detection
- ✅ Auth error handling
- ✅ Comprehensive error coverage

Use this system consistently throughout the app for better error management and user experience.

