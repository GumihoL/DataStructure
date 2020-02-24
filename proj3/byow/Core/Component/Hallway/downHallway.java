package byow.Core.Component.Hallway;

import byow.Core.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

// end在begin的正下方
public class downHallway extends Hallway{
    public downHallway(Orientation orientation, int length, Position begin) {
        super(orientation, length, begin);
        end = new Position(begin.getX(), begin.getY() - length + 1);
    }

    @Override
    public void drawHallway(TETile[][] world) {
        for (int i = 0; i < length; i++){
            world[begin.getX()][begin.getY() - i] = Tileset.FLOOR;
            world[begin.getX() - 1][begin.getY() - i] = Tileset.WALL;
            world[begin.getX() + 1][begin.getY() - i] = Tileset.WALL;
        }
        if(closed) world[end.getX()][end.getY()] = Tileset.WALL;
    }

    @Override
    public Position getLowerLeft() {
        return new Position(end.getX() - 1, end.getY());
    }

    @Override
    public Position getUpperRight() {
        return new Position(begin.getX() + 1, begin.getY());
    }
}
