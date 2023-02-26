package org.tlauncher.tlauncher.entity;

import java.util.HashSet;
import java.util.Set;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/BackupWorldList.class */
public class BackupWorldList {
    private Set<BackupWorld> worlds = new HashSet();

    public void setWorlds(Set<BackupWorld> worlds) {
        this.worlds = worlds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BackupWorldList) {
            BackupWorldList other = (BackupWorldList) o;
            if (other.canEqual(this)) {
                Object this$worlds = getWorlds();
                Object other$worlds = other.getWorlds();
                return this$worlds == null ? other$worlds == null : this$worlds.equals(other$worlds);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BackupWorldList;
    }

    public int hashCode() {
        Object $worlds = getWorlds();
        int result = (1 * 59) + ($worlds == null ? 43 : $worlds.hashCode());
        return result;
    }

    public String toString() {
        return "BackupWorldList(worlds=" + getWorlds() + ")";
    }

    public Set<BackupWorld> getWorlds() {
        return this.worlds;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/BackupWorldList$BackupWorld.class */
    public static class BackupWorld {
        private String name;
        private String source;
        private String destination;
        private String lastChanged;
        private boolean backup;

        public void setName(String name) {
            this.name = name;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public void setLastChanged(String lastChanged) {
            this.lastChanged = lastChanged;
        }

        public void setBackup(boolean backup) {
            this.backup = backup;
        }

        public String toString() {
            return "BackupWorldList.BackupWorld(name=" + getName() + ", source=" + getSource() + ", destination=" + getDestination() + ", lastChanged=" + getLastChanged() + ", backup=" + isBackup() + ")";
        }

        public BackupWorld(String name, String source, String destination, String lastChanged, boolean backup) {
            this.name = name;
            this.source = source;
            this.destination = destination;
            this.lastChanged = lastChanged;
            this.backup = backup;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof BackupWorld) {
                BackupWorld other = (BackupWorld) o;
                if (other.canEqual(this)) {
                    Object this$name = getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name != null) {
                            return false;
                        }
                    } else if (!this$name.equals(other$name)) {
                        return false;
                    }
                    Object this$source = getSource();
                    Object other$source = other.getSource();
                    if (this$source == null) {
                        if (other$source != null) {
                            return false;
                        }
                    } else if (!this$source.equals(other$source)) {
                        return false;
                    }
                    Object this$destination = getDestination();
                    Object other$destination = other.getDestination();
                    if (this$destination == null) {
                        if (other$destination != null) {
                            return false;
                        }
                    } else if (!this$destination.equals(other$destination)) {
                        return false;
                    }
                    Object this$lastChanged = getLastChanged();
                    Object other$lastChanged = other.getLastChanged();
                    if (this$lastChanged == null) {
                        if (other$lastChanged != null) {
                            return false;
                        }
                    } else if (!this$lastChanged.equals(other$lastChanged)) {
                        return false;
                    }
                    return isBackup() == other.isBackup();
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof BackupWorld;
        }

        public int hashCode() {
            Object $name = getName();
            int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
            Object $source = getSource();
            int result2 = (result * 59) + ($source == null ? 43 : $source.hashCode());
            Object $destination = getDestination();
            int result3 = (result2 * 59) + ($destination == null ? 43 : $destination.hashCode());
            Object $lastChanged = getLastChanged();
            return (((result3 * 59) + ($lastChanged == null ? 43 : $lastChanged.hashCode())) * 59) + (isBackup() ? 79 : 97);
        }

        public String getName() {
            return this.name;
        }

        public String getSource() {
            return this.source;
        }

        public String getDestination() {
            return this.destination;
        }

        public String getLastChanged() {
            return this.lastChanged;
        }

        public boolean isBackup() {
            return this.backup;
        }
    }
}
