package byow.Core.Component.Room;

import byow.Core.Position;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.HexWorld;

import java.util.List;
import java.util.Random;

public class TestRoom {
    private static final int HEIGHT = 45;
    private static final int WIDTH = 60;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        HexWorld.fillWithNOTHING(world);
        //Room r1 = new Room(new Position(14, 17), 6, 5);
        //r1.drawRoom(world);

        //Room r2 = new Room(new Position(20, 30), 10, 10);
        //r2.drawRoom(world);

        Room r3 = new Room(new Position(10, 10), 10, 10);
        r3.generateExit(new Random(12) );
        r3.drawRoom(world);

        //List<Position> list = r1.occupied();
        //List<Position> list2 = r2.occupied();
        List<Position> list3 = r3.occupied();
        //world[0][0] = Tileset.WATER;

        ter.renderFrame(world);
    }
}
