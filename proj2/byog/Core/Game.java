package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.HexWorld;
import byog.util.StdDraw;

import java.awt.*;
import java.lang.reflect.WildcardType;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private int x;
    private int y;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.enableDoubleBuffering();
        TERenderer tr = new TERenderer();
        tr.initialize(WIDTH,HEIGHT);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 25, "CS61B The Game");
        StdDraw.text(40, 15, "New Game (N) ");
        StdDraw.text(40, 17, "Load Game (L) ");
        StdDraw.text(40, 19, "Quit (Q) ");
        StdDraw.show();
        StdDraw.pause(100);
        TETile[][] world = new TETile[0][];
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'N':
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.text(40, 25,"enter a random seed");
                        StdDraw.show();
                        StringBuilder sb = new StringBuilder();
                        while(true){
                            if(StdDraw.hasNextKeyTyped()) {
                                char c1 = StdDraw.nextKeyTyped();
                                if (c1 <= '9' && c1 >= '0') {
                                    sb.append(c1);
                                } else {
                                    break;
                                }
                            }
                        }
                        int seed = Integer.parseInt(sb.toString());
                        System.out.println(seed);
                        HexWorld hexWorld = new HexWorld(seed);
                        world = hexWorld.world;
                        ter.renderFrame(world);
                        break;
                    case 'Q':
                        System.exit(0);
                        break;
                    case 'L' :
                        int see1d1 = 0;
//                        world = LoadGame(see1d1);
                        ter.renderFrame(world);
                        break;
                    default:
                        break;
                }
                break;
            }

        }
        while(true){
            if(StdDraw.hasNextKeyTyped()){
                char c = StdDraw.nextKeyTyped();
                boolean flag = true;
                switch (c){
                    case 'A':
                        goNext(world,-1,0);
                        break;
                    case 'S':
                        goNext(world,1,0);
                        break;
                    case 'D':
                        goNext(world,0,-1);
                        break;
                    case 'Q':
                        flag = false;
                        break;
                    case 'W':
                        goNext(world,0,1);
                        break;
                    default:
                        break;
                }
                if(!flag){
                    break;
                }
            }
        }
    }
    void goNext(TETile[][] world,int dx,int dy){
        if(world[x + dx][y + dy] == Tileset.FLOOR){
            x += dx;
            y += dy;
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
}
