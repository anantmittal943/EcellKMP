package com.anantmittal.ecellkmp.utility.domain

/**
 * Sealed interface representing all possible data-related errors in the application.
 * Divided into Remote (network/backend) and Local (database/storage) errors.
 */
sealed interface DataError : Error {

    /**
     * Remote data errors - Related to network operations, API calls, and Firebase services
     */
    enum class Remote : DataError {
        // Network Errors
        /** No internet connection available */
        NO_INTERNET,

        /** Request took too long to complete */
        REQUEST_TIMEOUT,

        /** Too many requests sent in a short time (Rate limiting) */
        TOO_MANY_REQUESTS,

        // Server Errors
        /** Server returned 5xx error or is unavailable */
        SERVER_ERROR,

        /** Server returned 4xx error (Bad request, Not found, etc.) */
        CLIENT_ERROR,

        // Authentication Errors
        /** User is not authenticated or session expired */
        UNAUTHORIZED,

        /** User does not have permission to access resource */
        FORBIDDEN,

        // Firebase-specific Errors
        /** Firebase Authentication failed */
        AUTH_FAILED,

        /** Email already exists in Firebase Auth */
        EMAIL_ALREADY_EXISTS,

        /** Invalid email format or credentials */
        INVALID_CREDENTIALS,

        /** User account not found in Firebase Auth */
        USER_NOT_FOUND,

        /** Firestore operation failed */
        FIRESTORE_ERROR,

        /** Document not found in Firestore */
        DOCUMENT_NOT_FOUND,

        /** Firestore query returned no results */
        NO_DOCUMENTS_FOUND,

        /** Duplicate entry in Firestore (email, library_id, etc.) */
        DUPLICATE_ENTRY,

        // Data Errors
        /** Failed to parse response from server */
        SERIALIZATION_ERROR,

        /** Response data is invalid or corrupted */
        INVALID_DATA,

        /** Required field is missing in response */
        MISSING_FIELD,

        // Job Cancellation
        /** Operation was cancelled (coroutine cancellation) */
        OPERATION_CANCELLED,

        // General
        /** Unknown or unhandled remote error */
        UNKNOWN
    }

    /**
     * Local data errors - Related to local database, cache, and device storage
     */
    enum class Local : DataError {
        // Database Errors
        /** SQLite database operation failed */
        DATABASE_ERROR,

        /** Failed to insert data into database */
        INSERT_FAILED,

        /** Failed to update data in database */
        UPDATE_FAILED,

        /** Failed to delete data from database */
        DELETE_FAILED,

        /** Query returned no results */
        NOT_FOUND,

        /** Query result is null */
        NULL_RESULT,

        /** Database constraint violation (foreign key, unique, etc.) */
        CONSTRAINT_VIOLATION,

        // Storage Errors
        /** Device storage is full */
        DISK_FULL,

        /** Not enough space to write data */
        INSUFFICIENT_SPACE,

        /** Failed to write to file system */
        WRITE_FAILED,

        /** Failed to read from file system */
        READ_FAILED,

        /** File or directory not found */
        FILE_NOT_FOUND,

        /** No permission to access storage */
        PERMISSION_DENIED,

        // Cache Errors
        /** Failed to cache data */
        CACHE_ERROR,

        /** Cache is corrupted or invalid */
        CACHE_CORRUPTED,

        /** Cache has expired */
        CACHE_EXPIRED,

        // Data Integrity
        /** Data is corrupted or invalid */
        DATA_CORRUPTED,

        /** Failed to map data between layers */
        MAPPING_ERROR,

        /** Data validation failed */
        VALIDATION_ERROR,

        // General
        /** Unknown or unhandled local error */
        UNKNOWN
    }
}

/**
 * Extension function to get a user-friendly error message
 */
fun DataError.toErrorMessage(): String {
    return when (this) {
        // Remote Errors
        DataError.Remote.NO_INTERNET -> "No internet connection. Please check your network."
        DataError.Remote.REQUEST_TIMEOUT -> "Request timed out. Please try again."
        DataError.Remote.TOO_MANY_REQUESTS -> "Too many requests. Please wait a moment."
        DataError.Remote.SERVER_ERROR -> "Server error. Please try again later."
        DataError.Remote.CLIENT_ERROR -> "Request failed. Please check your input."
        DataError.Remote.UNAUTHORIZED -> "Please login to continue."
        DataError.Remote.FORBIDDEN -> "You don't have permission to access this."
        DataError.Remote.AUTH_FAILED -> "Authentication failed. Please try again."
        DataError.Remote.EMAIL_ALREADY_EXISTS -> "This email is already registered."
        DataError.Remote.INVALID_CREDENTIALS -> "Invalid email or password."
        DataError.Remote.USER_NOT_FOUND -> "User account not found."
        DataError.Remote.FIRESTORE_ERROR -> "Database operation failed."
        DataError.Remote.DOCUMENT_NOT_FOUND -> "Data not found."
        DataError.Remote.NO_DOCUMENTS_FOUND -> "No data available."
        DataError.Remote.DUPLICATE_ENTRY -> "This entry already exists."
        DataError.Remote.SERIALIZATION_ERROR -> "Failed to process server response."
        DataError.Remote.INVALID_DATA -> "Received invalid data from server."
        DataError.Remote.MISSING_FIELD -> "Some required information is missing."
        DataError.Remote.OPERATION_CANCELLED -> "Operation was cancelled."
        DataError.Remote.UNKNOWN -> "An unexpected error occurred."

        // Local Errors
        DataError.Local.DATABASE_ERROR -> "Database error occurred."
        DataError.Local.INSERT_FAILED -> "Failed to save data."
        DataError.Local.UPDATE_FAILED -> "Failed to update data."
        DataError.Local.DELETE_FAILED -> "Failed to delete data."
        DataError.Local.NOT_FOUND -> "Data not found in local storage."
        DataError.Local.NULL_RESULT -> "No data available."
        DataError.Local.CONSTRAINT_VIOLATION -> "Data constraint violation."
        DataError.Local.DISK_FULL -> "Device storage is full."
        DataError.Local.INSUFFICIENT_SPACE -> "Not enough storage space."
        DataError.Local.WRITE_FAILED -> "Failed to write to storage."
        DataError.Local.READ_FAILED -> "Failed to read from storage."
        DataError.Local.FILE_NOT_FOUND -> "File not found."
        DataError.Local.PERMISSION_DENIED -> "Storage permission denied."
        DataError.Local.CACHE_ERROR -> "Cache operation failed."
        DataError.Local.CACHE_CORRUPTED -> "Cache is corrupted."
        DataError.Local.CACHE_EXPIRED -> "Cache has expired."
        DataError.Local.DATA_CORRUPTED -> "Data is corrupted."
        DataError.Local.MAPPING_ERROR -> "Failed to process data."
        DataError.Local.VALIDATION_ERROR -> "Data validation failed."
        DataError.Local.UNKNOWN -> "An unexpected error occurred."
    }
}

/**
 * Extension function to check if error is recoverable (can retry)
 */
fun DataError.isRecoverable(): Boolean {
    return when (this) {
        DataError.Remote.NO_INTERNET,
        DataError.Remote.REQUEST_TIMEOUT,
        DataError.Remote.SERVER_ERROR,
        DataError.Remote.OPERATION_CANCELLED -> true

        DataError.Local.CACHE_ERROR,
        DataError.Local.CACHE_EXPIRED -> true

        else -> false
    }
}

/**
 * Extension function to check if error requires authentication
 */
fun DataError.requiresAuth(): Boolean {
    return when (this) {
        DataError.Remote.UNAUTHORIZED,
        DataError.Remote.AUTH_FAILED,
        DataError.Remote.USER_NOT_FOUND -> true

        else -> false
    }
}
