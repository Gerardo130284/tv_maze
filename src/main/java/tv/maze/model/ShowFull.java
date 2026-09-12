package tv.maze.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


public record ShowFull (
    int id,
    String url,
    String name,
    String type,
    String language,
    List<String> genres,
    String status,
    Integer runtime,
    Integer averageRuntime,
    String premiered,
    String ended,
    String officialSite,
    Schedule schedule,
    Rating rating,
    int weight,
    Network network,
    WebChannel webChannel,
    DvdCountry dvdCountry,
    Externals externals,
    Image image,
    String summary,
    int updated,
    Links _links
) {
    
    public record Schedule(
        String time,
        List<String> days
    ) {}

    public record Rating(
        Double average
    ) {}

    public record Network(
        int id,
        String name,
        Country country,
        String officialSite
    ) {}

    public record WebChannel(
        int id,
        String name,
        Country country,
        String officialSite
    ) {}

    public record Country(
        String name,
        String code,
        String timezone
    ) {}

    public record DvdCountry(
        String name,
        String code,
        String timezone
    ) {}

    public record Externals(
        Integer tvrage,
        Integer thetvdb,
        String imdb
    ) {}

    public record Image(
        String medium,
        String original
    ) {}

    public record Links(
        Self self,
        @JsonProperty("previousepisode") PreviousEpisode previousEpisode
    ) {
        public record Self(String href) {}
        public record PreviousEpisode(String href, String name) {}
    }
}
