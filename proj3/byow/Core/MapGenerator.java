package byow.Core;

import byow.Core.Component.Component;
import byow.Core.Component.Hallway.*;
import byow.Core.Component.LHallway.LHallway;
import byow.Core.Component.Room.Room;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class MapGenerator {
    //how to generate a world, decompose huge task into smaller task.
    //生成一个 TETile[][] world, 每个tile都只属于一个component

    private final int HEIGHT;
    private final int WIDTH;
    private static final int MAX_ROOM_EDGE = 7;
    private static final int MIN_ROOM_EDGE = 3;
    private static final int MAX_HALLWAY_LENGTH = 7;
    private static final int MIN_HALLWAY_LENGTH = 1;
    public Player player;

    private TETile[][] world;
    private ComponentChecker checker;
    private Set<Component> totalComponents;
    private ComponentGenerator cg;
    private Random rand;

    public MapGenerator(int width, int height, long seed) {
        //Initialize MapGenerator
        HEIGHT = height;
        WIDTH = width;
        world = new TETile[WIDTH][HEIGHT];
        fillWithNOTHING(world);
        checker = new ComponentChecker(HEIGHT, WIDTH);
        rand = new Random(seed);
        totalComponents = new HashSet<>();
        cg = new ComponentGenerator(rand, MAX_ROOM_EDGE, MIN_ROOM_EDGE,
                                    MAX_HALLWAY_LENGTH, MIN_HALLWAY_LENGTH);

        // 在world的中部生成一个初始Room，并为它生成exits
        Position initialPosition = new Position(WIDTH / 2, HEIGHT / 2);
        Room initialRoom = new Room(initialPosition, cg.generateRandomRoomEdge(), cg.generateRandomRoomEdge());
        initialRoom.generateExit(rand);
        if (checker.isComponentValid(initialRoom)){
            totalComponents.add(initialRoom);
            checker.add(initialRoom);
        }

        // 迭代生成其它Component
        Queue<Room> qr = new LinkedList<>();
        Queue<Hallway> qh = new LinkedList<>();
        Queue<LHallway> ql = new LinkedList<>();

        qr.add(initialRoom);
        while(!qr.isEmpty() || !qh.isEmpty() || !ql.isEmpty()){
            Room r = qr.poll();
            if (r != null) {
                List<Position> exits = r.getExits();
                for (int i = 0; i < 4; i++) {
                    if (RandomUtils.bernoulli(rand)) {
                        Hallway h = cg.generateHallwayFromRoom(r, i);
                        if (checker.isComponentValid(h)) {
                            checker.add(h);
                            totalComponents.add(h);
                            qh.add(h);
                        }
                        else r.close(i);//若生成的component不合法，则关闭生成该coponent的出口

                    } else {
                        LHallway lh = cg.generateLHallwayFromRoom(r, i);
                        if (checker.isComponentValid(lh)) {
                            checker.isComponentValid(lh);
                            totalComponents.add(lh);
                            ql.add(lh);
                        }else r.close(i);
                    }
                }
            }
            Hallway h = qh.poll();
            if (h != null){
                if(RandomUtils.bernoulli(rand)){
                    Room r1 = cg.generateRoomFromHallway(h);
                    if (checker.isComponentValid(r1)){
                        r1.generateExit(rand);
                        checker.add(r1);
                        totalComponents.add(r1);
                        qr.add(r1);
                    }else h.close();
                }else{
                    LHallway lh1 = cg.generateLHallwayFromHallway(h);
                    if (checker.isComponentValid(lh1)){
                        checker.add(lh1);
                        totalComponents.add(lh1);
                        ql.add(lh1);
                    }else h.close();
                }
            }
            LHallway lh = ql.poll();
            if(lh != null){
                Hallway h2 = cg.generateHallwayFromLHallway(lh);
                if(h2 != null){
                    if(checker.isComponentValid(h2)){
                        checker.add(h2);
                        totalComponents.add(h2);
                        qh.add(h2);
                    }else lh.close();
                }
            }
        }

        // render，仅仅是在world上设置Tileset的类型。
        for(Component c : totalComponents){
            c.draw(world);
        }

        // 创建角色的初始位置
        Position playerPosition = generatePlayerPosition();
        player = new Player(playerPosition);
    }

    public static void fillWithNOTHING(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] getWorld() {
        return world;
    }

    public Position generatePlayerPosition(){
        while (true){
            int x = RandomUtils.uniform(rand, WIDTH);
            int y = RandomUtils.uniform(rand, HEIGHT);
            if (world[x][y].equals(Tileset.FLOOR))
                return new Position(x, y);
        }
    }

    public void generatePlayer(Player p){
        player = p;
        world[p.getPosition().getX()][p.getPosition().getY()] = Player.playerTile;
    }
}



