package org.tlauncher.modpack.domain.client.share;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/ParsedElementDTO.class */
public class ParsedElementDTO {
    private boolean parse;
    private long updated;
    private long nextUpdated;

    public void setParse(boolean parse) {
        this.parse = parse;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setNextUpdated(long nextUpdated) {
        this.nextUpdated = nextUpdated;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ParsedElementDTO) {
            ParsedElementDTO other = (ParsedElementDTO) o;
            return other.canEqual(this) && isParse() == other.isParse() && getUpdated() == other.getUpdated() && getNextUpdated() == other.getNextUpdated();
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ParsedElementDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + (isParse() ? 79 : 97);
        long $updated = getUpdated();
        int result2 = (result * 59) + ((int) (($updated >>> 32) ^ $updated));
        long $nextUpdated = getNextUpdated();
        return (result2 * 59) + ((int) (($nextUpdated >>> 32) ^ $nextUpdated));
    }

    public String toString() {
        return "ParsedElementDTO(parse=" + isParse() + ", updated=" + getUpdated() + ", nextUpdated=" + getNextUpdated() + ")";
    }

    public boolean isParse() {
        return this.parse;
    }

    public long getUpdated() {
        return this.updated;
    }

    public long getNextUpdated() {
        return this.nextUpdated;
    }
}
