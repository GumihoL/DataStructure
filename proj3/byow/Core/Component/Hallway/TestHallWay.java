package byow.Core.Component.Hallway;

import byow.Core.Position;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TestHallWay {
    private static final int HEIGHT = 40;
    private static final int WIDTH = 60;
    private static TETile[][] createEmptyWorld(){
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++){
            for (int y = 0; y < HEIGHT; y++){
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }


    public static void main(String args[]){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = createEmptyWorld();
        Queue<Hallway> hs = new LinkedList<>();

        Hallway h1 = new upHallway(Hallway.Orientation.U,
                                5,
                                new Position(15, 13)
                                );
        Hallway h2 = new downHallway(Hallway.Orientation.D,
                                 7,
                                 new Position( 25, 25));
        Hallway h3 = new leftHallway(Hallway.Orientation.L,
                5,
                new Position(35, 19)
        );
        Hallway h4 = new rightHallway(Hallway.Orientation.R,
                7,
                new Position( 40, 15));
        hs.add(h1);
        hs.add(h2);
        hs.add(h3);
        hs.add(h4);

        for (int i = 0; i < 4; i++){
            Hallway h = hs.poll();
            h.drawHallway(world);
            Position begin = h.getBegin();
            Position end = h.getEnd();
            Position lowerleft = h.getLowerLeft();
            Position upperRight = h.getUpperRight();
            world[lowerleft.getX()][lowerleft.getY()] = Tileset.WATER;
            world[upperRight.getX()][upperRight.getY()] = Tileset.GRASS;
            world[begin.getX()][begin.getY()] = Tileset.MOUNTAIN;
            world[end.getX()][end.getY()] = Tileset.SAND;
        }
        ter.renderFrame(world);
    }

    @Test
    public void testBoring(){
        String seedString = "";
        char c = '7';
        if (c <= 9 && c >= 0){
            seedString += c;
        }
        int i = (int)c;
        System.out.println(i);
    }
}
