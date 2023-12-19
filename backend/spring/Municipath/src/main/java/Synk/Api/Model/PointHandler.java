package Synk.Api.Model;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PointHandler {

    Map<Point, ArrayList<Post>> postMap;
    private WeatherForecast weather;
    private CityHandler cityhandler;
    private GroupHandler groupHandler;

    int idCounter = 0;

    public PointHandler(CityHandler ch, GroupHandler gh) {
        postMap = new HashMap<>();
        weather = new WeatherForecast();
        this.cityhandler = ch;
        this.groupHandler = gh;
    }
    

    
    public boolean createPost(String title, PostType type, File newFile, String text,
                              String author, Position pos, String cityID, int postId) {
            Post p1 = new Post(title, type, newFile, author,text, pos, cityID, idCounter);
            idCounter++;
            Point point = new Point(pos);
            postMap.put(point, p1);
            System.out.println("Post created");
            return true;
    }

    public List<Point> getPoints (String cityID) {
       List<Point> points = new ArrayList<Point>();
         for (Point point : postMap.keySet()) {
              if (postMap.get(point).getId().equals(cityID)) {
                points.add(point);
              }
         }
       return points;
    }

    public void deleteCityPoints (String cityId) {
        for (Point point : postMap.keySet()) {
            if (postMap.get(point).getId().equals(cityId)) {
                postMap.remove(point);
                postMap.entrySet().removeIf(entry -> entry.getKey().equals(point));
            }
        }
    }

    public List<Post> getPosts (int postId) {
        List<Post> posts = new ArrayList<Post>();
        for (Point point : postMap.keySet()) {
            if (postMap.get(point).getPostId() == postId) {
                posts.add(postMap.get(point));
            }
        }
        return posts;
    }

    public void deletePost (String postId) {
        for (Point point : postMap.keySet()) {
            if (postMap.get(point).getId().equals(postId)) {
                postMap.remove(point);
                postMap.entrySet().removeIf(entry -> entry.getKey().equals(point));
            }
        }
    }


}
