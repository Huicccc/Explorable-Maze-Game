package byog.Core;

import java.util.Stack;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


import java.util.Random;


public class WorldGenerator {
  private static final int WIDTH = Constance.WIDTH;
  private static final int HEIGHT = Constance.HEIGHT;

  private static final int UNIT_W = Constance.UNIT_W;
  private static final int UNIT_H = Constance.UNIT_H;




  private static final int W = Constance.W;
  private static final int H = Constance.H;
  private static final int U_NOS = W * H;
  private static final int MAX_VISIT_NOS = U_NOS / 10 * 8;

  private int numVisited;

  private int startXUnit;
  private int startYUnit;

  private int currentXUnit;
  private int currentYUnit;
  private Dir direction;

  public int nextXUnit;
  public int nextYUnit;

  private TileUnit[][] tileUnit;

  private Stack<TileUnit> tileUnitStack;

  /*****  Constructors  ***************************************/
  WorldGenerator(){
    this(Tileset.NOTHING);
  }

  WorldGenerator(TETile pattern){
    this.tileUnit = new TileUnit[W][H];

    //
    for(int xUnit = 0; xUnit < W; xUnit++){
      for(int yUnit = 0; yUnit < H; yUnit++){
        this.tileUnit[xUnit][yUnit] = new TileUnit(pattern, xUnit, yUnit);
      }
    }

    this.startXUnit = Constance.RANDOM.nextInt(W);
    this.startYUnit = Constance.RANDOM.nextInt(H);
    this.currentXUnit = startXUnit;
    this.currentYUnit = startYUnit;
    this.tileUnitStack = new Stack<TileUnit>();
    this.tileUnitStack.push(this.tileUnit[this.startXUnit][this.startYUnit]);
    // after push the first unit into stack, pop its direction out
    this.direction = this.tileUnit[this.currentXUnit][this.currentYUnit].direction.popOne();
    this.numVisited = 1;
  }

  /*****  Actions with small tiles  ***************************************/
  public TETile[][] getWorld(){
    TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    for(int x = 0; x < WIDTH; x++){
      for(int y = 0; y < HEIGHT; y++ ){
        int xUnit = x / UNIT_W;
        int yUnit = y / UNIT_H;
        int xRela = x % UNIT_W;
        int yRela = y % UNIT_H;
        tiles[x][y] = this.tileUnit[xUnit][yUnit].getUnit()[xRela][yRela];
      }
    }
    return tiles;
  }

  public void setTile(int x, int y, TETile pattern){
    int xUnit = x / UNIT_W;
    int yUnit = y / UNIT_H;
    int xRela = x % UNIT_W;
    int yRela = y % UNIT_H;
    this.tileUnit[xUnit][yUnit].setTile(xRela, yRela, pattern);
  }

  public void quickSetTile(int x, int y, TETile pattern){
    int xUnit = x / UNIT_W;
    int yUnit = y / UNIT_H;
    int xRela = x % UNIT_W;
    int yRela = y % UNIT_H;
    this.tileUnit[xUnit][yUnit].quickSetTile(xRela, yRela, pattern);
  }

  public TETile getTile(int x, int y){
    int xUnit = x / UNIT_W;
    int yUnit = y / UNIT_H;
    int xRela = x % UNIT_W;
    int yRela = y % UNIT_H;
    return this.tileUnit[xUnit][yUnit].getUnit()[xRela][yRela];
  }

  /*****  Actions with tile units  ***************************************/
  // set tiles (9 nos.) in a tile Unit to a same pattern
  public void setTileUnitPattern(int x, int y, TETile pattern){
    this.tileUnit[x][y].setUnitPattern(pattern);
  }

  public TileUnit[][] getTileUnit(){
    return this.tileUnit;
  }


  /*****  Generate a Maze  ***************************************/
  public void maze(){
    this.tileUnit[this.currentXUnit][this.currentYUnit].visiSelf();
    this.autoStepping();
    //this.tileUnit[startXUnit][startYUnit].quickSetTile(0, 1, Tileset.LOCKED_DOOR);
    this.setGate();
  } // end of maze

  /*****  Move  ***************************************/

  public void autoStepping() {
    outerloop:
    while (this.numVisited <= MAX_VISIT_NOS) {
      if (this.canMoveNext()) {
        this.tileUnitStack.push(this.tileUnit[this.currentXUnit][this.currentYUnit]);
        visitOneDir();
        // by now the direction hasn't been updated, but the coordinates has been updated
        // create rooms here. 20% possibility to create a room(within a unit)
        if(Constance.RANDOM.nextInt(3) % 2 == 1){
          this.createRoom();
        }


        // the new node, don't need to worry if its direction is empty, because is it's first time
        // being visited
        this.direction = this.tileUnit[this.currentXUnit][this.currentYUnit].direction.popOne();
        continue;
      } else {
        if (this.tileUnitStack.size() == 0) {
          break;  // break outerloop
        }
        while (true) {
          TileUnit temp = this.tileUnitStack.pop();
          this.currentXUnit = temp.getXUnit();
          this.currentYUnit = temp.getYUnit();
          if (this.tileUnit[this.currentXUnit][this.currentYUnit].direction.isEmpty()) {
            if (this.tileUnitStack.size() == 0) {
              break outerloop;
            }
            continue;
            }
          this.direction = this.tileUnit[this.currentXUnit][this.currentYUnit].direction.popOne();
          break;  // back to outerloop
        }
      }
    }
  }


  private boolean canMoveNext(){
    this.mockMove();
    if(this.tileUnit[this.nextXUnit][this.nextYUnit].isVisited()){
      while(!this.tileUnit[this.currentXUnit][this.currentYUnit].direction.isEmpty()){
        this.direction = this.tileUnit[this.currentXUnit][this.currentYUnit].direction.popOne();
        mockMove();
        if(this.tileUnit[this.nextXUnit][this.nextYUnit].isVisited()) {continue;}
        return true;
      }
      return false;
    }
    return true;
  }

  private void visitOneDir(){
    this.numVisited++;
    this.tileUnit[this.currentXUnit][this.currentYUnit].visitNeighbour(this.direction.dir);

    String opposite = Point.getOppositeDir(this.direction.dir);
    //System.out.print("("+this.currentXUnit + ","+ this.currentYUnit+") ");
    this.moveCurrent();
    if(currentXUnit==0 && currentYUnit ==8){
      System.out.println("visit "+ "("+this.currentXUnit + ","+ this.currentYUnit+")");
    }
    this.tileUnit[this.currentXUnit][this.currentYUnit].visitedFromNeighbour(opposite);
    //System.out.println("visit "+ "("+this.currentXUnit + ","+ this.currentYUnit+")");
  }  // end of visitOneDir

  //update the Unit coordinate to a given direction
  private void moveCurrent(){
    switch (this.direction.dir){
      case "N":
        this.currentYUnit ++;
        break;
      case "S":
        this.currentYUnit --;
        break;
      case "W":
        this.currentXUnit --;
        break;
      case "E":
        this.currentXUnit ++;
        break;
      default:
        System.out.println("In moveCurrent:");
        System.out.println("your input " + this.direction.dir + " is invalid!");
        break;
    }
  } // end of moveCurrent

  private void mockMove(){
    this.nextXUnit = this.currentXUnit;
    this.nextYUnit = this.currentYUnit;
    switch (this.direction.dir){
      case "N":
        this.nextYUnit++;
        break;
      case "S":
        this.nextYUnit --;
        break;
      case "W":
        this.nextXUnit --;
        break;
      case "E":
        this.nextXUnit ++;
        break;
      default:
        System.out.println("In mockMove:");
        System.out.println("your input " + this.direction.dir + " is invalid!");
        break;
    }
  } // end of mockMove

  private void createRoom(){
    // below, the direction is an outdated one, which means where the scanner comes from
    // but the coordinates are updated
    if(this.direction.dir == "N"){
      int rightX = this.currentXUnit + 1;
      int rightY = this.currentYUnit;

      int leftX = this.currentXUnit - 1;
      int leftY = this.currentYUnit;

      if (rightX < W){
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(1, 0, Tileset.FLOOR);
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(2, 0, Tileset.FLOOR);
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(2, 1, Tileset.FLOOR);

        this.tileUnit[rightX][rightY].setTile(0, 0, Tileset.FLOOR);
        this.tileUnit[rightX][rightY].setTile(0, 1, Tileset.FLOOR);
        this.tileUnit[rightX][rightY].setTile(1, 0, Tileset.FLOOR);
        this.tileUnit[rightX][rightY].setTile(1, 1, Tileset.FLOOR);

        this.tileUnit[rightX][rightY].setTile(0, 2, Tileset.WALL);
        this.tileUnit[rightX][rightY].setTile(1, 2, Tileset.WALL);
        this.tileUnit[rightX][rightY].setTile(2, 2, Tileset.WALL);
        this.tileUnit[rightX][rightY].setTile(2, 1, Tileset.WALL);
        this.tileUnit[rightX][rightY].setTile(2, 0, Tileset.WALL);

        if(!this.tileUnit[rightX][rightY].isVisited()){
          this.tileUnit[rightX][rightY].setVisited();
          this.numVisited++;
          }

        if(this.tileUnit[rightX][rightY - 1].isVisited()){
          this.tileUnit[rightX][rightY-1].setTile(0,2, Tileset.FLOOR);
          this.tileUnit[rightX][rightY-1].setTile(1,2, Tileset.FLOOR);
          this.tileUnit[this.currentXUnit][rightY-1].setTile(2,2, Tileset.FLOOR);
          //this.tileUnit[rightX][rightY-1].setTile(2,2, Tileset.FLOOR);
        }

        if(this.tileUnit[rightX][rightY - 1].isOutside()){
          this.tileUnit[rightX][rightY-1].setUnitTopRow(Tileset.GRASS);
          this.tileUnit[rightX][rightY-1].setTile(0,2, Tileset.WALL);
          this.tileUnit[rightX][rightY-1].setTile(1,2, Tileset.WALL);
          this.tileUnit[rightX][rightY-1].setTile(2,2, Tileset.WALL);
          this.tileUnit[this.currentXUnit][rightY-1].setTile(2,2, Tileset.WALL);
          if(!this.tileUnit[rightX][rightY - 1].isVisited()){
            this.tileUnit[rightX][rightY - 1].setVisited();
            this.numVisited++;
          }
        }
      }

      if(leftX >= 0){
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(0, 0, Tileset.FLOOR);
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(0, 1, Tileset.FLOOR);
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(1, 0, Tileset.FLOOR);

        this.tileUnit[leftX][leftY].setTile(2, 1, Tileset.FLOOR);
        this.tileUnit[leftX][leftY].setTile(2, 0, Tileset.FLOOR);
        this.tileUnit[leftX][leftY].setTile(1, 0, Tileset.FLOOR);
        this.tileUnit[leftX][leftY].setTile(1, 1, Tileset.FLOOR);
        this.tileUnit[leftX][leftY].setTile(0, 0, Tileset.WALL);
        this.tileUnit[leftX][leftY].setTile(0, 1, Tileset.WALL);
        this.tileUnit[leftX][leftY].setTile(0, 2, Tileset.WALL);
        this.tileUnit[leftX][leftY].setTile(1, 2, Tileset.WALL);
        this.tileUnit[leftX][leftY].setTile(2, 2, Tileset.WALL);

        if(!this.tileUnit[leftX][leftY].isVisited()){
          this.tileUnit[leftX][leftY].setVisited();
          this.numVisited++;
        }


        //LOCALRANDOM.nextInt(2)%2==0
        if(true){
          if(this.tileUnit[leftX][leftY - 1].isVisited()){
            this.tileUnit[leftX][leftY-1].setTile(2,2, Tileset.FLOOR);
            this.tileUnit[leftX][leftY-1].setTile(1,2, Tileset.FLOOR);
           // this.tileUnit[this.currentXUnit][leftY-1].setTile(0,2, Tileset.FLOOR);
            //this.tileUnit[rightX][rightY-1].setTile(2,2, Tileset.FLOOR);
          }
          if(this.tileUnit[leftX][leftY - 1].isOutside()){
            this.tileUnit[leftX][leftY - 1].setUnitTopRow(Tileset.GRASS);

            this.tileUnit[leftX][leftY-1].setTile(2,2, Tileset.WALL);
            this.tileUnit[leftX][leftY-1].setTile(1,2, Tileset.WALL);
            this.tileUnit[leftX][leftY-1].setTile(0,2, Tileset.WALL);

            this.tileUnit[currentXUnit][currentYUnit].setTile(0,2, Tileset.WALL);
            if (!this.tileUnit[leftX][leftY - 1].isVisited()){
              this.tileUnit[leftX][leftY - 1].setVisited();
              this.numVisited++;
            }
          }
        }


      }
    }

    // below, the direction is an outdated one, which means where the scanner comes from
    // but the coordinates are updated
    if(this.direction.dir == "E"){
      int upX = this.currentXUnit;
      int upY = this.currentYUnit + 1;

      int downX = this.currentXUnit;
      int downY = this.currentYUnit - 1;

      if(upY < H){
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(0, 2, Tileset.FLOOR);
        this.tileUnit[this.currentXUnit][this.currentYUnit].setTile(1, 2, Tileset.FLOOR);

        this.tileUnit[upX][upY].setTile(0, 0, Tileset.FLOOR);
        this.tileUnit[upX][upY].setTile(1, 0, Tileset.FLOOR);
        this.tileUnit[upX][upY].setTile(0, 1, Tileset.FLOOR);
        this.tileUnit[upX][upY].setTile(1, 1, Tileset.FLOOR);

        this.tileUnit[upX][upY].setTile(0,2,Tileset.WALL);
        this.tileUnit[upX][upY].setTile(1,2,Tileset.WALL);
        this.tileUnit[upX][upY].setTile(2,2,Tileset.WALL);
        this.tileUnit[upX][upY].setTile(2,1,Tileset.WALL);
        this.tileUnit[upX][upY].setTile(2,0,Tileset.WALL);

        if(!this.tileUnit[upX][upY].isVisited()){
          this.tileUnit[upX][upY].setVisited();
          this.numVisited++;
        }

        if(this.tileUnit[upX-1][upY].getUnit()[2][0].equals(Tileset.GRASS) ||
            this.tileUnit[upX-1][upY].getUnit()[2][1].equals(Tileset.GRASS) ||
            this.tileUnit[upX-1][upY].getUnit()[2][2].equals(Tileset.GRASS)){
          this.tileUnit[upX-1][upY].setTile(2, 0, Tileset.WALL);
          this.tileUnit[upX-1][upY].setTile(2, 1, Tileset.WALL);
          this.tileUnit[upX-1][upY].setTile(2, 2, Tileset.WALL);
          this.tileUnit[upX-1][upY].setVisited();
          this.numVisited++;
        }

        /**if(!this.tileUnit[upX-1][upY].isVisited()){
          this.tileUnit[upX-1][upY].setTile(2,0, Tileset.WALL);
          this.tileUnit[upX-1][upY].setTile(2,1, Tileset.WALL);
          this.tileUnit[upX-1][upY].setTile(2,2, Tileset.WALL);
          this.tileUnit[upX-1][upY].setVisited();
          this.numVisited++;
        }*/
      }

      if(downY >= 0){

      }

    }
    return;
  } // end of creatRoom

  /*****  SetUp Special Tiles  ***************************************/

  private void setGate(){
    int left = WIDTH - 1;
    int right = 0;
    int bottom1 = HEIGHT - 1;
    int bottom2 = HEIGHT - 1;
    for(int x=0; x<WIDTH; x++){
      for(int y=0; y<HEIGHT; y++){
        if(getWorld()[x][y].equals(Tileset.FLOOR)){
          left = Math.min(x, left);
          right = Math.max(x, right);
        }
      }
    }

    for(int i=0; i<HEIGHT; i++){
      if(getWorld()[left][i].equals(Tileset.FLOOR)){bottom1 = Math.min(bottom1, i);}
      if(getWorld()[right][i].equals(Tileset.FLOOR)){bottom2 = Math.min(bottom2, i);}
    }
    // set the gate
    quickSetTile(left, bottom1-1, Tileset.LOCKED_DOOR);
    quickSetTile(right, bottom2-1, Tileset.UNLOCKED_DOOR);

    //set the position of player
    quickSetTile(left, bottom1, Tileset.PLAYER);
  }







//  public static void main(String[] args){
//    TERenderer ter = new TERenderer();
//    ter.initialize(Constance.WIDTH, Constance.HEIGHT);
//
//    WorldGenerator world = new WorldGenerator(Tileset.GRASS);
//
//    //world.setTileUnitPattern(3,7, Tileset.WALL);
//    world.maze();
//
//
//    ter.renderFrame(world.getWorld());
//
//  } //end of main


}
