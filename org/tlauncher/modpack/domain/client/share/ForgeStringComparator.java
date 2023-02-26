package org.tlauncher.modpack.domain.client.share;

import java.util.Comparator;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/ForgeStringComparator.class */
public class ForgeStringComparator implements Comparator<String> {
    @Override // java.util.Comparator
    public int compare(String o2, String o1) {
        int res;
        String[] versions1 = o1.split("\\.");
        String[] versions2 = o2.split("\\.");
        int length = Math.min(versions1.length, versions2.length);
        for (int i = 0; i < length; i++) {
            try {
                res = Integer.valueOf(versions1[i]).compareTo(Integer.valueOf(versions2[i]));
            } catch (NumberFormatException e) {
                res = versions1[i].compareTo(versions2[i]);
            }
            if (res != 0) {
                return res;
            }
        }
        if (versions1.length != versions2.length) {
            if (length == versions1.length) {
                return -1;
            }
            return 1;
        }
        return 0;
    }
}
