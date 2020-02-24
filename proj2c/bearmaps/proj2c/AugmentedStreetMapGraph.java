package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.lab9.MyTrieSet;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;
import org.apache.commons.math3.geometry.partitioning.NodesSet;
import bearmaps.lab9.TrieSet61B;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private KDTree kdt;
    private Map<Point, Long> coor2VertexID;
    private TrieSet61B trie;
    private Map<String, LinkedList<Node>> cleanedNameToNode;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = getNodes();
        List<Point> list = new LinkedList<>();
        coor2VertexID = new HashMap<>();
        for(Node node : nodes){
            if (!neighbors(node.id()).isEmpty()) {
                coor2VertexID.put(new Point(node.lon(), node.lat()), node.id());
                list.add(new Point(node.lon(), node.lat()));
            }
        }
        kdt = new KDTree(list);

        // Construct name trie
        trie = new MyTrieSet<Integer>();
        cleanedNameToNode = new HashMap<>();
        for( Node node : nodes){
            if (node.name() != null){
                String cleanedName = cleanString(node.name());
                trie.add(cleanedName);

                if(!cleanedNameToNode.containsKey(cleanedName)){
                    cleanedNameToNode.put(cleanedName, new LinkedList<>());
                }
                LinkedList<Node> nodeList = cleanedNameToNode.get(cleanedName);
                nodeList.addLast(node);
            }
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        long nearestVertexID = coor2VertexID.get(kdt.nearest(lon, lat));
        return nearestVertexID;
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix = cleanString(prefix);
        List<String> matchedNames = trie.keysWithPrefix(cleanedPrefix);
        List<String> actualNames = new LinkedList<>();
        for(String cleanName : matchedNames){
            for(Node node : cleanedNameToNode.get(cleanName) ){
                actualNames.add(node.name());
            }
        }
        return actualNames;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleanLocationName = cleanString(locationName);
        List<Map<String, Object>> locations = new LinkedList<>();

        if (!cleanedNameToNode.containsKey(cleanLocationName)) return locations;
        for(Node node : cleanedNameToNode.get(cleanLocationName)){
            Map<String, Object> locationInfo = new HashMap<>();
            locationInfo.put("id", node.id());
            locationInfo.put("name", node.name());
            locationInfo.put("lat", node.lat());
            locationInfo.put("lon", node.lon());
            locations.add(locationInfo);
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
