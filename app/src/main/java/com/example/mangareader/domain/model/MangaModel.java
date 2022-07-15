package com.example.mangareader.domain.model;

import java.io.Serializable;

public class MangaModel implements Serializable {

    private String id;
    private String title;
    private String author;
    private String desc;
    private String status;
    private int year;
    private String createdAt;
    private String updatedAt;
    private String state;
    private String lastVolume;
    private int lastChapter;
    private String publicationDemographic;
    private String mangaCover;

    public MangaModel(String id, String mangaCover){
        this.id = id;
        this.mangaCover = mangaCover;
    }

    public MangaModel(String id, String title, String author, String desc, String status, int year, String state, String createdAt, String updatedAt,  String lastVolume, int lastChapter, String publicationDemographic, String mangaCover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.desc = desc;
        this.status = status;
        this.year = year;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.state = state;
        this.lastVolume = lastVolume;
        this.lastChapter = lastChapter;
        this.publicationDemographic = publicationDemographic;
        this.mangaCover = mangaCover;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLastVolume() {
        return lastVolume;
    }

    public void setLastVolume(String lastVolume) {
        this.lastVolume = lastVolume;
    }

    public int getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(int lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getPublicationDemographic() {
        return publicationDemographic;
    }

    public void setPublicationDemographic(String publicationDemographic) {
        this.publicationDemographic = publicationDemographic;
    }

    public String getMangaCover() {
        return mangaCover;
    }

    public void setMangaCover(String mangaCover) {
        this.mangaCover = mangaCover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
