# VayaLink — Android Prototype 

This is a working Kotlin/Android Studio project implementing the VayaLink
prototype described in my Planning & Design document. It is built as a
standard Gradle project — open the `VayaLink/` folder directly in Android
Studio (Hedgehog or later).

## What this prototype demonstrates

| Requirement | Where it lives |
|---|---|
| **RESTful API in an Android app** | `data/remote/ApiService.kt` + `RetrofitClient.kt` — GET `/routes`, GET `/routes/{id}`, GET `/alerts`, POST `/reports` |
| **External library** | Retrofit + OkHttp + Gson (networking/JSON), and Room (offline cache) |
| **Appropriate SDK** | Firebase Authentication (register/login/logout/password change) + Firestore (profile) |
| **Detailed unit testing** | `app/src/test/java/com/vayalink/app/` — `FareCalculatorTest`, `ValidationUtilsTest`, `RouteRepositoryTest`, `ReportRepositoryTest` (JUnit + Mockito + coroutines-test) |

## Features included 

1. Register / login with Firebase Auth — passwords are hashed and stored by
   Firebase, never by the app (FR1–FR5).
2. Settings: change password, preferred language (EN/ZU), notifications,
   data-saver mode (FR6–FR10).
3. Journey planner — searches routes by origin/destination against your
   hosted REST API, shows fare + travel-time estimates (FR11–FR15).
4. Live alerts — pulls strike/traffic/delay/safety alerts from the same API
   (FR17, FR19).
5. Report incident — submits driver-behaviour or safety reports via
   `POST /reports` (FR23, FR24).
6. Saved routes — cached offline with Room so they're visible without a
   connection (FR8, FR20–22, NFR8).

## Before build

1. **Firebase**: create a Firebase project, add an Android app with
   applicationId `com.vayalink.app`, enable **Authentication → Email/Password**
   and **Cloud Firestore**, then download the real `google-services.json` and
   replace the placeholder at `app/google-services.json`.

2. **Hosted REST API**: this prototype needs *your own* hosted REST API
   (per the assignment brief, "connect to a REST API you create... or any
   that fits your idea... must be hosted"). The quickest option for a
   prototype is a free [mockapi.io](https://mockapi.io) project with three
   resources — `routes`, `alerts`, `reports` — matching the field names in
   `data/model/Route.kt`, `Alert.kt` and `IncidentReport.kt`. Once created,
   update `BASE_URL` in `data/remote/RetrofitClient.kt` with your project's
   URL. If you'd rather build your own backend (Node/Express, Firebase
   Cloud Functions, etc.) instead, point `BASE_URL` at that instead — the
   `ApiService` interface doesn't need to change as long as the endpoint
   shapes match.

3. Sync Gradle, then run on a device/emulator.

## Unit tests

Run from Android Studio (right-click `app/src/test` → Run Tests) or:

```
./gradlew testDebugUnitTest
```

These test pure logic (fare calculation, validation rules) and repository
behaviour with the network layer mocked out, so they run instantly with no
device, emulator, or live network connection required — exactly what your
markers will want to see for "detailed unit testing."

## Notes for your demonstration video

- Show registration and login, and mention that Firebase Authentication
  hashes/encrypts the password server-side.
- Show changing a setting (e.g. toggling notifications or changing the
  password) and log back in to prove it persisted.
- Show the Journey Planner making a live call to your hosted API (you can
  show the API's dashboard/data alongside the app to prove it's really
  hosted and not hard-coded).
- Show Live Alerts and Report Incident hitting the same API.
- Voice-over should call out where each Learning Unit 1/2 requirement is
  being demonstrated (REST API call, external library, SDK, and — separately,
  in your written submission — the unit test results).

## Project structure

```
app/src/main/java/com/vayalink/app/
├── data/
│   ├── model/        Route, Alert, IncidentReport, User
│   ├── remote/        ApiService (Retrofit), RetrofitClient
│   ├── local/         Room entity, DAO, database
│   └── repository/    AuthRepository, RouteRepository, AlertRepository, ReportRepository
├── viewmodel/         AuthViewModel, JourneyViewModel, AlertsViewModel, ReportViewModel
├── ui/
│   ├── splash/ auth/ home/ journey/ alerts/ report/ savedroutes/ profile/
│   └── adapter/       RecyclerView adapters
└── util/              Resource, ValidationUtils, FareCalculator, SessionManager

app/src/test/java/com/vayalink/app/
├── FareCalculatorTest.kt
├── ValidationUtilsTest.kt
├── RouteRepositoryTest.kt
└── ReportRepositoryTest.kt
```

## Honesty note on scope

This prototype deliberately keeps some things simple to stay buildable and
demoable in the time you have: it does not implement live GPS-tracking of
taxis (FR16/FR18), multi-taxi transfer planning (FR15), or the WhatsApp
chatbot channel — the design document flags these as fine to defer to the
final PoE submission. The four Learning Unit 1/2 marking criteria (REST API,
external library, SDK, unit testing) and a working prototype UI covering
several Part 1 features are fully implemented.
