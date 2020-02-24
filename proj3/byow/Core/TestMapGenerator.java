package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import org.junit.Test;

import java.util.Map;

public class TestMapGenerator {
    private static final int HEIGHT = 45;
    private static final int WIDTH = 70;

    public static void main(String args[]){
        int seed = 899;
        MapGenerator myMap = new MapGenerator(WIDTH, HEIGHT, seed);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        ter.renderFrame(myMap.getWorld());
    }
}
