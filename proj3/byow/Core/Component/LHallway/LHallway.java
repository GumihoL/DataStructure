package byow.Core.Component.LHallway;

// L形走廊

import byow.TileEngine.TETile;
import byow.Core.Position;
import byow.TileEngine.Tileset;
import byow.Core.Component.Component;

import java.util.LinkedList;
import java.util.List;

/**
 *
 *     # . #    # . #   # # #   # # #
 *     # . .    . . #   # . .   . . #
 *     # # #    # # #   # . #   # . #
 *
 *      UR       UL       DR      DL
 */
public class LHallway implements Component{
    public enum LHallwayType{
        UR, UL, DR, DL
    }

    private LHallwayType lHallwayType;
    private Position center;
    private Position begin;
    private Position end;
    private boolean closed;

    public void close(){
        closed = true;
    }


    public LHallway(LHallwayType lhType, Position center, Position begin){
        lHallwayType = lhType;
        this.center = center;
        this.begin = begin;
        closed = false;
        switch (lhType){
            case UR:
                end = begin.equals(center.above()) ? center.right() : center.above();
                break;
            case UL:
                end = begin.equals(center.above()) ? center.left() : center.above();
                break;
            case DR:
                end = begin.equals(center.below()) ? center.right() : center.below();
                break;
            case DL:
                end = begin.equals(center.below()) ? center.left() : center.below();
                break;
        }
    }

    public Position getBegin() {
        return begin;
    }

    public Position getEnd() {
        return end;
    }

    public Position getCenter() {
        return center;
    }


    public void drawLHallway(TETile[][] world){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                world[center.getX() - 1 + i][center.getY() - 1 + j] = Tileset.WALL;
            }
        }
        switch (lHallwayType){
            case DL: drawDL(world);
            break;
            case DR: drawDR(world);
            break;
            case UL: drawUL(world);
            break;
            case UR: drawUR(world);
            break;
            default: drawUR(world);
        }
        if(closed) world[end.getX()][end.getY()] = Tileset.WALL;
    }

    private void drawDL(TETile[][] world){
        world[center.getX()][center.getY()] = Tileset.FLOOR;
        world[center.getX()][center.getY() - 1] = Tileset.FLOOR;
        world[center.getX() - 1][center.getY()] = Tileset.FLOOR;
    }

    private void drawDR(TETile[][] world){
        world[center.getX()][center.getY()] = Tileset.FLOOR;
        world[center.getX()][center.getY() - 1] = Tileset.FLOOR;
        world[center.getX() + 1][center.getY()] = Tileset.FLOOR;
    }

    private void drawUL(TETile[][] world){
        world[center.getX()][center.getY()] = Tileset.FLOOR;
        world[center.getX()][center.getY() + 1] = Tileset.FLOOR;
        world[center.getX() - 1][center.getY()] = Tileset.FLOOR;
    }

    private void drawUR(TETile[][] world){
        world[center.getX()][center.getY()] = Tileset.FLOOR;
        world[center.getX()][center.getY() + 1] = Tileset.FLOOR;
        world[center.getX() + 1][center.getY()] = Tileset.FLOOR;
    }

    public Position lowerLeft(){
        return new Position(center.getX() - 1, center.getY() - 1);
    }
    public Position upperRight(){
        return new Position(center.getX() + 1, center.getY() + 1);
    }

    @Override
    public List<Position> occupied(){
        List<Position> occupiedPositions = new LinkedList<>();
        for (int x = 0; x < 3; x++){
            for (int y = 0; y < 3; y++){
                occupiedPositions.add(new Position(lowerLeft().getX() + x,
                        lowerLeft().getY() + y));
            }
        }
        return occupiedPositions;
    }

    @Override
    public void draw(TETile[][] world) {
        drawLHallway(world);
    }

}
