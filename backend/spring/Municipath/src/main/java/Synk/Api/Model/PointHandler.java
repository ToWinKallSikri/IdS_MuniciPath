package Synk.Api.Model;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PointHandler {

    Map<Point, Post> ppstMap;
    WeatherForecast w1;

    PointHandler() {
        ppstMap = new HashMap<Point, Post>();
        w1 = new WeatherForecast();
        }
    

    
    public boolean createPost(String title, PostType type, File newFile, String author, Position pos, String cityID) {
            Post p1 = new Post(title, type, newFile, author, pos, cityID);
            Point point = new Point(pos);
            ppstMap.put(point, p1);
            System.out.println("Post created");
            return true;
    }

    public List<Point> getPoints (String cityID) {
       List<Point> points = new ArrayList<Point>();
         for (Point point : ppstMap.keySet()) {
              if (ppstMap.get(point).getId().equals(cityID)) {
                points.add(point);
              }
         }
       return points;
    }
}
