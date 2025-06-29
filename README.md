# Distance Calculator - Running Tracker

A simple Android app to track running activities, calculate distance, and monitor speed using device location.

## Features

- Start, pause, resume, and finish tracking your run
- Calculates total distance covered using GPS
- Displays current speed based on distance and elapsed time
- Stores location data locally

## Tech Stack

- Kotlin
- Android Jetpack Compose
- Coroutines & StateFlow
- Room (for local data storage)

## Getting Started

1. **Clone the repository:**
2. **Open in Android Studio:**
   - Open the project in Android Studio (2025.1.1 or later recommended).

3. **Build & Run:**
   - Connect an Android device or use an emulator.
   - Click **Run**.

## Project Structure

- `app/src/main/java/com/scorpio/distancecalculator/`
  - `tracker/RunningTracker.kt` - Core tracking logic
  - `locationproducer/` - Location update provider
  - `LocationDao`, `LocationEntity` - Local database entities and access

## Usage

- Tap **START** to begin tracking.
- Tap **PAUSE** to pause tracking.
- Tap **RESUME** to continue.
- Tap **FINISH** to end and reset the session.


# TODO

- [ ] Setup CI/CD
  - [ ] Build
  - [ ] Test
  - [ ] Lint
- [ ] Change to a more user-friendly UI
- [ ] Add Hilt for Dependency Injection
- [ ] Organize dependency versions in `libs.toml.version`

## License

MIT License. See `LICENSE` for details.