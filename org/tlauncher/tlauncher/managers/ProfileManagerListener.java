package org.tlauncher.tlauncher.managers;

import org.tlauncher.tlauncher.entity.profile.ClientProfile;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ProfileManagerListener.class */
public interface ProfileManagerListener {
    void fireRefreshed(ClientProfile clientProfile);

    void fireClientProfileChanged(ClientProfile clientProfile);
}
