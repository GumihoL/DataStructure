package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 50;


    /**
     * 在位置p处（以p为中心）添加一个TEile为t、边长为sideLength的六边形。
     * @param tiles
     * @param p
     * @param t
     * @param sideLength
     */
    public static void addHexagon(TETile[][] tiles, Position p, TETile t, int sideLength){
        int height = tiles[0].length;
        int middleLength = sideLength + (sideLength - 1) * 2;
        for (int i = 0; i < sideLength && p.y + i < height; i++){
            int length = middleLength - i * 2;
            drawALine(tiles, new Position(p.x, p.y + i), t, length);
        }
        for (int i = 0; i < sideLength && p.y - 1 - i >= 0; i++){
            int length = middleLength - i * 2;
            drawALine(tiles, new Position(p.x, p.y - 1 - i), t, length);
        }
    }

    /**
     * 以Position p为中心，在该行画s个TETile类型为t的tile。
     * 如果s为奇数，如s=7， 那么左边4个为leftSide，
     */
    private static void drawALine(TETile[][] tiles, Position p, TETile t, int s){
        int tilesWidth = tiles.length;
        int leftSide;
        int rightSide;
        if (s % 2 == 0){
            leftSide = s / 2;
            rightSide = s / 2 - 1;
        }else {
            leftSide = s / 2;
            rightSide = s / 2;
        }
        for(int i = 0; i <= leftSide && p.x - i >= 0; i++){
            tiles[p.x - i][p.y] = t;
        }
        for(int i = 0; i <= rightSide && p.x + i < tilesWidth; i++){
            tiles[p.x + i][p.y] = t;
        }
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
    private static TETile randomTile(){
        Random dice = new Random();
        int tileNum = dice.nextInt(7);
        switch (tileNum){
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.TREE;
            case 5: return Tileset.WATER;
            case 6: return Tileset.SAND;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * 在位置p处添加N个密铺的六边形。
     */
    public static void addTesselationOfHexagon(TETile[][] tiles, Position p, int sideLength, int N){
        int height = tiles[0].length;
        int width = tiles.length;
        Set<Position> validPositions = new HashSet<>();
        int count = 0;
        Queue<Position> queue = new LinkedList<>();
        queue.add(p);
        while(count < N){
            Position tempPosition = queue.poll();
            queue.addAll(getAllValidAdjacentPositions(tempPosition, sideLength, validPositions));
            if(isValidPosition(tempPosition, height, width) && isTileNothing(tiles, tempPosition) &&
                    !validPositions.contains(tempPosition)){
                validPositions.add(tempPosition);
                addHexagon(tiles, tempPosition, randomTile(), sideLength);
                count++;
            }
        }
    }

    /**
     * 获取与位于位置p的六边形 相邻的六边形的中心
     */
    private static Queue<Position> getAllValidAdjacentPositions( Position p, int sideLength , Set<Position> validPositions){
        Queue<Position> positionQueue = new LinkedList<>();
        if(!validPositions.contains(getUpper(p, sideLength)))
            {   positionQueue.add(getUpper(p, sideLength));}
        if(!validPositions.contains(getLower(p, sideLength)))
            {   positionQueue.add(getLower(p, sideLength));}
        if(!validPositions.contains(getUpperLeft(p, sideLength)))
            {   positionQueue.add(getUpperLeft(p, sideLength));}
        if(!validPositions.contains(getUpperRight(p, sideLength)))
            {   positionQueue.add(getUpperRight(p, sideLength));}
        if(!validPositions.contains(getLowerLeft(p, sideLength)))
            {   positionQueue.add(getLowerLeft(p, sideLength));}
        if(!validPositions.contains(getLowerRight(p, sideLength)))
            {   positionQueue.add(getLowerRight(p, sideLength));}

        return positionQueue;
    }

    /**
     * 如果输入参数位置p是有效的，位于tiles的范围之内，则返回true，否则返回false。
     */
    private static boolean isValidPosition(Position p, int tilesHeight, int tilesWidth){
        return p.x >= 0 && p.x < tilesWidth && p.y >= 0 && p.y < tilesHeight;
    }

    private static boolean isTileNothing(TETile[][] tiles, Position p){
        return tiles[p.x][p.y] == Tileset.NOTHING;
    }

    /**
     * 获取与中心位于p，且边长为sideLength的相邻的上、下、左上、右上、坐下、右下的六边形的中心。
     */
    private static Position getUpper( Position p, int sideLength ){
        return new Position(p.x, p.y + 2 * sideLength );
    }
    private static Position getLower( Position p, int sideLength ){
        return new Position(p.x, p.y - 2 * sideLength);
    }
    private static Position getUpperLeft( Position p, int sideLength){
        return new Position(p.x - 2 * sideLength + 1, p.y + sideLength);
    }
    private  static Position getUpperRight( Position p, int sideLength){
        return new Position(p.x + 2 * sideLength - 1, p.y + sideLength);
    }
    private static Position getLowerLeft( Position p, int sideLength){
        return new Position(p.x - 2 * sideLength + 1, p.y - sideLength);
    }
    private static Position getLowerRight( Position p, int sideLength){
        return new Position(p.x + 2 * sideLength - 1, p.y - sideLength);
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        fillWithNOTHING(tiles);
        Position p = new Position(35, 25);
        Position p1 = new Position(40, 40);
        /**
        addHexagon(tiles, p, Tileset.FLOWER, 4);
        addHexagon(tiles, new Position(23, 20), Tileset.WATER, 3);
        addHexagon(tiles, new Position(40, 30), Tileset.GRASS, 2);
        addHexagon(tiles, new Position(1, 1), Tileset.MOUNTAIN, 3);
        addHexagon(tiles, new Position(49, 1), Tileset.LOCKED_DOOR, 1);
         */
        addTesselationOfHexagon(tiles, p, 3, 19);
        //addTesselationOfHexagon(tiles, p1, 2, 15);

        ter.renderFrame(tiles);
    }

}