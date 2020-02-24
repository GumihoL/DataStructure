package byow.Core;

import byow.Core.Component.Component;
import byow.Core.Component.Hallway.*;
import byow.Core.Component.LHallway.LHallway;
import byow.Core.Component.Room.Room;
import byow.TileEngine.Tileset;

import java.lang.reflect.Type;
import java.util.Random;

public class ComponentGenerator {
    private final int MAX_ROOM_EDGE;
    private final int MIN_ROOM_EDGE;
    private final int MAX_HALLWAY_LENGTH;
    private final int MIN_HALLWAY_LENGTH;
    private Random random;

    public ComponentGenerator(Random random, int maxRoomEdge, int minRoomEdge,
                              int maxHallwayLength, int minHallwayLength){
        this.random = random;
        MAX_ROOM_EDGE = maxRoomEdge;
        MIN_ROOM_EDGE = minRoomEdge;
        MAX_HALLWAY_LENGTH = maxHallwayLength;
        MIN_HALLWAY_LENGTH = minHallwayLength;
    }
    public Component generateFrom(Component c){
        return null;
    }

    public LHallway generateLHallwayFromHallway(Hallway h){
        Position next;
        switch (h.getOrientation()){
            case U:
                next = h.getEnd().above();
                return generateLHallwayFromUpHallway(next);
            case D:
                next = h.getEnd().below();
                return generateLHallwayFromDownHallway(next);
            case L:
                next = h.getEnd().left();
                return generateLHallwayFromLeftHallway(next);
            case R:
                next = h.getEnd().right();
                return generateLHallwayFromRightHallway(next);
        }
        return null;
    }

    // U型Hallway的end端口只能外接DL型或者DR型LHallway
    private LHallway generateLHallwayFromUpHallway(Position next){
        Position center = next.above();
        LHallway lh;
        if (RandomUtils.bernoulli(random)){
            lh = new LHallway(LHallway.LHallwayType.DL, center, next);
        }
        else{
            lh = new LHallway(LHallway.LHallwayType.DR, center, next);
        }
        return lh;
    }

    // D型Hallway的end端口只能外接UL型或者UR型LHallway
    private LHallway generateLHallwayFromDownHallway(Position next){
        Position center = next.below();
        LHallway lh;
        if (RandomUtils.bernoulli(random)){
            lh = new LHallway(LHallway.LHallwayType.UL, center, next);
        }
        else{
            lh = new LHallway(LHallway.LHallwayType.UR, center, next);
        }
        return lh;
    }

    // L型Hallway的end端口只能外接UR型或者DR型LHallway
    private LHallway generateLHallwayFromLeftHallway(Position next){
        Position center = next.left();
        LHallway lh;
        if (RandomUtils.bernoulli(random)){
            lh = new LHallway(LHallway.LHallwayType.UR, center, next);
        }
        else{
            lh = new LHallway(LHallway.LHallwayType.DR, center, next);
        }
        return lh;
    }

    // R型Hallway的end端口只能外接UR型或者DR型LHallway
    private LHallway generateLHallwayFromRightHallway(Position next){
        Position center = next.right();
        LHallway lh;
        if (RandomUtils.bernoulli(random)){
            lh = new LHallway(LHallway.LHallwayType.UL, center, next);
        }
        else{
            lh = new LHallway(LHallway.LHallwayType.DL, center, next);
        }
        return lh;
    }

    // 为Room的某个出口r.getExits().get(i)生成LHallway
    public LHallway generateLHallwayFromRoom(Room r, int e){
        Position exit = r.getExits().get(e);
        if(exit == null) return null;
        Position next;
        LHallway lh;
        switch (e){
            case 0:
                next = exit.above();
                lh = new LHallway(randomLHallType(LHallway.LHallwayType.DR, LHallway.LHallwayType.DL),
                                  next.above(), next);
                break;
            case 1:
                next = exit.below();
                lh = new LHallway(randomLHallType(LHallway.LHallwayType.UR, LHallway.LHallwayType.UL),
                                  next.below(), next);
                break;
            case 2:
                next = exit.left();
                lh = new LHallway(randomLHallType(LHallway.LHallwayType.UR, LHallway.LHallwayType.DR),
                        next.left(), next);
                break;
            case 3:
                next = exit.right();
                lh = new LHallway(randomLHallType(LHallway.LHallwayType.UL, LHallway.LHallwayType.DL),
                        next.right(), next);
                break;
            default:
                lh = null;
        }
        return lh;
    }

    private LHallway.LHallwayType randomLHallType(LHallway.LHallwayType a, LHallway.LHallwayType b){
        return RandomUtils.bernoulli(random) ? a : b;
    }

    // 为Room的某个出口生成Hallway
    public Hallway generateHallwayFromRoom(Room r, int e){
        Position exit = r.getExits().get(e);
        if (exit == null) return null;
        Position next;
        Hallway h;
        switch (e){
            case 0:
                next = exit.above();
                h = generateUpHallway(next);
                break;
            case 1:
                next = exit.below();
                h = generateDownHallway(next);
                break;
            case 2:
                next = exit.left();
                h = generateLeftHallway(next);
                break;
            case 3:
                next = exit.right();
                h = generateRightHallway(next);
                break;
            default: h = null;

        }
        return h;
    }
    private Hallway generateUpHallway( Position u){
        Hallway uh = new upHallway(Hallway.Orientation.U, generateRandomLength(), u);
        return uh;
    }
    private Hallway generateDownHallway( Position d){
        Hallway dh = new downHallway(Hallway.Orientation.D, generateRandomLength(), d);
        return dh;
    }
    private Hallway generateLeftHallway( Position l){
        Hallway lh = new leftHallway(Hallway.Orientation.L, generateRandomLength(), l);
        return lh;
    }
    private Hallway generateRightHallway( Position rr){
        Hallway rrh = new rightHallway(Hallway.Orientation.R, generateRandomLength(), rr);
        return rrh;
    }
    public int generateRandomLength(){
        return RandomUtils.uniform(random, MIN_HALLWAY_LENGTH, MAX_HALLWAY_LENGTH + 1);
    }

    // Generate a room for a hallway.
    public Room generateRoomFromHallway(Hallway h){
        Position next;
        Room r;
        switch(h.getOrientation()){
            case U: {
                next = h.getEnd().above();
                r = new Room(next, generateRandomRoomEdge(), generateRandomRoomEdge());
                int deltaX = RandomUtils.uniform(random, 1 , r.getWidth() - 1);
                r.westTranslate(deltaX);
                r.setExits(1, h.getEnd().above());
                r.setShouldNotClose(1);
                break;
            }
            case D: {
                next = h.getEnd().below();
                r = new Room(next, generateRandomRoomEdge(), generateRandomRoomEdge());
                r.southWestTranslate();
                int deltaX = RandomUtils.uniform(random, 1 , r.getWidth() - 1);
                r.eastTranslate(deltaX);
                r.setExits(0, h.getEnd().below());
                r.setShouldNotClose(0);
                break;
            }
            case L: {
                next = h.getEnd().left();
                r = new Room(next, generateRandomRoomEdge(), generateRandomRoomEdge());
                r.southWestTranslate();
                int deltaY = RandomUtils.uniform(random, 1, r.getHeight() - 1);
                r.northTranslate(deltaY);
                r.setExits(3, h.getEnd().left());
                r.setShouldNotClose(3);
                break;
            }
            case R: {
                next = h.getEnd().right();
                r = new Room(next, generateRandomRoomEdge(), generateRandomRoomEdge());
                int deltaY = RandomUtils.uniform(random, 1, r.getHeight() - 1);
                r.southTranslate(deltaY);
                r.setExits(2, h.getEnd().right());
                r.setShouldNotClose(2);
                break;
            }
            default:
                r = null;
        }
        return r;
    }
    public int generateRandomRoomEdge(){
        return RandomUtils.uniform(random, MIN_ROOM_EDGE, MAX_ROOM_EDGE + 1);
    }

    public Hallway generateHallwayFromLHallway(LHallway lh){
        Hallway h;
        Position end = lh.getEnd();
        Position center = lh.getCenter();
        if (end.equals(center.below())){
            Position next = end.below();
            h = generateDownHallway(next);
        }else if (end.equals(center.above())){
            Position next = end.above();
            h = generateUpHallway( next);
        }else if(end.equals(center.left())){
            Position next = end.left();
            h = generateLeftHallway(next);
        }else {
            Position next = end.right();
            h = generateRightHallway( next);
        }
        return h;
    }

/*
        generateLHallwayForHallway() done
        generateLHallwayForRoom() done
        generateHallwayForRoom() done
        generateRoomForHallway() done
        generateRoomForLHallway()
        generateHallwayForLHallway() done
    }*/
}
