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
        FIREBASE,
        KTOR_API,
        HYBRID
    }

    /**
     * Current data source being used
     * Change this value to switch implementations
     */
    val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
    const val TAG = "xyz"

    /**
     * Feature flags
     */
    object FeatureFlags {
        // Data source specific flags
        const val CACHE_TEAM_MEMBERS = true
        const val AUTO_SYNC_ACCOUNT = true

        // Hybrid mode flags - Control which operations use API vs Firebase
        const val USE_API_FOR_TEAM_MEMBERS = false
        const val USE_API_FOR_EVENTS = false
        const val USE_API_FOR_ANNOUNCEMENTS = false
        const val ENABLE_API_FALLBACK = true
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
        const val DAY_SCHOLAR_TAG: String = "DAY SCHOLAR"
        const val HOSTELER_TAG: String = "HOSTELER"
        const val SHARED_PREFS: String = "Shared Prefs"
        const val EMAIL_PREFS: String = "Email"
        const val ON_BOARD_STATUS_PREFS: String = "OnBoarding Status"
        const val LOGIN_STATUS_PREFS: String = "Login Status"
        const val END_PASSWORD_PREFS: String = "Endeavour Password"
        const val END_UID_PREFS: String = "Endeavour UID"
        const val END_TOKEN_PREFS: String = "Endeavour Token"
        const val TEAM_MODE_PREFS: String = "TEAM MODE"
        const val SETTINGS_TAG = "Settings"
        const val BANNERS_TAG = "Banners"
        const val TEAM_MEMBERS_TAG = "Team Members"
        const val EVENTS_TAG = "Events"
        const val MEETINGS_TAG = "Meetings"
        const val MEET_ABSENTEES_REASON_TAG = "Meet Absentees Reason"
        const val DOMAINS_TAG = "Domains"
        const val END_EVENTS_ATTENDANCE = "Endeavour Events Attendance"
        const val END_EVENTS_TAG = "EndEvents"
        const val END_SPEAKERS_TAG = "EndSpeakers"
        const val END_SPONSORS_TAG = "EndSponsors"
        const val USER_ACCESS: String = "USER ACCESS"
        const val TEAM_ACCESS: String = "TEAM ACCESS"
        const val ADMIN_ACCESS: String = "ADMIN ACCESS"
        const val USER_ACCOUNT: String = "USER"
        const val TEAM_MEMBER_ACCOUNT: String = "TEAM MEMBER"
        const val STATUS_PENDING: String = "PENDING"
        const val STATUS_IN_REVIEW: String = "IN REVIEW"
        const val STATUS_VERIFIED: String = "VERIFIED"
        const val STATUS_BLOCKED: String = "BLOCKED"
        const val MEET_OFFLINE: String = "0"
        const val MEET_ONLINE: String = "1"
        const val TOPIC_ALL: String = "ALL"
        const val TOPIC_TEAM: String = "Team"
        const val TOPIC_ADMIN: String = "Admin"
        const val TOPIC_1_YEAR: String = "Year_1"
        const val TOPIC_2_YEAR: String = "Year_2"
        const val TOPIC_3_YEAR: String = "Year_3"
        const val TOPIC_4_YEAR: String = "Year_4"
    }

    /**
     * Database Configuration
     */
    object DatabaseConfig {
        const val DB_NAME = "ecell_accounts.db"
        const val DB_VERSION = 2
    }
}

