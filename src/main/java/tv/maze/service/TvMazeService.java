package tv.maze.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tv.maze.repository.CommentsRepository;
import tv.maze.repository.ShowFullRepository;
import tv.maze.model.Comments;
import tv.maze.model.ShowFull;
import tv.maze.model.ShowFullDocument;
import tv.maze.model.ShowShort;


@Slf4j
@Service
public class TvMazeService {

    private final RestClient restClient;
    private final ShowFullRepository showFullRepository;
    private final CommentsRepository commentsRepository;

    public TvMazeService(RestClient restClient, ShowFullRepository showFullRepository, CommentsRepository commentsRepository) {
        this.restClient = restClient;
        this.showFullRepository = showFullRepository;
        this.commentsRepository = commentsRepository;
    }
    
    public List<ShowShort> obtenerShows(String query) {
    	
    	List<ShowShort> shows = new ArrayList<ShowShort>();
    	JsonNode rootArray = null;
    	
    	try {
    	
        rootArray = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/shows")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .body(JsonNode.class);

        
    	} catch (RestClientResponseException ex) {
    	    log.error("Código: " + ex.getStatusCode());
    	    log.error("Error: " + ex.getResponseBodyAsString());
    	    
    	    throw HttpClientErrorException.create(
                    ex.getStatusCode(), 
                    ex.getStatusText(), 
                    ex.getResponseHeaders(), 
                    ex.getResponseBodyAsByteArray(), 
                    null
            ); 
    	      
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


            List<Comments> comments = new ArrayList<>(); 
            
            try {
                List<Comments> dbComentarios = commentsRepository.findByShowId(id);
                for (Comments comentario : dbComentarios) {
                	//comments.add(new ShowShort.CommentDTO(comentario.comment(), comentario.rating()));
                	comments.add(comentario);
                }
            } catch (DataAccessException e) {
                log.error("Error en MongoDB para el show ID {}: {}", id, e.getMessage());
            }
                        
            shows.add(new ShowShort(id, name, channel, summary, genres, null)); 
            shows.add(new ShowShort(id, name, channel, summary, genres, comments));

            //shows.add(new ShowShort(id, name, channel, summary, genres));  

        }
        
    	return shows;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public ShowFull obtenerDetalleShow(int id) {
    	
    	try {
    	
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/shows/{id}")
                        .build(id))
                .retrieve()
                .body(ShowFull.class);
        
    	} catch (RestClientResponseException ex) {
    	    log.error("Código: " + ex.getStatusCode());
    	    log.error("Error: " + ex.getResponseBodyAsString());
   	    
    	    throw HttpClientErrorException.create(
                    ex.getStatusCode(), 
                    ex.getStatusText(), 
                    ex.getResponseHeaders(), 
                    ex.getResponseBodyAsByteArray(), 
                    null
            );     
    	}
 
    }
    

    public ShowFull getShowById(int id) {
        try {
            // Existe en Mongo?
            Optional<ShowFullDocument> cachedDocument = showFullRepository.findById(id);
            
            if (cachedDocument.isPresent()) {
                log.info("Show ID {} desde MongoDB.", id);
                return cachedDocument.get().toAPIResponse(); 
            }

            log.info("Show ID {} desde API de TVmaze.", id);
            
            ShowFull apiResponse = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/shows/{id}").build(id))
                    .retrieve()
                    .body(ShowFull.class);

            if (apiResponse != null) {
                var documentToCache = ShowFullDocument.toDocumentMongo(apiResponse);
                showFullRepository.save(documentToCache);
                log.info("Show ID {} guardado en MongoDB.", id);
            }

            return apiResponse;

        } catch (RestClientResponseException e) {
            log.error("Error al consumir el API de TVmaze, Código: {}", e.getStatusCode());
            if (e.getStatusCode().is4xxClientError()) {
                throw HttpClientErrorException.create(
                        e.getStatusCode(), e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), null);
            } else {
                throw HttpServerErrorException.create(
                        e.getStatusCode(), e.getStatusText(), e.getResponseHeaders(), e.getResponseBodyAsByteArray(), null);
            }
        } catch (Exception e) {
            log.error("Fallo general para el show ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error General", e);
        }
    }
    
    
    public HttpStatusCode escribirComentario (Comments comment) {
    	
        try {
        	commentsRepository.save(comment);
        	log.info("Comentario guardado");

        	return ResponseEntity.status(HttpStatus.CREATED).build().getStatusCode();
        	
        } catch (DataAccessException e) {
            log.error("Error al guardar el comentario en MongoDB para el show ID {}: {}", comment.showId(), e.getMessage());
            throw new RuntimeException("Error en el guardado del comentario", e);
        }
    }

}
