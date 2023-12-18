package Synk.Api.Model;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PointHandler {

    Map<Point, Post> ppstMap;
    WeatherForecast w1;

    int idCounter = 0;

    PointHandler() {
        ppstMap = new HashMap<Point, Post>();
        w1 = new WeatherForecast();
        }
    

    
    public boolean createPost(String title, PostType type, File newFile, String text,
                              String author, Position pos, String cityID, int postId) {
            Post p1 = new Post(title, type, newFile, author,text, pos, cityID, idCounter);
            idCounter++;
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

    public void deleteCityPoints (String cityId) {
        for (Point point : ppstMap.keySet()) {
            if (ppstMap.get(point).getId().equals(cityId)) {
                ppstMap.remove(point);
                ppstMap.entrySet().removeIf(entry -> entry.getKey().equals(point));
            }
        }
    }

    public List<Post> getPosts (int postId) {
        List<Post> posts = new ArrayList<Post>();
        for (Point point : ppstMap.keySet()) {
            if (ppstMap.get(point).getPostId() == postId) {
                posts.add(ppstMap.get(point));
            }
        }
        return posts;
    }

    public void deletePost (String postId) {
        for (Point point : ppstMap.keySet()) {
            if (ppstMap.get(point).getId().equals(postId)) {
                ppstMap.remove(point);
                ppstMap.entrySet().removeIf(entry -> entry.getKey().equals(point));
            }
        }
    }


}
