package byow.Core.Component;

import byow.Core.Position;
import byow.TileEngine.TETile;

import java.util.List;

public interface Component {
    public List<Position> occupied(); //返回该Component所占用的位置所构成的list
    public void draw(TETile[][] world);
}
