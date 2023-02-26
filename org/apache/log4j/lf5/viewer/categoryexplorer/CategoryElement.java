package org.apache.log4j.lf5.viewer.categoryexplorer;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/lf5/viewer/categoryexplorer/CategoryElement.class */
public class CategoryElement {
    protected String _categoryTitle;

    public CategoryElement() {
    }

    public CategoryElement(String title) {
        this._categoryTitle = title;
    }

    public String getTitle() {
        return this._categoryTitle;
    }

    public void setTitle(String title) {
        this._categoryTitle = title;
    }
}
