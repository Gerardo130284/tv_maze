package tv.maze.util;

import java.time.LocalDateTime;

public record ErrorComun(
		
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {}
