package org.tlauncher.util.advertising;

import javax.xml.bind.annotation.XmlElement;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/advertising/AdvertisingRequestDTO.class */
public class AdvertisingRequestDTO {
    @XmlElement
    private String accessToken;
    @XmlElement
    private String clientToken;

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientToken() {
        return this.clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }
}
