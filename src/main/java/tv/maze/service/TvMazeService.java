package tv.maze.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;
import tv.maze.model.ShowDTO;


@Service
public class TvMazeService {

    private final RestClient restClient;
    
    public TvMazeService(RestClient restClient) {
        this.restClient = restClient;
    }

    
    public List<ShowDTO> obtenerShows(String query) {
    	
    	List<ShowDTO> shows = new ArrayList<ShowDTO>();
    	JsonNode rootArray = null;
    	
    	try {
    	
        rootArray = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/shows")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .body(JsonNode.class);

        
    	} catch (org.springframework.web.client.RestClientResponseException ex) {
    	    String errorHtml = ex.getResponseBodyAsString(); 
    	    System.err.println("Código: " + ex.getStatusCode());
    	    System.err.println("Error: " + errorHtml);
    	    
    	} catch (org.springframework.web.client.UnknownContentTypeException ex) {
    	    String rawBody = ex.getResponseBodyAsString();
    	    System.err.println("Mensaje: " + rawBody);
    	}
    	
    	
        if (rootArray == null || !rootArray.isArray()) {
            return shows;
        }
    	
        for (JsonNode resultNode : rootArray) {

            JsonNode showNode = resultNode.path("show");
            if (showNode.isMissingNode()) {
                continue; 
            }

            int id = showNode.path("id").asInt();
            String name = showNode.path("name").asString();
            String summary = showNode.path("summary").asString();
            String channel = "";
           
            if(!showNode.path("network").path("name").asString().equals("")) {
            	channel = showNode.path("network").path("name").asString("Unknow");
            }else{
            	channel = showNode.path("webChannel").path("name").asString("Unknow");
            }

            List<String> genres = new ArrayList<>();
            JsonNode genresNode = showNode.path("genres");
            if (genresNode.isArray()) {
                for (JsonNode genre : genresNode) {
                    genres.add(genre.asString());
                }
            }

            shows.add(new ShowDTO(id, name, channel, summary, genres));        	
        	
        }
    	return shows;
    }
}
