import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.HashMap;

public class SeparableEnemySolver {
    Graph g;
    /**
     * Creates a SeparableEnemySolver for a file with name filename. Enemy
     * relationships are biderectional (if A is an enemy of B, B is an enemy of A).
     */
    SeparableEnemySolver(String filename) throws java.io.FileNotFoundException {
        this.g = graphFromFile(filename);
    }

    /** Alterntive constructor that requires a Graph object. */
    SeparableEnemySolver(Graph g) {
        this.g = g;
    }

    /**
     * Returns true if input is separable, false otherwise.
     * 如果图是二部的，那么必然可以将图中的每个结点分为两组，构造这两个组，如果在构造的过程中
     * 没有矛盾产生，那么图是bipartite. Breadth first search.
     */
    public boolean isSeparable() {
        // TODO: Fix me
        Map<String, Integer> map = new HashMap<>();

        Set<String> keys = g.labels();
        int i = 0;
        for (String s1 : keys){
            map.put(s1, i);
            i++;
        }
        boolean[] groupA = new boolean[i];
        boolean[] groupB = new boolean[i];
        for (String str : keys){
            Set<String> str_Adj = g.neighbors(str);
			// 如果str确实在A组中，那么与str相连的每个结点都不能在A组中，并且将它们加入到B组中。
             if (groupA[map.get(str)]){
                 if(!notInGroup(str_Adj, groupA, map)) return false;
                 putIntoGroup(str_Adj, groupB, map);
             }
             else if (groupB[map.get(str)]){
                 if(!notInGroup(str_Adj, groupB, map)) return false;
                 putIntoGroup(str_Adj, groupA, map);
             }
             else {
                 // str is neither in groupA nor groupB, put str into groupA, check adj.
                 groupA[map.get(str)] = true;
                 if (!notInGroup(str_Adj, groupA, map)){
                     // contradiction, put str into groupB, check adj.
                     groupA[map.get(str)] = false;
                     groupB[map.get(str)] = true;
                     if (!notInGroup(str_Adj, groupB, map))
                         // Still has contradiction, return false.
                         return false;
                     else putIntoGroup(str_Adj, groupA, map);
                 }
                 else putIntoGroup(str_Adj, groupB, map);
             }
        }
        return true;
    }

    /** If every string in adj is not in someGroup return true, otherwise return false. */
    private boolean notInGroup(Set<String> adj, boolean[] someGroup, Map<String, Integer> map){
        for (String s : adj){
            if (someGroup[map.get(s)]){ return false; }
        }
        return true;
    }

    /** Put each string in adj into someGroup. */
    private void putIntoGroup(Set<String> adj, boolean[] someGroup, Map<String, Integer> map){
        for (String s : adj) someGroup[map.get(s)] = true;
    }

    /* HELPERS FOR READING IN CSV FILES. */

    /**
     * Creates graph from filename. File should be comma-separated. The first line
     * contains comma-separated names of all people. Subsequent lines each have two
     * comma-separated names of enemy pairs.
     */
    private Graph graphFromFile(String filename) throws FileNotFoundException {
        List<List<String>> lines = readCSV(filename);
        Graph input = new Graph();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                for (String name : lines.get(i)) {
                    input.addNode(name);
                }
                continue;
            }
            assert(lines.get(i).size() == 2);
            input.connect(lines.get(i).get(0), lines.get(i).get(1));
        }
        return input;
    }

    /**
     * Reads an entire CSV and returns a List of Lists. Each inner
     * List represents a line of the CSV with each comma-seperated
     * value as an entry. Assumes CSV file does not contain commas
     * except as separators.
     * Returns null if invalid filename.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<List<String>> readCSV(String filename) throws java.io.FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            records.add(getRecordFromLine(scanner.nextLine()));
        }
        return records;
    }

    /**
     * Reads one line of a CSV.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(",");
        while (rowScanner.hasNext()) {
            String r = rowScanner.next();
            r = r.trim();
            values.add(rowScanner.next().trim());
        }
        return values;
    }

    /* END HELPERS  FOR READING IN CSV FILES. */

}
