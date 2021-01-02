package byog.lab5;

import byog.TileEngine.TERenderer;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class testSerialzable {
    static void saveWorld(HexWorld world){
        File f = new File("./" + world.seed + ".ser");
        try{
            if(!f.exists()){
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
        }catch (FileNotFoundException e){
            System.out.println(e);
            System.exit(0);
        }catch (IOException e){
            System.out.println(e);
            System.exit(0);
        }
    }
    static HexWorld loadWorld(int seed){
        File f = new File("./" + seed + ".ser");
        try{
            if(!f.exists()){
                return null;
            }
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            HexWorld ans = (HexWorld) ois.readObject();
            return ans;
        }catch (FileNotFoundException e){
            System.out.println(e);
            System.exit(0);
        }catch (IOException e){
            System.out.println(e);
            System.exit(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        while(true){
            int seed = in.nextInt();
            HexWorld world = loadWorld(seed);
            if(world == null){
                world = new HexWorld(seed);
            }
            world.fillWithTile();
            TERenderer ter = new TERenderer();
            ter.initialize(80,30);
            ter.renderFrame(world.world);
            saveWorld(world);
        }

    }
}
