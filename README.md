<p align="center">
  <a href=" ">
    <img src="https://i.imgur.com/ZjU4By3.png" height="200px">
  </a>
</p>
&nbsp;

Dungeons & Trolls is a "rogue-lite" dungeon crawller, inspired by D&D, fully created and playable in IntelliJ IDEA.
This was a group project, created in less than a week, for the MindSwap bootcamp at Mindera.

## Main features:

- Randomized Map every new game
- Server allows for multiple game instances
- 3 player multiplayer
- Voting system for players for moving options and other actions
  
## Documentation:

- To Start the game players must choose a name and Character class from a list of three:
<b>Knight, Mage or Squire</b>

<p align="left"><a href=" "><img src="https://i.imgur.com/ZeMR6G0.png" height="200px"></a></p>

- Players start at position [0][0] and have to find their way to position [5][5] to fight the dungeon BOSS

<p align="left"><a href=" "><img src="https://i.imgur.com/e7cCekG.png" height="400px"></a></p>

If the players don't reach majority in their votes, the game will decide which position to move for them.<p>
Likewise, if the players vote to go to an invalid direction (if they are at the border of the map)<br>the game will also choose the direction for them. 

- The Map only reveals what obstacle the players encounter after the voting turn finishes and they move to the new position

<p align="left"><a href=" "><img src="https://i.imgur.com/uZxaXHW.png" height="200px"></a></p>

## Fighting monsters:

Players can choose from three different actions: <b>Attack, Dodge or Defend.</b><p>
Attack: attacks monster.<br>
Defend: reduces incoming attack by half.<br>
Dodge: has a chance to evade the incoming attack,<br>which can fail according to the number of dodges tried.<p>

<p align="left"><a href=" "><img src="https://i.imgur.com/P1NbAF0.png" height="400px"></a></p>

- There are 5 types of Enemies and 2 Types of non-attackable entities all extending from a father class <b>"Obstacle"</b><p>

- Attackable Monsters:<p>

<b>GOBLIN</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/Z4wO9Lb.png" height="200px"></a></p>
<b>SLIME</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/MqTiyMW.png" height="200px"></a></p>
<b>TROLL</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/xmCzNiP.png" height="200px"></a></p>
<b>MINIBOSS</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/Au44HcR.png" height="200px"></a></p>
<b>BOSS</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/8vX6XXc.png" height="200px"></a></p>

- non-attackable entities:<p>

<b>FAIRY</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/oKpHPbY.png" height="100px"></a></p>
<b>CHEST</b>
<p align="left"><a href=" "><img src="https://i.imgur.com/eptgOhR.png" height="120px"></a></p>
<p align="left"><a href=" "><img src="https://i.imgur.com/EKudxva.png" height="80px"></a></p>
<p align="left"><a href=" "><img src="https://i.imgur.com/7X2b015.png" height="80px"></a></p>

- There is also an empty room instance, which can send three different messages to the players

<p align="left"><a href=" "><img src="https://i.imgur.com/yyKwCQN.png" height="100px"></a></p>

- There are two different end-game conditions: if the boss is dead OR if all players die

<p align="left"><a href=" "><img src="https://i.imgur.com/ShHCm0r.png" height="240px"></a></p>
<p align="left"><a href=" "><img src="https://i.imgur.com/IK3LyuF.png" height="240px"></a></p>

<h1>THANKS FOR PLAYING! </h1>

## Contributors 

<table>
  <tr>
  <td align="center"><a href="https://github.com/HenriqueGuim"><img src="https://avatars.githubusercontent.com/u/108727486?v=4" width="100px;" alt="Henrique Guimarães"/><br /><sub><b>Henrique Guimarães</b></sub></a><br /></td>
  <td align="center"><a href="https://github.com/JoseFerreira1995"><img src="https://avatars.githubusercontent.com/u/108727573?v=4" width="100px;" alt="José Ferreira"/><br /><sub><b>José Ferreira</b></sub></a><br /></td>
  <td align="center"><a href="https://github.com/Iamcogita"><img src="https://avatars.githubusercontent.com/u/99983918?v=4" width="100px;" alt="David Mendes"/><br /><sub><b>David Mendes</b></sub></a><br /></td>
  </tr>
  
</table>
