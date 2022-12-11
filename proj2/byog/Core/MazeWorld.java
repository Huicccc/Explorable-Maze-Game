package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Stack;

public class MazeWorld {
  private TETile[][] world;
  private Point playerLoc; // store x and y of player character's location
  private Point startLoc; //store x and y of start's location
  private Point endLoc; // store x and y of end's location

  /*****  CONSTRUCTOR  ***************************************/
  MazeWorld(TETile[][] tiles){
    playerLoc = new Point(0, 0);
    startLoc = new Point(0, 0);
    endLoc = new Point(0, 0);
    outerloop:
    for(int x=0; x<tiles.length; x++){
      for(int y=0; y<tiles[0].length; y++){
        if(tiles[x][y].equals(Tileset.PLAYER)){
          playerLoc.setXY(x, y);
          //break outerloop;
        }
        if(tiles[x][y].equals(Tileset.LOCKED_DOOR))
          startLoc.setXY(x, y);
        if(tiles[x][y].equals(Tileset.UNLOCKED_DOOR))
          endLoc.setXY(x, y);
      }
    }
    world = tiles;
    System.out.println("Player: " + playerLoc);
    System.out.println("Start: " + startLoc);
    System.out.println("End: " + endLoc);

  }


  /*****  Get Variables  ***************************************/
  public TETile[][] getWorld(){
    return world;
  }

  /*****  MOVE PLAYER  ***************************************/
  // by giving dir( W S A D ), update the coordinate of playerLoc
  public boolean move(String dir){
    // dir given to this method could only be "W" "S" "A" "D"
    // if success to move, return true, else return false
    switch (dir){
      //move NORTH  ↑
      case "W":
        if(!world[playerLoc.getX()][playerLoc.getY()+1].equals(Tileset.WALL)){
          world[playerLoc.getX()][playerLoc.getY()+1] = Tileset.PLAYER;
          world[playerLoc.getX()][playerLoc.getY()] = Tileset.FLOOR;
          this.playerLoc.setY(playerLoc.getY()+1);
          resetStart();
          return true;
        }
        break;

      //move EAST →
      case "D":
        if(!world[playerLoc.getX()+1][playerLoc.getY()].equals(Tileset.WALL)){
          world[playerLoc.getX()+1][playerLoc.getY()] = Tileset.PLAYER;
          world[playerLoc.getX()][playerLoc.getY()] = Tileset.FLOOR;
          this.playerLoc.setX(playerLoc.getX()+1);
          resetStart();
          return true;
        }
        break;

      //move SOUTH ↓
      case "S":
        if(!world[playerLoc.getX()][playerLoc.getY()-1].equals(Tileset.WALL)){
          world[playerLoc.getX()][playerLoc.getY()-1] = Tileset.PLAYER;
          world[playerLoc.getX()][playerLoc.getY()] = Tileset.FLOOR;
          this.playerLoc.setY(playerLoc.getY()-1);
          resetStart();
          return true;
        }
        break;

      //move WEST ←
      case "A":
        if(!world[playerLoc.getX()-1][playerLoc.getY()].equals(Tileset.WALL)){
          world[playerLoc.getX()-1][playerLoc.getY()] = Tileset.PLAYER;
          world[playerLoc.getX()][playerLoc.getY()] = Tileset.FLOOR;
          this.playerLoc.setX(playerLoc.getX()-1);
          resetStart();
          return true;
        }
        break;

      default:
        break;
    }
    return false;
  }

  /*****  JUSTIFICATION  AND OTHERS ***************************************/
  public boolean ifReachEnd(){
    if(this.playerLoc.equals(endLoc)) return true;
    return false;
  }

  private void resetStart(){
    if(!startLoc.equals(playerLoc))
      world[startLoc.getX()][startLoc.getY()] = Tileset.LOCKED_DOOR;
  }



}
