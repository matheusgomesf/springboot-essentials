package study.projects.spring.essentials.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import study.projects.spring.essentials.domain.Anime;
import study.projects.spring.essentials.service.AnimeService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static study.projects.spring.essentials.util.AnimeCreator.createAnimeToBeSaved;
import static study.projects.spring.essentials.util.AnimeCreator.createValidAnime;

@ExtendWith(SpringExtension.class)
public class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeService;

    private Anime validAnime;

    @BeforeEach
    public void setup() {
        validAnime = createValidAnime();
        PageImpl<Anime> animePage = new PageImpl<>(Collections.singletonList(validAnime));
        BDDMockito.when(animeService.pageAll(any())).thenReturn(animePage);
        BDDMockito.when(animeService.listAll()).thenReturn(Collections.singletonList(validAnime));
        BDDMockito.when(animeService.findById(anyInt())).thenReturn(validAnime);
        BDDMockito.when(animeService.findByName(anyString())).thenReturn(Collections.singletonList(validAnime));
        BDDMockito.when(animeService.save(any())).thenReturn(validAnime);
        BDDMockito.doNothing().when(animeService).delete(anyInt());
        BDDMockito.doNothing().when(animeService).update(any());
    }

    @Test
    public void pageAll_Success() {
        Page<Anime> anime = animeController.pageAll(null, null).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.toList()).isNotEmpty();
        Assertions.assertThat(anime.toList().get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void listAll_Success() {
        List<Anime> anime = animeController.listAll().getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void findById_Success() {
        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
    }

    @Test
    public void findByName_Success() {
        List<Anime> anime = animeController.findByName("1").getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void save_Success() {
        Anime anime = animeController.saveAnime(createAnimeToBeSaved()).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
        Assertions.assertThat(anime.getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void delete_Success() {
        ResponseEntity<Void> responseEntity = animeController.deleteAnime(1);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    public void update_Success() {
        ResponseEntity<Void> responseEntity = animeController.updateAnime(createAnimeToBeSaved());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }
}
