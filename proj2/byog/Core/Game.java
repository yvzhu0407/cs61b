package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.HexWorld;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private HexWorld hexWorld;
    int seed = 0;
    TERenderer tr = null;
    private boolean isPlayerRound = false;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        if( !isPlayerRound ) {
            initStdDraw();
        }
        tenderMain();
        while ( true ) {
            if ( StdDraw.hasNextKeyTyped() ) {
                char c = StdDraw.nextKeyTyped();
                switch ( c ) {
                    case 'N':
                        tenderTips();
                        StringBuilder sb = new StringBuilder();
                        while( true ) {
                            if( StdDraw.hasNextKeyTyped() ) {
                                char c1 = StdDraw.nextKeyTyped();
                                if ( c1 <= '9' && c1 >= '0') {
                                    sb.append( c1 );
                                } else {
                                    break;
                                }
                            }
                        }
                        seed = Integer.parseInt( sb.toString() );
//                        System.out.println( seed );
                        hexWorld = new HexWorld( seed );
                        hexWorld.fillWithTile();
                        ter.renderFrame(hexWorld.world);
                        isPlayerRound = true;
                        break;
                    case 'Q':
                        System.exit(0);
                        break;
                    case 'L' :
                        StdDraw.clear(StdDraw.BLACK);
                        hexWorld = this.loadWorld();
                        if(hexWorld == null){
                            hexWorld = new HexWorld(seed);
                        }
                        hexWorld.fillWithTile();
                        ter.renderFrame(hexWorld.world);
                        isPlayerRound = true;
                        break;
                    default:
                        break;
                }
                break;
            }
        }
        //change it to play with string
        // and play with key word should use the same fuction;
//        StringBuilder sb = new StringBuilder();
        while( isPlayerRound ){
            if( StdDraw.hasNextKeyTyped() ){
                char ch = StdDraw.nextKeyTyped();
                boolean flag = true;
                if(ch == 'A' || ch == 'D' || ch == 'S' || ch == 'W'){
                    goNext(ch);
                    ter.renderFrame(hexWorld.world);
                }else if(ch == 'Q'){
                    isPlayerRound = false;
                }
            }
        }
        saveWorld(hexWorld);
        playWithKeyboard();
    }
    void tenderMain(){
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 25, "CS61B The Game");
        StdDraw.text(40, 15, "New Game (N) ");
        StdDraw.text(40, 17, "Load Game (L) ");
        StdDraw.text(40, 19, "Quit (Q) ");
        StdDraw.show();
        StdDraw.pause(100);
    }
    void tenderTips(){
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(40, 25,"enter a random seed");
        StdDraw.show();
    }
    void initStdDraw(){
        StdDraw.enableDoubleBuffering();
        tr = new TERenderer();
        tr.initialize(WIDTH,HEIGHT);
    }
    public void fun(int dx,int dy){
        int xx = hexWorld.x;
        int yy = hexWorld.y;
        hexWorld.world[xx + dx][yy + dy] = Tileset.PLAYER;
        hexWorld.world[xx][yy] = Tileset.FLOOR;
        hexWorld.map[xx][yy] = 1;
        hexWorld.map[xx + dx][yy + dy] = 4;
        hexWorld.x += dx;
        hexWorld.y += dy;
    }
    public void goNext(char ch){
        int xx = hexWorld.x;
        int yy = hexWorld.y;
        switch (ch){
            case 'A' :
                if(hexWorld.world[xx - 1][yy] == Tileset.FLOOR){
                    fun(-1,0);
                }
                break;
            case 'S' :
                if(hexWorld.world[xx][yy - 1] == Tileset.FLOOR){
                    fun(0,-1);
                }
                break;
            case 'W' :
                if(hexWorld.world[xx][yy + 1] == Tileset.FLOOR){
                    fun(0,1);
                }
                break;
            case 'D' :
                if(hexWorld.world[xx + 1][yy] == Tileset.FLOOR){
                    fun(1,0);
                }
                break;
            default:
                break;
        }
    }
    void saveWorld(HexWorld world){
        File f = new File("./" + world.seed + ".ser");
        try{
            if(!f.exists()){
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
        } catch (IOException e){
            System.out.println(e);
            System.exit(0);
        }
    }
    HexWorld loadWorld(){
        File f = new File("./" + seed + ".ser");
        HexWorld ans = null;
        try{
            if(!f.exists()){
                System.out.println("not fount this file and start to create a new world");
                return null;
            }
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ans = (HexWorld) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }
        return ans;
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
        if(!isPlayerRound){
            initStdDraw();
        }
        int i = 1;
        if(input.charAt(0) == 'N'){
            seed = 0;
            while(i < input.length() && input.charAt(i) <= '9' && input.charAt(i) >= '0'){
                seed = seed * 10 + input.charAt(i) - '0';
                i++;
            }
            hexWorld = new HexWorld(seed);
            hexWorld.fillWithTile();
            ter.renderFrame(hexWorld.world);
        }else{
            hexWorld = loadWorld();
            hexWorld.fillWithTile();
            i++;
            ter.renderFrame(hexWorld.world);
        }
        while(i < input.length() && input.charAt(i) != ':'){
            goNext(input.charAt(i));
            ter.renderFrame(hexWorld.world);
            i++;
        }
        saveWorld(hexWorld);
        return hexWorld.world;
    }
}
