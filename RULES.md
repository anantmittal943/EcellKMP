# Project Rules and Conventions

This document outlines the rules and conventions to be followed while working on the E-Cell KMP project. These rules ensure consistency, maintainability, and scalability of the
codebase.

## Architecture

This project follows a **Clean Architecture** pattern with three main layers: `presentation`, `domain`, and `data`, plus a shared `utility` layer.

### Layer Structure

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (UI, ViewModels, Screens, Components) │
└──────────────┬──────────────────────────┘
               │ depends on
               ↓
┌─────────────────────────────────────────┐
│           Domain Layer                  │
│  (Models, Repository Interfaces,        │
│   Business Logic, Use Cases)            │
└──────────────↑──────────────────────────┘
               │ depends on
               │
┌──────────────┴──────────────────────────┐
│            Data Layer                   │
│  (Repository Implementations,           │
│   Data Sources, DTOs, Mappers)          │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│          Utility Layer                  │
│  (Shared utilities accessible to all)  │
└─────────────────────────────────────────┘
```

### Layer Descriptions

#### 1. Presentation Layer (`presentation/`)

**Purpose**: Handle UI and user interactions

**Contains**:

- Composable functions (UI screens and components)
- ViewModels (state management and business logic coordination)
- State classes (immutable UI state)
- Action/Event classes (user interactions)
- UI-specific models (if needed)

**Rules**:

- ✅ **CAN** access Domain layer
- ❌ **CANNOT** access Data layer directly
- ❌ **CANNOT** contain business logic (delegate to Domain)
- ✅ **SHOULD** use StateFlow for state management
- ✅ **MUST** use unidirectional data flow pattern

**Example Structure**:

```kotlin
// State (immutable)
data class HomeState(
    val teamMembers: List<AccountModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)

// Actions (sealed interface)
sealed interface HomeAction {
    data class OnTeamMemberClick(val accountModel: AccountModel) : HomeAction
    data object OnViewAllClick : HomeAction
}

// ViewModel
class HomeViewModel(
    private val repository: EcellRepository // Domain interface
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnTeamMemberClick -> handleTeamMemberClick(action.accountModel)
            HomeAction.OnViewAllClick -> handleViewAll()
        }
    }
}
```

#### 2. Domain Layer (`domain/`)

**Purpose**: Contain core business logic and rules

**Contains**:

- Domain models (pure Kotlin data classes)
- Repository interfaces
- Use cases (business operations)
- Business validation logic

**Rules**:

- ❌ **CANNOT** access Presentation layer
- ❌ **CANNOT** access Data layer
- ❌ **CANNOT** have Android/iOS dependencies
- ✅ **MUST** be platform-independent
- ✅ **MUST** define repository interfaces
- ✅ **SHOULD** contain only pure Kotlin code

**Example**:

```kotlin
// Domain Model
data class AccountModel(
    val id: String,
    val name: String,
    val email: String,
    val designation: String
)

// Repository Interface
interface EcellRepository {
    suspend fun loadTeamAccountsRemotely(): Result<List<AccountModel>, DataError.Remote>
    suspend fun cacheLoggedInAccount(accountModel: AccountModel): EmptyResult<DataError.Local>
}
```

#### 3. Data Layer (`data/`)

**Purpose**: Provide data to the application

**Contains**:

- Repository implementations
- Data sources (Remote: Firebase, Local: Room)
- Data Transfer Objects (DTOs)
- Mappers (DTO ↔ Domain Model)
- Network/Database logic

**Rules**:

- ✅ **CAN** access Domain layer
- ❌ **CANNOT** access Presentation layer
- ✅ **MUST** implement Domain repository interfaces
- ✅ **MUST** use mappers for data transformation
- ✅ **SHOULD** handle caching logic
- ✅ **SHOULD** catch and map exceptions to domain errors

**Example**:

```kotlin
// DTO
@Serializable
data class AccountDTO(
    val id: String,
    val name: String,
    val email: String
)

// Mapper
fun AccountDTO.toAccountModel(): AccountModel {
    return AccountModel(
        id = id,
        name = name,
        email = email
    )
}

// Repository Implementation
class DefaultEcellRepository(
    private val authSource: EcellAuthSource,
    private val dao: EcellAccountsDao
) : EcellRepository {
    override suspend fun loadTeamAccountsRemotely(): Result<List<AccountModel>, DataError.Remote> {
        return try {
            val dtos = authSource.getTeamAccounts()
            Result.Success(dtos.map { it.toAccountModel() })
        } catch (e: Exception) {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}
```

#### 4. Utility Layer (`utility/`)

**Purpose**: Provide shared utilities and helpers

**Contains**:

- Common utilities (logging, validation, etc.)
- Extension functions
- Constants and variables
- Common UI components (colors, themes, typography)
- Result wrappers (Result, EmptyResult, DataError)

**Rules**:

- ✅ **CAN** be accessed by all layers
- ❌ **SHOULD NOT** depend on specific layers
- ✅ **MUST** be reusable across the project

**Example**:

```kotlin
// Result wrapper
sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E>(val error: E) : Result<Nothing, E>
}

// Constants
object Variables {
    const val TAG = "xyz"
    const val TEAM_MEMBERS_TAG = "Team Members"
}
```

### Layer Dependencies

**Allowed Dependencies**:

- `Presentation` → `Domain` ✅
- `Data` → `Domain` ✅
- `Any` → `Utility` ✅

**Forbidden Dependencies**:

- `Domain` → `Presentation` ❌
- `Domain` → `Data` ❌
- `Data` → `Presentation` ❌
- `Presentation` → `Data` ❌

## Coding Conventions

### 1. Naming Conventions

**Files and Classes**:

- **Screens**: `<Name>Screen.kt` (e.g., `HomeScreen.kt`)
- **ViewModels**: `<Name>ViewModel.kt` (e.g., `HomeViewModel.kt`)
- **State**: `<Name>State.kt` (e.g., `HomeState.kt`)
- **Actions**: `<Name>Action.kt` (e.g., `HomeAction.kt`)
- **Components**: Descriptive names (e.g., `TeamMemberCard.kt`)
- **Models**: `<Name>Model.kt` (e.g., `AccountModel.kt`)
- **DTOs**: `<Name>DTO.kt` (e.g., `AccountDTO.kt`)
- **Repository Interface**: `<Name>Repository.kt` (e.g., `EcellRepository.kt`)
- **Repository Implementation**: `Default<Name>Repository.kt` (e.g., `DefaultEcellRepository.kt`)

**Variables and Functions**:

- Use `camelCase` for variables and functions
- Use `PascalCase` for classes and objects
- Use `UPPER_SNAKE_CASE` for constants
- Use descriptive names (avoid single letters except for common cases like `i`, `x`, `y`)

### 2. Kotlin Coding Style

Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// ✅ Good
fun loadTeamMembers(
    includeInactive: Boolean = false,
    sortByName: Boolean = true
): List<AccountModel> {
    return repository.getTeamMembers()
        .filter { it.isActive || includeInactive }
        .let { members ->
            if (sortByName) members.sortedBy { it.name }
            else members
        }
}

// ❌ Bad
fun load_team_members(include_inactive: Boolean = false, sort_by_name: Boolean = true): List<AccountModel> {
    return repository.getTeamMembers().filter { it.isActive || include_inactive }.let { members -> if (sort_by_name) members.sortedBy { it.name } else members }
}
```

### 3. Compose Best Practices

**State Management**:

```kotlin
// ✅ Good - Hoisted state
@Composable
fun TeamMemberCard(
    accountModel: AccountModel,
    onClick: (AccountModel) -> Unit,
    modifier: Modifier = Modifier
) {
    // UI implementation
}

// ❌ Bad - Internal state that should be hoisted
@Composable
fun TeamMemberCard(accountModel: AccountModel) {
    var isExpanded by remember { mutableStateOf(false) }
    // This state might need to be hoisted
}
```

**Composable Structure**:

```kotlin
// ✅ Good - Clear separation
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    // Pure UI implementation
}
```

### 4. Error Handling

**Use Result Wrapper**:

```kotlin
// ✅ Good
suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote> {
    return try {
        authSource.login(loginModel)
        Result.Success(Unit)
    } catch (e: FirebaseAuthException) {
        Result.Error(DataError.Remote.UNAUTHORIZED)
    }
}

// Handle in ViewModel
viewModelScope.launch {
    repository.login(loginModel).onSuccess {
        // Navigate to home
    }.onError { error ->
        // Show error message
    }
}
```

### 5. Dependency Injection

**Use Koin for DI**:

```kotlin
// Module definition
val dataModule = module {
    single<EcellRepository> { DefaultEcellRepository(get(), get()) }
    single { EcellAccountsDao(get()) }
}

val presentationModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}

// Usage in Composable
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    // ...
}
```

### 6. Comments and Documentation

```kotlin
// ✅ Good - Explain WHY, not WHAT
// Using a coroutine scope with SupervisorJob to prevent 
// cancellation of all repository operations if one fails
private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

// ❌ Bad - Obvious comments
// This variable stores the user's name
val userName = "John"
```

## File Organization

### Directory Structure

```
presentation/
├── <feature_name>/
│   ├── components/
│   │   ├── <Component1>.kt
│   │   └── <Component2>.kt
│   ├── <Feature>Screen.kt
│   ├── <Feature>ViewModel.kt
│   ├── <Feature>State.kt
│   └── <Feature>Action.kt

domain/
├── models/
│   ├── <Model1>.kt
│   └── <Model2>.kt
└── repository/
    └── <Name>Repository.kt

data/
├── database/
│   ├── <Name>Dao.kt
│   └── <Name>Entity.kt
├── dto/
│   └── <Name>DTO.kt
├── mappers/
│   └── Mappers.kt
├── network/
│   └── <Name>Source.kt
└── repository/
    └── Default<Name>Repository.kt
```

## Testing Guidelines

### Unit Tests

**ViewModel Tests**:

```kotlin
class HomeViewModelTest {
    @Test
    fun `onAction OnTeamMemberClick should update selected member`() {
        // Arrange
        val viewModel = HomeViewModel(mockRepository)
        val member = AccountModel(id = "1", name = "Test")

        // Act
        viewModel.onAction(HomeAction.OnTeamMemberClick(member))

        // Assert
        assertEquals(member, viewModel.state.value.selectedMember)
    }
}
```

**Repository Tests**:

```kotlin
class DefaultEcellRepositoryTest {
    @Test
    fun `loadTeamAccountsRemotely should return success with data`() = runTest {
        // Arrange
        val mockSource = mockk<EcellAuthSource>()
        coEvery { mockSource.getTeamAccounts() } returns listOf(mockAccountDTO)
        val repository = DefaultEcellRepository(mockSource, mockDao)

        // Act
        val result = repository.loadTeamAccountsRemotely()

        // Assert
        assertTrue(result is Result.Success)
    }
}
```

## Git Workflow

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types**:

- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code refactoring
- `style`: Formatting changes
- `docs`: Documentation changes
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Examples**:

```
feat(home): add team members list with staggered grid layout

fix(auth): resolve login error handling for invalid credentials

refactor(data): extract mapper functions to separate file

docs(readme): update architecture documentation
```

### Branch Naming

```
feature/<feature-name>
bugfix/<bug-description>
refactor/<refactor-description>
```

## Performance Guidelines

1. **Avoid Recomposition**: Use `remember`, `derivedStateOf`, and `key` appropriately
2. **Use LazyColumn/LazyRow**: For large lists
3. **Optimize Image Loading**: Use proper image loading libraries
4. **Background Operations**: Use coroutines for heavy operations
5. **Cache Data**: Implement caching strategy (Room + Firebase)

## Security Guidelines

1. **Never commit sensitive data**: API keys, passwords, tokens
2. **Use Firebase Security Rules**: Proper Firestore security rules
3. **Validate User Input**: Always validate and sanitize user input
4. **Use HTTPS**: All network calls should use HTTPS
5. **Store credentials securely**: Use platform-specific secure storage

## Accessibility Guidelines

1. **Content Descriptions**: Add content descriptions to all images
2. **Touch Targets**: Minimum 48dp touch target size
3. **Color Contrast**: Ensure proper color contrast ratios
4. **Screen Reader Support**: Test with TalkBack/VoiceOver

## Code Review Checklist

Before submitting PR:

- [ ] Code follows architecture rules
- [ ] No layer dependency violations
- [ ] Proper error handling implemented
- [ ] UI follows design patterns
- [ ] No hardcoded strings (use resources)
- [ ] Added appropriate comments
- [ ] Tests written (if applicable)
- [ ] No unused imports
- [ ] No compiler warnings
- [ ] Follows naming conventions
- [ ] Proper dependency injection

## Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Koin Documentation](https://insert-koin.io/)

---

**Remember**: Consistency is key. Follow these rules to maintain a clean, maintainable, and scalable codebase.
