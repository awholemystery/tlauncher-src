package org.tlauncher.tlauncher.updater.client;

import ch.qos.logback.core.joran.action.Action;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.util.IntegerArray;
import org.tlauncher.util.Reflect;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Notices.class */
public class Notices {
    private final Map<String, NoticeList> map = new HashMap();
    private final Map<String, NoticeList> unmodifiable = Collections.unmodifiableMap(this.map);

    public final Map<String, NoticeList> getMap() {
        return this.unmodifiable;
    }

    protected final Map<String, NoticeList> map() {
        return this.map;
    }

    public final NoticeList getByName(String name) {
        return this.map.get(name);
    }

    protected void add(NoticeList list) {
        if (list == null) {
            throw new NullPointerException("list");
        }
        this.map.put(list.name, list);
    }

    protected void add(String listName, Notice notice) {
        if (notice == null) {
            throw new NullPointerException("notice");
        }
        NoticeList list = this.map.get(listName);
        boolean add = list == null;
        if (add) {
            list = new NoticeList(listName);
        }
        list.add(notice);
        if (add) {
            add(list);
        }
    }

    public String toString() {
        return getClass().getSimpleName() + this.map;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Notices$NoticeList.class */
    public static class NoticeList {
        private final String name;
        private final List<Notice> list = new ArrayList();
        private final List<Notice> unmodifiable = Collections.unmodifiableList(this.list);
        private final Notice[] chances = new Notice[100];
        private int totalChance = 0;

        public NoticeList(String name) {
            if (name == null) {
                throw new NullPointerException(Action.NAME_ATTRIBUTE);
            }
            if (name.isEmpty()) {
                throw new IllegalArgumentException("name is empty");
            }
            this.name = name;
        }

        public final String getName() {
            return this.name;
        }

        public final List<Notice> getList() {
            return this.unmodifiable;
        }

        protected final List<Notice> list() {
            return this.list;
        }

        public final Notice getRandom() {
            return this.chances[new Random().nextInt(100)];
        }

        protected void add(Notice notice) {
            if (notice == null) {
                throw new NullPointerException();
            }
            if (this.totalChance + notice.chance > 100) {
                throw new IllegalArgumentException("chance overflow: " + (this.totalChance + notice.chance));
            }
            this.list.add(notice);
            Arrays.fill(this.chances, this.totalChance, this.totalChance + notice.chance, notice);
            this.totalChance += notice.chance;
        }

        public String toString() {
            return getClass().getSimpleName() + list();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Notices$Notice.class */
    public static class Notice {
        private String content;
        private int chance = 100;
        private NoticeType type = NoticeType.NOTICE;
        private int[] size = new int[2];
        private String image;

        public final int getChance() {
            return this.chance;
        }

        public final void setChance(int chance) {
            if (chance < 1 || chance > 100) {
                throw new IllegalArgumentException("illegal chance: " + chance);
            }
            this.chance = chance;
        }

        public final String getContent() {
            return this.content;
        }

        public final void setContent(String content) {
            if (StringUtils.isBlank(content)) {
                throw new IllegalArgumentException("content is empty or is null");
            }
            this.content = content;
        }

        public final NoticeType getType() {
            return this.type;
        }

        public final void setType(NoticeType type) {
            this.type = type;
        }

        public final int[] getSize() {
            return (int[]) this.size.clone();
        }

        public final void setSize(int[] size) {
            if (size == null) {
                throw new NullPointerException();
            }
            if (size.length != 2) {
                throw new IllegalArgumentException("illegal length");
            }
            setWidth(size[0]);
            setHeight(size[1]);
        }

        public final int getWidth() {
            return this.size[0];
        }

        public final void setWidth(int width) {
            if (width < 1) {
                throw new IllegalArgumentException("width must be greater than 0");
            }
            this.size[0] = width;
        }

        public final int getHeight() {
            return this.size[1];
        }

        public final void setHeight(int height) {
            if (height < 1) {
                throw new IllegalArgumentException("height must be greater than 0");
            }
            this.size[1] = height;
        }

        public final String getImage() {
            return this.image;
        }

        public final void setImage(String image) {
            this.image = StringUtils.isBlank(image) ? null : Notices.parseImage(image);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(getClass().getSimpleName()).append("{").append("size=").append(this.size[0]).append('x').append(this.size[1]).append(';').append("chance=").append(this.chance).append(';').append("content=\"");
            if (this.content.length() < 50) {
                builder.append(this.content);
            } else {
                builder.append(this.content.substring(0, 46)).append("...");
            }
            builder.append("\";").append("image=");
            if (this.image != null && this.image.length() > 24) {
                builder.append(this.image.substring(0, 22)).append("...");
            } else {
                builder.append(this.image);
            }
            builder.append('}');
            return builder.toString();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Notices$NoticeType.class */
    public enum NoticeType {
        NOTICE(false),
        WARNING(false),
        AD_SERVER,
        AD_YOUTUBE,
        AD_OTHER;
        
        private final boolean advert;

        NoticeType(boolean advert) {
            this.advert = advert;
        }

        NoticeType() {
            this(true);
        }

        public boolean isAdvert() {
            return this.advert;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String parseImage(String image) {
        if (image == null) {
            return null;
        }
        if (image.startsWith("data:image")) {
            return image;
        }
        URL url = ImageCache.getRes(image);
        if (url == null) {
            return null;
        }
        return url.toString();
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Notices$Deserializer.class */
    public static class Deserializer implements JsonDeserializer<Notices> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public Notices deserialize(JsonElement root, Type type, JsonDeserializationContext context) throws JsonParseException {
            try {
                return deserialize0(root);
            } catch (Exception e) {
                U.log("Cannot parse notices:", e);
                return new Notices();
            }
        }

        private Notices deserialize0(JsonElement root) throws JsonParseException {
            Notices notices = new Notices();
            JsonObject rootObject = root.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
                String listName = entry.getKey();
                JsonArray ntArray = entry.getValue().getAsJsonArray();
                Iterator<JsonElement> it = ntArray.iterator();
                while (it.hasNext()) {
                    JsonElement elem = it.next();
                    JsonObject ntObj = elem.getAsJsonObject();
                    if (ntObj.has(ClientCookie.VERSION_ATTR)) {
                        String version = ntObj.get(ClientCookie.VERSION_ATTR).getAsString();
                        Pattern pattern = Pattern.compile(version);
                        if (pattern.matcher(String.valueOf(TLauncher.getVersion())).matches()) {
                        }
                    }
                    Notice notice = new Notice();
                    notice.setContent(ntObj.get("content").getAsString());
                    notice.setSize(IntegerArray.parseIntegerArray(ntObj.get("size").getAsString(), 'x').toArray());
                    if (ntObj.has("chance")) {
                        notice.setChance(ntObj.get("chance").getAsInt());
                    }
                    if (ntObj.has("type")) {
                        notice.setType((NoticeType) Reflect.parseEnum(NoticeType.class, ntObj.get("type").getAsString()));
                    }
                    if (ntObj.has("image")) {
                        notice.setImage(ntObj.get("image").getAsString());
                    }
                    notices.add(listName, notice);
                }
            }
            if (notices.getByName("uk_UA") == null && notices.getByName("ru_RU") != null) {
                for (Notice notice2 : notices.getByName("ru_RU").getList()) {
                    notices.add("uk_UA", notice2);
                }
            }
            return notices;
        }
    }
}
