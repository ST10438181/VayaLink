# VayaLink — Android Prototype (Final Submission)

This is a working Kotlin/Android Studio project implementing the VayaLink prototype. It is built as a standard Gradle project — open the `VayaLink/` folder directly in Android Studio (Hedgehog or later).

**Demonstration Video:**  https://youtube.com/shorts/Xdz2dkmW92o?si=1iIlkIRfhqjnbZTx )

## What this prototype demonstrates

| Requirement | Where it lives |
|---|---|
| **RESTful API in an Android app** | `data/remote/ApiService.kt` + `RetrofitClient.kt` — GET `/routes`, GET `/routes/{id}`, GET `/alerts`, POST `/reports` |
| **External libraries** | Retrofit + OkHttp + Gson (networking/JSON), Room (offline cache), Firebase Cloud Messaging (push notifications) |
| **Appropriate SDKs** | Firebase Authentication (Email/Password + Google SSO) + Firestore (profile) + Firebase Cloud Messaging |
| **NoSQL Database** | Firebase Firestore (used for user profile data) |
| **Offline mode with sync** | Room database caches saved routes locally for offline viewing |
| **Real-time Notifications** | Firebase Cloud Messaging (FCM) implemented via `VayaLinkMessagingService.kt` |
| **Multi-language support** | English (`values/strings.xml`) and isiZulu (`values-zu/strings.xml`) |
| **Detailed unit testing** | `app/src/test/java/com/vayalink/app/` — `FareCalculatorTest`, `ValidationUtilsTest`, `RouteRepositoryTest`, `ReportRepositoryTest` (JUnit + Mockito + coroutines-test) |

## Features included (from the Part 1 & Part 3 rubric)

1. Register / login with Firebase Auth — passwords are hashed and stored by Firebase, never by the app.
2. **Single Sign-On (SSO)** — Login via Google Sign-In integrated with Firebase Auth.
3. Settings: change password, preferred language (English/isiZulu), notification preferences.
4. Journey planner — searches routes by origin/destination against your hosted REST API, shows fare + travel-time estimates.
5. Live alerts — pulls strike/traffic/delay/safety alerts from the same API.
6. Report incident — submits driver-behaviour or safety reports via `POST /reports`.
7. Saved routes — cached offline with Room so they're visible without a connection.
8. **Push Notifications** — Real-time alerts sent via Firebase Cloud Messaging.

## Before you build

1. **Firebase**: 
   - Create a Firebase project, add an Android app with applicationId `com.vayalink.app`.
   - Enable **Authentication → Email/Password** and **Google** (SSO).
   - Enable **Cloud Firestore**.
   - Enable **Cloud Messaging** (FCM).
   - Add your **SHA-1 fingerprint** in Firebase Project Settings.
   - Download the real `google-services.json` and replace the placeholder at `app/google-services.json`.

2. **Hosted REST API**: 
   - This prototype connects to a hosted API (mockapi.io) with `routes` and `alerts` resources. 
   - Update `BASE_URL` in `data/remote/RetrofitClient.kt` with your project's URL.

3. Sync Gradle, then run on a physical device (as required by the brief).

## Unit tests

Run from Android Studio (right-click `app/src/test` → Run Tests) or:
app/src/main/java/com/vayalink/app/
├── data/
│ ├── model/ Route, Alert, IncidentReport, User
│ ├── remote/ ApiService (Retrofit), RetrofitClient
│ ├── local/ Room entity, DAO, database
│ └── repository/ AuthRepository, RouteRepository, AlertRepository, ReportRepository
├── viewmodel/ AuthViewModel, JourneyViewModel, AlertsViewModel, ReportViewModel
├── ui/
│ ├── splash/ auth/ home/ journey/ alerts/ report/ savedroutes/ profile/
│ └── adapter/ RecyclerView adapters
├── util/ Resource, ValidationUtils, FareCalculator, SessionManager
└── VayaLinkMessagingService.kt (FCM Push Notifications)

app/src/test/java/com/vayalink/app/
├── FareCalculatorTest.kt
├── ValidationUtilsTest.kt
├── RouteRepositoryTest.kt
└── ReportRepositoryTest.kt

## Use of GitHub and GitHub Actions

This repository uses GitHub for version control. A GitHub Actions workflow (`.github/workflows/android-ci.yml`) runs automatically on every push to `main`. It:
1. Sets up JDK 17
2. Runs unit tests (`./gradlew testDebugUnitTest`)
3. Runs Android Lint (`./gradlew lintDebug`)
4. Builds a debug APK (`./gradlew assembleDebug`)
5. Uploads the APK as a downloadable artifact

## Honesty note on scope

This prototype implements the core requirements for Part 1 and Part 3. Live GPS-tracking of taxis and multi-taxi transfer planning were deferred as per the design document. Blob storage (image upload) was deferred due to Firebase Storage requiring a paid Blaze plan at the time of submission; instead, the app fully implements NoSQL database storage (Firestore) and Push Notifications
