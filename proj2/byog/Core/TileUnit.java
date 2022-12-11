package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class TileUnit {

  private static final int W = Constance.W;
  private static final int H = Constance.H;

  private int UNIT_W = Constance.UNIT_W;
  private int UNIT_H = Constance.UNIT_H;

  private TETile[][] unit;
  public Directions direction;
  public Dir fromDir;

  private boolean visited;
  private boolean extended;

  private int xUnit;
  private int yUnit;

  private boolean outside = true;




  /*****  Constructor ***************************************/
  TileUnit(TETile pattern, int xUnit, int yUnit){   // initialize the whole unit with one pattern
    this.unit = new TETile[UNIT_W][UNIT_H];
    for(int x = 0; x < UNIT_W; x++){
      for(int y = 0; y < UNIT_H; y++){
        this.unit[x][y] = pattern;
      }
    }
    this.xUnit = xUnit;
    this.yUnit = yUnit;
    this.direction = new Directions(this.xUnit, this.yUnit);
    this.fromDir = new Dir();
    this.checkEdge();
    this.visited = false;
    this.extended = false;
  }

  TileUnit(TETile[][] tiles, int xUnit, int yUnit){
    this.unit = new TETile[UNIT_W][UNIT_H];

    for(int x = 0; x < UNIT_W; x++){
      for(int y = 0; y < UNIT_H; y++){
        this.unit[x][y] = tiles[x][y];
      }
    }
    this.xUnit = xUnit;
    this.yUnit = yUnit;
    this.direction = new Directions(this.xUnit, this.yUnit);
    this.fromDir = new Dir();
    this.checkEdge();
    this.visited = false;
    this.extended = false;
  }

  public void checkEdge(){
    if(this.xUnit == 0) this.direction.removeDir("W");
    if(this.xUnit == W - 1) this.direction.removeDir("E");
    if(this.yUnit == 0) this.direction.removeDir("S");
    if(this.yUnit == H - 1) this.direction.removeDir("N");
  }

  /*****  Flag of Visiting ***************************************/
  public void setVisited(){
    //this.unit[1][1] = Tileset.TREE;
    this.visited = true;
  }

  public void setExtended(){
    this.extended = true;
  }

  public void setUnVisited(){
    this.visited = false;
  }

  public boolean isVisited(){
    return this.visited;
  }

  public boolean isExtended() { return this.extended; }

  /*****  Visiting ***************************************/
  public void visitedFromNeighbour(String dir){
    this.setUnitPattern(Tileset.WALL);
    this.setCenterPattern(Tileset.FLOOR);
    this.setDirPattern(dir, Tileset.FLOOR);
    this.setVisited();
    this.outside = false;
  }

  public void visitNeighbour(String dir){
    this.setDirPattern(dir, Tileset.FLOOR);
  }


  public void visiSelf(){
    this.setUnitPattern(Tileset.WALL);
    this.unit[UNIT_W / 2][UNIT_H / 2] =Tileset.FLOOR;
    this.setVisited();
    this.outside = false;
  }

  public void setDirPattern(String dir, TETile pattern){
    switch(dir){
      case "N":
        this.unit[UNIT_W / 2][UNIT_H - 1] = pattern;
        this.unit[UNIT_W / 2][UNIT_H - 1].makeUnChangable();

        break;
      case "S":
        this.unit[UNIT_W / 2][0] = pattern;
        this.unit[UNIT_W / 2][0].makeUnChangable();

        break;
      case "W":
        this.unit[0][UNIT_H / 2] = pattern;
        this.unit[0][UNIT_H / 2].makeUnChangable();

        break;
      case "E":
        this.unit[UNIT_W - 1][UNIT_H / 2] = pattern;
        this.unit[UNIT_W - 1][UNIT_H / 2].makeUnChangable();
        break;
      default:
        break;
    }
  }


  /*****  Position of this Unit ***************************************/
  public boolean atEdge(){  ///// need to re-write
    if(this.xUnit == 0) return true;
    return false;
  }


  // all the tiles inside the unit will be in one same pattern
  public void setUnitPattern(TETile pattern){
    for(int x = 0; x < UNIT_W; x++){
      for(int y = 0; y < UNIT_H; y++){
        this.unit[x][y] = pattern;
        //System.out.println("x:" + x + ".y:" + y);
      }
    }
  }
  public void setUnitTopRow(TETile pattern){
    for(int x = 0; x <UNIT_W; x++){
      this.unit[x][UNIT_H-1] = pattern;
    }
  }

  public void setCenterPattern(TETile pattern){
    this.unit[UNIT_W / 2][UNIT_H / 2] =pattern;
  }

  public TETile[][] getUnit(){return this.unit;}

  public void setTile(int x, int y, TETile pattern){
    if(!this.unit[x][y].isChangable()){
      return;
    }
    this.unit[x][y] = pattern;
  }

  public void quickSetTile(int x, int y, TETile pattern){
    this.unit[x][y] = pattern;
  }

  public int getXUnit() {return this.xUnit;}
  public int getYUnit() {return this.yUnit;}

  public boolean isOutside(){
    return this.outside;
  }

}
