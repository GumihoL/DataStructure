package clab6;
public class UnionFind {

    private int[] parent;
    private int[] size;
    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i+=1){
            parent[i] = -1;
            size[i] = 1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex >= 0 && vertex < parent.length){
            return;
        }
        throw new IllegalArgumentException(" This is not a valid index.");
    }


    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        validate(v1);
        return size[v1];
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        validate(v1);
        return parent[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a
       vertex with itself or vertices that are already connected should not
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);
        if (connected(v1, v2)){
            return;
        }
        int v1p = find(v1);
        int v2p = find(v2);
        if (size[v1p] > size[v2p]){
            parent[v2p] = v1p;
            size[v1p] += size[v2p];
            parent[v1p] = -size[v1p];
        }else{
            parent[v1p] = v2p;
            size[v2p] += size[v1p];
            parent[v2p] = -size[v2p];
        }

    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        validate(vertex);
        int tempVertex = vertex;
        // get root
        while(parent[vertex] > 0 ){
            vertex = parent[vertex];
        }
        // if vertex is a root, or vertex is already connected to its root, do not compress path.
        if (vertex == tempVertex || parent[tempVertex] == vertex){
            return vertex;
        }
        parent[tempVertex] = vertex; // Path compression
        size[vertex] += 1;
        parent[vertex] = -size[vertex];
        return vertex;
    }

}