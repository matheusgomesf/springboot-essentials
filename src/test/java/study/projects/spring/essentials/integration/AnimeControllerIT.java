package study.projects.spring.essentials.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.projects.spring.essentials.domain.Anime;
import study.projects.spring.essentials.repository.AnimeRepository;
import study.projects.spring.essentials.wrapper.PageableResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static study.projects.spring.essentials.util.AnimeCreator.createAnimeToBeSaved;
import static study.projects.spring.essentials.util.AnimeCreator.createValidAnime;
import static study.projects.spring.essentials.util.AnimeCreator.createValidUpdatedAnime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimeControllerIT {

    @Autowired
    @Qualifier("testRestTemplateRoleUserCreator")
    private TestRestTemplate testRestTemplate;
    @MockBean
    private AnimeRepository animeRepository;

    private Anime validAnime;

    @Lazy
    @TestConfiguration
    static class config {

        @Bean("testRestTemplateRoleUserCreator")
        public TestRestTemplate restTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "senha123");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @BeforeEach
    public void setup() {
        validAnime = createValidAnime();
        PageImpl<Anime> animePage = new PageImpl<>(Collections.singletonList(validAnime));
        BDDMockito.when(animeRepository.findAll(any(Pageable.class))).thenReturn(animePage);
        BDDMockito.when(animeRepository.findAll()).thenReturn(Collections.singletonList(validAnime));
        BDDMockito.when(animeRepository.findById(anyInt())).thenReturn(Optional.of(validAnime));
        BDDMockito.when(animeRepository.findByName(anyString())).thenReturn(Collections.singletonList(validAnime));
        BDDMockito.when(animeRepository.save(any())).thenReturn(validAnime);
        BDDMockito.doNothing().when(animeRepository).delete(any());
    }

    @Test
    public void pageAll_Success() {
        Page<Anime> anime = testRestTemplate.exchange("/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {})
                .getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.toList()).isNotEmpty();
        Assertions.assertThat(anime.toList().get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void listAll_Success() {
        List<Anime> anime = testRestTemplate.exchange("/animes/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {})
                .getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void findById_Success() {
        Anime anime = testRestTemplate.getForObject("/animes/1",Anime.class);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
    }

    @Test
    public void findByName_Success() {
        List<Anime> anime = testRestTemplate.exchange("/animes/find?name='DBZ'",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {})
                .getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void save_Success() {
        Anime anime = testRestTemplate.postForObject("/animes", createAnimeToBeSaved(), Anime.class);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
        Assertions.assertThat(anime.getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void delete_Success() {
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange("/animes/admin/1",
                HttpMethod.DELETE,
                null,
                Void.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void update_Success() {
        testRestTemplate.put("/animes", createValidUpdatedAnime(), Anime.class);
        verify(animeRepository, times(1)).save(validAnime);
    }
}
