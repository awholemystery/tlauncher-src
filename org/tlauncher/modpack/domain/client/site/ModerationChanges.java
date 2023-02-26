package org.tlauncher.modpack.domain.client.site;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/ModerationChanges.class */
public class ModerationChanges {
    private List<String> gameEntityChanges;
    private List<List<String>> versionChanges;

    public void setGameEntityChanges(List<String> gameEntityChanges) {
        this.gameEntityChanges = gameEntityChanges;
    }

    public void setVersionChanges(List<List<String>> versionChanges) {
        this.versionChanges = versionChanges;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModerationChanges) {
            ModerationChanges other = (ModerationChanges) o;
            if (other.canEqual(this)) {
                Object this$gameEntityChanges = getGameEntityChanges();
                Object other$gameEntityChanges = other.getGameEntityChanges();
                if (this$gameEntityChanges == null) {
                    if (other$gameEntityChanges != null) {
                        return false;
                    }
                } else if (!this$gameEntityChanges.equals(other$gameEntityChanges)) {
                    return false;
                }
                Object this$versionChanges = getVersionChanges();
                Object other$versionChanges = other.getVersionChanges();
                return this$versionChanges == null ? other$versionChanges == null : this$versionChanges.equals(other$versionChanges);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ModerationChanges;
    }

    public int hashCode() {
        Object $gameEntityChanges = getGameEntityChanges();
        int result = (1 * 59) + ($gameEntityChanges == null ? 43 : $gameEntityChanges.hashCode());
        Object $versionChanges = getVersionChanges();
        return (result * 59) + ($versionChanges == null ? 43 : $versionChanges.hashCode());
    }

    public String toString() {
        return "ModerationChanges(gameEntityChanges=" + getGameEntityChanges() + ", versionChanges=" + getVersionChanges() + ")";
    }

    public List<String> getGameEntityChanges() {
        return this.gameEntityChanges;
    }

    public List<List<String>> getVersionChanges() {
        return this.versionChanges;
    }
}
