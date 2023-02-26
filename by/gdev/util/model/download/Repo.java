package by.gdev.util.model.download;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/model/download/Repo.class */
public class Repo {
    private List<String> repositories;
    private List<Metadata> resources;
    private boolean remoteServerSHA1;

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public void setResources(List<Metadata> resources) {
        this.resources = resources;
    }

    public void setRemoteServerSHA1(boolean remoteServerSHA1) {
        this.remoteServerSHA1 = remoteServerSHA1;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Repo) {
            Repo other = (Repo) o;
            if (other.canEqual(this)) {
                Object this$repositories = getRepositories();
                Object other$repositories = other.getRepositories();
                if (this$repositories == null) {
                    if (other$repositories != null) {
                        return false;
                    }
                } else if (!this$repositories.equals(other$repositories)) {
                    return false;
                }
                Object this$resources = getResources();
                Object other$resources = other.getResources();
                if (this$resources == null) {
                    if (other$resources != null) {
                        return false;
                    }
                } else if (!this$resources.equals(other$resources)) {
                    return false;
                }
                return isRemoteServerSHA1() == other.isRemoteServerSHA1();
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Repo;
    }

    public int hashCode() {
        Object $repositories = getRepositories();
        int result = (1 * 59) + ($repositories == null ? 43 : $repositories.hashCode());
        Object $resources = getResources();
        return (((result * 59) + ($resources == null ? 43 : $resources.hashCode())) * 59) + (isRemoteServerSHA1() ? 79 : 97);
    }

    public String toString() {
        return "Repo(repositories=" + getRepositories() + ", resources=" + getResources() + ", remoteServerSHA1=" + isRemoteServerSHA1() + ")";
    }

    public List<String> getRepositories() {
        return this.repositories;
    }

    public List<Metadata> getResources() {
        return this.resources;
    }

    public boolean isRemoteServerSHA1() {
        return this.remoteServerSHA1;
    }
}
