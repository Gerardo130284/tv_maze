package tv.maze.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowDTO {

	private int id;
	private String name;
	private String channel;
	private String summary;
	private List<String> genres;
	
}
