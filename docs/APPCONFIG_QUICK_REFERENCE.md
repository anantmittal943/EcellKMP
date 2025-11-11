# AppConfig Quick Reference Card

## 🎯 How to Switch Data Sources

### Current Setup (Firebase Only)

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

**Result**: All operations use Firebase ✅

---

### Enable Hybrid Mode

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID

object FeatureFlags {
    const val USE_API_FOR_TEAM_MEMBERS = false  // Start with false
}
```

**Result**: Routes through hybrid, but still uses Firebase for everything ✅

---

### Enable API for Team Members

```kotlin
// AppConfig.kt
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID

object FeatureFlags {
    const val USE_API_FOR_TEAM_MEMBERS = true  // Now using API!
}
```

**Result**:

- Team Members → API (with Firebase fallback)
- Everything else → Firebase
  ✅

---

## 🛠️ What Each Mode Does

| Mode         | Account Operations | Team Members  | Events        | Use Case           |
|--------------|--------------------|---------------|---------------|--------------------|
| **FIREBASE** | Firebase           | Firebase      | Firebase      | Current production |
| **KTOR_API** | API                | API           | API           | Future full API    |
| **HYBRID**   | Firebase           | API/Firebase* | API/Firebase* | Gradual migration  |

*Based on feature flags

---

## 📝 Adding New Operations to Hybrid

When you add a new feature (e.g., Events):

### Step 1: Add Feature Flag

```kotlin
// AppConfig.kt
object FeatureFlags {
    const val USE_API_FOR_EVENTS = false  // Add this
}
```

### Step 2: Implement in Ktor

```kotlin
// KtorRemoteEcellDataSource.kt
override suspend fun getEvents(): Result<List<EventDTO>, DataError.Remote> {
    // Your API implementation
}
```

### Step 3: Add to Hybrid

```kotlin
// HybridRemoteEcellDataSource.kt
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

### Step 4: Enable When Ready

```kotlin
// AppConfig.kt
const val USE_API_FOR_EVENTS = true  // Enable!
```

---

## ⚡ Quick Actions

### Emergency: Switch Back to Firebase

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.FIREBASE
```

### Test API Endpoint

```kotlin
val CURRENT_DATA_SOURCE = DataSourceType.HYBRID
const val USE_API_FOR_TEAM_MEMBERS = true
```

### Disable Problematic API Endpoint

```kotlin
// Keep hybrid, just disable one endpoint
const val USE_API_FOR_TEAM_MEMBERS = false
```

---

## 🎨 Current Architecture

```
                    AppConfig
                       │
                       ▼
              [CURRENT_DATA_SOURCE]
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
    FIREBASE      KTOR_API        HYBRID
        │              │              │
        │              │              ├─► Firebase (Auth ops)
        │              │              └─► Ktor (Content ops)
        │              │
        └──────────────┴──────────────┘
                       │
                       ▼
           DefaultEcellRepository
           (No changes needed!)
```

---

## ✅ Checklist: Enabling Hybrid for New Feature

- [ ] Add feature flag to `AppConfig.FeatureFlags`
- [ ] Implement in `KtorRemoteEcellDataSource`
- [ ] Add routing logic to `HybridRemoteEcellDataSource`
- [ ] Test with flag = false (should use Firebase)
- [ ] Test with flag = true (should use API)
- [ ] Test API failure (should fallback to Firebase)
- [ ] Enable in production

---

## 🚨 Troubleshooting

**Q: I changed AppConfig but nothing happened?**
A: Clean and rebuild the project. AppConfig is compiled into the app.

**Q: How do I know which source is being used?**
A: Check logs - HybridRemoteEcellDataSource logs every decision.

**Q: Can I use different sources for different users?**
A: Yes! Change AppConfig logic:

```kotlin
val CURRENT_DATA_SOURCE = if (user.isBetaTester) {
    DataSourceType.HYBRID
} else {
    DataSourceType.FIREBASE
}
```

**Q: What if I want Firebase for some operations in HYBRID mode?**
A: That's the default! Set the feature flag to `false`.

---

## 📚 More Info

- Full guide: `docs/HYBRID_STRATEGY_GUIDE.md`
- DI architecture: `docs/DEPENDENCY_INJECTION.md`
- Ktor setup: `docs/KTOR_IMPLEMENTATION.md`

