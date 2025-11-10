# E-Cell KMP App

A production-ready Kotlin Multiplatform Mobile (KMM) application for the Entrepreneurship Cell (E-Cell) community, built with industry-standard architecture and best practices.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.6.0-brightgreen.svg)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Implementation Details](#implementation-details)
- [Contributing](#contributing)

## 🎯 Project Overview

E-Cell KMP is a cross-platform mobile application designed to provide a comprehensive platform for the Entrepreneurship Cell community. Built using Kotlin Multiplatform, it shares
business logic across Android and iOS while maintaining native UI experiences.

### Key Objectives

- 📱 Cross-platform development (Android & iOS)
- 🏗️ Industry-standard Clean Architecture
- 🔄 Offline-first approach with local caching
- 🔐 Secure authentication and data management
- ⚡ High performance with reactive programming
- 🎨 Modern UI with Jetpack Compose

## ✨ Features

### Implemented Features

- ✅ **Authentication System**
    - Firebase Authentication (Email/Password)
    - Secure signup and login flows
    - Session management with automatic re-authentication
    - Job-cancellation-safe operations with `NonCancellable` context

- ✅ **User Profile Management**
    - View and edit user profiles
    - Account details with personal information
    - Social links integration (LinkedIn, Instagram, Portfolio)
    - Profile picture support

- ✅ **Local Data Caching**
    - Room Database integration for offline support
    - Local-first data loading strategy
    - Automatic background sync
    - SQLite-based persistent storage

- ✅ **Team Members Directory**
    - Browse team members with staggered grid layout
    - Filter by domain and position
    - View detailed member profiles
    - Click-to-view functionality

- ✅ **Navigation System**
    - Bottom navigation bar
    - Type-safe Compose Navigation
    - Nested navigation graphs
    - Deep linking support

- ✅ **Loading States & Error Handling**
    - Material 3 loading indicators
    - Comprehensive error messages
    - Graceful failure handling
    - User-friendly feedback

### Upcoming Features

- 🔄 Events and glimpses showcase
- 🔄 Domain exploration
- 🔄 Meeting schedules for team members
- 🔄 Push notifications
- 🔄 Real-time updates

## 🛠️ Tech Stack

### Core Technologies

| Technology                | Purpose                                |
|---------------------------|----------------------------------------|
| **Kotlin Multiplatform**  | Shared business logic across platforms |
| **Compose Multiplatform** | Declarative UI framework               |
| **Kotlin Coroutines**     | Asynchronous programming               |
| **Kotlin Flow**           | Reactive data streams                  |

### Architecture & Patterns

| Component                | Implementation                            |
|--------------------------|-------------------------------------------|
| **Architecture**         | Clean Architecture (3-layer)              |
| **Presentation**         | MVVM with unidirectional data flow        |
| **Dependency Injection** | Koin                                      |
| **Navigation**           | Compose Navigation with type-safe routing |
| **State Management**     | StateFlow + Immutable State classes       |

### Backend & Database

| Service                | Purpose                |
|------------------------|------------------------|
| **Firebase Auth**      | User authentication    |
| **Firebase Firestore** | Cloud NoSQL database   |
| **Room Database**      | Local SQLite caching   |
| **Firebase Storage**   | Image and file storage |

### UI/UX

| Library                | Purpose                   |
|------------------------|---------------------------|
| **Material 3**         | Modern design system      |
| **Coil**               | Image loading and caching |
| **Compose Foundation** | Core UI components        |

## 🏗️ Architecture

This project implements **Clean Architecture** with strict layer separation and dependency rules.

### Layer Structure

```
┌─────────────────────────────────────────────────────┐
│                 Presentation Layer                   │
│  • Composable screens & components                   │
│  • ViewModels (state management)                     │
│  • UI State & Action classes                         │
│  • Can access: Domain layer only                     │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────────────┐
│                   Domain Layer                       │
│  • Business logic & use cases                        │
│  • Domain models (pure Kotlin)                       │
│  • Repository interfaces                             │
│  • Independent - no dependencies                     │
└──────────────────┬──────────────────────────────────┘
                   ↑
                   │
┌─────────────────────────────────────────────────────┐
│                    Data Layer                        │
│  • Repository implementations                        │
│  • Data sources (Remote & Local)                     │
│  • DTOs & Mappers                                    │
│  • Can access: Domain layer only                     │
└─────────────────────────────────────────────────────┘

        ┌────────────────────────────┐
        │      Utility Layer         │
        │  Accessible by all layers  │
        └────────────────────────────┘
```

### Architectural Rules

#### ✅ Allowed Dependencies

- **Presentation → Domain** ✅
- **Data → Domain** ✅
- **All Layers → Utility** ✅

#### ❌ Forbidden Dependencies

- **Domain → Presentation** ❌
- **Domain → Data** ❌
- **Presentation → Data** ❌

### Design Patterns

#### 1. MVVM Pattern

```kotlin
@Composable
fun Screen(viewModel: ViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // UI renders based on state
    when {
        state.isLoading -> LoadingIndicator()
        state.data != null -> DataDisplay(state.data)
        state.error != null -> ErrorMessage(state.error)
    }

    // User actions sent to ViewModel
    Button(onClick = { viewModel.onAction(Action.ButtonClicked) })
}
```

#### 2. Repository Pattern

```kotlin
// Domain layer - Interface
interface Repository {
    suspend fun getData(): Result<Data, Error>
}

// Data layer - Implementation
class RepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : Repository {
    override suspend fun getData(): Result<Data, Error> {
        // Local-first strategy
        return localSource.getData() ?: remoteSource.getData()
    }
}
```

#### 3. Result Wrapper Pattern

```kotlin
sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : Error>(val error: E) : Result<Nothing, E>
}

// Usage
when (val result = repository.getData()) {
    is Result.Success -> handleSuccess(result.data)
    is Result.Error -> handleError(result.error)
}
```

#### 4. State Management Pattern

```kotlin
data class ScreenState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)

sealed interface ScreenAction {
    data object LoadData : ScreenAction
    data class ItemClicked(val id: String) : ScreenAction
}
```

## 📁 Project Structure

```
EcellKMP/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/kotlin/com/anantmittal/ecellkmp/
│   │   │   ├── app/                    # Application setup
│   │   │   │   ├── App.kt              # Main app entry point
│   │   │   │   └── navigation/         # Navigation graphs
│   │   │   │
│   │   │   ├── data/                   # Data Layer
│   │   │   │   ├── database/           # Room database
│   │   │   │   │   ├── EcellAccountsDao.kt
│   │   │   │   │   ├── EcellAccountsEntity.kt
│   │   │   │   │   └── EcellAccountsDatabase.kt
│   │   │   │   ├── dto/                # Data Transfer Objects
│   │   │   │   │   └── AccountDTO.kt
│   │   │   │   ├── mappers/            # DTO ↔ Model mappers
│   │   │   │   │   └── Mappers.kt
│   │   │   │   ├── network/            # Network data sources
│   │   │   │   │   └── authenticationsource/
│   │   │   │   │       ├── EcellAuthSource.kt
│   │   │   │   │       └── FirebaseEcellAuthSource.kt
│   │   │   │   └── repository/         # Repository implementations
│   │   │   │       └── DefaultEcellRepository.kt
│   │   │   │
│   │   │   ├── domain/                 # Domain Layer
│   │   │   │   ├── models/             # Domain models
│   │   │   │   │   ├── AccountModel.kt
│   │   │   │   │   ├── User.kt
│   │   │   │   │   ├── LoginModel.kt
│   │   │   │   │   └── SignupModel.kt
│   │   │   │   └── repository/         # Repository interfaces
│   │   │   │       └── EcellRepository.kt
│   │   │   │
│   │   │   ├── presentation/           # Presentation Layer
│   │   │   │   ├── splash_screen/
│   │   │   │   │   └── SplashScreen.kt
│   │   │   │   ├── login_screen/
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── LoginViewModel.kt
│   │   │   │   │   ├── LoginState.kt
│   │   │   │   │   ├── LoginAction.kt
│   │   │   │   │   └── components/
│   │   │   │   ├── signup_screen/
│   │   │   │   │   ├── SignupScreen.kt
│   │   │   │   │   ├── SignupViewModel.kt
│   │   │   │   │   ├── SignupState.kt
│   │   │   │   │   └── SignupAction.kt
│   │   │   │   ├── home_screen/
│   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   ├── HomeViewModel.kt
│   │   │   │   │   ├── HomeState.kt
│   │   │   │   │   ├── HomeAction.kt
│   │   │   │   │   └── components/
│   │   │   │   │       ├── EventGlimpseBanner.kt
│   │   │   │   │       └── TeamMembersList.kt
│   │   │   │   ├── account_screen/
│   │   │   │   │   ├── AccountScreen.kt
│   │   │   │   │   ├── AccountViewModel.kt
│   │   │   │   │   ├── AccountState.kt
│   │   │   │   │   ├── AccountAction.kt
│   │   │   │   │   └── components/
│   │   │   │   ├── meetings_screen/
│   │   │   │   └── bottom_navigation/
│   │   │   │       └── BottomNavigation.kt
│   │   │   │
│   │   │   ├── di/                     # Dependency Injection
│   │   │   │   └── Modules.kt
│   │   │   │
│   │   │   └── utility/                # Utilities
│   │   │       ├── domain/             # Domain utilities
│   │   │       │   ├── AppLogger.kt
│   │   │       │   ├── DataError.kt
│   │   │       │   ├── Result.kt
│   │   │       │   └── Variables.kt
│   │   │       └── presentation/       # UI utilities
│   │   │           ├── Colors.kt
│   │   │           ├── UiText.kt
│   │   │           └── components/
│   │   │               └── LoadingIndicator.kt
│   │   │
│   │   ├── androidMain/                # Android-specific code
│   │   │   └── kotlin/
│   │   │       ├── MainActivity.kt
│   │   │       └── App.android.kt
│   │   │
│   │   └── iosMain/                    # iOS-specific code
│   │       └── kotlin/
│   │           └── MainViewController.kt
│   │
│   ├── build.gradle.kts
│   └── google-services.json
│
├── gradle/
│   └── libs.versions.toml             # Centralized dependency versions
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
└── RULES.md                            # Architecture rules and guidelines
```

## 🚀 Setup Instructions

### Prerequisites

- **JDK 17 or higher**
- **Android Studio Hedgehog (2023.1.1) or newer**
- **Xcode 15+ (for iOS development)**
- **Kotlin 2.0.0+**
- **Gradle 8.0+**

### Firebase Setup

1. **Create Firebase Project**
    - Go to [Firebase Console](https://console.firebase.google.com/)
    - Create a new project or use existing one

2. **Add Android App**
    - Package name: `com.anantmittal.ecellkmp`
    - Download `google-services.json`
    - Place in `composeApp/` directory

3. **Add iOS App**
    - Bundle ID: `com.anantmittal.ecellkmp`
    - Download `GoogleService-Info.plist`
    - Place in `iosApp/iosApp/` directory

4. **Enable Authentication**
    - Go to Firebase Console → Authentication
    - Enable Email/Password sign-in method

5. **Setup Firestore**
    - Go to Firebase Console → Firestore Database
    - Create database in production mode
    - Create collection: `team_members`

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/EcellKMP.git
   cd EcellKMP
   ```

2. **Open in Android Studio**
    - Open the project in Android Studio
    - Wait for Gradle sync to complete

3. **Add Firebase configuration files**
    - Place `google-services.json` in `composeApp/`
    - Place `GoogleService-Info.plist` in `iosApp/iosApp/`

4. **Build the project**
   ```bash
   ./gradlew build
   ```

### Running the App

#### Android

```bash
./gradlew :composeApp:installDebug
```

Or use the "Run" button in Android Studio

#### iOS

```bash
./gradlew :composeApp:iosSimulatorArm64Test
```

Or open `iosApp/iosApp.xcodeproj` in Xcode and run

## 💡 Implementation Details

### Local-First Data Loading Strategy

The app implements a robust local-first data loading strategy for optimal performance:

```kotlin
suspend fun loadAccount(email: String): Result<AccountModel, DataError.Remote> {
    // Step 1: Try local cache first (instant load)
    when (val localResult = loadAccountLocally(email)) {
        is Result.Success -> return Result.Success(localResult.data)
        is Result.Error -> // Continue to remote
    }

    // Step 2: Fetch from remote and cache
    when (val remoteResult = loadAccountRemotely(email)) {
        is Result.Success -> {
            cacheLocally(remoteResult.data)  // Cache for next time
            return Result.Success(remoteResult.data)
        }
        is Result.Error -> return Result.Error(remoteResult.error)
    }
}
```

**Benefits:**

- ⚡ Instant load from cache (subsequent opens)
- 🔄 Automatic background sync
- 📴 Offline support
- 🎯 Reduced network calls

### Job-Cancellation-Safe Operations

Critical operations (signup, login) use `NonCancellable` context to prevent job cancellation:

```kotlin
override suspend fun signup(signupModel: SignupModel): Result<AccountModel, DataError.Remote> {
    return withContext(NonCancellable) {
        // Firebase auth signup
        // Firestore account creation
        // Local caching
        // All operations complete even if screen navigates away
    }
}
```

**Why this matters:**

- ✅ Prevents "Job was cancelled" errors
- ✅ Ensures data integrity
- ✅ Completes Firestore writes even during navigation
- ✅ Reliable account creation

### Comprehensive Logging

All critical operations have detailed logging for debugging:

```kotlin
AppLogger.d(TAG, "Starting signup for email: ${email}")
AppLogger.d(TAG, "Firestore: Query returned ${documents.size} documents")
AppLogger.e(TAG, "Failed to load account: ${error}")
```

**Log levels:**

- `d` - Debug information
- `e` - Error conditions
- All logs tagged with `Variables.TAG = "xyz"`

### Material 3 Loading States

Consistent loading indicators across the app:

```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    // Content
    if (state.isLoading) {
        LoadingIndicator()  // Centered Material 3 loader
    }
}
```

## 📝 Contributing

### Development Guidelines

1. **Follow Clean Architecture rules** (see `RULES.md`)
2. **Use existing patterns** for consistency
3. **Write comprehensive logging** for debugging
4. **Handle all error cases** properly
5. **Test on both platforms** before committing

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable names
- Add KDoc comments for public APIs
- Keep functions small and focused

### Commit Messages

Use conventional commits format:

```
feat: add team member filtering
fix: resolve job cancellation in signup
docs: update README with setup instructions
refactor: improve repository caching logic
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Team

- **Anant Mittal** - Project Lead & Developer

## 🙏 Acknowledgments

- Kotlin Multiplatform team for the amazing framework
- Firebase for backend services
- Jetpack Compose team for the UI toolkit
- E-Cell KIET for the opportunity

---

**Made with ❤️ using Kotlin Multiplatform**
- ✅ Team Members Display
- ✅ Bottom Navigation
- ✅ Nested Navigation Graphs (Auth, Normal User, Team)
- ✅ Splash Screen
- ✅ Event Glimpse Banner

### Planned Features

- 🔄 Events List & Details
- 🔄 Domains Exploration
- 🔄 Team Member Profiles
- 🔄 Meeting Management (Team Access)
- 🔄 Image Loading (Network images)
- 🔄 Push Notifications (FCM)
- 🔄 Profile Editing

## Navigation Structure

```
App
├── Auth Nav Graph
│   ├── Login Screen
│   └── Signup Screen
├── Normal User Nav Graph
│   ├── Home Screen
│   └── Account Screen
└── Team Nav Graph
    ├── Home Screen
    ├── Meetings Screen
    └── Account Screen
```

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Xcode (for iOS development)
- JDK 17 or higher
- Firebase project setup

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd EcellKMP
   ```

2. **Configure Firebase**
    - Add `google-services.json` to `composeApp/`
    - Add `GoogleService-Info.plist` to `iosApp/iosApp/`

3. **Open in Android Studio**
    - Open the project in Android Studio
    - Wait for Gradle sync to complete

4. **Run the app**
    - **Android**: Select Android run configuration and run
    - **iOS**: Select iosApp run configuration and run

### Build Commands

```bash
# Build Android
./gradlew :composeApp:assembleDebug

# Build iOS
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

## Code Style & Conventions

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Write comments for complex logic
- Use Compose best practices (remember, LaunchedEffect, etc.)

## Dependency Injection

The project uses **Koin** for dependency injection:

```kotlin
// Module definition
val appModule = module {
    single<EcellRepository> { DefaultEcellRepository(get(), get()) }
    viewModel { HomeViewModel() }
}
```

## State Management Pattern

```kotlin
// State
data class HomeState(
    val teamMembers: List<AccountModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)

// Actions
sealed interface HomeAction {
    data class OnTeamMemberClick(val accountModel: AccountModel) : HomeAction
}

// ViewModel
class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) { /* handle action */
    }
}
```

## Testing

- Unit tests for ViewModels and Use Cases
- Repository tests with mock data sources
- UI tests for critical user flows (planned)

## Contributing

When contributing, please:

1. Follow the architecture rules defined in [RULES.md](RULES.md)
2. Write clean, maintainable code
3. Add appropriate tests
4. Update documentation as needed
5. Create descriptive commit messages

## License

[Add License Information]

## Resources

- [Kotlin Multiplatform Documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Koin Documentation](https://insert-koin.io/)

## Contact

For any queries or issues, please contact the E-Cell development team.
