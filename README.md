# Store App (Android Store TDD Project)

A modern Android application built to demonstrate **Clean Architecture**, **Modularization**, and **Test-Driven Development (TDD)** using the latest Android tech stack. 

The app connects to the [FakeStoreAPI](https://fakestoreapi.com/) to fetch products and users, and uses a local database to manage favorite products.

## Features

- **Product Listing:** Browse a catalog of products with images, prices, titles, and categories.
- **Favorites Management:** Like and unlike products. Favorites are persisted locally and survive app restarts.
- **Favorites Filter:** A dedicated screen to view only your favorite products.
- **User Profile:** Displays the currently logged-in user's information and a reactive counter of favorite items.
- **Offline Support (Partial):** Favorites are stored locally using Room.
- **Responsive UI:** Built entirely with Jetpack Compose using Material Design 3 guidelines.

## Architecture & Modularization

The project strictly follows **Clean Architecture** principles and is heavily modularized into **8 independent Gradle modules** to ensure separation of concerns, build speed optimization, and high testability.

### Module Breakdown:
1. `:app` - The application module holding the Dependency Injection graph (Koin) and Navigation.
2. `:domain` - The core business logic, entities, interfaces, and Use Cases. (Pure Kotlin, no Android dependencies).
3. `:core:network` - Handles API requests using Retrofit. Translates DTOs to Domain models.
4. `:core:database` - Implements local persistence using Room Database.
5. `:core:ui` - Shared UI components, theme, and design system elements (Compose).
6. `:feature:products` - The main product feed screen and its ViewModel.
7. `:feature:favorites` - The favorites screen and its ViewModel.
8. `:feature:profile` - The user profile screen and its ViewModel.

> ** Dependency Inversion Note:** `core:network` does not depend on `core:database`. Instead, the `domain` module exposes a `FavoriteLocalDataSource` interface, which is implemented in `core:database` and injected via Koin where needed. This ensures true decoupling.

## Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM / MVI State Management with `StateFlow`
- **Asynchrony:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Dependency Injection:** [Koin](https://insert-koin.io/)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) (with Logging Interceptor)
- **Local Persistence:** [Room Database](https://developer.android.com/training/data-storage/room)
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`) & Version Catalogs (`libs.versions.toml`)

## Testing (TDD)

The application was built following a strict **Test-Driven Development (TDD)** approach.

- **Unit Testing:** JUnit 4, [MockK](https://mockk.io/), and Coroutines Testing library.
  - *Domain layer:* All Use Cases are unit tested.
  - *Data layer:* Repositories are tested mocking the Network and Database layers.
  - *Presentation layer:* ViewModels are thoroughly tested tracking state changes (Loading, Content, Error).
- **UI Testing:** Jetpack Compose UI Testing API is used to test components and fully integrated screens.

### How to run the tests
```bash
# Run all Unit Tests (Fast, no emulator required)
./gradlew test

# Run UI/Integration Tests (Emulator/Device required)
./gradlew connectedAndroidTest
```

## Getting Started

1. Clone the repository.
2. Open the project in **Android Studio Ladybug** (or newer).
3. Let Gradle sync and download all dependencies defined in the Version Catalog.
4. Run the app on an emulator or physical device.

## License
MIT License.
