# GTA Game
<img width="450" alt="intro" src="https://user-images.githubusercontent.com/31792170/181679196-c5b68d0f-0cf5-4039-873d-6f4d92e104ac.jpg">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/aaroncorona/GTA-Game">
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/aaroncorona/GTA-Game">


## 🚔 Overview
This is a 2D game written in Java. It is inspired by the original GTA game released in 1997.


## 🕹️ Game Features
1. 🏎️ **City Driving**: Simulates driving on an island with buildings. Don't crash or drown! 
2. 🔥 **Nitro**: Click R to get a nitro boost, which doubles the car's speed 
3. ⚔️ **Gun Fight**: Win a shootout with a cop to earn money and increase your score
4. 🥇 **High Scores**: Save and display high scores to see where you rank all time


## 🗂️ Source File Descriptions
* **Panel.java** - Panel logic and the game logic live here
* **Frame.java** - The Panel is instantiated within the contstructor for a JFrame
* **Main.java** - The JFrame is instantiated, therefore launching the game
* **Assets/Images** - Folder for PNG files used by the Java ImageIcon class to produce graphics.
  * The file naming pattern for player files is car + direction + regular vs nitro (e.g. "car_R_N"). This allows for 8 different images that correspond to the state of the car.
  * I used remove.bg to remove the PNG backgrounds for the files to give an icon effect on the panel
* **Assets/gta_high_scores.csv** - A local CSV where high scores are stored and read by the program


## 🚀 Installation
1. Clone this repo locally 
2. Navigate to the directory where the source Java files are stored
3. Run the Main file:
```
$ javac Main.java
$ java Main
```
4. *(Optional)* Erase the high scores to track your own personal best.
