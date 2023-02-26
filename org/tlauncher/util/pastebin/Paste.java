package org.tlauncher.util.pastebin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.launcher.Http;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.util.pastebin.PasteResult;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/pastebin/Paste.class */
public class Paste {
    private static final String DEV_KEY = "394691d8a27e3852a24969dbeba85e53";
    private static final URL POST_URL = Http.constantURL("https://pastebin.com/api/api_post.php");
    private String title;
    private String content;
    private String format;
    private ExpireDate expires = ExpireDate.ONE_WEEK;
    private Visibility visibility = Visibility.NOT_LISTED;
    private final ArrayList<PasteListener> listeners = new ArrayList<>();
    private PasteResult result;

    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getContent() {
        return this.content;
    }

    public final void setContent(String content) {
        this.content = content;
    }

    public final String getFormat() {
        return this.format;
    }

    public final void setFormat(String format) {
        this.format = format;
    }

    public final ExpireDate getExpireDate() {
        return this.expires;
    }

    public final void setExpireDate(ExpireDate date) {
        this.expires = date;
    }

    public final Visibility getVisibility() {
        return this.visibility;
    }

    public final void setVisibility(Visibility vis) {
        this.visibility = vis;
    }

    public void addListener(PasteListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(PasteListener listener) {
        this.listeners.remove(listener);
    }

    public PasteResult paste() {
        Iterator<PasteListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            PasteListener listener = it.next();
            listener.pasteUploading(this);
        }
        try {
            this.result = doPaste();
        } catch (Throwable t) {
            this.result = new PasteResult.PasteFailed(this, t);
        }
        Iterator<PasteListener> it2 = this.listeners.iterator();
        while (it2.hasNext()) {
            PasteListener listener2 = it2.next();
            listener2.pasteDone(this);
        }
        return this.result;
    }

    private PasteResult.PasteUploaded doPaste() throws IOException {
        if (StringUtils.isEmpty(getContent())) {
            throw new IllegalArgumentException("content is empty");
        }
        if (getVisibility() == null) {
            throw new NullPointerException("visibility");
        }
        if (getExpireDate() == null) {
            throw new NullPointerException("expire date");
        }
        HashMap<String, Object> query = new HashMap<>();
        query.put("api_dev_key", DEV_KEY);
        query.put("api_option", "paste");
        query.put("api_paste_name", getTitle());
        query.put("api_paste_code", getContent());
        query.put("api_paste_private", Integer.valueOf(getVisibility().getValue()));
        query.put("api_paste_expire_date", getExpireDate().getValue());
        String answer = Http.performPost(POST_URL, query);
        if (answer.startsWith("http")) {
            return new PasteResult.PasteUploaded(this, new URL(answer));
        }
        throw new IOException("illegal answer: \"" + answer + '\"');
    }
}
