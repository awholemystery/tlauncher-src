package org.tlauncher.util.gson.serializer;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.modpack.domain.client.share.JavaEnum;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/gson/serializer/ElementCollectionsPool.class */
public class ElementCollectionsPool {
    private static final Map<String, String> stringElements = new HashMap();
    private static final Map<Set<String>, Set<String>> set = new HashMap();
    private static final Map<List<JavaEnum>, List<JavaEnum>> javaEnums = new HashMap();
    private static final Map<List<CategoryDTO>, List<CategoryDTO>> categories = new HashMap();

    public static void fill(GameEntityDTO en) {
        if (en.getVersions() == null) {
            return;
        }
        for (VersionDTO v : en.getVersions()) {
            if (v.getGameVersions() != null) {
                Set<String> games = new HashSet<>();
                for (String s : v.getGameVersions()) {
                    if (!stringElements.containsKey(s)) {
                        stringElements.put(s, s);
                        s = stringElements.get(s);
                    }
                    games.add(stringElements.get(s));
                }
                if (set.containsKey(games)) {
                    v.setGameVersions(Lists.newArrayList(set.get(games)));
                } else {
                    v.setGameVersions(Lists.newArrayList(games));
                    set.put(games, games);
                }
            }
            List<JavaEnum> java = v.getJavaVersions();
            if (java != null) {
                if (javaEnums.containsKey(java)) {
                    v.setJavaVersions(javaEnums.get(java));
                } else {
                    javaEnums.put(java, java);
                }
            }
            List<CategoryDTO> listCategories = en.getCategories();
            listCategories.removeIf(e -> {
                return Objects.isNull(e);
            });
            if (listCategories != null) {
                if (categories.containsKey(listCategories)) {
                    en.setCategories(categories.get(listCategories));
                } else {
                    categories.put(listCategories, listCategories);
                }
            }
        }
    }
}
