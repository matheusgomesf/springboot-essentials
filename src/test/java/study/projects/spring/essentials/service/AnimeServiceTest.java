package study.projects.spring.essentials.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import study.projects.spring.essentials.domain.Anime;
import study.projects.spring.essentials.repository.AnimeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static study.projects.spring.essentials.util.AnimeCreator.createAnimeToBeSaved;
import static study.projects.spring.essentials.util.AnimeCreator.createValidAnime;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepository;

    private Anime validAnime;

    @BeforeEach
    public void setup() {
        validAnime = createValidAnime();
        PageImpl<Anime> animePage = new PageImpl<>(Collections.singletonList(validAnime));
        BDDMockito.when(animeRepository.findAll(any(Pageable.class))).thenReturn(animePage);
        BDDMockito.when(animeRepository.findById(anyInt())).thenReturn(Optional.of(validAnime));
        BDDMockito.when(animeRepository.findByName(anyString())).thenReturn(Collections.singletonList(validAnime));
        BDDMockito.when(animeRepository.save(any())).thenReturn(validAnime);
        BDDMockito.doNothing().when(animeRepository).delete(any());
    }

    @Test
    public void pageAll_Success() {
        Page<Anime> anime = animeService.pageAll(PageRequest.of(1, 1));

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.toList()).isNotEmpty();
        Assertions.assertThat(anime.toList().get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void findById_Success() {
        Anime anime = animeService.findById(1);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
    }

    @Test
    public void findByName_Success() {
        List<Anime> anime = animeService.findByName("1");

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isNotEmpty();
        Assertions.assertThat(anime.get(0).getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void save_Success() {
        Anime anime = animeService.save(createAnimeToBeSaved());

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
        Assertions.assertThat(anime.getName()).isEqualTo(validAnime.getName());
    }

    @Test
    public void delete_Success() {
        Assertions.assertThatCode(() -> animeService.delete(1)).doesNotThrowAnyException();
    }


    @Test
    public void update_Success() {
        Anime anime = animeService.save(createAnimeToBeSaved());

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(validAnime.getId());
        Assertions.assertThat(anime.getName()).isEqualTo(validAnime.getName());
    }
}