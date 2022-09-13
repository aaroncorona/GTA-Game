# GTA Game
<img width="700" alt="Screen Shot 2022-09-12 at 9 33 29 PM" src="https://user-images.githubusercontent.com/31792170/189809441-d0935322-bb8a-40ad-b445-13f440e872e9.png">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/aaroncorona/GTA-Game">
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/aaroncorona/GTA-Game">


## 🚔 Overview
This is a 2D game built purely with Java. It is inspired by the original GTA game released in 1997.


## 🕹️ Game Features
1. 🌎 **World Map**: Drive around and explore the island
2. 🔥 **Nitro**: Click R to get a nitro boost, which doubles the car's speed 
3. 🚨 **Wanted Levels**: Shooting cops and collecting money raises your wanted level
4. 🧠 **Intelligent NPCs** The A* algorithm gives Cops pathfinding ability to your location
5. 💰 **Earn Money**: Collect money and increase your score by winning gun fights
6. 🥇 **High Scores**: Save and display high scores to see where you rank all time
7. 🔉 **Sound Effects**: Enjoy retro arcade sound effects


*Gameplay Screenshots:*
<br>
<br>
<img width="500" alt="Screen Shot 2022-09-12 at 9 37 11 PM" src="https://user-images.githubusercontent.com/31792170/189809929-6667754b-6e24-41bf-bd75-51a881c95cfc.png">
<br>
<br>
<img width="500" alt="Screen Shot 2022-09-12 at 9 32 06 PM" src="https://user-images.githubusercontent.com/31792170/189809283-3e5c0a0a-055b-44c2-bdd2-24ac7fdadea5.png">
<br>
<br>
<img width="500" alt="Screen Shot 2022-09-12 at 9 35 00 PM" src="https://user-images.githubusercontent.com/31792170/189809726-b2f94c87-9509-46db-9515-8cc962d1fe23.png">
<br>
<br>

*Gameplay Short Clips:*
<br>
* [Getting the first Wanted star](https://user-images.githubusercontent.com/31792170/189811171-1263d92d-38de-4731-87f8-4369e1a4be0e.mp4)
* [Getting chased and shot](https://user-images.githubusercontent.com/31792170/189811286-5ceb944f-74f5-4e82-9ed7-0955a86255b9.mp4)
* [5 star craziness](https://user-images.githubusercontent.com/31792170/189811357-578e8ff7-b70d-451f-a822-90e70c5510f9.mp4)



## 🗂️ Source Code - Java Package Descriptions
* **entity** - Includes all possible game constituents besides the background. There is an Interface and Super class for creating cars. There is also  a Super class for creating items like bullets and money.
* **main** - Core game mechanics and UI like the screen settings, key inputs, and sound.
* **menu** - All logic for when to open and close menus, what to display, and backend logic when needed (e.g. pulling high scores for the game over menu).
* **tile** - Everything related to drawing background, navigating through tiles with an A* algorithm, and detecting collisions.

## 🗂️ Source Code - Asset Descriptions
* **images** - Directory for PNG files used by the Java Graphics and BufferedImage classes to draw illustrations. I mainly used a tool called Piskel to draw the sprites to ensure they have the right background (usually transparent) and the correct pixel size (same as UNIT_SIZE).
* **maps** - Directory for world maps. Maps are text files that stores data about aa map, specifically describing which belongs to which location.  The tile/TileManager class translates the map data into graphics.
* **scores** - Directory for CSV files where high scores are stored and read by the menu/GameOverMenu class.
* **sounds** - Directory for wav files that are loaded to game sound clips by the main/Sound class


## 🚀 Installation
1. Clone this repo locally 
2. Navigate to the directory where the source Java files are stored
3. Run the main.Main file:
```
$ javac main.Main.java
$ java main.Main
```
4. *(Optional)* Erase the high scores to track your own personal high scores.

