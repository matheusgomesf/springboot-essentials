package study.projects.spring.essentials.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.client.RestTemplate;
import study.projects.spring.essentials.domain.Anime;
import study.projects.spring.essentials.wrapper.PageableResponse;

import java.util.List;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {
        log.info("{}", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("senha123"));

    //testGet();

        Anime anime = Anime.builder()
                        .name("Overlord")
                        .url("http://Overlord.com")
                        .build();
//       Anime post = new RestTemplate().postForObject("http://localhost:8080/animes/", anime, Anime.class);
//       log.info("{}",post);

        Anime stainsGate = Anime.builder()
                .name("stainsGate")
                .url("http://stainsGate.com")
                .build();
        ResponseEntity<Anime> exchange = new RestTemplate().exchange("http://localhost:8080/animes/", HttpMethod.POST, new HttpEntity<>(stainsGate), Anime.class);
        log.info("{}",exchange.getBody());
    }

    private static void testGet() {
        Anime anime = new RestTemplate().getForObject("http://localhost:8080/animes/1", Anime.class);
        log.info(anime.toString());

        ResponseEntity<List<Anime>> animesList = new RestTemplate()
                .exchange("http://localhost:8080/animes/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {});
        log.info(animesList.getBody().toString());

        ResponseEntity<PageableResponse<Anime>> animesPageable = new RestTemplate()
                .exchange("http://localhost:8080/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {});
        log.info(animesList.getBody().toString());
    }
}
