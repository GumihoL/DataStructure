package clab6;
public class BubbleGrid {
    private int[][] grid;
    private int rowNum;
    private int colNum;

    /* Create new BubbleGrid with bubble/space locations specified by grid.
     * Grid is composed of only 1's and 0's, where 1's denote a bubble, and
     * 0's denote a space. */
    public BubbleGrid(int[][] grid) {
        this.rowNum = grid.length;
        this.colNum = grid[0].length;
        this.grid = grid;
    }

    // If bubble at (x, y) was hit or fall, set grid[x][y] to 0.
    private void setZero(int x, int y){
        grid[x][y] = 0;
    }

    // Recursively union the bubble's neighbor bubbles.
    private void unionNeighbors(int x, int y, UnionFind uf){
        int me = xyTo1D(x, y);
        int left = xyTo1D(x - 1, y);
        int right = xyTo1D(x + 1, y);
        int up = xyTo1D(x, y - 1);
        int down = xyTo1D(x, y + 1);
        if ( y - 1 >= 0 && grid[x][y - 1] == 1 && !uf.connected(me, up)){
            uf.union(me, up);
            unionNeighbors(x , y -1, uf);
        }
        if (y + 1 < rowNum && grid[x][y + 1] == 1 && !uf.connected(me, down)){
            uf.union(me, up);
            unionNeighbors(x, y + 1, uf);
        }
        if (x - 1 >= 0 && grid[x - 1][y] == 1 && !uf.connected(me, left)){
            uf.union(me, left);
            unionNeighbors(x - 1, y, uf);
        }
        if (x + 1 < colNum && grid[x + 1][y] == 1 && uf.connected(me, right)){
            uf.union(me, right);
            unionNeighbors(x + 1, y, uf);
        }
    }
    // Translate position coordinate to 1D
    private int xyTo1D(int x, int y){
        return x * colNum + y;
    }

    /* Returns an array whose i-th element is the number of bubbles that
     * fall after the i-th dart is thrown. Assume all elements of darts
     * are unique, valid locations in the grid. Must be non-destructive
     * and have no side-effects to grid. */
    public int[] popBubbles(int[][] darts) {
        // TODO
        int dartsNumber = darts.length;
        int[] bubblesFallen = new int[dartsNumber];
        int dartNum = 0; // No.what dart

        for (int[] dart : darts){
            // Step 1：Set the position hit by dart to 0.
            setZero(dart[0], dart[1]);

            // Step 2: make the remaining bubbles a UnionFind, initially disjoint.
            UnionFind uf = new UnionFind(rowNum * colNum);

            // Connect the ceiling bubbles and stuck bubbles
            for (int i = 0; i < colNum; i++){
                if (grid[0][i] == 1){
                    unionNeighbors(0, i, uf);
                }
            }

            // Step 3: Count fallen bubbles. If a bubble is not connected to a ceiling
            // bubble set,and itself is not a ceiling bubble, it must fall.
            int bubblesFallenCount = 0;
            for (int x = 1; x < rowNum; x++){
                for (int y = 0; y < colNum; y++){
                    if (grid[x][y] != 0 && uf.find(xyTo1D(x, y)) > colNum){
                        grid[x][y] = 0;
                        bubblesFallenCount++;
                    }
                }
            }
            bubblesFallen[dartNum] = bubblesFallenCount;
            dartNum++;
        }

        return bubblesFallen;
    }
}
