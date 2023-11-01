## Contents

- [ScreenShots](#screenshots)
- [Project Setup](#project-setup)
- [Architecture](#architecture)
- [Libraries](#libraries)

## ScreenShots

## Project Setup

- Import or Open this project on Android Studio
- Sync Gradle and Rebuild Project
- Run app via emulator or device

## Architecture

The project is built using MVVM design pattern, and also use Dependency Injection and Dependency
Inversion to make it easier to test.

## Libraries

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Used for UI
- [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI
  related data
- [Room](https://developer.android.com/training/data-storage/room) - Used for local DB
- [Coroutines](https://developer.android.com/kotlin/coroutines) - Used for asynchronous programming.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Used for
  Dependency injection
- [Retrofit](https://square.github.io/retrofit/) - Network - Request / API Call
- [Okhttp](https://square.github.io/okhttp/) - Network - Http client
- [Gson](https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson) - Network -
  Retrofit Gson converter
- [Mockk](https://mockk.io/ANDROID.html) - Used for mock data to testing purpose
- [Coil](https://coil-kt.github.io/coil/) - Media management and image loading framework for Android
- [Chucker](https://github.com/ChuckerTeam/chucker) - Debug Tools