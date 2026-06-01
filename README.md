
# Sudoku_The_Material_One

This repository contains the source code for **Sudoku - The Material One**, an Android game developed over two months and published on the Google Play Store. The game features a modern Material Design interface and includes both classic and complex Sudoku variations.

## Core Implementations
The game is built with a custom Sudoku engine from the ground up. Key technical features include:
*   **Dynamic 2D Grid System:** A flexible grid engine that supports various Sudoku layouts, including the complex overlapping grids of Samurai Sudoku.
*   **Asynchronous Grid Loading:** Efficiently loads and renders large Sudoku boards using `AsyncTask` to prevent blocking the UI thread.
*   **Custom Sudoku Board View:** A highly customized view (`SudokuBoardView` and `CellView`) for rendering the game board, handling user input, displaying hints, and managing themes.
*   **Advanced Cell Logic:** A complex system of cell rules and conditions (`CellRule`, `CheckCondition`) to handle the intricate constraints of different Sudoku variants.

## Gameplay and Algorithms
The core gameplay logic relies on a combination of algorithms to generate and solve puzzles:
*   **Backtracking:** A recursive backtracking algorithm is implemented in `SudokuSolver` and `SamuraiSudokuSolver` to generate valid, solvable Sudoku boards from a blank state.
*   **Constraint Propagation:** The logic enforces Sudoku rules across rows, columns, and blocks to validate user input and assist in generating hints.

## Features
*   **Multiple Sudoku Types:**
    *   **Standard Sudoku:** Classic Sudoku with grid sizes from 2x2 up to 5x5 (4x4 to 25x25 cells).
    *   **Samurai Sudoku:** Challenging variants with five overlapping grids, including `Gattai-2`, `Gattai-3`, and a massive "Super" Samurai puzzle.
    *   **Daily Challenges:** Unique puzzles available every day.
*   **Six Difficulty Levels:** From Beginner to Insane, catering to all skill levels by adjusting the number of blank cells.
*   **Customizable Themes:** A selection of Material Design color themes to personalize the look and feel of the game, with support for system-level dark mode.
*   **Game State Management:** Automatically saves your progress using `SharedPreferences`, allowing you to resume your game at any time.
*   **User Assistance Features:**
    *   **Hints:** Shows possible candidates for empty cells.
    *   **Undo:** Reverts your moves.
    *   **Input Validation:** Highlights conflicting numbers.
*   **Player Statistics:** Tracks performance, including games won and completion times for different game modes and difficulties.
*   **Monetization:** Includes Google AdMob integration (banner, interstitial, and app open ads) and an in-app purchase option to remove them via the Google Play Billing Library.

## Project Structure
The codebase is organized into several key packages:
*   **`core`**: Contains all the core logic for the Sudoku game, including puzzle generation (`SudokuCreator`), solving (`SudokuSolver`), and the data structures for the board (`Board`, `Cell`, `Sudoku`). The `sudokutypes` sub-package defines the structure for Standard and various Samurai grids.
*   **`view`**: Houses the Android View components responsible for the UI. The most notable are `SudokuBoardView`, which renders the interactive game board, and `SudokuParamsView` for the number input pad.
*   **`activities`**: Defines the main screens of the application, such as `MainActivity`, `Gameplay`, and `Statistics`.
*   **`data`**: Handles all data persistence using `SharedPreferences` for game state, user settings, and statistics.
*   **`theme`**: Manages the application's theming system, allowing users to switch between different color palettes.
*   **`ads` & `billing`**: Implements monetization through Google AdMob and the Google Play Billing Library.
