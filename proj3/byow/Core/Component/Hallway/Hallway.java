package byow.Core.Component.Hallway;

import byow.Core.Position;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.Component.Component;

import java.util.LinkedList;
import java.util.List;

abstract public class Hallway implements Component{
    @Override
    public List<Position> occupied() {
        List<Position> occupiedPositions = new LinkedList<>();
        int deltaX = getUpperRight().getX() - getLowerLeft().getX();
        int deltaY = getUpperRight().getY() - getLowerLeft().getY();
        for (int x = 0; x <= deltaX; x++){
            for (int y = 0; y <= deltaY; y++){
                occupiedPositions.add(new Position(getLowerLeft().getX() + x,
                        getLowerLeft().getY() + y));
            }
        }
        return occupiedPositions;

    }
    @Override
    public void draw(TETile[][] world){
        drawHallway(world);
    }

    // HALLWAY有两种方向，竖直的和水平的。
    public enum Orientation{
        U, D, L, R
    }


    private Orientation orientation;

    protected int length; // Hallway 的长度
    protected Position begin; // 一定是floor
    protected Position end; // 如果是双端开口的，则是floor，单端开口则为wall
    protected boolean closed;

    public Orientation getOrientation(){return orientation;}
    public Position getBegin(){
        return begin;
    }
    public Position getEnd(){
        return end;
    }

    public void close(){
        closed = true;
    }
    public Hallway(Orientation orientation, int length, Position begin){
        this.orientation = orientation;
        this.length = length;
        this.begin = begin;
        closed = false;
    }

    abstract public void drawHallway(TETile[][] world);

    abstract public Position getLowerLeft();

    abstract public Position getUpperRight();

}
