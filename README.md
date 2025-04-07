---

# 👤 GitSeek — GitHub Profile Viewer

GitSeek is a modern Android application that allows users to search and view GitHub profiles. It is built with a focus on clean architecture, modular design, and reactive UI using **Jetpack Compose**. It also demonstrates clean state handling, one-time event flows, and separation of concerns to make the app scalable and testable.

---

## **Architectural Choices**

### **1. Clean Architecture**
The project is structured around a clean architecture with **feature-based modularity**, dividing responsibilities across three distinct layers:

- **Presentation Layer**: Handles UI state management and user interactions. Built entirely using **Jetpack Compose** and managed via `ViewModel`s.
- **Domain Layer**: Contains business logic and contracts (interfaces) such as `UserDataSource`.
- **Data Layer**: Responsible for actual data operations (network, cache) and implementing the interfaces defined in the domain layer.

This structure improves **testability**, **reusability**, and makes the app easier to scale and maintain.

---

### **2. Dependency Injection**
The app uses **Koin** for dependency injection. This ensures:
- Loose coupling between layers.
- Easy swapping of implementations for testing.
- Declarative and readable dependency management.

---

### **3. State Management**
The state management in the `UserProfileViewModel` is structured as follows:

- **State (`UserProfileState`)**: Holds the UI data such as loading status, current username, fetched profile, refresh state, and error messages.
- **Actions (`UserProfileAction`)**: Represent UI interactions (e.g., pull-to-refresh, retry button).
- **Events (`UserProfileEvent`)**: One-time effects like showing error messages (via `Channel`/`Flow`).

This model enables a **unidirectional data flow**, enhancing maintainability and reducing bugs due to inconsistent states.

---

### **4. Asynchronous Programming**
- **Kotlin Coroutines**: All asynchronous work (e.g., API requests, refresh delays) is handled using coroutines.
- **Flow**: The app uses `StateFlow` and `Channel` to observe and emit reactive UI changes.

---

## **Technical Insights**

- **Jetpack Compose**: The entire UI is built using Compose for a declarative, responsive, and modular experience.
- **Kotlin-first development**: Written entirely in Kotlin, embracing modern Android development best practices.
- **Modularity**: The app is modularized by feature, making it easy to scale and debug.

---

## **How to Run the Project**

### 🔧 Prerequisites
- Android Studio Meerkat or newer
- JDK 11+
- Android device or emulator running API 33+

### ▶️ Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/sijan8s3/gitseek.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and install dependencies.
4. Run the app on a device or emulator.

---

## **Features**

- 🔍 **Search GitHub Profiles**: Enter a GitHub username to fetch and view the profile.
- ♻️ **Pull to Refresh**: Refresh the current user profile with a swipe gesture.
- ❌ **Error Handling**: Displays user-friendly error messages with retry support when something goes wrong.
- 📦 **Testable ViewModel**: The business logic is covered with unit tests to ensure robustness.

---

## **Testing**

Unit tests are written for the `UserProfileViewModel`, validating:
- State transitions when setting a username
- Success and error flows from the data source
- Refresh and retry logic behavior

To run tests:
```bash
./gradlew test
```

---

## **Future Improvements**

- 🔁 Add username auto-suggestions using GitHub's user search API.
- 💾 Cache previously fetched profiles locally.
- 🌙 Support for light/dark theme switching.
- 🧪 Improve UI test coverage using Jetpack Compose Test APIs.
- 🧠 Add more business logic to demonstrate full clean architecture in action.

---

## 📂 Folder Structure (Simplified)

```
user_profile/
├── data/          // Data source implementation (e.g., GitHub API)
├── domain/        // Interfaces and models (UserDataSource, Profile)
├── presentation/  // ViewModel, Actions, State, Events, Composables
```

---

## 🧑‍💻 Author

**Sijan Neupane**  
Built with ❤️ in Toronto.  
[LinkedIn](https://www.linkedin.com/in/sijanneupane) • [GitHub](https://github.com/sijan8s3)

---
