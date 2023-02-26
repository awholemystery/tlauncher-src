package org.tlauncher.util.git;

import java.util.HashMap;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/git/MapTokenResolver.class */
public class MapTokenResolver implements ITokenResolver {
    protected Map<String, String> tokenMap;

    public MapTokenResolver(Map<String, String> tokenMap) {
        this.tokenMap = new HashMap();
        this.tokenMap = tokenMap;
    }

    @Override // org.tlauncher.util.git.ITokenResolver
    public String resolveToken(String tokenName) {
        return this.tokenMap.get(tokenName);
    }
}
