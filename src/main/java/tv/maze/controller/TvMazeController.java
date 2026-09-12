package tv.maze.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tv.maze.model.ShowFull;
import tv.maze.model.ShowDTO;
import tv.maze.service.TvMazeService;


@RestController
@RequestMapping("/tvmaze")
public class TvMazeController {
	
	private final TvMazeService tvMazeService;

    public TvMazeController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }
    

    @GetMapping("/search")
    public ResponseEntity<List<ShowDTO>> buscar (
            @RequestParam(name = "search_query", required = false) String query){
        
        List<ShowDTO> shows = tvMazeService.obtenerShows(query);
        return ResponseEntity.ok(shows);
    }
    
    
    @GetMapping("/show/{show_id}")
    public ResponseEntity<ShowFull> detalleShow (
    		@PathVariable(name = "show_id", required = true) int id){   
    	
        ShowFull show = tvMazeService.obtenerDetalleShow(id);
        return ResponseEntity.ok(show);
    }
}
