package tv.maze.repository;

import tv.maze.model.ShowFullDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowFullRepository extends MongoRepository<ShowFullDocument, Integer> {
}
