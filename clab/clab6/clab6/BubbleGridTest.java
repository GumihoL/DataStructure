package clab6;
import org.junit.Test;
import static org.junit.Assert.*;

public class BubbleGridTest {

    @Test
    public void testBasic() {

        int[][] grid = {{1, 0, 0, 0},
                        {1, 1, 1, 0}};
        int[][] darts = {{1, 0}};
        int[] expected = {2};

        validate(grid, darts, expected);
    }

    private void validate(int[][] grid, int[][] darts, int[] expected) {
        BubbleGrid sol = new BubbleGrid(grid);
        assertArrayEquals(expected, sol.popBubbles(darts));
    }
    @Test
    public void testAdvanced(){
        int[][] grid = {{1, 0, 0, 0},
                        {1, 1, 1, 0},
                        {1, 1, 0, 1},
                        {0, 0, 1, 0}};
        int[][] darts = {{1, 0}, {3, 0}, {0, 0}};
        int[] expected = {6, 0, 0};

        validate(grid, darts, expected);
    }
    @Test
    public void testBubbleGrid(){
        int[][] grid = {{1, 0, 0, 0},
                        {1, 1, 1, 0}};
        BubbleGrid b = new BubbleGrid(grid);
        int a = 0;
    }
}
