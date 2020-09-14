package study.projects.spring.essentials.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.projects.spring.essentials.domain.Anime;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static study.projects.spring.essentials.util.AnimeCreator.createAnimeToBeSaved;
import static study.projects.spring.essentials.util.AnimeCreator.createValidAnime;
import static study.projects.spring.essentials.util.AnimeCreator.createValidUpdatedAnime;

@DataJpaTest
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    void saveAnime_Success() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);


        assertNotNull(savedAnime);
        assertNotNull(savedAnime.getId());
        assertNotNull(savedAnime.getName());
        assertNotNull(savedAnime.getUrl());
        assertEquals(savedAnime.getName(), anime.getName());
    }

    @Test
    void saveAnime_Failed_EmptyName() {
        Anime anime = new Anime();
        assertThrows(ConstraintViolationException.class, () -> this.animeRepository.save(anime));
    }

    @Test
    void updateAnime_Success() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);

        savedAnime.setName("NewTest");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        assertNotNull(savedAnime);
        assertNotNull(savedAnime.getId());
        assertNotNull(savedAnime.getName());
        assertNotNull(savedAnime.getUrl());
        assertEquals(updatedAnime.getName(), savedAnime.getName());
    }

    @Test
    void deleteAnime_Success() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);

        this.animeRepository.delete(savedAnime);

        Optional<Anime> optionalAnime = this.animeRepository.findById(savedAnime.getId());

        assertFalse(optionalAnime.isPresent());
    }

    @Test
    void findByNameAnime_Success() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);

        List<Anime> animesByName = this.animeRepository.findByName(savedAnime.getName());

        assertFalse(animesByName.isEmpty());
        assertTrue(animesByName.contains(savedAnime));
    }

    @Test
    void findByNameAnime_Failed() {
        Anime anime = createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);

        List<Anime> animesByName = this.animeRepository.findByName("NoName");

        assertTrue(animesByName.isEmpty());
    }
}