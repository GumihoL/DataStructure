package byow.Core.Component.Hallway;

import byow.Core.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

// end在begin的正右方
public class rightHallway extends Hallway {
    public rightHallway(Orientation orientation, int length, Position begin) {
        super(orientation, length, begin);
        end = new Position(begin.getX() + length - 1, begin.getY());
    }

    @Override
    public void drawHallway(TETile[][] world) {
        for(int i = 0; i < length; i++){
            world[begin.getX() + i][begin.getY()] = Tileset.FLOOR;
            world[begin.getX() + i][begin.getY() + 1] = Tileset.WALL;
            world[begin.getX() + i][begin.getY() - 1] = Tileset.WALL;
        }
        if(closed) world[end.getX()][end.getY()] = Tileset.WALL;
    }

    @Override
    public Position getLowerLeft() {
        return new Position(begin.getX(), begin.getY() - 1);
    }

    @Override
    public Position getUpperRight() {
        return new Position(end.getX(), end.getY() + 1);
    }
}
