package org.tlauncher.tlauncher.updater.client;

import com.google.gson.GsonBuilder;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.launcher.Http;
import org.apache.commons.io.IOUtils;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.gson.serializer.UpdateDeserializer;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Updater.class */
public class Updater {
    private Update update;
    private final List<UpdaterListener> listeners = Collections.synchronizedList(new ArrayList());

    public Update getUpdate() {
        return this.update;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SearchResult findUpdate0() {
        SearchResult result = null;
        log("Requesting an update...");
        List<Throwable> errorList = new ArrayList<>();
        String get = "?version=" + Http.encode(String.valueOf(TLauncher.getVersion())) + "&client=" + Http.encode(TLauncher.getInstance().getConfiguration().getClient().toString());
        Iterator<String> it = getUpdateUrlList().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String updateUrl = it.next();
            long startTime = System.currentTimeMillis();
            log("Requesting from:", updateUrl);
            String response = null;
            try {
                URL url = new URL(updateUrl + get);
                log("Making request:", url);
                HttpURLConnection connection = Downloadable.setUp(url.openConnection(U.getProxy()), true);
                connection.setDoOutput(true);
                response = IOUtils.toString(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
                Update update = (Update) new GsonBuilder().registerTypeAdapter(Update.class, new UpdateDeserializer()).create().fromJson(response, (Class<Object>) Update.class);
                result = new SearchSucceeded(update);
            } catch (Exception e) {
                log("Failed to request from:", updateUrl, e);
                if (response != null) {
                    log("Response:", response);
                }
                result = null;
                errorList.add(e);
            }
            log("Request time:", Long.valueOf(System.currentTimeMillis() - startTime), "ms");
            if (result != null) {
                log("Successfully requested from:", updateUrl);
                break;
            }
        }
        return result == null ? new SearchFailed(errorList) : result;
    }

    private SearchResult findUpdate() {
        try {
            SearchResult result = findUpdate0();
            dispatchResult(result);
            return result;
        } catch (Exception e) {
            log(e);
            return null;
        }
    }

    public void asyncFindUpdate() {
        AsyncThread.execute(() -> {
            findUpdate();
        });
    }

    public void addListener(UpdaterListener l) {
        this.listeners.add(l);
    }

    public void removeListener(UpdaterListener l) {
        this.listeners.remove(l);
    }

    private void dispatchResult(SearchResult result) {
        requireNotNull(result, "result");
        if (result instanceof SearchSucceeded) {
            synchronized (this.listeners) {
                for (UpdaterListener l : this.listeners) {
                    l.onUpdaterSucceeded((SearchSucceeded) result);
                }
            }
        } else if (result instanceof SearchFailed) {
            synchronized (this.listeners) {
                for (UpdaterListener l2 : this.listeners) {
                    l2.onUpdaterErrored((SearchFailed) result);
                }
            }
        } else {
            throw new IllegalArgumentException("unknown result of " + result.getClass());
        }
    }

    protected void onUpdaterRequests() {
        synchronized (this.listeners) {
            for (UpdaterListener l : this.listeners) {
                l.onUpdaterRequesting(this);
            }
        }
    }

    protected List<String> getUpdateUrlList() {
        return Arrays.asList(TLauncher.getUpdateRepos());
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Updater$SearchResult.class */
    public abstract class SearchResult {
        protected final Update response;

        public SearchResult(Update response) {
            this.response = response;
        }

        public final Update getResponse() {
            return this.response;
        }

        public final Updater getUpdater() {
            return Updater.this;
        }

        public String toString() {
            return getClass().getSimpleName() + "{response=" + this.response + "}";
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Updater$SearchSucceeded.class */
    public class SearchSucceeded extends SearchResult {
        public SearchSucceeded(Update response) {
            super((Update) Updater.requireNotNull(response, "response"));
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Updater$SearchFailed.class */
    public class SearchFailed extends SearchResult {
        protected final List<Throwable> errorList;

        public SearchFailed(List<Throwable> list) {
            super(null);
            this.errorList = new ArrayList();
            for (Throwable t : list) {
                if (t == null) {
                    throw new NullPointerException();
                }
            }
            this.errorList.addAll(list);
        }

        public final List<Throwable> getCauseList() {
            return this.errorList;
        }

        @Override // org.tlauncher.tlauncher.updater.client.Updater.SearchResult
        public String toString() {
            return getClass().getSimpleName() + "{errors=" + this.errorList + "}";
        }
    }

    protected void log(Object... o) {
        U.log("[Updater]", o);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T requireNotNull(T obj, String name) {
        if (obj == null) {
            throw new NullPointerException(name);
        }
        return obj;
    }
}
