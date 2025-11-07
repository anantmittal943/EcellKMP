# Project Rules and Conventions

This document outlines the rules and conventions to be followed while working on this project.

## Architecture

This project follows a Clean Architecture pattern with three main layers: `presentation`, `domain`, and `data`.

- **`presentation`**: This layer is responsible for the UI and user interactions. It contains Composables, ViewModels, and UI-related state.
- **`domain`**: This is the core layer of the application. It contains the business logic, use cases, and domain models. It is independent of the other layers.
- **`data`**: This layer is responsible for providing data to the application. It contains repositories, data sources (remote and local), and data models.

### Layer Dependencies

The dependencies between the layers are as follows:

- `presentation` depends on `domain`.
- `data` depends on `domain`.
- `domain` has no dependencies on `presentation` or `data`.
- `utility` is a shared module that can be accessed by all layers.

## Coding Style

- Follow the official [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html).
- Use dependency injection to provide dependencies to different parts of the application.
- Write unit tests for ViewModels, UseCases, and Repositories.

## Project Structure

The project is a Kotlin Multiplatform (KMP) project with shared code in `commonMain`.

- `composeApp/src/commonMain/kotlin/com/anantmittal/ecellkmp`:
    - `presentation`: Contains UI-related code (screens, ViewModels).
    - `domain`: Contains business logic (use cases, models, repository interfaces).
    - `data`: Contains data-related code (repository implementations, data sources).
    - `di`: Contains dependency injection setup.
    - `utility`: Contains shared utility classes and functions.

