package org.tlauncher.modpack.domain.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/BasicId.class */
public class BasicId {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BasicId) {
            BasicId other = (BasicId) o;
            if (other.canEqual(this)) {
                Object this$id = getId();
                Object other$id = other.getId();
                return this$id == null ? other$id == null : this$id.equals(other$id);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BasicId;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public String toString() {
        return "BasicId(id=" + getId() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public static BasicId create(Long id) {
        BasicId b = new BasicId();
        b.setId(id);
        return b;
    }
}
