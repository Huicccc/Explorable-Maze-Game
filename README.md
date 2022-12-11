## About
- Imagine there is a robot in an empty room, starting to explore the room by itself. In each step of the robot, it may go in a random direction, either straight forward, turn left, turn right, etc. When the robot stands in front of the edge of the room, it may turn back to find another way out. At the end, the track of the robot inside the room will form a basic layout of a maze. 
- This behavior is what we will capture in our explorable maze world. The topic of this project is about how to create a maze map which is auto-generated with a given random seed. In addition to the maze world itself, the topic is also about a solution on how to include various elements in the maze layout during the generation process.

## Algorithm
- Some issues that arise from using a pure Depth First Search traversal to construct our maze is that the resultant path does not accurately represent a maze (more of a snake-like structure than maze) and the starting point and ending point, if randomly selected, could be inside the maze instead of on the edge. 
- To remove the snake-like path that a depth first search produces, for each tile in the traversal, we will create a 20% chance of creating a ‘room’ for that path. We create these 'rooms' by taking the direction that the traversal was going before the current move and creating a room with a max distance of 3 tiles in that direction. This will give our grid a maze-like look with subsection rooms that typical mazes contain.
- The modified Depth-First Search algorithm we created takes O(nm) where n the width of our maze area and m is the height. We also have to store the stack of the traversed path in the file which requires O(nm) space.
- Our finished game with our GUI libraries incorporates the use of this autostepping algorithm and provides input for a random seed between 0-10,000. This random seed will be the same across all instances of the game since we use our own custom seed for the maze generation. Therefore at seed 1 in the game, the direction choice and 20% chance of room generation will be the same at each iteration of the seed traversal.

## User Manul
- Application consists of a title screen with menu options of starting a new game or quitting out. When you start a new game, you will then enter a number from 0-10,000 to represent the map seed for the maze. The program will populate the grid with the GUI libraries incorporated into the program and then you can play!
- The maze runner game will allow the user to use his keyboard to control the in-game player. We will bind the movement options of the player to the commonly used ‘w’, ‘a’, ‘s’, ‘d’ keys that are normally used for ‘up’, ‘left’, ‘down’, ‘right’ in keyboard games. 

<img width="1072" alt="Screen Shot 2022-12-10 at 6 13 23 PM" src="https://user-images.githubusercontent.com/105135459/206888457-8e42d6c2-40b4-4269-93d6-2436929e2db2.png">
<img width="1072" alt="Screen Shot 2022-12-10 at 7 43 41 PM" src="https://user-images.githubusercontent.com/105135459/206888459-6eca9f95-1c27-4cd6-a8aa-dff68b0da2dd.png">
<img width="1072" alt="Screen Shot 2022-12-10 at 7 44 42 PM" src="https://user-images.githubusercontent.com/105135459/206888460-f2ad7521-0e68-4335-9661-f973667946fc.png">
<img width="1072" alt="Screen Shot 2022-12-11 at 1 07 21 AM" src="https://user-images.githubusercontent.com/105135459/206889097-e781a1c1-76b9-4d18-8c2a-cbe67531cdab.png">


