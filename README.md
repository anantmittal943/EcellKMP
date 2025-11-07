# E-Cell KMP App

This is a Kotlin Multiplatform project for the E-Cell App, targeting Android and iOS. The project follows Clean Architecture principles and aims for a high-quality, maintainable
codebase.

## Project Overview

The E-Cell KMP App is designed to provide information about E-Cell events, domains, and team members. It allows users to register, log in, and view various aspects of the E-Cell.

## Project Structure

The project is organized into the following main modules:

- `composeApp`: This is the shared module containing the core logic of the application, written in Kotlin. It's further divided into:
    - `commonMain`: Code that is common for all targets (Android and iOS).
    - `androidMain`: Platform-specific code for Android.
    - `iosMain`: Platform-specific code for iOS.
- `iosApp`: This module contains the iOS application entry point and any iOS-specific UI or configuration.

## Architecture and Rules

This project follows a Clean Architecture pattern. For a detailed explanation of the architecture, coding conventions, and project rules, please refer to the [RULES.md](RULES.md)
file.

## Getting Started

To get started with the project, clone the repository and open it in Android Studio. You can build and run the Android and iOS apps from there.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…