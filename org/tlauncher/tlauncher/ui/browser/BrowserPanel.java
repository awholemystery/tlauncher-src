package org.tlauncher.tlauncher.ui.browser;

import ch.qos.logback.core.CoreConstants;
import com.google.common.collect.Maps;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.io.Charsets;
import org.launcher.resource.TlauncherResource;
import org.tlauncher.skin.domain.AdvertisingDTO;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.handlers.ExceptionHandler;
import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tlauncher.util.advertising.AdvertisingStatusObserver;
import org.tlauncher.util.lang.PageUtil;
import org.tlauncher.util.statistics.StatisticsUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.html.HTMLAnchorElement;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/BrowserPanel.class */
public class BrowserPanel extends JFXPanel implements LocalizableComponent, Blockable, AdvertisingStatusObserver {
    private static final long serialVersionUID = 5857121254870623943L;
    private static final boolean freeSurf = false;
    final BrowserHolder holder;
    private String failPage;
    private String style;
    private String scripts;
    private String background;
    private String currentDefaultPage;
    private int width;
    private int height;
    private boolean success;
    private int counter;
    private long lastTime;
    private boolean block;
    private Group group;
    private WebView view;
    private WebEngine engine;
    private AdvertisingDTO advertisingDTO;
    private volatile boolean specialFlag = false;
    private final Object advertisingSynchronizedObject = new Object();
    private long start = System.currentTimeMillis();
    final BrowserBridge bridge = new BrowserBridge(this);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BrowserPanel(BrowserHolder h) throws IOException {
        this.holder = h;
        loadResources();
        loadPageSync();
        Platform.runLater(() -> {
            Thread.currentThread().setPriority(10);
            log("Running in JavaFX Thread");
            prepareFX();
            initBrowser();
        });
        setOpaque(false);
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.browser.BrowserPanel.1
            public void componentResized(ComponentEvent e) {
                BrowserPanel.this.width = BrowserPanel.this.holder.getWidth();
                BrowserPanel.this.height = BrowserPanel.this.holder.getHeight();
                Platform.runLater(() -> {
                    if (BrowserPanel.this.view != null) {
                        BrowserPanel.this.view.setPrefSize(BrowserPanel.this.width, BrowserPanel.this.height);
                    }
                });
            }
        });
    }

    private static void log(Object... o) {
        U.log("[Browser]", o);
    }

    private static void engine(WebEngine engine, Object... o) {
        U.log("[Browser@" + engine.hashCode() + "]", o);
    }

    private static void page(WebEngine engine, String text, String page) {
        engine(engine, text + ':', '\"' + page + '\"');
    }

    private synchronized void loadResources() throws IOException {
        log("Loading resources...");
        this.failPage = FileUtil.readStream(TlauncherResource.getResource("fail.html").openStream());
        this.style = FileUtil.readStream(TlauncherResource.getResource("style.css").openStream());
        URL jqueryResource = TlauncherResource.getResource("jquery.js");
        URL pageScriptResource = TlauncherResource.getResource("scripts.js");
        this.scripts = FileUtil.readStream(jqueryResource.openStream()) + '\n' + FileUtil.readStream(pageScriptResource.openStream());
        ImageCache.getRes("plains.png");
        log("Loading background...");
        new Object();
        URL url = TlauncherResource.getResource("background.image.base64.txt");
        this.background = FileUtil.readStream(url.openStream(), Charsets.UTF_8.toString());
        log("Cleaning up after loading:");
        U.gc();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void cleanupResources() {
        this.style = null;
        this.scripts = null;
        this.background = null;
    }

    private void prepareFX() {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Call this method from Platform.runLater()");
        }
        log("Preparing JavaFX...");
        Thread.currentThread().setUncaughtExceptionHandler(ExceptionHandler.getInstance());
        this.group = new Group();
        Scene scene = new Scene(this.group);
        setScene(scene);
    }

    private synchronized void initBrowser() {
        log("Initializing...");
        this.group.getChildren().removeAll(new Node[0]);
        this.view = JFXStartPageLoader.getInstance().getWebView();
        this.view.setContextMenuEnabled(false);
        this.view.setPrefSize(this.width, this.height);
        this.engine = this.view.getEngine();
        WebEngine currentEngine = this.engine;
        this.engine.setOnAlert(event -> {
            Alert.showMessage(currentEngine.getTitle(), (String) event.getData());
        });
        this.success = true;
        this.engine.getLoadWorker().stateProperty().addListener(observable, oldValue, newValue -> {
            try {
                onBrowserStateChanged(currentEngine, newValue);
            } catch (Throwable e) {
                engine(currentEngine, "State change handle error:", e);
            }
        });
        if (this.engine.getLoadWorker().getState() == Worker.State.FAILED && !this.specialFlag) {
            String location = this.engine.getLocation();
            failLoad(location);
        }
        if (this.engine.getLoadWorker().getState() == Worker.State.SUCCEEDED && !this.specialFlag) {
            String location2 = this.engine.getLocation();
            loadSucceeded(location2);
        }
        this.group.getChildren().add(this.view);
    }

    private void failLoad(String location) {
        this.specialFlag = true;
        page(this.engine, "Failed to load", location);
        if (!this.success) {
            this.holder.setBrowserShown("error", false);
            return;
        }
        this.success = false;
        loadContent(this.failPage);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void onBrowserStateChanged(WebEngine engine, Worker.State val) {
        if (this.engine != engine) {
            return;
        }
        if (val == null) {
            throw new NullPointerException("State is NULL!");
        }
        String location = engine.getLocation();
        this.view.setMouseTransparent(true);
        switch (AnonymousClass2.$SwitchMap$javafx$concurrent$Worker$State[val.ordinal()]) {
            case 1:
                page(engine, "Loading", location);
                if (!location.isEmpty() && !location.startsWith(TLauncher.getInstance().getPagePrefix())) {
                    U.debug("[state] scheduled in");
                    URI uri = U.makeURI(location);
                    if (uri == null) {
                        uri = U.fixInvallidLink(location);
                    }
                    OS.openLink(uri);
                    Platform.runLater(() -> {
                        if (this.block) {
                            return;
                        }
                        deniedRedirectedRecursion();
                        String last = getLastEntry().getUrl();
                        page(this.engine, "Last entry", last);
                        loadPage(last);
                    });
                    return;
                }
                return;
            case 2:
                failLoad(location);
                break;
            case 3:
                break;
            default:
                return;
        }
        loadSucceeded(location);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.tlauncher.tlauncher.ui.browser.BrowserPanel$2  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/BrowserPanel$2.class */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$javafx$concurrent$Worker$State = new int[Worker.State.values().length];

        static {
            try {
                $SwitchMap$javafx$concurrent$Worker$State[Worker.State.SCHEDULED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javafx$concurrent$Worker$State[Worker.State.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$concurrent$Worker$State[Worker.State.SUCCEEDED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private void deniedRedirectedRecursion() {
        if (this.counter > 3) {
            if (new Date().before(new Date(this.lastTime + 2000))) {
                this.block = true;
            } else if (this.counter > 3) {
                this.counter = 0;
            }
        }
        this.lastTime = System.currentTimeMillis();
        this.counter++;
    }

    private void loadSucceeded(String location) {
        this.specialFlag = true;
        page(this.engine, "Loaded successfully", location);
        this.holder.setBrowserShown("error", true);
        this.success = true;
        Document document = this.engine.getDocument();
        if (document == null) {
            return;
        }
        NodeList bodies = document.getElementsByTagName("body");
        org.w3c.dom.Node body = bodies.item(0);
        if (body == null) {
            engine(this.engine, "What the...? Couldn't find <body> element!");
            return;
        }
        if (!location.isEmpty()) {
            String locale = TLauncher.getInstance().getConfiguration().getLocale().getLanguage();
            if (!locale.equals(new Locale("zh").getLanguage())) {
                Element styleElement = document.createElement("style");
                styleElement.setAttribute("type", "text/css");
                styleElement.setTextContent(this.style);
                body.appendChild(styleElement);
            }
        }
        Element scriptElement = document.createElement("script");
        scriptElement.setAttribute("type", "text/javascript");
        scriptElement.setTextContent(this.scripts);
        body.appendChild(scriptElement);
        this.engine.executeScript(this.background);
        JSObject jsobj = (JSObject) this.engine.executeScript("window");
        jsobj.setMember("bridge", this.bridge);
        cleanAdvertising(document);
        NodeList linkList = document.getElementsByTagName("a");
        for (int i = 0; i < linkList.getLength(); i++) {
            EventTarget eventTarget = linkList.item(i);
            eventTarget.addEventListener("click", evt -> {
                HTMLAnchorElement anchorElement = evt.getCurrentTarget();
                String href = anchorElement.getHref();
                if (href == null || href.isEmpty() || href.startsWith("javascript:")) {
                    return;
                }
                URI uri = U.makeURI(href);
                if (uri == null) {
                    uri = U.fixInvallidLink(href);
                }
                OS.openLink(uri);
                evt.preventDefault();
            }, false);
        }
        EventTarget elementById = document.getElementById("adyoutube");
        if (elementById != null) {
            elementById.addEventListener("click", evt2 -> {
                StatisticsUtil.startSending("save/adyoutube", null, Maps.newHashMap());
            }, false);
        }
        mixServers(document);
        addListensOnMenuServer(document);
        EventTarget elementById2 = document.getElementById("server_info_page");
        if (elementById2 != null) {
            elementById2.addEventListener("click", evt3 -> {
                if (((HotServerManager) TLauncher.getInjector().getInstance(HotServerManager.class)).isReady()) {
                    MainPane mp = TLauncher.getInstance().getFrame().mp;
                    mp.setScene(mp.additionalHostServerScene);
                }
            }, false);
        }
        processSidebarMessage(document);
        if (Blocker.isBlocked(this)) {
            block(null);
        }
        this.view.setMouseTransparent(false);
    }

    private void processSidebarMessage(Document document) {
        EventTarget elementById = document.getElementById("sidebar_1_button");
        Configuration configuration = TLauncher.getInstance().getConfiguration();
        if (elementById != null) {
            Element el = document.getElementById("sidebar_1");
            if (configuration.getBoolean("main.cross.close")) {
                if (el != null) {
                    removeChilds(el);
                    return;
                }
                return;
            }
            elementById.addEventListener("click", evt -> {
                if (el != null) {
                    removeChilds(el);
                    el.set("main.cross.close", (Object) true);
                }
            }, false);
        }
    }

    private void cleanAdvertising(Document document) {
        synchronized (this.advertisingSynchronizedObject) {
            if (this.advertisingDTO == null) {
                try {
                    log("fx thread has waited status of ad");
                    this.advertisingSynchronizedObject.wait();
                    log("fx thread continuing work");
                } catch (InterruptedException e) {
                    log(String.format("got status of advertising: %s", this.advertisingDTO));
                }
            }
        }
        if (this.advertisingDTO.isInfoServerAdvertising()) {
            removeNode("advertising_block", document);
        }
        if (this.advertisingDTO.isCenterAdvertising()) {
            removeNode("adnew", document);
        }
        if (this.advertisingDTO.isVideoAdvertising()) {
            removeNode("adyoutube", document);
        }
    }

    private void removeNode(String name, Document document) {
        org.w3c.dom.Node node = document.getElementById(name);
        if (node != null) {
            node.getParentNode().removeChild(node);
        }
    }

    private void removeChilds(org.w3c.dom.Node node) {
        while (node.hasChildNodes()) {
            node.removeChild(node.getFirstChild());
        }
    }

    private void mixServers(Document document) {
        NodeList list;
        org.w3c.dom.Node node = document.getElementById("advertising_servers");
        if (node == null || (list = node.getChildNodes()) == null || list.getLength() < 2) {
            return;
        }
        org.w3c.dom.Node element = list.item(1);
        NodeList listRowServer = element.getChildNodes();
        ArrayList<org.w3c.dom.Node> listOld = new ArrayList<>();
        for (int i = 0; i < listRowServer.getLength(); i++) {
            org.w3c.dom.Node el = listRowServer.item(i);
            listOld.add(el);
        }
        removeChilds(element);
        Random random = new Random();
        while (!listOld.isEmpty()) {
            int index = random.nextInt(listOld.size());
            element.appendChild(listOld.remove(index));
        }
    }

    private void addListensOnMenuServer(Document doc) {
        log("add listens into server_choose");
        NodeList list = doc.getElementsByTagName("server");
        for (int i = 0; i < list.getLength(); i++) {
            list.item(i).addEventListener("mouseover", evt -> {
                ((HotServerManager) TLauncher.getInjector().getInstance(HotServerManager.class)).processingEvent(extractServer(evt));
            }, false);
        }
        log("finished listens into server_choose");
    }

    private WebHistory.Entry getLastEntry() {
        WebHistory hist = this.engine.getHistory();
        return (WebHistory.Entry) hist.getEntries().get(hist.getCurrentIndex());
    }

    private String extractServer(Event evt) {
        org.w3c.dom.Node node = evt.getCurrentTarget();
        NamedNodeMap map = node.getAttributes();
        if (map == null || map.getLength() < 2) {
            log("map=" + map + "or map.getLength() <3");
            return CoreConstants.EMPTY_STRING;
        }
        org.w3c.dom.Node idServer = map.item(1);
        if (idServer != null) {
            if (idServer.getNodeValue() != null && !idServer.getNodeValue().isEmpty()) {
                if (evt instanceof MouseEvent) {
                    return idServer.getNodeValue();
                }
                log("problems with your browser");
                return null;
            }
            log("idServer is null or empty");
            return null;
        }
        log("error the node doesn't have childNodes " + node.toString());
        return null;
    }

    private void loadPage(String url) {
        U.debug("load started after init object = " + ((System.currentTimeMillis() - this.start) / 1000));
        engine(this.engine, "Trying to load URL: \"" + url + "\"");
        this.engine.load(url);
    }

    private void loadContent(String content) {
        this.engine.loadContent(content);
    }

    void execute(String script) {
        try {
            this.engine.executeScript(script);
        } catch (Exception e) {
            U.log("Hidden JS exception:", e);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Platform.runLater(() -> {
            execute("page.visibility.hide();");
        });
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Platform.runLater(() -> {
            execute("page.visibility.show();");
        });
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        String oldDefaultPage = this.currentDefaultPage;
        this.currentDefaultPage = PageUtil.buildPagePath();
        if (oldDefaultPage == null || oldDefaultPage.equals(this.currentDefaultPage)) {
            return;
        }
        Platform.runLater(() -> {
            loadPage(this.currentDefaultPage);
        });
    }

    private void loadPageSync() {
        String oldDefaultPage = this.currentDefaultPage;
        this.currentDefaultPage = PageUtil.buildPagePath();
        if (oldDefaultPage == null || oldDefaultPage.equals(this.currentDefaultPage)) {
            return;
        }
        loadPage(this.currentDefaultPage);
    }

    public void setAdvertising(AdvertisingDTO dto) {
        this.advertisingDTO = dto;
    }

    @Override // org.tlauncher.util.advertising.AdvertisingStatusObserver
    public void advertisingReceived(AdvertisingDTO advertisingDTO) {
        synchronized (this.advertisingSynchronizedObject) {
            this.advertisingDTO = advertisingDTO;
            this.advertisingSynchronizedObject.notify();
        }
    }
}
