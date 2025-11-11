# Dependency Injection Architecture

## Overview

This project uses **Koin** for dependency injection, following clean architecture principles with proper separation of concerns.

## Multiple Implementations Strategy

### Problem

We have multiple implementations of `RemoteEcellDataSource`:

- `FirebaseRemoteEcellDataSource` - Current implementation using Firebase Firestore
- `KtorRemoteEcellDataSource` - Future implementation for REST API

### Solution: Qualifiers

We use **Koin Qualifiers** to register multiple implementations:

```kotlin
// Define qualifiers
object DataSourceQualifiers {
    val FIREBASE = named("firebase")
    val KTOR = named("ktor")
}

// Register both implementations
single<RemoteEcellDataSource>(DataSourceQualifiers.FIREBASE) {
    FirebaseRemoteEcellDataSource(get())
}
single<RemoteEcellDataSource>(DataSourceQualifiers.KTOR) {
    KtorRemoteEcellDataSource()
}

// Set default implementation
single<RemoteEcellDataSource> {
    get(DataSourceQualifiers.FIREBASE)
}
```

### How It Works

1. **Default Usage** - Repository automatically gets Firebase implementation:
   ```kotlin
   class DefaultEcellRepository(
       private val remoteEcellDataSource: RemoteEcellDataSource // Gets Firebase
   )
   ```

2. **Explicit Qualifier** - Directly inject specific implementation:
   ```kotlin
   class SomeClass(
       private val firebaseSource: RemoteEcellDataSource // Inject with qualifier
   ) {
       // Constructor in Koin:
       // single { SomeClass(get(DataSourceQualifiers.FIREBASE)) }
   }
   ```

3. **Switching Implementations** - Change in one place:
   ```kotlin
   // To switch to Ktor:
   single<RemoteEcellDataSource> { 
       get(DataSourceQualifiers.KTOR) 
   }
   ```

## Benefits

✅ **Flexibility** - Easy to switch between implementations  
✅ **Testing** - Can inject mock implementations  
✅ **Feature Flags** - Can enable different sources based on configuration  
✅ **Clean Code** - No changes needed in business logic

## Adding New Implementations

1. Create new implementation of `RemoteEcellDataSource`
2. Add new qualifier:
   ```kotlin
   object DataSourceQualifiers {
       val FIREBASE = named("firebase")
       val KTOR = named("ktor")
       val MY_NEW_SOURCE = named("myNewSource") // Add here
   }
   ```
3. Register in Koin:
   ```kotlin
   single<RemoteEcellDataSource>(DataSourceQualifiers.MY_NEW_SOURCE) { 
       MyNewDataSource() 
   }
   ```

## Future Enhancements

### 1. Feature Flag Based Selection

```kotlin
single<RemoteEcellDataSource> {
    if (BuildConfig.USE_FIREBASE) {
        get(DataSourceQualifiers.FIREBASE)
    } else {
        get(DataSourceQualifiers.KTOR)
    }
}
```

### 2. Strategy Pattern

Create a composite data source that can use multiple sources:

```kotlin
class CompositeRemoteEcellDataSource(
    private val firebaseSource: RemoteEcellDataSource,
    private val ktorSource: RemoteEcellDataSource
) : RemoteEcellDataSource {
    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        // Try Firebase first, fallback to Ktor
        return when (val result = firebaseSource.getAccountDb(email)) {
            is Result.Success -> result
            is Result.Error -> ktorSource.getAccountDb(email)
        }
    }
}
```

### 3. Repository Pattern with Multiple Sources

```kotlin
class HybridEcellRepository(
    private val authDataSource: RemoteEcellDataSource, // Firebase for auth
    private val contentDataSource: RemoteEcellDataSource // Ktor for content
) : EcellRepository {
    // Use different sources for different operations
}
```

## Best Practices

1. **Single Responsibility** - Each implementation handles one source
2. **Interface Segregation** - Keep interface focused on data operations
3. **Dependency Inversion** - Depend on abstractions, not concrete implementations
4. **Open/Closed** - Open for extension (new implementations), closed for modification

## Current Architecture Flow

```
┌─────────────────────────────────────────────────┐
│           Presentation Layer (ViewModels)        │
│  - LoginViewModel                                │
│  - SignupViewModel                               │
│  - AccountViewModel                              │
└──────────────────┬──────────────────────────────┘
                   │ depends on
                   ▼
┌─────────────────────────────────────────────────┐
│           Domain Layer (Repository)              │
│  - EcellRepository (interface)                   │
└──────────────────┬──────────────────────────────┘
                   │ depends on
                   ▼
┌─────────────────────────────────────────────────┐
│              Data Layer                          │
│  ┌─────────────────────────────────────────┐    │
│  │  DefaultEcellRepository (implementation) │    │
│  │    - EcellAuthSource                     │    │
│  │    - RemoteEcellDataSource ─────────┐   │    │
│  │    - EcellAccountsDao               │   │    │
│  └─────────────────────────────────────┘   │    │
│                                             │    │
│  RemoteEcellDataSource Interface           │    │
│         ▲                  ▲                │    │
│         │                  │                │    │
│         │                  └────────────────┘    │
│         │                                        │
│  ┌──────┴──────┐         ┌─────────────────┐    │
│  │  Firebase   │         │  Ktor (Future)  │    │
│  │ Implementation│       │  Implementation │    │
│  └─────────────┘         └─────────────────┘    │
└─────────────────────────────────────────────────┘
```

## Testing Strategy

Mock implementations for testing:

```kotlin
class MockRemoteEcellDataSource : RemoteEcellDataSource {
    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        return Result.Success(AccountDTO(/* test data */))
    }
}

// In test Koin module:
single<RemoteEcellDataSource> { MockRemoteEcellDataSource() }
```

