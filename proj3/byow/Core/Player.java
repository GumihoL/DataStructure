package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Player {
    public static final TETile playerTile = Tileset.AVATAR;
    private Position position;

    public Player(Position p){
        position = p;
    }

    public Position getPosition() {
        return position;
    }

    public void move(Move m, TETile[][] world){
        if (!validateMove(m, world)) return;
        switch (m) {
            case UP:
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = position.above();
                world[position.getX()][position.getY()] = playerTile;
                break;
            case DOWN:
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = position.below();
                world[position.getX()][position.getY()] = playerTile;
                break;
            case LEFT:
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = position.left();
                world[position.getX()][position.getY()] = playerTile;
                break;
            case RIGHT:
                world[position.getX()][position.getY()] = Tileset.FLOOR;
                position = position.right();
                world[position.getX()][position.getY()] = playerTile;
                break;
            default:
                break;
        }
        draw(world);
    }
    private boolean validateMove(Move m, TETile[][] world){
        Position temp;
        switch (m){
            case UP:
                temp = position.above();
                return world[temp.getX()][temp.getY()].equals(Tileset.FLOOR);
            case DOWN:
                temp = position.below();
                return world[temp.getX()][temp.getY()].equals(Tileset.FLOOR);
            case LEFT:
                temp = position.left();
                return world[temp.getX()][temp.getY()].equals(Tileset.FLOOR);
            case RIGHT:
                temp = position.right();
                return world[temp.getX()][temp.getY()].equals(Tileset.FLOOR);
            default: return false;
        }
    }

    public static Player generatePlayer(TETile[][] world, Random rand){
        int height = world[0].length;
        int width = world.length;
        while (true){
            int x = RandomUtils.uniform(rand, width);
            int y = RandomUtils.uniform(rand, height);
            if (world[x][y].equals(Tileset.FLOOR)) {
                Position playerPosition = new Position(x, y);
                Player player =  new Player(playerPosition);
                player.draw(world);
                return player;
            }
        }
    }

    public void draw(TETile[][] world){
        world[position.getX()][position.getY()] = playerTile;
    }
}
