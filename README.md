# **Word Wrap**

## **Overview**
Word Wrap is an engaging word-finding puzzle game inspired by the word game Boggle where players drag their fingers across a grid of letters to form words. The objective is to discover as many valid words as possible on the board within a play session. The game combines strategy, vocabulary, and quick thinking.

This project is an Android application developed in **Java**, leveraging custom views, animations, and Android's shared preferences for storing statistics. It also uses advanced UI techniques like dynamic layouts and transitions to enhance the user experience.

---

## **Features**
- **Dynamic Gameboard**: A 4x4 grid dynamically created at runtime with random letters.
- **Word Selection**: Intuitive touch input where users drag across the grid to form words in 8 possible directions.
- **Score Tracking**: A scoring system that rewards longer words with higher points.
- **Game Statistics**: Persistent stats, such as total score, words found, and average words per game, saved using shared preferences.
- **Animations**: Interactive tile animations when valid words are found, with tiles flying off the grid.
- **Bottom Sheets**: Slide-up panels to display found words or remaining words.
- **Customizable UI**: Buttons styled as arrows for navigation and custom shape-drawn buttons.

---

## **Technology Stack**
- **Language**: Java
- **Framework**: Android SDK
- **Storage**: SharedPreferences for persistent data storage.
- **Animation**: Android Transition Framework and custom animations with `ObjectAnimator` and `AnimatorSet`.

---

## **Project Structure**
### **Core Components**
1. **Activities**:
   - `MainActivity`: The app's home screen with navigation options.
   - `GameActivity`"
   - `StatsActivity`: Displays cumulative game statistics.
2. **Fragments**:
   - `GameboardFragment`: The main game logic and UI.
   - `WordDisplayBottomSheet`: Bottom sheet to display found or remaining words.
3. **Logic**:
   - `Boggle`: Handles game logic, including word validation and scoring.
   - `Dictionary`: Provides the graph data structure which efficiently solves the board using DFS.
   - `Die`: Represents a single die on the board with configurable sides.
4. **Layouts**:
   - `fragment_gameboard.xml`: The primary game UI with a grid and score display.
   - `activity_main.xml`: The home screen layout with navigation buttons.
   - `activity_stats.xml`: Layout for the stats display.
5. **Resources**:
   - **Drawable**: Custom shapes and arrows for UI elements.
   - **Transition**: XML-defined transitions for animations.

---

## **Gameplay**
### **How to Play**
1. Launch the app and start a new game from the home screen.
2. A 4x4 grid of letters appears. Drag your finger across the grid to form words.
3. Words can be created in 8 directions:
   - **North, South, East, West, Northeast, Northwest, Southeast, Southwest**
4. Words are validated against a dictionary. Valid words earn points, and tiles animate when a word is successfully formed.
5. View found words or remaining words using the respective buttons.

---

## **Scoring System**
- Words are scored based on their length:
  - 1-2 letters: **0.5 points**
  - 3-4 letters: **1 point**
  - 5 letters: **2 points**
  - 6 letters: **3 points**
  - 7 letters: **5 points**
  - 8+ letters: **11 points**

---

## **Key Features**
### **Dynamic Gameboard**
- The gameboard is generated dynamically using `CardView` and `GridLayout`.
- Dice rolls determine the letters on the board.
- Each die is configured with the standard Boggle Die configuration

### **Tile Animation**
- Tiles fly off the board when a valid word is found, using `AnimatorSet`.

### **Statistics**
- Tracks total score, total games played, and average words per game.
- Saves data using `SharedPreferences`.

### **Bottom Sheets**
- Found words and remaining words are displayed in a `BottomSheetDialogFragment`.

### **Custom UI Elements**
- Buttons styled as arrows (`<--` and `-->`) using shape drawables.
- Game tiles have a polished design with rounded corners and a highlighted selection state.

---

## **Setup and Installation**
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/word-wrap-game.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and ensure all dependencies are installed.
4. Run the application on an Android emulator or device.

---

## **How to Contribute**
1. Fork the repository and create your branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes and commit:
   ```bash
   git commit -m "Add your message"
   ```
3. Push your branch and create a pull request.

---

## **Future Improvements**
- Multiplayer mode for competitive play.
- Timed game modes with countdown timers.
- Expandable board sizes (e.g., 5x5, 6x6).
- Enhanced animations and sound effects.

---

## **Acknowledgments**
- Android SDK documentation for guidance on animations and transitions.
- The Boggle game concept for inspiration.

Feel free to reach out for any suggestions or questions!
