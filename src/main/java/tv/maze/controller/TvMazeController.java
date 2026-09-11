package tv.maze.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<ShowDTO>> buscar(
            @RequestParam(name = "search_query", required = false) String query){
        
        List<ShowDTO> shows = tvMazeService.obtenerShows(query);
        return ResponseEntity.ok(shows);
    }	
    
}
