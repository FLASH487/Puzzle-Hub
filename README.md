# Puzzle Hub 🧩💖

A mini-game hub Android app featuring **Memory Match Cards** and **Sliding Number Puzzle**. Built with Java, AndroidX, Material Components, and Room database. Designed with a soft pastel feminine UI theme.

## 📱 Project Description

Puzzle Hub is a beginner-friendly Android app that contains two classic puzzle games. Players can challenge themselves, track their scores, and share results with friends. The app demonstrates core Android development concepts including Activities, Fragments, Intents, RecyclerView, and Room Database.

## 🎮 Features

- **Memory Match Cards** – Find matching pairs on 4×4 (Easy) or 6×6 (Hard) grids
- **Sliding Number Puzzle** – Arrange numbered tiles in order on 3×3 or 4×4 grids
- **Score Tracking** – Save and view game results with Room database
- **Share Results** – Share your scores via any app using Android's share intent
- **Score History** – Filter scores by game type with Material filter chips
- **Beautiful UI** – Soft pastel feminine design with Material 3 components

## 🏗️ Activities Used (9 Activities)

| # | Activity | Description |
|---|----------|-------------|
| 1 | `SplashActivity` | App launch screen with logo (1.5s delay) |
| 2 | `HomeActivity` | Main menu with navigation buttons |
| 3 | `GameHubActivity` | Game selection with MaterialCardView |
| 4 | `MemoryMatchActivity` | Memory Match game controller (timer + moves) |
| 5 | `SlidingPuzzleActivity` | Sliding Puzzle game controller (timer + moves) |
| 6 | `ResultActivity` | Game results display with save/share options |
| 7 | `ScoreHistoryActivity` | Score history list with filter chips |
| 8 | `HowToPlayActivity` | Game instructions screen |
| 9 | `AboutActivity` | App information with project link |

## 🧩 Fragment Usage (4 Fragments + 2 Base Classes)

Fragments are used as **reusable UI components** for game boards:

| Fragment | Description |
|----------|-------------|
| `BaseMemoryFragment` | Abstract base with shared memory match logic |
| `MemoryEasyFragment` | 4×4 Easy memory game (extends base) |
| `MemoryHardFragment` | 6×6 Hard memory game (extends base) |
| `BaseSlidingFragment` | Abstract base with shared sliding puzzle logic |
| `Slide3x3Fragment` | 3×3 sliding puzzle (extends base) |
| `Slide4x4Fragment` | 4×4 sliding puzzle (extends base) |

Fragments are loaded into a `FrameLayout` container using `FragmentTransaction.replace()`.

## 📐 Layout Types Used (3+ Types)

| Layout Type | Where Used |
|-------------|------------|
| **ConstraintLayout** | `activity_home.xml` – Main menu with constrained buttons |
| **LinearLayout** | `activity_game_hub.xml`, `activity_result.xml`, `activity_about.xml`, toolbars |
| **FrameLayout** | `fragment_memory_board.xml`, `fragment_sliding_board.xml`, fragment containers |
| ScrollView | `activity_how_to_play.xml`, `activity_result.xml` |
| RecyclerView | Game boards (GridLayoutManager), Score history (LinearLayoutManager) |

## 🔗 Intent Usage

### Explicit Intents (Activity to Activity)
Used throughout the app for navigation between screens:
```java
// Example: Navigate from HomeActivity to GameHubActivity
Intent intent = new Intent(this, GameHubActivity.class);
startActivity(intent);
```

### Passing Data with Intent Extras
Game data is passed between Activities using `putExtra()` and `getStringExtra()`/`getIntExtra()`:
```java
// Sending data
intent.putExtra("GAME_TYPE", "MEMORY");
intent.putExtra("DIFFICULTY", "EASY");
intent.putExtra("MOVES", moves);
intent.putExtra("TIME_SECONDS", timeSeconds);

// Receiving data
String gameType = getIntent().getStringExtra("GAME_TYPE");
int moves = getIntent().getIntExtra("MOVES", 0);
```

### Implicit Intents
1. **ACTION_SEND** – Share game results via any app (WhatsApp, Email, etc.)
   ```java
   Intent shareIntent = new Intent(Intent.ACTION_SEND);
   shareIntent.setType("text/plain");
   shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
   startActivity(Intent.createChooser(shareIntent, "Share your score"));
   ```

2. **ACTION_VIEW** – Open a URL in the default browser (AboutActivity)
   ```java
   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"));
   startActivity(intent);
   ```

## 💾 Database Implementation (Room)

### Entity: `ScoreEntity`
| Field | Type | Description |
|-------|------|-------------|
| `id` | int | Auto-generated primary key |
| `gameType` | String | "MEMORY" or "SLIDE" |
| `difficulty` | String | "EASY", "HARD", "3x3", or "4x4" |
| `moves` | int | Number of moves made |
| `timeSeconds` | int | Time taken in seconds |
| `dateMillis` | long | Timestamp when score was saved |

### DAO: `ScoreDao`
- `insert(ScoreEntity)` – Save a new score
- `getAllScores()` – Get all scores (returns LiveData)
- `getScoresByType(String)` – Filter by game type
- `deleteAll()` – Clear all scores

### Database: `AppDatabase`
- Singleton pattern using `Room.databaseBuilder()`
- Simple implementation with no repository layer

## 🚀 How to Run the App

1. Open the project in **Android Studio** (Arctic Fox or later)
2. Ensure you have **SDK 34** installed
3. Connect an Android device or start an emulator (API 24+)
4. Click **Run** ▶️ or use:
   ```bash
   ./gradlew assembleDebug
   ```
5. Install the APK on your device

## 📸 Screenshot Checklist for Report

Use this checklist to capture screenshots for your assignment report:

- [ ] **Splash Screen** – App logo and welcome message
- [ ] **Home Screen** – Main menu with all navigation buttons
- [ ] **Game Hub** – Game selection cards (Memory Match & Sliding Puzzle)
- [ ] **Difficulty Dialog** – Difficulty selection popup
- [ ] **Memory Match (Easy)** – 4×4 game board with cards
- [ ] **Memory Match (Hard)** – 6×6 game board with cards
- [ ] **Memory Match (Playing)** – Cards flipped showing matching logic
- [ ] **Sliding Puzzle (3×3)** – 3×3 game board
- [ ] **Sliding Puzzle (4×4)** – 4×4 game board
- [ ] **Result Screen** – Game results with save/share buttons
- [ ] **Share Intent** – Android share chooser dialog
- [ ] **Score History** – List of saved scores
- [ ] **Score History (Filtered)** – Scores filtered by game type
- [ ] **How To Play** – Instructions screen
- [ ] **About Screen** – App information with project link
- [ ] **Browser (Implicit Intent)** – URL opened from About screen

## 🎤 Short Explanation for Presentation

> "Puzzle Hub is an Android app with two mini-games: Memory Match Cards and Sliding Number Puzzle. It demonstrates key Android concepts:
>
> - **9 Activities** for different screens (Splash, Home, Game Hub, games, results, etc.)
> - **Fragments** as reusable UI components for game boards with different difficulties
> - **3 layout types**: ConstraintLayout, LinearLayout, and FrameLayout
> - **Explicit Intents** for navigating between Activities and passing game data
> - **Implicit Intents** for sharing scores (ACTION_SEND) and opening URLs (ACTION_VIEW)
> - **Room Database** for saving and retrieving game scores locally
> - **RecyclerView** with GridLayoutManager for game boards and LinearLayoutManager for score history
> - **Material 3 design** with a soft pastel color scheme
>
> The code is written in plain Java with clear comments explaining each concept."

## ✅ Assignment Requirements Checklist

| Requirement | Status | Where |
|-------------|--------|-------|
| At least 5 Activities | ✅ (9 Activities) | All Activity classes |
| At least 1 Fragment | ✅ (4 Fragments + 2 base) | fragment package |
| At least 3 layout types | ✅ (Constraint, Linear, Frame) | layout XML files |
| Explicit Intents | ✅ | HomeActivity, GameHubActivity, etc. |
| Implicit Intents | ✅ | ResultActivity (ACTION_SEND), AboutActivity (ACTION_VIEW) |
| Room Database | ✅ | db package (Entity, DAO, Database) |
| Custom app icon | ✅ | mipmap/ic_launcher (pink theme) |
| Clear navigation | ✅ | Splash → Home → GameHub → Game → Result |
| RecyclerView | ✅ | Game boards + Score history |
| Clean code comments | ✅ | All Java files commented |

## 🛠️ Tech Stack

- **Language**: Java (Android)
- **UI**: AndroidX + Material Components 3
- **Database**: Room 2.6.1
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Build**: Gradle 8.2 + AGP 8.2.0
