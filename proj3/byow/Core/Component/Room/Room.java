package byow.Core.Component.Room;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.Core.Position;
import byow.TileEngine.Tileset;
import byow.Core.Component.Component;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

// 长方形的room

/**
 *       #E#########      ###########   ###########   ###########
 *       #         #      E         #   #         #   #         #
 *       #         #      #         #   #         #   #         E
 *       ###########      ###########   #########E#   ###########
 *
 */
public class Room implements Component{

    private int height;
    private int width;
    private int area; // 该Room占用多少个tile，包括wall
    private int perimeter;
    private Position lowerLeft;
    private Position upperRight;
    private List<Position> exits; // exit顺序：上下左右
    private static final Position sentinal = new Position(-1, -1);
    private boolean[] closed;
    private TreeSet<Integer> shouldNotClose;

    public void setShouldNotClose(int i){
        shouldNotClose.add(i);
    }

    public Room(Position lowerLeft, int height, int width){
        this.lowerLeft = lowerLeft;
        this.upperRight = new Position(lowerLeft.getX() + width - 1, lowerLeft.getY() + height -1);
        this.height = height;
        this.width = width;
        this.area = height * width;
        this.perimeter = (height + width) * 2 - 4;
        exits = new LinkedList<>();
        closed = new boolean[4];
        for (int i = 0; i < 4; i++) {
            exits.add(null);
            closed[i] = false;
        }
        shouldNotClose = new TreeSet<>();
    }
    public void close(int i){
        if (shouldNotClose.contains(i)) return;
        closed[i] = true;
    }
    public List<Position> getExits(){
        return exits;
    }


    public int getArea() {
        return area;
    }

    public int getHeight() {
        return height;
    }

    public int getPerimeter() {
        return perimeter;
    }

    public int getWidth() {
        return width;
    }

    public void drawRoom(TETile[][] world){
        drawFloorOfRoom(world);
        drawWallsOfRoom(world);
        drawExits(world);
        for(int i = 0; i < 4; i++){
            if(closed[i]){
                world[exits.get(i).getX()][exits.get(i).getY()] = Tileset.WALL;
            }
        }
    }
    private void drawWallsOfRoom(TETile[][] world){
        for(int i = 0; i < width; i++){
            world[lowerLeft.getX() + i][lowerLeft.getY()] = Tileset.WALL;
            world[upperRight.getX() - i][upperRight.getY()] = Tileset.WALL;
        }
        for(int i = 0; i < height; i++){
            world[lowerLeft.getX()][lowerLeft.getY() + i] = Tileset.WALL;
            world[upperRight.getX()][upperRight.getY() - i] = Tileset.WALL;
        }
    }

    private void drawFloorOfRoom(TETile[][] world){
        for(int y = 1; y < height - 1; y++){
            for(int x = 1; x <width - 1; x++){
                world[lowerLeft.getX() + x][lowerLeft.getY() + y] = Tileset.FLOOR;
            }
        }
    }

    private void drawExits(TETile[][] world){
        for (Position p : exits){
            if(p != null) world[p.getX()][p.getY()] = Tileset.FLOOR;
        }
    }

    @Override
    public List<Position> occupied() {
        List<Position> occupiedPositions = new LinkedList<>();
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                occupiedPositions.add(new Position(lowerLeft.getX() + x,
                                    lowerLeft.getY() + y));
            }
        }
        return occupiedPositions;
    }

    @Override
    public void draw(TETile[][] world) {
        drawRoom(world);
    }

    public void generateExit(Random r){

        Position downExit = new Position(lowerLeft.getX() + RandomUtils.uniform(r, 1, width - 1),
                                            lowerLeft.getY());
        Position upExit = new Position(upperRight.getX() - RandomUtils.uniform(r, 1,width - 1),
                                          upperRight.getY());
        Position leftExit = new Position(lowerLeft.getX(),
                                      lowerLeft.getY() + RandomUtils.uniform(r, 1,height - 1));
        Position rightExit = new Position(upperRight.getX(),
                                        upperRight.getY() - RandomUtils.uniform(r, 1,height - 1));
        if(exits.get(0) == null) exits.set(0, upExit);
        if(exits.get(1) == null) exits.set(1, downExit);
        if(exits.get(2) == null) exits.set(2, leftExit);
        if(exits.get(3) == null) exits.set(3, rightExit);
    }
    // 将room沿对角线向左下平移, 即是将upperRight平移到原来的leftLower的位置
    public void southWestTranslate(){
        lowerLeft = lowerLeft.southWestTranslate(width - 1, height - 1);
        upperRight = upperRight.southWestTranslate(width - 1, height - 1);
        for(Position p : exits){
            if(p != null) p.southWestTranslate(width - 1, height - 1);
        }
    }

    // 将Room整体向左平移deltaX个单位
    public void westTranslate(int deltaX){
        lowerLeft = lowerLeft.westTranslate(deltaX);
        upperRight = upperRight.westTranslate(deltaX);
        for (Position p : exits){
            if(p != null) p.westTranslate(deltaX);
        }
    }

    // 将Room整体向右平移deltaX个单位
    public void eastTranslate(int deltaX){
        lowerLeft = lowerLeft.eastTranslate(deltaX);
        upperRight = upperRight.eastTranslate(deltaX);
        for(Position p : exits){
            if(p != null) p.eastTranslate(deltaX);
        }
    }

    // 将Room整体向右下平移delta个单位
    public void southTranslate(int deltaY){
        lowerLeft = lowerLeft.southTranslate(deltaY);
        upperRight = upperRight.southTranslate(deltaY);
        for(Position p : exits){
            if(p != null) p.southTranslate(deltaY);
        }
    }

    // 将Room整体向上平移deltaY个单位
    public void northTranslate(int deltaY){
        lowerLeft = lowerLeft.northTranslate(deltaY);
        upperRight = upperRight.northTranslate(deltaY);
        for(Position p: exits){
            if(p != null) p.northTranslate(deltaY);
        }
    }

    // 设置Room的上、下、左、右（0、1、2、3）出口中的某个位于p。
    public void setExits(int i, Position p){
        if (i < 0 || i > 4 )
            throw new IllegalArgumentException("illegal index in setExits()");
        exits.set(i, p);
    }

}
