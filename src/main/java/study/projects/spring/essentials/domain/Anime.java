package study.projects.spring.essentials.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "The name of the anime can't be empty")
    @NotEmpty(message = "The name of the anime can't be empty")
    @Schema(example = "DBZ")
    private String name;

    @URL(message = "The format of the URL is not corect")
    @NotNull(message = "The URL of the anime can't be empty")
    @NotEmpty(message = "The URL of the anime can't be empty")
    @Schema(example = "http://...")
    private String url;
}
