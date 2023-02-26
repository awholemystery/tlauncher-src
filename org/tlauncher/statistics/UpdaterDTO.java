package org.tlauncher.statistics;

import org.codehaus.jackson.annotate.JsonProperty;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/statistics/UpdaterDTO.class */
public class UpdaterDTO {
    private String client;
    private String offer;
    private String country;
    private double currentVersion;
    private double newVersion;
    private String args;
    @JsonProperty("isUpdaterLater")
    private boolean updaterLater;
    private String requestTime;

    public void setClient(String client) {
        this.client = client;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCurrentVersion(double currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void setNewVersion(double newVersion) {
        this.newVersion = newVersion;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setUpdaterLater(boolean updaterLater) {
        this.updaterLater = updaterLater;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof UpdaterDTO) {
            UpdaterDTO other = (UpdaterDTO) o;
            if (other.canEqual(this)) {
                Object this$client = getClient();
                Object other$client = other.getClient();
                if (this$client == null) {
                    if (other$client != null) {
                        return false;
                    }
                } else if (!this$client.equals(other$client)) {
                    return false;
                }
                Object this$offer = getOffer();
                Object other$offer = other.getOffer();
                if (this$offer == null) {
                    if (other$offer != null) {
                        return false;
                    }
                } else if (!this$offer.equals(other$offer)) {
                    return false;
                }
                Object this$country = getCountry();
                Object other$country = other.getCountry();
                if (this$country == null) {
                    if (other$country != null) {
                        return false;
                    }
                } else if (!this$country.equals(other$country)) {
                    return false;
                }
                if (Double.compare(getCurrentVersion(), other.getCurrentVersion()) == 0 && Double.compare(getNewVersion(), other.getNewVersion()) == 0) {
                    Object this$args = getArgs();
                    Object other$args = other.getArgs();
                    if (this$args == null) {
                        if (other$args != null) {
                            return false;
                        }
                    } else if (!this$args.equals(other$args)) {
                        return false;
                    }
                    if (isUpdaterLater() != other.isUpdaterLater()) {
                        return false;
                    }
                    Object this$requestTime = getRequestTime();
                    Object other$requestTime = other.getRequestTime();
                    return this$requestTime == null ? other$requestTime == null : this$requestTime.equals(other$requestTime);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdaterDTO;
    }

    public int hashCode() {
        Object $client = getClient();
        int result = (1 * 59) + ($client == null ? 43 : $client.hashCode());
        Object $offer = getOffer();
        int result2 = (result * 59) + ($offer == null ? 43 : $offer.hashCode());
        Object $country = getCountry();
        int result3 = (result2 * 59) + ($country == null ? 43 : $country.hashCode());
        long $currentVersion = Double.doubleToLongBits(getCurrentVersion());
        int result4 = (result3 * 59) + ((int) (($currentVersion >>> 32) ^ $currentVersion));
        long $newVersion = Double.doubleToLongBits(getNewVersion());
        int result5 = (result4 * 59) + ((int) (($newVersion >>> 32) ^ $newVersion));
        Object $args = getArgs();
        int result6 = (((result5 * 59) + ($args == null ? 43 : $args.hashCode())) * 59) + (isUpdaterLater() ? 79 : 97);
        Object $requestTime = getRequestTime();
        return (result6 * 59) + ($requestTime == null ? 43 : $requestTime.hashCode());
    }

    public String toString() {
        return "UpdaterDTO(client=" + getClient() + ", offer=" + getOffer() + ", country=" + getCountry() + ", currentVersion=" + getCurrentVersion() + ", newVersion=" + getNewVersion() + ", args=" + getArgs() + ", updaterLater=" + isUpdaterLater() + ", requestTime=" + getRequestTime() + ")";
    }

    public String getClient() {
        return this.client;
    }

    public String getOffer() {
        return this.offer;
    }

    public String getCountry() {
        return this.country;
    }

    public double getCurrentVersion() {
        return this.currentVersion;
    }

    public double getNewVersion() {
        return this.newVersion;
    }

    public String getArgs() {
        return this.args;
    }

    public boolean isUpdaterLater() {
        return this.updaterLater;
    }

    public String getRequestTime() {
        return this.requestTime;
    }
}
