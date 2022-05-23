import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    /** Each tile is 256x256 pixels. */
    private static final int TILE_SIZE = 256;
    private static int maxDepth = 7;

    public Rasterer() {
        // YOUR CODE HERE
    }

    /*
     * validating the order of two coreners.
     */
    private boolean validate(double ullon, double ullat, double lrlon, double lrlat, double w){
        if(ullon >= lrlon || ullat <= lrlat || w <= 0) return false;
        return true;
    }
    /*
     * checking whether this query box is out of bounds.
     */
    private boolean queryBoxOutOfBound(double ullon, double ullat, double lrlon, double lrlat){
        if(ullon >= ROOT_LRLON || lrlon <= ROOT_ULLON || ullat <= ROOT_LRLAT || lrlat >= ROOT_ULLAT) return true;
        return false;
    }

    private double lonDPP(double llon, double rlon, double w){
        return (rlon - llon)/w;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *{lrlon=-122.2104604264636, ullon=-122.30410170759153, w=1085.0, h=566.0, ullat=37.870213571328854, lrlat=37.8318576119893}
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double ullon = params.get("ullon"), ullat = params.get("ullat"), lrlon = params.get("lrlon"),
                lrlat = params.get("lrlat"), w = params.get("w");
        if(validate( ullon, ullat, lrlon, lrlat, w) == false || queryBoxOutOfBound(ullon, ullat, lrlon, lrlat)){///?:what if ullon or other params is out of bound?
            results.put("query_success", false);
            return results;
        }
        double baseLonDPP = lonDPP(ullon, lrlon, w);
        ////wraping bounds.
        ullon = Math.max(ullon, ROOT_ULLON);
        lrlat = Math.max(lrlat, ROOT_LRLAT);
        ullat = Math.min(ullat, ROOT_ULLAT);
        lrlon = Math.min(lrlon, ROOT_LRLON);
        int depth = -1;
        double londiff = ROOT_LRLON-ROOT_ULLON;
        for(int i = 0;i < 8;++i ){
            double nowLonDPP = londiff/256;
            if(nowLonDPP <= baseLonDPP){
                depth = i;
                break;
            }
            londiff /= 2;
        }
        if(depth == -1) depth = 7;
        double dlon = (ROOT_LRLON-ROOT_ULLON)/Math.pow(2,depth), dlat = (ROOT_LRLAT-ROOT_ULLAT)/Math.pow(2,depth);
        int st_lon = (int)((ullon-ROOT_ULLON)/dlon), st_lat = (int)((ullat-ROOT_ULLAT)/dlat);
        double raster_ul_lon = ROOT_ULLON+ st_lon*dlon, raster_ul_lat = ROOT_ULLAT+st_lat*dlat;//(int) priority? ans: bigger than */%
        int M = (int)Math.ceil((lrlon-raster_ul_lon)/dlon), N = (int)Math.ceil((lrlat-raster_ul_lat)/dlat);
        double raster_lr_lon = raster_ul_lon + M*dlon, raster_lr_lat = raster_ul_lat + N*dlat;

        String[][] render_grid = new String[N][M];
        for(int i = 0;i < N;++i ){
            for(int j = 0;j < M;++j )
                render_grid[i][j] = "d"+depth+"_x"+(st_lon+j)+"_y"+(st_lat+i)+".png";
        }
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
        results.put("query_success", true);
        results.put("depth", depth);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("render_grid", render_grid);
        return results;
    }

}
