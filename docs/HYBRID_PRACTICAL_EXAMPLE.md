# Practical Example: Adding Events with Hybrid Support

This example shows exactly how to add a new feature (Events) that can use either Firebase or API, controlled by AppConfig.

## Scenario

You want to add Events functionality. You have:

- ✅ Events stored in Firebase (current)
- 🔄 Events API being developed
- ✅ Want to switch between them easily

## Step-by-Step Implementation

### Step 1: Add Feature Flag

```kotlin
// File: AppConfig.kt
object FeatureFlags {
    const val USE_API_FOR_TEAM_MEMBERS = false
    const val USE_API_FOR_EVENTS = false  // ← Add this
    const val ENABLE_API_FALLBACK = true
}
```

### Step 2: Add Events DTO (if not exists)

```kotlin
// File: data/dto/EventDTO.kt
package com.anantmittal.ecellkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDTO(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: Long = 0L,
    val imageUrl: String = "",
    val location: String = ""
)
```

### Step 3: Add Events to RemoteEcellDataSource Interface

```kotlin
// File: data/network/datasource/RemoteEcellDataSource.kt
interface RemoteEcellDataSource {
    suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote>
    suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote>
    suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote>

    // Add this:
    suspend fun getEvents(): Result<List<EventDTO>, DataError.Remote>
}
```

### Step 4: Implement in Firebase

```kotlin
// File: data/network/datasource/FirebaseRemoteEcellDataSource.kt
class FirebaseRemoteEcellDataSource(
    private val firestore: FirebaseFirestore
) : RemoteEcellDataSource {

    // ...existing code...

    override suspend fun getEvents(): Result<List<EventDTO>, DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Firestore: Getting events")

            suspendCoroutine { continuation ->
                firestore.collection("events")
                    .orderBy("date", Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val events = snapshot.documents.map { doc ->
                            EventDTO(
                                id = doc.id,
                                title = doc.getString("title") ?: "",
                                description = doc.getString("description") ?: "",
                                date = doc.getLong("date") ?: 0L,
                                imageUrl = doc.getString("imageUrl") ?: "",
                                location = doc.getString("location") ?: ""
                            )
                        }
                        AppLogger.d(Variables.TAG, "Firestore: Retrieved ${events.size} events")
                        continuation.resume(Result.Success(events))
                    }
                    .addOnFailureListener { exception ->
                        AppLogger.e(Variables.TAG, "Firestore: Error getting events: ${exception.message}")
                        continuation.resume(Result.Error(DataError.Remote.UNKNOWN))
                    }
            }
        } catch (e: Exception) {
            AppLogger.e(Variables.TAG, "Exception in getEvents: ${e.message}")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}
```

### Step 5: Implement in Ktor (Stub for now)

```kotlin
// File: data/network/datasource/KtorRemoteEcellDataSource.kt
class KtorRemoteEcellDataSource : RemoteEcellDataSource {

    // ...existing code...

    override suspend fun getEvents(): Result<List<EventDTO>, DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Ktor: Getting events")

            val response = httpClient.get("/events") {
                parameter("orderBy", "date")
                parameter("order", "desc")
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val events = response.body<List<EventDTO>>()
                    AppLogger.d(Variables.TAG, "Ktor: Retrieved ${events.size} events")
                    Result.Success(events)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.Error(DataError.Remote.UNAUTHORIZED)
                }
                HttpStatusCode.RequestTimeout -> {
                    Result.Error(DataError.Remote.REQUEST_TIMEOUT)
                }
                else -> {
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            AppLogger.e(Variables.TAG, "Ktor: Exception getting events: ${e.message}")
            Result.Error(DataError.Remote.NO_INTERNET)
        }
    }
}
```

### Step 6: Implement Smart Routing in Hybrid

```kotlin
// File: data/network/datasource/HybridRemoteEcellDataSource.kt
class HybridRemoteEcellDataSource(
    private val firebaseSource: RemoteEcellDataSource,
    private val ktorSource: RemoteEcellDataSource
) : RemoteEcellDataSource {

    // ...existing code...

    override suspend fun getEvents(): Result<List<EventDTO>, DataError.Remote> {
        return if (AppConfig.FeatureFlags.USE_API_FOR_EVENTS) {
            AppLogger.d(Variables.TAG, "Hybrid: Using API for getEvents")

            when (val apiResult = ktorSource.getEvents()) {
                is Result.Success -> {
                    AppLogger.d(Variables.TAG, "Hybrid: API getEvents succeeded")
                    apiResult
                }
                is Result.Error -> {
                    if (AppConfig.FeatureFlags.ENABLE_API_FALLBACK) {
                        AppLogger.w(Variables.TAG, "Hybrid: API failed, falling back to Firebase")
                        firebaseSource.getEvents()
                    } else {
                        AppLogger.e(Variables.TAG, "Hybrid: API failed, no fallback enabled")
                        apiResult
                    }
                }
            }
        } else {
            AppLogger.d(Variables.TAG, "Hybrid: Using Firebase for getEvents")
            firebaseSource.getEvents()
        }
    }
}
```

### Step 7: Update Repository Interface

```kotlin
// File: domain/repository/EcellRepository.kt
interface EcellRepository {
    // ...existing methods...

    suspend fun getEvents(): Result<List<EventModel>, DataError.Remote>
}
```

### Step 8: Implement in Repository

```kotlin
// File: data/repository/DefaultEcellRepository.kt
class DefaultEcellRepository(
    private val remoteEcellDataSource: RemoteEcellDataSource,
    // ...
) : EcellRepository {

    // ...existing code...

    override suspend fun getEvents(): Result<List<EventModel>, DataError.Remote> {
        return when (val result = remoteEcellDataSource.getEvents()) {
            is Result.Success -> {
                val events = result.data.map { it.toEventModel() }
                Result.Success(events)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}
```

## Testing the Implementation

### Test 1: Firebase Mode (Current)

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

**Result**: Events come from Firebase ✅

### Test 2: Hybrid Mode with Firebase

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_EVENTS = false  // Use Firebase
```

**Result**: Events routed through Hybrid, but still from Firebase ✅

### Test 3: Hybrid Mode with API

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_EVENTS = true  // Use API
```

**Result**: Events come from API, fallback to Firebase if API fails ✅

### Test 4: API Only

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.KTOR_API
```

**Result**: All operations including events use API ✅

## Migration Path

### Week 1: Firebase Only

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

- ✅ Everything working
- ✅ No changes

### Week 2: Add Hybrid Infrastructure

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_EVENTS = false
```

- ✅ Everything still using Firebase
- ✅ Hybrid routing in place
- ✅ Ready to switch

### Week 3: Test API Endpoint

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_EVENTS = true
const val ENABLE_API_FALLBACK = true
```

- ✅ API being used
- ✅ Automatic fallback if issues
- 🔄 Monitor logs

### Week 4: Production API

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_EVENTS = true
const val ENABLE_API_FALLBACK = true
```

- ✅ Stable API
- ✅ Fallback still enabled
- ✅ Production ready

### Future: All API

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.KTOR_API
```

- ✅ All operations on API
- ✅ Firebase can be deprecated

## Complete Example: Multiple Features

```kotlin
// AppConfig.kt
object AppConfig {
    val CURRENT_DATA_SOURCE = DataSourceType.HYBRID

    object FeatureFlags {
        // Content operations - Use API
        const val USE_API_FOR_TEAM_MEMBERS = true   ✅
        const val USE_API_FOR_EVENTS = true          ✅
        const val USE_API_FOR_ANNOUNCEMENTS = true   ✅

        // New features - Not ready yet
        const val USE_API_FOR_SPONSORS = false       🔄
        const val USE_API_FOR_GALLERY = false        🔄

        // Safety
        const val ENABLE_API_FALLBACK = true
    }
}
```

**Result**:

- Team Members → API (fallback to Firebase)
- Events → API (fallback to Firebase)
- Announcements → API (fallback to Firebase)
- Sponsors → Firebase
- Gallery → Firebase
- Auth Operations → Always Firebase

## Benefits Demonstrated

✅ **Gradual Migration** - One feature at a time  
✅ **Zero Risk** - Always have fallback  
✅ **Easy Testing** - Toggle flags  
✅ **No Code Changes** - Change only AppConfig  
✅ **Instant Rollback** - Set flag to false  
✅ **Clean Logs** - Know exactly what's happening

## Common Patterns

### Pattern 1: Try API First

```kotlin
when (val result = ktorSource.getEvents()) {
    is Result.Success -> result
    is Result.Error -> firebaseSource.getEvents()
}
```

### Pattern 2: Firebase for Critical, API for Content

```kotlin
// Critical operations (auth)
override suspend fun createAccount(...) = firebaseSource.createAccount(...)

// Content operations (can use API)
override suspend fun getEvents(...) = if (USE_API) api else firebase
```

### Pattern 3: Parallel with Merge

```kotlin
val firebaseEvents = async { firebaseSource.getEvents() }
val apiEvents = async { ktorSource.getEvents() }

merge(firebaseEvents.await(), apiEvents.await())
```

This example shows the complete flow for adding any new feature with hybrid support! 🚀

