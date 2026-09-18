package tv.maze.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

@Document(collection = "show")
public record ShowFullDocument(
    
	@Id
    int id, // ID de TVmaze como llave primaria en Mongo
    
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

    @Field("links") 
    Links links, 
    
    //CONFIGURACIÓN DE CACHÉ DE 1 DÍA: MongoDB eliminará el registro 86400 segundos después de esta hora
    @Indexed(expireAfterSeconds = 86400)
    @Field("created_at")
    Instant createdAt
    
) {

    public record Schedule(String time, List<String> days) {}
    public record Rating(Double average) {}
    public record Country(String name, String code, String timezone) {}
    public record Network(int id, String name, Country country, String officialSite) {}
    public record WebChannel(int id, String name, Country country, String officialSite) {}
    public record DvdCountry(String name, String code, String timezone) {}
    public record Externals(Integer tvrage, Integer thetvdb, String imdb) {}
    public record Image(String medium, String original) {}
    
    public record Links(
        Self self,
        @JsonProperty("previousepisode") PreviousEpisode previousEpisode
    ) {
        public record Self(String href) {}
        public record PreviousEpisode(String href, String name) {}
    }

    /**
     * Convierte a documento para Mongo
     */
    public static ShowFullDocument toDocumentMongo(ShowFull api) {
        return new ShowFullDocument(
            api.id(), api.url(), api.name(), api.type(), api.language(), api.genres(),
            api.status(), api.runtime(), api.averageRuntime(), api.premiered(), api.ended(),
            api.officialSite(),
            api.schedule() != null ? new Schedule(api.schedule().time(), api.schedule().days()) : null,
            api.rating() != null ? new Rating(api.rating().average()) : null,
            api.weight(),
            api.network() != null ? new Network(api.network().id(), api.network().name(), api.network().country() != null ? new Country(api.network().country().name(), api.network().country().code(), api.network().country().timezone()) : null, api.network().officialSite()) : null,
            api.webChannel() != null ? new WebChannel(api.webChannel().id(), api.webChannel().name(), api.webChannel().country() != null ? new Country(api.webChannel().country().name(), api.webChannel().country().code(), api.webChannel().country().timezone()) : null, api.webChannel().officialSite()) : null,
            api.dvdCountry() != null ? new DvdCountry(api.dvdCountry().name(), api.dvdCountry().code(), api.dvdCountry().timezone()) : null,
            api.externals() != null ? new Externals(api.externals().tvrage(), api.externals().thetvdb(), api.externals().imdb()) : null,
            api.image() != null ? new Image(api.image().medium(), api.image().original()) : null,
            api.summary(), api.updated(),
            api._links() != null ? new Links(
                api._links().self() != null ? new Links.Self(api._links().self().href()) : null,
                api._links().previousEpisode() != null ? new Links.PreviousEpisode(api._links().previousEpisode().href(), api._links().previousEpisode().name()) : null
            ) : null, null
        );
    }
    
    
    /**
     * Convierte a objeto de respuesta del API 
     */
    public ShowFull toAPIResponse() {
        return new ShowFull(
            this.id(), this.url(), this.name(), this.type(), this.language(), this.genres(),
            this.status(), this.runtime(), this.averageRuntime(), this.premiered(), this.ended(),
            this.officialSite(),
            this.schedule() != null ? new ShowFull.Schedule(this.schedule().time(), this.schedule().days()) : null,
            this.rating() != null ? new ShowFull.Rating(this.rating().average()) : null,
            this.weight(),
            this.network() != null ? new ShowFull.Network(this.network().id(), this.network().name(), this.network().country() != null ? new ShowFull.Country(this.network().country().name(), this.network().country().code(), this.network().country().timezone()) : null, this.network().officialSite()) : null,
            this.webChannel() != null ? new ShowFull.WebChannel(this.webChannel().id(), this.webChannel().name(), this.webChannel().country() != null ? new ShowFull.Country(this.webChannel().country().name(), this.webChannel().country().code(), this.webChannel().country().timezone()) : null, this.webChannel().officialSite()) : null,
            this.dvdCountry() != null ? new ShowFull.DvdCountry(this.dvdCountry().name(), this.dvdCountry().code(), this.dvdCountry().timezone()) : null,
            this.externals() != null ? new ShowFull.Externals(this.externals().tvrage(), this.externals().thetvdb(), this.externals().imdb()) : null,
            this.image() != null ? new ShowFull.Image(this.image().medium(), this.image().original()) : null,
            this.summary(), this.updated(),
            this.links() != null ? new ShowFull.Links(
                this.links().self() != null ? new ShowFull.Links.Self(this.links().self().href()) : null,
                this.links().previousEpisode() != null ? new ShowFull.Links.PreviousEpisode(this.links().previousEpisode().href(), this.links().previousEpisode().name()) : null
            ) : null
        );
    }

}
