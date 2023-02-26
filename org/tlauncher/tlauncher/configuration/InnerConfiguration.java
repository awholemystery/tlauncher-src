package org.tlauncher.tlauncher.configuration;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/InnerConfiguration.class */
public class InnerConfiguration extends SimpleConfiguration {
    public InnerConfiguration(InputStream in) throws IOException {
        super(in);
    }

    public String getAccessRepoTlauncherOrg(String key) {
        return get(key);
    }

    public String[] getArrayAccess(String key) {
        return getAccessRepoTlauncherOrg(key).split(",");
    }

    public String[] getArrayShuffle(String key) {
        List<String> list = Lists.newArrayList(getArray(key));
        Collections.shuffle(list);
        return (String[]) list.toArray(new String[0]);
    }

    public String[] getArray(String key) {
        return get(key).split(",");
    }
}
