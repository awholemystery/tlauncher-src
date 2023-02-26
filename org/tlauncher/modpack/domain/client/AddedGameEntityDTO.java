package org.tlauncher.modpack.domain.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/AddedGameEntityDTO.class */
public class AddedGameEntityDTO {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AddedGameEntityDTO) {
            AddedGameEntityDTO other = (AddedGameEntityDTO) o;
            if (other.canEqual(this)) {
                Object this$url = getUrl();
                Object other$url = other.getUrl();
                return this$url == null ? other$url == null : this$url.equals(other$url);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddedGameEntityDTO;
    }

    public int hashCode() {
        Object $url = getUrl();
        int result = (1 * 59) + ($url == null ? 43 : $url.hashCode());
        return result;
    }

    public String toString() {
        return "AddedGameEntityDTO(url=" + getUrl() + ")";
    }

    public String getUrl() {
        return this.url;
    }
}
