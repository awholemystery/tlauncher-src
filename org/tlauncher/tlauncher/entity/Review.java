package org.tlauncher.tlauncher.entity;

import java.io.File;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/Review.class */
public class Review {
    private String title;
    private String description;
    private String mailReview;
    private String typeReview;
    private List<File> listFiles;

    public Review() {
    }

    public Review(String title, String description, String mailReview, String typeReview, List<File> listFiles) {
        this.title = title;
        this.description = description;
        this.mailReview = mailReview;
        this.typeReview = typeReview;
        this.listFiles = listFiles;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMailReview() {
        return this.mailReview;
    }

    public void setMailReview(String mailReview) {
        this.mailReview = mailReview;
    }

    public String getTypeReview() {
        return this.typeReview;
    }

    public void setTypeReview(String typeReview) {
        this.typeReview = typeReview;
    }

    public List<File> getListFiles() {
        return this.listFiles;
    }

    public void setListFiles(List<File> listFiles) {
        this.listFiles = listFiles;
    }
}
