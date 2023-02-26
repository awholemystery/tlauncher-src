package org.tlauncher.tlauncher.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/share/ReviewMessage.class */
public class ReviewMessage implements Serializable {
    private static final long serialVersionUID = 5899496321871739561L;
    private String title;
    private String description;
    private String mailReview;
    private String typeReview;
    private List<String> listNamesOfFiles;

    public ReviewMessage(String title, String description, String mailReview, String typeReview, List<String> listNamesOfFiles) {
        this.title = title;
        this.description = description;
        this.mailReview = mailReview;
        this.typeReview = typeReview;
        this.listNamesOfFiles = listNamesOfFiles;
    }

    public ReviewMessage() {
        this.listNamesOfFiles = new ArrayList();
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

    public List<String> getListNamesOfFiles() {
        return this.listNamesOfFiles;
    }

    public void setListNamesOfFiles(List<String> listNamesOfFiles) {
        this.listNamesOfFiles = listNamesOfFiles;
    }
}
