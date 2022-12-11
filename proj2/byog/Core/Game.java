package byog.Core;

import static java.lang.System.exit;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Font;
import java.awt.Color;

import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */

    private static final int WIDTH = Constance.WIDTH;
    private static final int HEIGHT = Constance.HEIGHT;

    private static final int WINWIDTH = Constance.WIDTH;
    private static final int WINHEIGHT = Constance.HEIGHT+2;  // 2 for info bar

    private boolean inGame;

    private MazeWorld mazeWorld;

    //Status of game
    private int steps;

    /**
     * Constructor & Constructor Helpers.
     */
    Game(){
        steps = 0;

        inGame = false;  //when launch game, should be in panel waiting for input, instead of ingame
        StdDraw.setCanvasSize(WINWIDTH * 16, WINHEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        panel();

    }

    /*****  DRAWING  ***************************************/
    private void panel(){
        // title
        Font titleFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(titleFont);
        StdDraw.line(WIDTH/10, HEIGHT*3/4, WIDTH*4/10, HEIGHT*3/4);
        StdDraw.line(WIDTH*6/10, HEIGHT*3/4, WIDTH*9/10, HEIGHT*3/4);
        StdDraw.text(WIDTH/2, HEIGHT*3/4, "MAZE RUNNER");

        // Menu
        Font menuFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH/2, HEIGHT*8/16, "New Game (N)");
        //StdDraw.text(WIDTH/2, HEIGHT*7/16, "Load Game (L)");
        StdDraw.text(WIDTH/2, HEIGHT*6/16, "Quit (Q)");

        StdDraw.show();
    }

    private int panel_randomSeed(){
        StdDraw.clear(Color.BLACK);
        Font menuFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH/2, HEIGHT*8/16, "PLEASE INPUT A RANDOM SEED (from 0 to 10000): ");
        StdDraw.show();
        String inputRandomSeedStr = "";
        int inputRandomSeedInt = 0;
        while(true){
            if(!StdDraw.hasNextKeyTyped()) continue;
            String newInput = (String.valueOf(StdDraw.nextKeyTyped()));
            if(newInput.equals("Q") || newInput.equals("q")) {
                System.out.println("We end here!0");
                endGame();
            }
            if(newInput.charAt(0)<='9' && newInput.charAt(0)>='0'){
                inputRandomSeedStr += newInput;
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH/2, HEIGHT*8/16, "PLEASE INPUT A RANDOM SEED (from 0 to 10000): ");
                StdDraw.text(WIDTH/2, HEIGHT*6/16, inputRandomSeedStr);
                StdDraw.show();
                continue;
            }
            if(newInput.equals("\n")) {
                System.out.println("Enter");
                if(inputRandomSeedStr.length()>5 || Integer.valueOf(inputRandomSeedStr)>10000 || inputRandomSeedStr.equals("")){
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(WIDTH/2, HEIGHT*8/16, "INVALID RANDOMSEED!");
                    StdDraw.show();
                    inputRandomSeedStr = "";
                    //sleep for 1.5s
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {}

                    //wait for input again
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setFont(menuFont);
                    StdDraw.text(WIDTH/2, HEIGHT*8/16, "PLEASE INPUT A RANDOM SEED (from 0 to 10000): ");
                    StdDraw.show();
                    continue;
                }else {
                    inputRandomSeedInt = Integer.valueOf(inputRandomSeedStr);
                    return inputRandomSeedInt;
                }
            }
        }
        //return inputRandomSeedInt;
    }

    private void infoBar(){
        StdDraw.setPenColor(StdDraw.WHITE);
        String msgSteps = "STEPS: " + String.valueOf(steps);
        String quitMsg = "PRESS 'Q' TO QUIT";
        // INFORMATION TEXT
        Font infoFont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(infoFont);
        StdDraw.text(msgSteps.length() / 2 * 0.7, HEIGHT+1, msgSteps);
        StdDraw.text(WIDTH/2, HEIGHT+1, "MAP SEED: " + Integer.toString((int)Constance.SEED));
        StdDraw.text(WIDTH-quitMsg.length() / 2 * 0.7, HEIGHT+1, quitMsg);

        //LINE:
        StdDraw.line(0, HEIGHT+0.5, WINWIDTH, HEIGHT+0.5);
        StdDraw.line(0, HEIGHT+0.3, WINWIDTH, HEIGHT+0.3);
        StdDraw.show();
    }

    public void refreshGame(){
        ter.renderFrame(mazeWorld.getWorld());
        infoBar();
    }



    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        String input = "";
        /** in the MENU PANEL interface **/
        while(true){
            if(!StdDraw.hasNextKeyTyped()) continue;
            String newInput = (String.valueOf(StdDraw.nextKeyTyped())).toUpperCase();

            if(newInput.equals("Q")) endGame();

            if(newInput.equals("N")) {
                int randomSeedInput = panel_randomSeed();
                ter.initialize(WINWIDTH, WINHEIGHT);
                newGame(randomSeedInput);
                break;
            }
        }

        /** in the GAME interface **/
        while(true){
            if(mazeWorld.ifReachEnd()){
                winGame();
            }
            if(!StdDraw.hasNextKeyTyped()) continue;
            String newInput = (String.valueOf(StdDraw.nextKeyTyped())).toUpperCase();
            System.out.println(newInput);

            if(newInput.equals("Q")) endGame();

            // call this.mazeWorld.moveWSAD
            // ter.renderFrame(mazeWorld.getWorld());
            if(newInput.equals("W") || newInput.equals("S") || newInput.equals("A") || newInput.equals("D")){
                if(mazeWorld.move(newInput)){
                    steps++;
                    refreshGame();
                    continue;
                }

            }

        }
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    /**
     * Methods used for dealing with input.
     */

    public void newGame(int randomSeed){
        Constance.SEED = randomSeed;
        Constance.RANDOM = new Random(Constance.SEED);
        WorldGenerator world = new WorldGenerator(Tileset.GRASS);
        world.maze();
        TETile[][] screenTiles = new TETile[WINWIDTH][WINHEIGHT];  // a temp variable to hold all the tiles on screen (including top info bar)
        for(int x=0; x<WINWIDTH; x++){
            for(int y=0; y<WINHEIGHT; y++){ screenTiles[x][y] = (y>=WINHEIGHT-2) ? Tileset.GRASS : world.getWorld()[x][y]; }
        }
        // mazeWorld, includes all the tiles, and an info bar
        mazeWorld = new MazeWorld(screenTiles);
        refreshGame();
    }
    public void winGame(){
        StdDraw.clear(Color.black);
        Font menuFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(menuFont);
        StdDraw.text(WIDTH/2, HEIGHT*8/16, "CONGRATULATIONS!");
        StdDraw.text(WIDTH/2, HEIGHT*6/16, "YOU WIN WITH " + Integer.toString(steps) + " STEPS");
        StdDraw.show();

        //sleep for 1.5s
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {}

        endGame();

    }
    public void endGame(){
        //TODO: save datas
        exit(0);
    }
}
