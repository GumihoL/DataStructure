package lab6;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestUnionFind {
    @Test
    public void testUnionFind(){
        UnionFind U = new UnionFind(5);
        assertFalse(U.connected(1,2));
        U.union(0,1);
        assertTrue(U.connected(0,1));
        U.union(3,4);
        U.union(1,3);
        U.union(1,2);
    }
    public static void main(String[] args){
        UnionFind U = new UnionFind(5);
        U.union(0,1);
        U.union(3,4);
        U.union(1,3);
        U.union(1,2);
    }
}
