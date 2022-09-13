# GTA Game
<img width="600" alt="GTA_SS" src="https://user-images.githubusercontent.com/31792170/189803612-b4cafcb6-f92b-48d2-bdde-041f7f20bad4.png">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/aaroncorona/GTA-Game">
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/aaroncorona/GTA-Game">


## 🚔 Overview
This is a 2D game built purely with Java. It is inspired by the original GTA game released in 1997.


## 🕹️ Game Features
1. 🏎️ **City Driving**: Simulates driving on an island with buildings. Don't crash or drown! 
3. 🔥 **Nitro**: Click R to get a nitro boost, which doubles the car's speed 
4. 🚨 **Wanted Levels**: Shooting cops and collecting money raises your wanted level. More and more cops (NPCs with pathfinding ability!) will chase you.
5. 🌎 **World Map**: Explore the Island as you evade the police
6. 💰 **Earn Money**: Collect money and increase your score by winning gun fights
7. 🥇 **High Scores**: Save and display high scores to see where you rank all time
8. 🔉 **Sound Effects**: Enjoy retro arcade sound effects


*Gameplay Screenshots:*
<br>
<br>
<img width="500" alt="Screen Shot 2022-09-12 at 8 56 27 PM" src="https://user-images.githubusercontent.com/31792170/189804128-78285f81-6533-4e4d-96ce-6385b01cdc3f.png">
<br>
<br>
<img width="500" alt="GTA_SS_GameOver" src="https://user-images.githubusercontent.com/31792170/189803672-992e5aa7-ef17-4b02-87a4-774d99542032.png">
<br>
<br>
*Gameplay Videos:*
<br>
* 
<br>


## 🗂️ Source Code - Package Descriptions
* **entity** -
* **main** - 
* **menu** - 
* **tile** - 

## 🗂️ Asset Descriptions
* **assets/images** - Directory for PNG files used by the Java Graphics and BufferedImage classes to draw illustrations. I mainly used a tool called Piskel to draw the sprites to ensure they have the right background (usually transparent) and the correct pixel size (same as UNIT_SIZE).
* **assets/maps** - Directory for world maps. Maps are text files that stores data about aa map, specifically describing which belongs to which location.  The tile/TileManager class translates the map data into graphics.
* **assets/scores** - Directory for CSV files where high scores are stored and read by the menu/GameOverMenu class.
* **assets/sounds** - Directory for wav files that are loaded to game sound clips by the main/Sound class


## 🚀 Installation
1. Clone this repo locally 
2. Navigate to the directory where the source Java files are stored
3. Run the main.Main file:
```
$ javac main.Main.java
$ java main.Main
```
4. *(Optional)* Erase the high scores to track your own personal high scores.

