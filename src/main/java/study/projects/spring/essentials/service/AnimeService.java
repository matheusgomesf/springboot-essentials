package study.projects.spring.essentials.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.projects.spring.essentials.domain.Anime;
import study.projects.spring.essentials.exception.ResourceNotFoundException;
import study.projects.spring.essentials.repository.AnimeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Page<Anime> pageAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public Anime findById(int id) {
        return getAnimeOrNotFound(id);
    }

    @Transactional
    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public void delete(int id) {
        animeRepository.delete(getAnimeOrNotFound(id));
    }

    private Anime getAnimeOrNotFound(int id) {
        return animeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anime not found"));
    }

    public void update(Anime anime) {
        animeRepository.save(anime);
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }
}
