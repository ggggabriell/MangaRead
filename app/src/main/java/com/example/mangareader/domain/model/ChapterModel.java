package com.example.mangareader.domain.model;

import java.io.Serializable;
import java.util.List;

public class ChapterModel implements Serializable {
    private String id;
    private String volume;
    private String chapter;
    private String title;
    private int pages;
    private List<String> pagesUrl;

    public ChapterModel(String id, String volume, String chapter, String title, int pages) {
        this.id = id;
        this.volume = volume;
        this.chapter = chapter;
        this.title = title;
        this.pages = pages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<String> getPagesUrl() {
        return pagesUrl;
    }

    public void setPagesUrl(List<String> pagesUrl) {
        this.pagesUrl = pagesUrl;
    }
}
