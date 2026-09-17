package tv.maze.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tv.maze.model.Comments;

@Repository
public interface CommentsRepository extends MongoRepository<Comments, String> {
	
}