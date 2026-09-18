package tv.maze.model;

import java.util.List;


public record ShowShort (
		
	int id,
	String name,
	String channel,
	String summary,
	List<String> genres,
	List<Comments> comments
	
	){
	
    public record CommentDTO(
            String comment,
            int rating
        ) {}
}
