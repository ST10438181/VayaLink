# VayaLink

VayaLink is a mobile app prototype built to close the information gap in
South Africa's minibus taxi industry — giving commuters real-time route
information, fare estimates, live alerts, and a way to report incidents,
in one place.





---

## Purpose

An estimated 70% of South African public transport commuters rely on
minibus taxis, but there is no single, reliable source of information
about routes, fares, delays, or safety incidents. Existing solutions each
solve part of the problem — MiTaxi has industry backing but no real-time
tracking, GoTo has real-time tracking but almost no coverage, and the
George Passenger Application does multi-modal planning but only within
one town. VayaLink's prototype focuses on the overlap these apps miss:
searchable routes with fare/time estimates, live alerts, and
crowdsourced/driver reporting, backed by a real hosted REST API and
Firebase Authentication.



## Design considerations

- **Offline resilience.** Commuters often have inconsistent connectivity,
  so saved routes are cached locally with Room and remain viewable without
  a network connection.
- **Low barrier to account creation.** Firebase Authentication handles
  password hashing/salting server-side, so the app never stores or
  transmits a plain password itself.
- **Separation of concerns (MVVM).** UI (Activities), state/business logic
  (ViewModels), and data access (Repositories) are kept in separate
  layers so each piece can be tested and changed independently — see the
  unit tests under `app/src/test`, which mock the network layer entirely.
- **A single external REST API for both alerts and reports.** The
  prototype's hosted API (mockapi.io) has a resource limit on its free
  tier, so incident reports are posted into the same `alerts` resource
  they're later read back from, rather than a separate one — a deliberate
  simplification documented in `ApiService.kt` and `IncidentReport.kt`.

## Architecture

## Features implemented in this prototype

- Register / log in / log out via Firebase Authentication
- Change password, set preferred language and notification preferences
- Search taxi routes by origin/destination against the hosted REST API,
  with fare and travel-time estimates
- View live alerts (traffic, strikes, delays, safety, road closures)
- Report an incident, which is submitted to the hosted API
- Save routes offline for viewing without a connection

## Tech stack

- Kotlin, MVVM, Coroutines
- Retrofit + OkHttp + Gson — consumes the hosted RESTful API
- Room — offline cache for saved routes
- Firebase Authentication + Firestore — the app's SDK integration
- JUnit + Mockito + kotlinx-coroutines-test — unit tests

## Use of GitHub and GitHub Actions

This repository uses GitHub for version control, with commits made
incrementally as features were built (see commit history). A GitHub
Actions workflow is defined at `.github/workflows/android-ci.yml` and
runs automatically on every push and pull request to `main`. It:

1. Checks out the repository
2. Sets up JDK 17
3. Runs the project's unit tests (`./gradlew testDebugUnitTest`)
4. Runs Android Lint (`./gradlew lintDebug`)
5. Builds a debug APK (`./gradlew assembleDebug`)
6. Uploads the built APK as a downloadable workflow artifact

This means every change pushed to the repository is automatically
verified to compile and pass its tests, without needing to build it
locally first. You can see past runs under this repo's **Actions** tab.

## Setup

1. Clone this repository and open the `VayaLink` folder in Android
   Studio.
2. **Firebase**: create a Firebase project, register an Android app with
   package name `com.vayalink.app`, enable Authentication
   (Email/Password) and Firestore, then download your own
   `google-services.json` and place it in `app/google-services.json`
   (this file is git-ignored and not included in the repo, since it's
   project-specific and shouldn't be committed).
3. **Hosted REST API**: this project talks to a hosted mockapi.io
   project with `routes` and `alerts` resources. Update `BASE_URL` in
   `app/src/main/java/com/vayalink/app/data/remote/RetrofitClient.kt`
   with your own project's URL.
4. Sync Gradle and run.

## Running the tests
Tests live in `app/src/test/java/com/vayalink/app/` and cover fare
calculation, input validation, and repository logic with the network
layer mocked out — see `FareCalculatorTest.kt`, `ValidationUtilsTest.kt`,
`RouteRepositoryTest.kt`, and `ReportRepositoryTest.kt`.

