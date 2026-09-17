package tv.maze.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
public record Comments(
    @Id 
    String id,
    
    @Field("show_id") 
    int showId,
    
    String comment,
    
    int rating
) {

    public Comments(int showId, String comment, int rating) {
        this(null, showId, comment, rating);
    }
}
