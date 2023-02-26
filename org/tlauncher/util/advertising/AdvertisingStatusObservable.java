package org.tlauncher.util.advertising;

import java.util.ArrayList;
import java.util.List;
import org.tlauncher.skin.domain.AdvertisingDTO;
import org.tlauncher.tlauncher.entity.profile.ClientProfile;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/advertising/AdvertisingStatusObservable.class */
public class AdvertisingStatusObservable implements Runnable {
    private final ClientProfile clientProfile;
    private final ProfileManager profileManager;
    private List<AdvertisingStatusObserver> listeners = new ArrayList();

    public AdvertisingStatusObservable(ClientProfile clientProfile, ProfileManager profileManager) {
        this.clientProfile = clientProfile;
        this.profileManager = profileManager;
    }

    public void setListeners(List<AdvertisingStatusObserver> listeners) {
        this.listeners = listeners;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AdvertisingStatusObservable) {
            AdvertisingStatusObservable other = (AdvertisingStatusObservable) o;
            if (other.canEqual(this)) {
                Object this$clientProfile = getClientProfile();
                Object other$clientProfile = other.getClientProfile();
                if (this$clientProfile == null) {
                    if (other$clientProfile != null) {
                        return false;
                    }
                } else if (!this$clientProfile.equals(other$clientProfile)) {
                    return false;
                }
                Object this$profileManager = getProfileManager();
                Object other$profileManager = other.getProfileManager();
                if (this$profileManager == null) {
                    if (other$profileManager != null) {
                        return false;
                    }
                } else if (!this$profileManager.equals(other$profileManager)) {
                    return false;
                }
                Object this$listeners = getListeners();
                Object other$listeners = other.getListeners();
                return this$listeners == null ? other$listeners == null : this$listeners.equals(other$listeners);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdvertisingStatusObservable;
    }

    public int hashCode() {
        Object $clientProfile = getClientProfile();
        int result = (1 * 59) + ($clientProfile == null ? 43 : $clientProfile.hashCode());
        Object $profileManager = getProfileManager();
        int result2 = (result * 59) + ($profileManager == null ? 43 : $profileManager.hashCode());
        Object $listeners = getListeners();
        return (result2 * 59) + ($listeners == null ? 43 : $listeners.hashCode());
    }

    public String toString() {
        return "AdvertisingStatusObservable(clientProfile=" + getClientProfile() + ", profileManager=" + getProfileManager() + ", listeners=" + getListeners() + ")";
    }

    public ClientProfile getClientProfile() {
        return this.clientProfile;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public List<AdvertisingStatusObserver> getListeners() {
        return this.listeners;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x00f9, code lost:
        r0.setPremiumAccount(true);
        r7 = r0.getAdvertising();
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 288
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.util.advertising.AdvertisingStatusObservable.run():void");
    }

    public void addListeners(AdvertisingStatusObserver advertisingStatusListerner) {
        this.listeners.add(advertisingStatusListerner);
    }

    public void removeListener(AdvertisingStatusObserver advertisingStatusListerner) {
        this.listeners.remove(advertisingStatusListerner);
    }

    public void notifyObserver(AdvertisingDTO advertisingDTO) {
        for (AdvertisingStatusObserver advertisingStatusObserver : this.listeners) {
            advertisingStatusObserver.advertisingReceived(advertisingDTO);
        }
    }

    private void log(Object o) {
        U.log("[AdvertisingStatusObserver] ", o);
    }
}
