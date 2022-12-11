package byog.Core;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Directions {
  private  Dir N  = new Dir();
  private  Dir S  = new Dir();
  private  Dir W  = new Dir();
  private  Dir E  = new Dir();

  public int size;
  //private static final long SEED = 12345;
  //public static final Random RANDOM = new Random(SEED);

  private Stack<Dir> dirCombine;


  Directions(int xUnit, int yUnit){
    this.N.dir = "N";
    this.N.able = true;

    this.S.dir = "S";
    this.S.able = true;

    this.W.dir = "W";
    this.W.able = true;

    this.E.dir = "E";
    this.E.able = true;
    this.size = 4;

    this.checkEdge(xUnit, yUnit);

    this.dirCombine = new Stack<Dir>();
    this.randomDir();

  }


  public void randomDir(){
    int i = 0;
    ArrayList<Dir> temp = new ArrayList<Dir>();
    if(this.N.able) temp.add(this.N);
    if(this.S.able) temp.add(this.S);
    if(this.W.able) temp.add(this.W);
    if(this.E.able) temp.add(this.E);
    this.dirCombine = new Stack<>();
    while(!temp.isEmpty()){
      this.dirCombine.push(temp.remove(Constance.RANDOM.nextInt(temp.size())));
    }
  }

  public Stack<Dir> getDirCombine(){
    return this.dirCombine;
  }

  public Dir popOne(){
    if(!isEmpty()){
      Dir temp = this.dirCombine.pop();
      this.removeDir(temp.dir);
      return temp;
    }
    return null;
  }

  public boolean isEmpty(){
    if(size == 0) return true;
    return false;
  }

  public void removeDir(String dir){
    switch (dir){
      case "N":
        if(this.N.able == false){
          break;
        }
        this.N.able = false;
        this.size --;
        this.randomDir();
        break;
      case "S":
        if(this.S.able == false){
          break;
        }
        this.S.able = false;
        this.size --;
        this.randomDir();

        break;
      case "W":
        if(this.W.able == false){
          break;
        }
        this.W.able = false;
        this.size --;
        this.randomDir();

        break;
      case "E":
        if(this.E.able == false){
          break;
        }
        this.E.able = false;
        this.size --;
        this.randomDir();

        break;
      default:
        break;
    }
  }

  public void checkEdge(int xUnit, int yUnit){
    if(xUnit == 0) this.removeDir("W");
    if(xUnit == Constance.W - 1) this.removeDir("E");
    if(yUnit == 0) this.removeDir("S");
    if(yUnit == Constance.H - 1) this.removeDir("N");
  }



  //public class Dir{

  //}

}
