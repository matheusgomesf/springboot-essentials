package study.projects.spring.essentials.util;

import study.projects.spring.essentials.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("Test1")
                .url("http://Test1.com")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1)
                .name("Test1")
                .url("http://Test1.com")
                .build();
    }

    public static Anime createValidUpdatedAnime() {
        return Anime.builder()
                .id(1)
                .name("Test1")
                .url("http://Test1.com")
                .build();
    }
}
