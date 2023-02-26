package org.tlauncher.modpack.domain.client.share;

import ch.qos.logback.core.joran.action.Action;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/GameEntitySort.class */
public enum GameEntitySort {
    POPULAR("popularInstalled", "DESC"),
    NAME(Action.NAME_ATTRIBUTE, "ASC"),
    TOTAL_DOWNLOAD("totalInstalled", "DESC"),
    UPDATE("updated", "DESC"),
    VERSION_UPDATE("updateDate", "DESC"),
    FAVORITE("updated", "DESC"),
    LIKE("likeSummary", "DESC");
    
    private String field;
    private String direction;

    GameEntitySort(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return this.field;
    }

    public String getDirection() {
        return this.direction;
    }

    public static GameEntitySort[] getClientSortList() {
        return new GameEntitySort[]{POPULAR, NAME, TOTAL_DOWNLOAD, UPDATE};
    }
}
