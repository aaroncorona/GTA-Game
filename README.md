# GTA Game
<img width="600" alt="Screen Shot 2022-09-12 at 9 33 29 PM" src="https://user-images.githubusercontent.com/31792170/189809441-d0935322-bb8a-40ad-b445-13f440e872e9.png">
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
<img width="500" alt="Screen Shot 2022-09-12 at 9 37 11 PM" src="https://user-images.githubusercontent.com/31792170/189809929-6667754b-6e24-41bf-bd75-51a881c95cfc.png">

<br>
<br>
<img width="500" alt="Screen Shot 2022-09-12 at 9 32 06 PM" src="https://user-images.githubusercontent.com/31792170/189809283-3e5c0a0a-055b-44c2-bdd2-24ac7fdadea5.png">
<br>
<br>
<img width="500" alt="Screen Shot 2022-09-12 at 9 35 00 PM" src="https://user-images.githubusercontent.com/31792170/189809726-b2f94c87-9509-46db-9515-8cc962d1fe23.png">

<br>
<br>
*Gameplay Videos:*
<br>
* 
<br>


## 🗂️ Source Code - Java Package Descriptions
* **entity** -
* **main** - 
* **menu** - 
* **tile** - 

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

