package org.tlauncher.tlauncher.ui.model;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/model/CurrentUserPosition.class */
public class CurrentUserPosition {
    private volatile byte bad;
    private volatile byte good;

    public void setBad(byte bad) {
        this.bad = bad;
    }

    public void setGood(byte good) {
        this.good = good;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CurrentUserPosition) {
            CurrentUserPosition other = (CurrentUserPosition) o;
            return other.canEqual(this) && getBad() == other.getBad() && getGood() == other.getGood();
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CurrentUserPosition;
    }

    public int hashCode() {
        int result = (1 * 59) + getBad();
        return (result * 59) + getGood();
    }

    public String toString() {
        return "CurrentUserPosition(bad=" + ((int) getBad()) + ", good=" + ((int) getGood()) + ")";
    }

    public byte getBad() {
        return this.bad;
    }

    public byte getGood() {
        return this.good;
    }

    public void update(boolean position, boolean added) {
        if (position) {
            if (added) {
                this.good = (byte) 1;
            } else {
                this.good = (byte) -1;
            }
            this.bad = (byte) 0;
            return;
        }
        if (added) {
            this.bad = (byte) 1;
        } else {
            this.bad = (byte) -1;
        }
        this.good = (byte) 0;
    }

    public byte getByPosition(boolean pos) {
        return pos ? this.good : this.bad;
    }
}
