package byow.Core;

import byow.Core.Component.Component;
import byow.TileEngine.TETile;

import java.util.HashMap;
import java.util.Map;

// 检查每个Room，Hallway， LHallway是否有效：不越界，不与其它component香交叉。
public class ComponentChecker {

    private int height;
    private int width;
    private Map<Position, Component> tilesBelonging;
    public ComponentChecker(int Height, int Width){
        height = Height;
        width = Width;
        tilesBelonging = new HashMap<>();
    }

    public boolean isComponentValid(Component c){
        if (c == null) return false;
        for(Position p : c.occupied()){
            if (p.getX() < 0 || p.getX() >= width || p.getY() < 0 || p.getY() >= height)
                return false;
            if (tilesBelonging.containsKey(p))
                return false;
        }
        for(Position p : c.occupied()){
            tilesBelonging.put(p, c);
        }
        return true;
    }

    public void add(Component c){
        for (Position p : c.occupied()){
            tilesBelonging.put(p, c);
        }
    }
}
