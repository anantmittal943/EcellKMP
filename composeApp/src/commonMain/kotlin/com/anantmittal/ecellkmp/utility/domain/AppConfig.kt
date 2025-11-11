package com.anantmittal.ecellkmp.utility.domain

/**
 * Configuration object for managing app-wide settings and feature flags.
 *
 * This allows you to switch between different implementations without changing business logic.
 */
object AppConfig {
    /**
     * Data source configuration
     * Change this to switch between Firebase and API implementations
     */
    enum class DataSourceType {
        FIREBASE,  // Use Firebase Firestore
        KTOR_API,  // Use REST API with Ktor
        HYBRID     // Use both (Firebase for some operations, API for others)
    }

    /**
     * Current data source being used
     * Change this value to switch implementations
     */
    val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE

    /**
     * Feature flags
     */
    object FeatureFlags {
        // Enable/disable specific features
        const val ENABLE_OFFLINE_MODE = true
        const val ENABLE_ANALYTICS = false
        const val ENABLE_CRASH_REPORTING = false

        // Data source specific flags
        const val CACHE_TEAM_MEMBERS = true
        const val AUTO_SYNC_ACCOUNT = true

        // Hybrid mode flags - Control which operations use API vs Firebase
        const val USE_API_FOR_TEAM_MEMBERS = false  // Set to true to use API for team members
        const val USE_API_FOR_EVENTS = false         // Set to true when events API is ready
        const val USE_API_FOR_ANNOUNCEMENTS = false  // Set to true when announcements API is ready
        const val ENABLE_API_FALLBACK = true         // Fallback to Firebase if API fails
    }

    /**
     * API Configuration (for future Ktor implementation)
     */
    object ApiConfig {
        const val BASE_URL = "https://api.ecellkiet.org"
        const val TIMEOUT_SECONDS = 30L
        const val MAX_RETRIES = 3

        object Endpoints {
            const val CREATE_ACCOUNT = "/accounts/create"
            const val GET_ACCOUNT = "/accounts/get"
            const val GET_TEAM_MEMBERS = "/team/members"
        }
    }

    /**
     * Firebase Configuration
     */
    object FirebaseConfig {
        const val COLLECTION_TEAM_MEMBERS = "teamMembers"
        const val ENABLE_PERSISTENCE = true
    }

    /**
     * Database Configuration
     */
    object DatabaseConfig {
        const val DB_NAME = "ecell_accounts.db"
        const val DB_VERSION = 2
    }
}

