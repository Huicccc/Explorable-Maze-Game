package byog.Core;

import java.util.Random;

public class Constance {

  protected static final int UNIT_W = 3;  // numbers of tiles in each row of a tile unit
  protected static final int UNIT_H = 3;  // numbers of tiles in each column of a tile unit

  protected static final int WIDTH = 60; // have to be multiplication of 3. suggest 120 TETiles in width
  protected static final int HEIGHT = 24; // have to be multiplication of 3. suggest 45 TETiles in height

  protected static final int W = WIDTH / UNIT_W;  // numbers of tile units in a row of screen
  protected static final int H = HEIGHT / UNIT_H;  // numbers of tile units in a column of screen

  public static long SEED = 1231423;
  public static Random RANDOM = new Random(SEED);





}
