# Using AppConfig and Hybrid Data Source Strategy

## Overview

This guide explains how to use `AppConfig` to control which data source implementation is used, and how to implement a hybrid approach where some operations use Firebase and others
use your API.

## 🎯 The Problem We're Solving

You have:

- ✅ Firebase working for authentication and data
- 🔄 Want to add API for some operations (like team members, events)
- ✅ Don't want to break existing auth flows
- ✅ Want to switch implementations easily without changing code

## 🏗️ The Solution: Hybrid Approach

### Architecture

```
┌─────────────────────────────────────────────────────┐
│              Presentation Layer                      │
│              (No changes needed)                     │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│           DefaultEcellRepository                     │
│        (Depends on RemoteEcellDataSource)           │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────┐
│        RemoteEcellDataSource Interface              │
│                                                      │
│  Selected by AppConfig.CURRENT_DATA_SOURCE          │
└──────────────────┬──────────────────────────────────┘
                   │
        ┌──────────┼──────────┐
        ▼          ▼           ▼
   ┌────────┐ ┌────────┐ ┌──────────────────┐
   │Firebase│ │  Ktor  │ │  Hybrid (Smart)  │
   │        │ │  API   │ │                  │
   │  100%  │ │  100%  │ │ Firebase: Auth   │
   │Firebase│ │  API   │ │ API: Content     │
   └────────┘ └────────┘ └──────────────────┘
```

## 📝 How to Use AppConfig

### Step 1: Understand AppConfig Structure

```kotlin
object AppConfig {
    // Controls which implementation to use
    val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE  // or KTOR_API or HYBRID

    object FeatureFlags {
        // Control specific operations in hybrid mode
        const val USE_API_FOR_TEAM_MEMBERS = false
        const val USE_API_FOR_EVENTS = false
        const val ENABLE_API_FALLBACK = true
    }
}
```

### Step 2: Switch Between Implementations

**Option A: Use Firebase Only (Current - No changes needed)**

```kotlin
// In AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

Result: Everything uses Firebase ✅

**Option B: Use API Only (When fully ready)**

```kotlin
// In AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.KTOR_API
```

Result: Everything uses your API

**Option C: Use Hybrid (Recommended for gradual migration)**

```kotlin
// In AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
```

Result: Smart routing based on operation type

## 🎨 Hybrid Implementation Strategy

### How Hybrid Works

The `HybridRemoteEcellDataSource` intelligently routes operations:

```kotlin
class HybridRemoteEcellDataSource(
    private val firebaseSource: RemoteEcellDataSource,
    private val ktorSource: RemoteEcellDataSource
) : RemoteEcellDataSource {

    // Account operations → Always Firebase (auth-related)
    override suspend fun createAccountDb(...) {
        return firebaseSource.createAccountDb(...)
    }

    override suspend fun getAccountDb(...) {
        return firebaseSource.getAccountDb(...)
    }

    // Content operations → API if enabled, else Firebase
    override suspend fun getTeamMembers() {
        return if (AppConfig.FeatureFlags.USE_API_FOR_TEAM_MEMBERS) {
            // Try API first
            when (val result = ktorSource.getTeamMembers()) {
                is Result.Success -> result
                is Result.Error -> firebaseSource.getTeamMembers() // Fallback
            }
        } else {
            firebaseSource.getTeamMembers()
        }
    }
}
```

### Why This is Perfect for You

✅ **Auth stays with Firebase** - Login/Signup unchanged  
✅ **Content can use API** - Team members, events from API  
✅ **Automatic fallback** - If API fails, uses Firebase  
✅ **No code changes** - Repository doesn't know the difference  
✅ **Easy testing** - Toggle feature flags to test

## 🚀 Step-by-Step: Enable Hybrid Mode

### Step 1: Switch to Hybrid in AppConfig

```kotlin
// File: AppConfig.kt
object AppConfig {
    val CURRENT_DATA_SOURCE = DataSourceType.HYBRID  // ← Change this

    object FeatureFlags {
        const val USE_API_FOR_TEAM_MEMBERS = false  // ← Will change to true when ready
    }
}
```

**Result**: Now using hybrid, but all operations still use Firebase (safe)

### Step 2: Implement Your API Endpoint

When your team members API is ready:

```kotlin
// File: KtorRemoteEcellDataSource.kt
override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
    return try {
        val response = httpClient.get("/team/members")
        when (response.status) {
            HttpStatusCode.OK -> {
                val members = response.body<List<AccountDTO>>()
                Result.Success(members)
            }
            // ... handle other cases
        }
    } catch (e: Exception) {
        Result.Error(DataError.Remote.NO_INTERNET)
    }
}
```

### Step 3: Enable API for Team Members

```kotlin
// File: AppConfig.kt
object FeatureFlags {
    const val USE_API_FOR_TEAM_MEMBERS = true  // ← Change to true
}
```

**Result**: Team members now come from API, everything else from Firebase

### Step 4: Add More Operations Gradually

As you implement more API endpoints:

```kotlin
// AppConfig.kt
object FeatureFlags {
    const val USE_API_FOR_TEAM_MEMBERS = true   // ✅ API ready
    const val USE_API_FOR_EVENTS = true          // ✅ API ready
    const val USE_API_FOR_ANNOUNCEMENTS = false  // 🔄 Not ready yet
}
```

Then update `HybridRemoteEcellDataSource`:

```kotlin
override suspend fun getEvents(): Result<List<EventDTO>, DataError.Remote> {
    return if (AppConfig.FeatureFlags.USE_API_FOR_EVENTS) {
        when (val result = ktorSource.getEvents()) {
            is Result.Success -> result
            is Result.Error -> firebaseSource.getEvents() // Fallback
        }
    } else {
        firebaseSource.getEvents()
    }
}
```

## 📊 Use Cases and Examples

### Use Case 1: Testing New API

```kotlin
// Enable for testing
const val USE_API_FOR_TEAM_MEMBERS = true

// If issues found, quickly disable
const val USE_API_FOR_TEAM_MEMBERS = false
// Back to Firebase immediately, no code changes!
```

### Use Case 2: Gradual Rollout

```kotlin
Week 1:
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_TEAM_MEMBERS = false  // Still Firebase

Week 2 (API ready):
const val USE_API_FOR_TEAM_MEMBERS = true   // Now using API

Week 3 (More endpoints ready):
const val USE_API_FOR_EVENTS = true
```

### Use Case 3: Emergency Fallback

If your API has issues:

```kotlin
// Option 1: Disable specific endpoint
const val USE_API_FOR_TEAM_MEMBERS = false

// Option 2: Switch everything back to Firebase
val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

## 🎯 Real-World Workflow

### Current State (Now)

```kotlin
AppConfig.CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

- Login ✅ Firebase
- Signup ✅ Firebase
- Get Account ✅ Firebase
- Team Members ✅ Firebase
- Everything working as before ✅

### Phase 1: Enable Hybrid (Safe)

```kotlin
AppConfig.CURRENT_DATA_SOURCE = DataSourceType.HYBRID
FeatureFlags.USE_API_FOR_TEAM_MEMBERS = false
```

- Login ✅ Firebase
- Signup ✅ Firebase
- Get Account ✅ Firebase
- Team Members ✅ Firebase (routed through hybrid)
- Everything still working ✅
- Ready to switch when API is ready ✅

### Phase 2: Enable Team Members API

```kotlin
AppConfig.CURRENT_DATA_SOURCE = DataSourceType.HYBRID
FeatureFlags.USE_API_FOR_TEAM_MEMBERS = true
```

- Login ✅ Firebase
- Signup ✅ Firebase
- Get Account ✅ Firebase
- Team Members ✅ API (with Firebase fallback)
- Gradual migration ✅

### Phase 3: Add More APIs

```kotlin
AppConfig.CURRENT_DATA_SOURCE = DataSourceType.HYBRID
FeatureFlags.USE_API_FOR_TEAM_MEMBERS = true
FeatureFlags.USE_API_FOR_EVENTS = true
```

- Login ✅ Firebase
- Signup ✅ Firebase
- Get Account ✅ Firebase
- Team Members ✅ API
- Events ✅ API
- Mix of both ✅

## 🔧 Customizing Hybrid Behavior

### Example: No Fallback for Specific Operations

```kotlin
override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
    return if (AppConfig.FeatureFlags.USE_API_FOR_TEAM_MEMBERS) {
        ktorSource.getTeamMembers()  // No fallback, fail if API fails
    } else {
        firebaseSource.getTeamMembers()
    }
}
```

### Example: Always Try Both

```kotlin
override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
    // Get from both, merge results
    val firebaseResult = firebaseSource.getTeamMembers()
    val apiResult = ktorSource.getTeamMembers()

    return when {
        firebaseResult is Result.Success && apiResult is Result.Success -> {
            // Merge and deduplicate
            Result.Success(mergeResults(firebaseResult.data, apiResult.data))
        }
        firebaseResult is Result.Success -> firebaseResult
        apiResult is Result.Success -> apiResult
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}
```

### Example: Cache-First Strategy

```kotlin
override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
    // Try cache first (Room database)
    val cached = cacheSource.getTeamMembers()
    if (cached is Result.Success && !isCacheStale(cached)) {
        return cached
    }

    // Try API
    if (AppConfig.FeatureFlags.USE_API_FOR_TEAM_MEMBERS) {
        val apiResult = ktorSource.getTeamMembers()
        if (apiResult is Result.Success) {
            cacheSource.saveTeamMembers(apiResult.data)
            return apiResult
        }
    }

    // Fallback to Firebase
    return firebaseSource.getTeamMembers()
}
```

## ✅ Benefits of This Approach

1. **Zero Downtime Migration** - Switch back instantly if issues arise
2. **No Code Changes** - Repository and ViewModels unchanged
3. **Gradual Rollout** - One feature at a time
4. **Easy Testing** - Toggle flags for testing
5. **Automatic Fallback** - Resilient to API failures
6. **Clean Architecture** - Separation of concerns maintained
7. **Feature Flags** - Control without rebuilding

## 🎓 Summary

**To use Hybrid mode:**

1. Set `AppConfig.CURRENT_DATA_SOURCE = DataSourceType.HYBRID`
2. Implement API endpoints in `KtorRemoteEcellDataSource`
3. Enable feature flags as endpoints become ready
4. No changes needed in Repository or ViewModels

**The magic:**

- Change ONE value in AppConfig
- Control EVERYTHING from there
- No code changes in business logic
- Safe, gradual, reversible migration

Your current implementation keeps working, and you can gradually add API support without any risk! 🚀

