
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    private static class State{
        long parentid;
        long id;
//        long destid;
        double m;//the distance from st to this node.
        double e;
//        List<Long> lis;
        State prevS;

        public State(long id, long parentid, double m, double e, State prevS){
            this.id = id;
            this.m = m;
            this.parentid = parentid;
            this.e = e;
            this.prevS = prevS;
//            lis = new ArrayList<>();
//            for(int i = 0;i < lis1.size();i++ ) lis.add(lis1.get(i));
//            lis.add(id);
        }
    }

    private static class Cmp implements Comparator<State>{
        @Override
        public int compare(State s1, State s2){
            double t = ((s1.m+s1.e) - (s2.m+s2.e));
            if(t == 0)return 0;
            return t>0?1:-1;
        }
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        ///assuming that destid is reachable.
        List<Long> path = new ArrayList<>();
        Long stid = g.closest(stlon, stlat);
        Long destid = g.closest(destlon, destlat);
        Queue<State> q = new PriorityQueue<State>(10, new Cmp());
        q.add(new State(stid, -1, 0, g.distance(stid, destid), null));
        State s = null;
        HashSet<Long> visited = new HashSet<>();
        while(!q.isEmpty()){
            s = q.poll();
            if(visited.contains(s.id)) continue;
            if(s.id == destid){
                break;
            }
            visited.add(s.id);
            for(long nxt:g.adjacent(s.id)){
                if(!visited.contains(nxt)){
                    q.add(new State(nxt, s.id, s.m+g.distance(s.id, nxt), g.distance(nxt, destid), s));
                }
            }
        }
        while(s != null){
            path.add(s.id);
            s = s.prevS;
        }
        Collections.reverse(path);
        return path; // FIXME
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        if(route.size() < 2) return null;
        List<NavigationDirection> lis = new ArrayList<>();
        double preBearing = g.bearing(route.get(0), route.get(1));
        NavigationDirection n = new NavigationDirection();
        n.way = g.getWayName(route.get(0), route.get(1));
        n.direction = 0;
        n.distance = g.distance(route.get(0), route.get(1));
        lis.add(n);
//        System.out.println(n.way);
        for(int i = 2;i < route.size();++i ){
            double bearing = g.bearing(route.get(i-1), route.get(i));
//            long wayid = g.getWayId(route.get(i-1), route.get(i));
            String way2 = g.getWayName(route.get(i-1), route.get(i));
//            System.out.println(way2);
//            System.out.println(wayid);
            //System.out.println(n.way);
            if(n.way.equals(way2)){
                n.distance = n.distance + g.distance(route.get(i-1), route.get(i));
            }else{
                n = new NavigationDirection();
                n.way = way2;
                n.distance = g.distance(route.get(i-1), route.get(i));
                //double bearingDiff = Math.min(bearing-preBearing, 360-(bearing-preBearing));
                double bearingDiff = bearing-preBearing;
                if(bearingDiff > 180){
                    bearingDiff -= 360;
                }
                else if(bearingDiff < -180){
                    bearingDiff += 360;
                }
               // System.out.println("bearing:"+bearing+"prebearing:"+preBearing);
                if(-15 <= bearingDiff && bearingDiff <= 15) n.direction = 1;
                else if(-30 <= bearingDiff && bearingDiff < -15) n.direction = 2;
                else if(15 < bearingDiff && bearingDiff <= 30) n.direction = 3;
                else if(-100 <= bearingDiff && bearingDiff < -30) n.direction = 5;
                else if(30 < bearingDiff && bearingDiff <= 100) n.direction = 4;
                else if(bearingDiff < -100) n.direction = 6;
                else n.direction = 7;
                lis.add(n);
//                System.out.println(n.way);
            }
            preBearing = bearing;
        }
        return lis; // FIXME
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
