# Team Detail Navigation Implementation

## Overview

Implemented a robust navigation pattern for viewing team member details with proper data handling and process restoration support.

## Architecture

### 1. **Navigation Flow**

- User clicks on a team member in `HomeScreen`
- Navigation passes the member's email as a route parameter
- `TeamDetailScreen` loads and displays member details

### 2. **Data Flow**

#### Optimized Loading Strategy:

1. **Immediate Display**: Check `TeamSharedViewModel` for cached member data
    - If available, display it immediately (no loading delay)
2. **Fresh Data**: Always fetch from repository in background
    - Ensures data is up-to-date
    - Updates the UI when loaded
3. **Process Restoration**: If app process is killed and restored
    - Uses email from `SavedStateHandle`
    - Fetches fresh data from repository

### 3. **Components Created**

#### `TeamDetailViewModel`

```kotlin
class TeamDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val ecellRepository: EcellRepository,
    private val teamSharedViewModel: TeamSharedViewModel
)
```

- Gets member ID from navigation arguments via `SavedStateHandle`
- First checks `TeamSharedViewModel` for cached data (instant display)
- Always fetches fresh data from repository (background update)
- Handles loading and error states

#### `TeamDetailScreen`

- Full-screen profile view with:
    - Back button navigation
    - Profile picture
    - Member details (name, designation, email, etc.)
    - Social links
    - Scrollable content

#### `TeamDetailState`

```kotlin
data class TeamDetailState(
    val member: AccountModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

#### `TeamDetailAction`

```kotlin
sealed interface TeamDetailAction {
    data object OnBackClick : TeamDetailAction
}
```

### 4. **Navigation Setup**

#### Route Definition

In `Route.kt`:

```kotlin
@Serializable
data class ViewProfile(val email: String) : Route
```

#### Navigation Graph

In `App.kt`:

```kotlin
composable<Route.ViewProfile> {
    val viewModel = koinViewModel<TeamDetailViewModel>()
    TeamDetailScreenRoot(
        viewModel = viewModel,
        onBackClick = { navController.navigateUp() }
    )
}
```

#### Click Handler

In `HomeScreen`:

```kotlin
HomeScreenRoot(
    viewModel = viewModel,
    onTeamMemberClick = { profile ->
        teamSharedViewModel.selectMember(profile) // Cache for optimization
        navController.navigate(Route.ViewProfile(profile.email)) // Pass ID
    }
)
```

### 5. **Dependency Injection**

Registered in `Modules.kt`:

```kotlin
viewModelOf(::TeamDetailViewModel)
```

## Benefits

### ✅ **Performance**

- Instant UI display using cached data
- Background refresh ensures data freshness

### ✅ **Reliability**

- Works even if app process is killed
- Uses email as single source of truth
- Falls back to repository if cache is empty

### ✅ **Clean Architecture**

- Follows KMP project structure
- Presentation layer only accesses Domain
- ViewModel handles all business logic

### ✅ **User Experience**

- No unnecessary loading delays
- Smooth navigation
- Error handling with user feedback

## Layer Adherence

```
Presentation (TeamDetailScreen, TeamDetailViewModel)
    ↓
Domain (EcellRepository, AccountModel)
    ↓
Data (Repository Implementation)
```

- ✅ Presentation only accesses Domain
- ✅ No direct Data layer access from Presentation
- ✅ Proper separation of concerns

## Usage Example

```kotlin
// In your navigation graph
navController.navigate(Route.ViewProfile(email = "user@example.com"))

// TeamDetailViewModel automatically:
// 1. Gets email from SavedStateHandle
// 2. Checks TeamSharedViewModel for cached data
// 3. Displays cached data immediately if available
// 4. Fetches fresh data from repository
// 5. Updates UI with fresh data
```

## Future Enhancements

1. Add edit functionality for team members
2. Implement social media link opening
3. Add sharing functionality
4. Cache fetched data locally for offline access

