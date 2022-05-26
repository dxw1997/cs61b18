import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private Map<Long, Node> hNodes = new HashMap<>();;
    private Map<Long, Road> hRoads = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        ////Valid way since it’s a highway and it’s in the ALLOWED_HIGHWAY_TYPES
        ArrayList<Long> cids = new ArrayList<>();
        for(Map.Entry<Long, Node> e:hNodes.entrySet()){
            if(e.getValue().preNodes.size() == 0 && e.getValue().aftNodes.size() == 0){
                cids.add(e.getKey());
            }
        }
        for(int i = 0;i < cids.size();i++ ){
            hNodes.remove(cids.get(i));
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> res = new ArrayList<Long>();
        for(Map.Entry<Long, Node> e:hNodes.entrySet()){
            res.add(e.getKey());
        }
        return res;
    }

    long getWayId(long n1, long n2){
        if(hNodes.containsKey(n1) == false || hNodes.containsKey(n2) == false)
            return -2;
        Node n = hNodes.get(n1);
        for(int i = 0;i < n.aftNodes.size();i++ ){
            if(n.aftNodes.get(i) == n2){
                long w = n.aftWays.get(i);
                return w;
//                if(hRoads.get(w).name == "") return "unknown road";
//                else return hRoads.get(w).name;
            }///if
        }
        for(int i = 0;i < n.preNodes.size();i++ ){
            if(n.preNodes.get(i) == n2){
                return n.preWays.get(i);
            }
        }
        return -1;
    }

    String getWayName(long n1, long n2){
        long wayid = getWayId(n1, n2);
        if(hRoads.containsKey(wayid) == false) return "invalid wayid";
        if(hRoads.get(wayid).name == null) return "";
        else return hRoads.get(wayid).name;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v){
        ArrayList<Long> res = new ArrayList<>();
        if(hNodes.containsKey(v) == false) return res;
        for(int i = 0;i < hNodes.get(v).preNodes.size();i++ ){
            res.add(hNodes.get(v).preNodes.get(i));
        }
        for(int i = 0;i < hNodes.get(v).aftNodes.size();i++ ){
            res.add(hNodes.get(v).aftNodes.get(i));
        }
        return res;
    }


    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat){
        double mdist = 3.15*7000*7000;
        long r = -1;
        for(Map.Entry<Long, Node> e:hNodes.entrySet()){
            double tdist = distance(lon, lat, e.getValue().longitude, e.getValue().latitude);
//            System.out.println("key:"+e.getKey()+", dist:"+tdist);
            if(tdist < mdist){///if
                mdist = tdist;
                r = e.getKey();
            }
        }
        return r;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        if(hNodes.containsKey(v) == false){
            System.out.println("exception in lon()");
            return 0;
        }
        return hNodes.get(v).longitude;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        if(hNodes.containsKey(v) == false){
            System.out.println("exception in lat()");
            return 0;
        }
        return hNodes.get(v).latitude;
    }

    void addNode(Node n){
        hNodes.put(n.id, n);
    }

    void delNode(long id){
        hNodes.remove(id);
    }

    void addRoad(Road r){
        hRoads.put(r.id, r);
    }

    void delRoad(long id){
        hRoads.remove(id);
    }

    void setRoadMaxSpeed(long id, String speed){
        if(hRoads.get(id) != null){
            hRoads.get(id).maxspeed = speed;
        }
    }

    void setRoadName(long id, String name){
        if(hRoads.get(id) != null){
            hRoads.get(id).name = name;
        }
    }

    void setRoadHighway(long id, String highway){
        if(hRoads.get(id) != null){
            hRoads.get(id).highway = highway;
        }
    }

    void setRoadValid(long id, boolean v){
        if(hRoads.get(id) != null){
            hRoads.get(id).isValid = v;
        }
    }

    boolean getRoadValid(long id){
        if(hRoads.get(id) != null){
            return hRoads.get(id).isValid;
        }
        return false;
    }

    void setNodeName(long id, String name){
        if(hNodes.get(id) != null){
            hNodes.get(id).name = name;
        }
    }

    void connectNodeOnWay(long node1, long node2, long way){
        ///connnecting node1 ---> node2 on way.
        if(hNodes.containsKey(node1) == false || hNodes.containsKey(node2) == false){
            System.out.println("nodes not constructed!!!");
            return;
        }
        hNodes.get(node1).aftNodes.add(node2);
        hNodes.get(node1).aftWays.add(way);
        hNodes.get(node2).preNodes.add(node1);
        hNodes.get(node2).preWays.add(way);
    }

    protected class Node{
        /* writing get methods is very complex, so I use public to decorate them.*/
        public long id;
        public double longitude;
        public double latitude;
        public String name;
        public ArrayList<Long> preNodes;
        public ArrayList<Long> preWays;
        public ArrayList<Long> aftNodes;
        public ArrayList<Long> aftWays;

        public Node(long id){
            this.id = id;
            preNodes = new ArrayList<>();
            preWays = new ArrayList<>();
            aftNodes= new ArrayList<>();
            aftWays = new ArrayList<>();
        }
    }

    protected class Road{
        public long id;
        public String name;
        public String highway;
        public String maxspeed;
        public boolean isValid;

        public Road(long id){
            this.id = id;
        }
    }
}
