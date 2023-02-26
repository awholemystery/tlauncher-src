package org.tlauncher.modpack.domain.client.site;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/PageMetadataResponse.class */
public class PageMetadataResponse {
    private PageIndexEnum pageIndexEnum;
    private boolean ready;
    private boolean engTranslation;

    public void setPageIndexEnum(PageIndexEnum pageIndexEnum) {
        this.pageIndexEnum = pageIndexEnum;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setEngTranslation(boolean engTranslation) {
        this.engTranslation = engTranslation;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PageMetadataResponse) {
            PageMetadataResponse other = (PageMetadataResponse) o;
            if (other.canEqual(this) && isReady() == other.isReady() && isEngTranslation() == other.isEngTranslation()) {
                Object this$pageIndexEnum = getPageIndexEnum();
                Object other$pageIndexEnum = other.getPageIndexEnum();
                return this$pageIndexEnum == null ? other$pageIndexEnum == null : this$pageIndexEnum.equals(other$pageIndexEnum);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageMetadataResponse;
    }

    public int hashCode() {
        int result = (1 * 59) + (isReady() ? 79 : 97);
        int result2 = (result * 59) + (isEngTranslation() ? 79 : 97);
        Object $pageIndexEnum = getPageIndexEnum();
        return (result2 * 59) + ($pageIndexEnum == null ? 43 : $pageIndexEnum.hashCode());
    }

    public String toString() {
        return "PageMetadataResponse(pageIndexEnum=" + getPageIndexEnum() + ", ready=" + isReady() + ", engTranslation=" + isEngTranslation() + ")";
    }

    public PageIndexEnum getPageIndexEnum() {
        return this.pageIndexEnum;
    }

    public boolean isReady() {
        return this.ready;
    }

    public boolean isEngTranslation() {
        return this.engTranslation;
    }
}
