package tv.maze.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Document(collection = "comments")
public record Comments(
    @Id 
    String id,
    
    @Field("show_id") 
    int showId,
    
    String comment,
    
    @Min(value = 0, message = "La calificación mínima es 0")
    @Max(value = 5, message = "La calificación máxima es 5")
    int rating
) {

    public Comments(int showId, String comment, int rating) {
        this(null, showId, comment, rating);
    }
}
