# GTA Game
<img width="700" alt="Screen Shot 2022-09-12 at 9 33 29 PM" src="https://user-images.githubusercontent.com/31792170/189809441-d0935322-bb8a-40ad-b445-13f440e872e9.png">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/aaroncorona/GTA-Game">
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/aaroncorona/GTA-Game">

## đ Overview
This is a 2D game built purely with Java. It is inspired by the original GTA game from 1997.

## đšī¸ Game Features
1. đ **World Map**: Drive around and explore the island
2. đĨ **Nitro**: Click R to get a nitro boost, which doubles the car's speed 
3. đ¨ **Wanted Levels**: Shooting cops and collecting money raises your wanted level
4. đ§  **Intelligent NPCs**: The A* algorithm gives Cops pathfinding ability to your location
5. đ° **Earn Money**: Collect money and increase your score by winning gun fights
6. đĨ **High Scores**: Save and display high scores to see where you rank all time
7. đ **Sound Effects**: Enjoy retro arcade sound effects

## đ Gameplay Short Clips:

*Menus*

https://user-images.githubusercontent.com/31792170/190935779-17ece8c0-ee1e-4db5-a6c9-8018186fd9d7.mov


*Getting the 1st Wanted star*

https://user-images.githubusercontent.com/31792170/190885397-a8100232-67bb-4fa1-8384-040d8218ca59.mov


*NPC Cops chasing player*

https://user-images.githubusercontent.com/31792170/189811286-5ceb944f-74f5-4e82-9ed7-0955a86255b9.mp4


## đī¸ Source Code - Java Package Descriptions
* **entity** - Includes all possible game constituents besides the background. This package includes an Interface and Super classes (abstract) for creating entities, cars, and items. 
* **main** - Handles core game mechanics and UI like the game loop, screen settings, key inputs, and sound.
* **menu** -  Logic for when the menu displays and file reading for pulling high scores for the game over menu.
* **physics** - Handles collisions and pathfinding with an A* algorithm.
* **tile** - Everything related to drawing background from the world map file and the Camera to navigate the map.

## đī¸ Source Code - Asset Descriptions
* **images** - Directory for PNG files used by the Java Graphics and BufferedImage classes to draw illustrations. I mainly used a tool called Piskel to draw the sprites to ensure they have the right background (usually transparent) and the correct pixel size (same as UNIT_SIZE).
* **maps** - Directory for world maps. Maps are text files that stores data about aa map, specifically describing which belongs to which location.  The tile/TileManager class translates the map data into graphics.
* **scores** - Directory for CSV files where high scores are stored and read by the menu/GameOverMenu class
* **sounds** - Directory for WAV files that are loaded to game sound clips by the main/Sound class


## đ Installation
1. Clone this repo locally 
2. Navigate to the directory where the source Java files are stored
3. Run the Main file:
```
$ javac Main.java
$ java Main
```
4. *(Optional)* Erase the high scores to track your own personal high scores.

