# GTA Game
<img width="600" alt="GTA_SS" src="https://user-images.githubusercontent.com/31792170/189801013-2fa93873-29ff-437d-a05d-16403d16ed30.png">
<img alt="GitHub commit activity" src="https://img.shields.io/github/commit-activity/m/aaroncorona/GTA-Game">
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/aaroncorona/GTA-Game">


## 🚔 Overview
This is a 2D game written in Java. It is inspired by the original GTA game released in 1997.


## 🕹️ Game Features
1. 🏎️ **City Driving**: Simulates driving on an island with buildings. Don't crash or drown! 
2. 🔥 **Nitro**: Click R to get a nitro boost, which doubles the car's speed 
3. 🚨 **Wanted Levels**: Shooting cops and collecting money raises your wanted level. More and more cops will chase you.
4. 💰 **Earn Money**: Collect money and increase your score by winnnig gun fights
5. 🥇 **High Scores**: Save and display high scores to see where you rank all time


*Gameplay Screenshot:*
<br>
<br>
<img width="600" alt="GTA_SS" src="https://user-images.githubusercontent.com/31792170/189801013-2fa93873-29ff-437d-a05d-16403d16ed30.png">
<br>
<br>
*Gameplay Videos:*
<br>
* https://user-images.githubusercontent.com/31792170/182527366-04c2c83a-fd80-4a76-aeb5-f78b85685f6d.mov
* https://user-images.githubusercontent.com/31792170/182527684-f01e2c99-8eed-427a-8798-03a5550cc195.mov
<br>


## 🗂️ Source File Descriptions
* **main.Panel.java** - main.Panel logic and the game logic live here
* **main.Frame.java** - The main.Panel is instantiated within the contstructor for a JFrame
* **main.Main.java** - The JFrame is instantiated, therefore launching the game
* **assets/images** - Directory for PNG files used by the Java Graphics and BufferedImage classes to draw illustrations
  * The file naming pattern for entity image files is Class + direction + regular vs nitro (e.g. "player_car_R_nitro")
  * I used a tool called Piskel to draw the sprites to ensure they have the right background (usually transparent) and the correct pixel size (same as UNIT_SIZE) 
* **assets/scores** - Directory for CSV files where high scores are stored and read by the program


## 🚀 Installation
1. Clone this repo locally 
2. Navigate to the directory where the source Java files are stored
3. Run the main.Main file:
```
$ javac main.Main.java
$ java main.Main
```
4. *(Optional)* Erase the high scores to track your own personal high scores.

