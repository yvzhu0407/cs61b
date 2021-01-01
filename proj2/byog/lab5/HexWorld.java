package byog.lab5;
import byog.Core.RandomUtils;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private final double DEFAULT_LOAD_FACTOR = 0.4;
    private final double DEFAULT_LIMIT_FACOTR = 0.2;
    private final int WIDTH = 50;
    private final int HEIGHT = 50;

    //unite the two room
    void unite(int[] r1,int[] r2,int[][] map,Random random){
        //get the two rooms core position
        int mx1 = r1[0] + r1[2] / 2;
        int my1 = r1[1] + r1[3] / 2;
        int mx2 = r2[0] + r2[2] / 2;
        int my2 = r2[1] + r2[3] / 2;


        //we draw vertical line at mx which is the middle of mx1 and mx2
        //and the line is from min(my1,my2) to max(my1,my2)

        /**
         *                  ····
         *          ·       ·  ·
         * ·····    ·       ····
         * ·   ·    ·
         * ·   ·
         * ·····
         */
        int mx = (mx1 + mx2) / 2;
        for(int i = Math.min(my1,my2);i <= Math.max(my1,my2);i++){
            map[mx][i] = 1;
        }
        //next we draw a horizontal line form position(mx1,my1) to the middle line draw before
        while(mx1 != mx){
            if(mx > mx1){
                mx1 ++;
            }else {
                mx1 --;
            }
            map[mx1][my1] = 1;
        }
        //next we draw a horizontal line form position(mx2,my2) to the middle line draw before
        while (mx2 != mx){
            if(mx > mx2){
                mx2++;
            }else{
                mx2 --;
            }
            map[mx2][my2] = 1;
        }
    }

    /**
     * boring the world
     * 1. generate the numbers of rooms of this game by the seed;
     * 2. connect the disjointed rooms with hallways.
     * 3. fill the wall at last;
     * @param seed
     * @return
     */
    int[][] boringWorld(int seed){
        Random random = new Random(seed);
        int room_num = RandomUtils.uniform(random,20);
        int[][] map = new int[WIDTH][HEIGHT];
        //store the room position and area after it has been generated.
        List<int[]> list = new ArrayList<>();

        for(int k = 0;k < room_num;k++){
            //make sure the room width between 2 and 7;
            int w = RandomUtils.uniform(random,2,7);
            //make sure the room height between 2 and 7;
            int h = RandomUtils.uniform(random,2,7);
            //make the room x position will not exceed the game boundary
            int x = RandomUtils.uniform(random,2,WIDTH - w);
            //the same as the previous
            int y = RandomUtils.uniform(random,2,HEIGHT - h);
            //remember the new room disjoint the previous rooms added to map before
            boolean flag = false;
            for(int i = 0;i < w;i++){
                for(int j = 0;j < h;j++){
                    if(map[i + x][j + y] == 1){
                        flag = true;
                    }else map[i + x][j + y] = 1;
                }
            }
            list.add(new int[]{x,y,w,h});
            //if the new room is alone ,and we connect the new room with the previous one
            if(!flag && k > 0){
                unite(list.get(list.size() - 2),list.get(list.size() - 1),map,random);
            }
        }
        //fill the wall ,details see the function
        fillWall(map);
        //add a door int the map
        List<int[]> walls = new ArrayList<>();


        for(int i = 0;i < WIDTH;i++){
            for(int j = 0;j < HEIGHT;j++){
                if(map[i][j] == 2){
                    if(map[i - 1][j] + map[i + 1][j] == 1 || map[i][j - 1] + map[i][j + 1] == 1){
                        walls.add(new int[]{i,j});
                    }
                }
            }
        }
        int door = RandomUtils.uniform(random,walls.size());
        map[walls.get(door)[0]][walls.get(door)[1]] = 3;
        return map;
    }

    /**map[i][j] == 1 means this is a floor
     *  what we need to do is add wall to surround the floor
     * @param map
     */
    private void fillWall(int[][] map) {
        //every floor has 8 tiles surrounding it.
        //so we iterate the 8 tiles and fill a wall when and only when it is not a floor
        int[] dx = {1,1,1,0,0,-1,-1,-1};
        int[] dy = {1,0,-1,1,-1,1,0,-1};
        for(int i = 0;i < WIDTH;i++){
            for(int j = 0;j < HEIGHT;j++){
                if(map[i][j] == 1){
                    for(int k = 0;k < dx.length;k++){
                        map[i + dx[k]][j + dy[k]] = map[i + dx[k]][j + dy[k]] == 1 ? 1 : 2;
                    }
                }
            }
        }
    }

    //generate TETile map
    private void fillWithTile(TETile[][] randomTiles, int[][] map) {
        for(int i = 0;i < WIDTH;i++){
            for(int j = 0;j < HEIGHT;j++){
                switch (map[i][j]){
                    case 2 -> randomTiles[i][j] = Tileset.WALL;
                    case 1 -> randomTiles[i][j] = Tileset.FLOOR;
                    case 0 -> randomTiles[i][j] = Tileset.NOTHING;
                    case 3 -> randomTiles[i][j] = Tileset.LOCKED_DOOR;
                    default ->randomTiles[i][j] = Tileset.NOTHING;
                }
            }
        }
    }
    public static void main(String[] args) {
        HexWorld hexWorld = new HexWorld();
        int[][] map = hexWorld.boringWorld(15653516);
        TERenderer ter = new TERenderer();
        ter.initialize(hexWorld.WIDTH, hexWorld.HEIGHT);

        TETile[][] randomTiles = new TETile[hexWorld.WIDTH][hexWorld.HEIGHT];
        //fill the TETile map with map
        hexWorld.fillWithTile(randomTiles,map);
        //show the TETile map
        ter.renderFrame(randomTiles);
    }


}
