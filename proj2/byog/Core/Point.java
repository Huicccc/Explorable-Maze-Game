package byog.Core;

import java.util.Random;

public class Point {

  private int x;
  private int y;

  Point(int x, int y){
    this.x = x;
    this.y = y;
  }

  public static String getOppositeDir(String dir){
    String opposite = "";
    switch (dir){
      case "N":
        opposite = "S";
        break;
      case "S":
        opposite = "N";
        break;
      case "W":
        opposite = "E";
        break;
      case "E":
        opposite = "W";
        break;
      default:
        System.out.println("your input " + dir + " is invalid!");
        break;
    }
    return opposite;
  }

  public static String getDirection(int num){
    //int num = r.nextInt(4);
    String dir = "";
    switch (num){
      case 0:
        dir = "N";
        break;
      case 1:
        dir = "S";
        break;
      case 2:
        dir = "W";
        break;
      case 3:
        dir = "E";
        break;
      default:
        break;
    }
    return dir;
  }

  public int getX() { return this.x; }

  public int getY() { return this.y; }

  public void setX(int x){ this.x = x; }

  public void setY(int y){ this.y = y; }

  public void setXY(int x, int y){
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString(){
    return "X: " + String.valueOf(this.x) + ", Y: " + String.valueOf(this.y);
  }

  @Override
  public boolean equals(Object point2){
    if(point2==this) return true;
    if(!(point2 instanceof Point)) return false;
    Point p2 = (Point) point2;
    if(p2.x==this.x && p2.y==this.y) return true;
    return false;
  }


}
