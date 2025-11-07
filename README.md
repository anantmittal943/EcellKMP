# E-Cell KMP App

This is a Kotlin Multiplatform project for the E-Cell App, targeting Android and iOS. The project follows Clean Architecture principles with industry-level implementation standards
and aims for a high-quality, maintainable codebase.

## Project Overview

The E-Cell KMP App is designed to provide a comprehensive platform for the Entrepreneurship Cell (E-Cell) community. It allows users to:

- Register and authenticate (with Firebase Auth)
- View E-Cell events and glimpses
- Explore different domains
- Browse team members
- Access team-specific features (for team members)
- View meeting schedules (for team members)

## Tech Stack

### Core Technologies

- **Kotlin Multiplatform (KMP)**: Shared business logic across Android and iOS
- **Compose Multiplatform**: UI framework for both platforms
- **Kotlin Coroutines & Flow**: Asynchronous programming and reactive streams

### Architecture & Design Patterns

- **Clean Architecture**: Three-layer architecture (Data, Domain, Presentation)
- **MVVM Pattern**: ViewModel + State management
- **Repository Pattern**: Data abstraction layer
- **Dependency Injection**: Koin for DI

### Backend & Storage

- **Firebase Authentication**: User authentication
- **Firebase Firestore**: Cloud database
- **Room Database**: Local caching (SQLite)

### Navigation

- **Compose Navigation**: Type-safe navigation with nested graphs

## Project Structure

The project is organized into the following main modules:

```
composeApp/
├── src/
│   ├── commonMain/          # Shared code for all platforms
│   │   └── kotlin/com/anantmittal/ecellkmp/
│   │       ├── app/         # Application setup and navigation
│   │       ├── data/        # Data layer
│   │       │   ├── database/      # Local database (Room)
│   │       │   ├── dto/           # Data Transfer Objects
│   │       │   ├── mappers/       # Data mappers
│   │       │   ├── network/       # Network sources
│   │       │   └── repository/    # Repository implementations
│   │       ├── domain/      # Domain layer (Business logic)
│   │       │   ├── models/        # Domain models
│   │       │   └── repository/    # Repository interfaces
│   │       ├── presentation/ # Presentation layer (UI)
│   │       │   ├── bottom_navigation/
│   │       │   ├── home_screen/
│   │       │   │   ├── components/
│   │       │   │   ├── HomeScreen.kt
│   │       │   │   ├── HomeViewModel.kt
│   │       │   │   ├── HomeState.kt
│   │       │   │   └── HomeAction.kt
│   │       │   ├── login_screen/
│   │       │   ├── signup_screen/
│   │       │   └── splash_screen/
│   │       ├── di/          # Dependency Injection
│   │       └── utility/     # Shared utilities
│   │           ├── domain/        # Domain utilities
│   │           └── presentation/  # UI utilities
│   ├── androidMain/         # Android-specific code
│   └── iosMain/            # iOS-specific code
├── build.gradle.kts
└── google-services.json
```

## Architecture and Rules

This project follows a **Clean Architecture** pattern with strict layer separation:

### Layer Structure

1. **Presentation Layer** (`presentation/`)
    - Contains UI components (Composables)
    - ViewModels for state management
    - UI State classes
    - UI Action/Event classes
    - Can only access: Domain layer

2. **Domain Layer** (`domain/`)
    - Contains business logic and use cases
    - Domain models (pure Kotlin classes)
    - Repository interfaces
    - **Independent layer**: Cannot access Presentation or Data

3. **Data Layer** (`data/`)
    - Repository implementations
    - Data sources (Remote & Local)
    - Data Transfer Objects (DTOs)
    - Mappers between DTOs and Domain models
    - Can only access: Domain layer

4. **Utility Layer** (`utility/`)
    - Shared utilities and helpers
    - **Accessible by all layers**

### Key Principles

- **Dependency Rule**: Dependencies point inward (Presentation → Domain ← Data)
- **Interface Segregation**: Domain layer defines interfaces, Data layer implements them
- **Single Responsibility**: Each class has one reason to change
- **Type Safety**: Use sealed classes for actions, states, and results

### Design Patterns Used

1. **MVVM (Model-View-ViewModel)**
   ```kotlin
   Screen (View) ← State ← ViewModel ← Repository
   Screen → Action → ViewModel
   ```

2. **Repository Pattern**
   ```kotlin
   ViewModel → Repository Interface (Domain)
   Repository Implementation (Data) → Data Sources
   ```

3. **State Management**
    - Unidirectional data flow
    - Immutable state classes
    - StateFlow for reactive updates

4. **Result Wrapper Pattern**
   ```kotlin
   sealed interface Result<out D, out E>
   data class Success<out D>(val data: D) : Result<D, Nothing>
   data class Error<out E>(val error: E) : Result<Nothing, E>
   ```

## Features

### Implemented Features

- ✅ Firebase Authentication (Login/Signup)
- ✅ User Profile Management
- ✅ Local Database Caching (Room)
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
