package Synk.Api.View.Model;

import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.Post.PostType;

public class ProtoPost {
	
    public String title;
    public PostType type;
    public String text;
    public List<String> multimediaData;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public boolean persistence;
    
}
