package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static bearmaps.proj2c.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
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
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        System.out.println("yo, wanna know the parameters given by the web browser? They are:");
        System.out.println(requestParams);
        Map<String, Object> results = new HashMap<>();
        //System.out.println("Since you haven't implemented RasterAPIHandler.processRequest, nothing is displayed in "
        //       + "your browser.");

        if (!validateRequestParams(requestParams)){
            return queryFail();
        }
        double destLonDPP = computeLonDPP(requestParams.get("ullon"), requestParams.get("lrlon"),
                                          requestParams.get("w"));
        int destDepthLevel = computeDepthLevel(destLonDPP);

        int ULXIndex = getX_IndexOfImages(requestParams.get("ullon"), destDepthLevel);
        int ULYIndex = getY_IndexOfImages(requestParams.get("ullat"), destDepthLevel);
        int LRXIndex = getX_IndexOfImages(requestParams.get("lrlon"), destDepthLevel);
        int LRYIndex = getY_IndexOfImages(requestParams.get("lrlat"), destDepthLevel);

        String[][] render_grid = new String[LRYIndex - ULYIndex + 1][LRXIndex - ULXIndex + 1];
        for (int y = 0; y < LRYIndex - ULYIndex + 1; y++){
            for (int x = 0; x < LRXIndex - ULXIndex + 1; x++){
                render_grid[y][x] = "d" + destDepthLevel + "_x" + (x + ULXIndex) + "_y" + (y + ULYIndex) + ".png";
            }
        }
        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", getLeftLonOfImage(destDepthLevel, ULXIndex));
        results.put("raster_lr_lon", getRightLonOfImage(destDepthLevel, LRXIndex));
        results.put("raster_ul_lat", getUpperLatOfImage(destDepthLevel, ULYIndex));
        results.put("raster_lr_lat", getLowerLatOfImage(destDepthLevel, LRYIndex));
        results.put("depth", destDepthLevel);
        results.put("query_success", true);
        return results;
    }

    /**
     * Compute the longitudinal distance per pixel(LonDPP) for an image or a box.
     * @param ullon the bounding upper left longitude of the rastered image(or the box).
     * @param lrlon the bounding lower right longitude of the rastered image(or the box).
     * @param widthInPixels width of the image(or box) in pixels
     * @return
     */
    private double computeLonDPP(double ullon, double lrlon, double widthInPixels){
        return (lrlon - ullon)/widthInPixels;
    }

    /**
     * Compute the depth level appropriate to @param destLonDPP, the LonDPP of that level should be
     * greater than just @param destLonDPP.
     * @param destLonDPP
     * @return an integer(0~7) representing suitable depth level
     */
    private int computeDepthLevel(double destLonDPP){
        final int levelNum = 8;
        double eachLevelLonDPP;
        int i;
        for(i = 0; i < levelNum; i++){
            eachLevelLonDPP = ((ROOT_LRLON - ROOT_ULLON)/Math.pow(2, i))/TILE_SIZE;
            if (destLonDPP >= eachLevelLonDPP) { return i; }
        }
        return levelNum - 1;
    }

    private int getX_IndexOfImages(double lon, int depthLevel){
        double delta = (ROOT_LRLON - ROOT_ULLON)/Math.pow(2, depthLevel);
        double temp_lon = ROOT_ULLON + delta;
        int x = 0;
        for(; x < Math.pow(2, depthLevel); x++){
            if (lon <= temp_lon)
                return x;
            temp_lon += delta;
        }
        return x;
    }

    private int getY_IndexOfImages(double lat, int depthLevel){
        double delta = (ROOT_ULLAT - ROOT_LRLAT)/Math.pow(2, depthLevel);
        double temp_lat = ROOT_ULLAT - delta;
        int y = 0;
        for(; y < Math.pow(2, depthLevel); y++){
            if (lat >= temp_lat)
                return y;
            temp_lat -= delta;
        }
        return y;
    }
    // 获取image左边界的经度
    private double getLeftLonOfImage(int depthlevel, int XIndex){
        return ROOT_ULLON + XIndex * (ROOT_LRLON - ROOT_ULLON)/Math.pow(2, depthlevel);
    }
    // 获取image右边界的经度
    private double getRightLonOfImage(int depthlevel, int XIndex){
        return ROOT_ULLON + (XIndex + 1) * (ROOT_LRLON - ROOT_ULLON)/Math.pow(2, depthlevel);
    }
    //获取image上边界的纬度
    private double getUpperLatOfImage(int depthlevel, int YIndex){
        return ROOT_ULLAT - YIndex * (ROOT_ULLAT - ROOT_LRLAT)/Math.pow(2, depthlevel);
    }
    //获取image下边界的纬度
    private double getLowerLatOfImage(int depthlevel, int YIndex){
        return ROOT_ULLAT - (YIndex + 1) * (ROOT_ULLAT - ROOT_LRLAT)/Math.pow(2, depthlevel);
    }

    //确保输入的经纬度落在合理的范围内，若完全落在合理范围内或者有部分落在外则返回true，
    // 且调整为为合理的范围，若完全落在合理范围外，则返回false。
    boolean validateRequestParams(Map<String, Double> requestParams){
        double[] ULLon_Lat = {requestParams.get("ullon"), requestParams.get("ullat")};
        double[] LRLon_Lat = {requestParams.get("lrlon"), requestParams.get("lrlat")};

        // 输入经纬度完全落在合法范围内，无需调整
        if (inBerkeley(ULLon_Lat) && inBerkeley(LRLon_Lat)) return true;

        if (!isValidLon(ULLon_Lat[0])){
            if (ULLon_Lat[0] >= ROOT_LRLON) return false;
            requestParams.put("ullon", ROOT_ULLON);
        }
        if(!isValidLat(ULLon_Lat[1])){
            if (ULLon_Lat[1] <= ROOT_LRLAT) return false;
            requestParams.put("ullat", ROOT_ULLAT);
        }
        if(!isValidLon(LRLon_Lat[0])){
            if(LRLon_Lat[0] <= ROOT_ULLON) return false;
            requestParams.put("lrlon", ROOT_LRLON);
        }
        if(!isValidLat(LRLon_Lat[1])){
            if(LRLon_Lat[1] >= ROOT_ULLAT) return false;
            requestParams.put("lrlat", ROOT_LRLAT);
        }
        return true;
        /*
        double[] LLLon_Lat = {requestParams.get("ullon"), requestParams.get("lrlat")};
        double[] URLon_Lat = {requestParams.get("lrlon"), requestParams.get("ullat")};

        //部分落在合法范围内，需要调整
        if(inBerkeley(ULLon_Lat) || inBerkeley(LRLon_Lat) ||
                inBerkeley(LLLon_Lat) || inBerkeley(URLon_Lat)){
            if (!isValidLon(ULLon_Lat[0])) {
                requestParams.put("ullon", ROOT_ULLON);
            }
            if (!isValidLon(LRLon_Lat[0])){
                requestParams.put("lrlon", ROOT_LRLON);
            }
            if (!isValidLat(ULLon_Lat[1])) {
                requestParams.put("ullat", ROOT_ULLAT);
            }
            if (!isValidLat(LRLon_Lat[1])){
                requestParams.put("lrlat", ROOT_LRLAT);
            }
            return true;
        }
        return false;*/
    }

    // 输入参数为一个经纬度坐标，若该坐标在Berkeley范围内则返回true，否则返回false。
    boolean inBerkeley(double[] Lon_LatCoor){
        return Lon_LatCoor[0] >= ROOT_ULLON && Lon_LatCoor[0] <= ROOT_LRLON &&
                Lon_LatCoor[1] >= ROOT_LRLAT && Lon_LatCoor[1] <= ROOT_ULLAT;
    }

    boolean isValidLon(double lon){
        return lon <= ROOT_LRLON && lon >= ROOT_ULLON;
    }

    boolean isValidLat(double lat){
        return lat <= ROOT_ULLAT && lat >= ROOT_LRLAT;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
