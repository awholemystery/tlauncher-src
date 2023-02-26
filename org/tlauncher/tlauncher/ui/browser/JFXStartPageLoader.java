package org.tlauncher.tlauncher.ui.browser;

import ch.qos.logback.core.CoreConstants;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tlauncher.util.lang.PageUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/JFXStartPageLoader.class */
public class JFXStartPageLoader extends JFXPanel {
    private String loadPage;
    private static JFXStartPageLoader jfxStartPageLoader;
    private WebView view;
    private long start;

    private JFXStartPageLoader() {
        if (OS.is(OS.LINUX)) {
            try {
                new JFXPanel();
            } catch (NoClassDefFoundError e) {
                Alert.showErrorHtml("alert.error.linux.javafx", (int) HttpStatus.SC_BAD_REQUEST);
                System.exit(1);
            }
        }
        this.loadPage = PageUtil.buildPagePath();
        Platform.runLater(new Runnable() { // from class: org.tlauncher.tlauncher.ui.browser.JFXStartPageLoader.1
            @Override // java.lang.Runnable
            public void run() {
                JFXStartPageLoader.this.view = new WebView();
                JFXStartPageLoader.this.view.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() { // from class: org.tlauncher.tlauncher.ui.browser.JFXStartPageLoader.1.1
                    public /* bridge */ /* synthetic */ void changed(ObservableValue observableValue, Object obj, Object obj2) {
                        changed((ObservableValue<? extends Worker.State>) observableValue, (Worker.State) obj, (Worker.State) obj2);
                    }

                    public void changed(ObservableValue<? extends Worker.State> arg0, Worker.State arg1, Worker.State arg2) {
                        switch (AnonymousClass2.$SwitchMap$javafx$concurrent$Worker$State[arg2.ordinal()]) {
                            case 1:
                                JFXStartPageLoader.this.log("succeeded load page: " + JFXStartPageLoader.this.loadPage + " during " + ((System.currentTimeMillis() - JFXStartPageLoader.this.start) / 1000));
                                return;
                            case 2:
                                JFXStartPageLoader.this.log("fail load page: " + JFXStartPageLoader.this.loadPage);
                                return;
                            case 3:
                                JFXStartPageLoader.this.log("start load page: " + JFXStartPageLoader.this.loadPage);
                                JFXStartPageLoader.this.start = System.currentTimeMillis();
                                return;
                            default:
                                return;
                        }
                    }
                });
                JFXStartPageLoader.this.view.getEngine().load(JFXStartPageLoader.this.loadPage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.tlauncher.tlauncher.ui.browser.JFXStartPageLoader$2  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/JFXStartPageLoader$2.class */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$javafx$concurrent$Worker$State = new int[Worker.State.values().length];

        static {
            try {
                $SwitchMap$javafx$concurrent$Worker$State[Worker.State.SUCCEEDED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javafx$concurrent$Worker$State[Worker.State.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$concurrent$Worker$State[Worker.State.SCHEDULED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static synchronized JFXStartPageLoader getInstance() {
        try {
            if (jfxStartPageLoader == null) {
                jfxStartPageLoader = new JFXStartPageLoader();
            }
        } catch (RuntimeException e) {
            if (e.getMessage().contains("glass.dll")) {
                String url = " https://tlauncher.org/ru/unsatisfiedlinkerror-java-bin-glass.html";
                if (!TLauncher.getInnerSettings().isUSSRLocale()) {
                    url = " https://tlauncher.org/en/unsatisfiedlinkerror-java-bin-glass.html";
                }
                Alert.showErrorHtml(CoreConstants.EMPTY_STRING, Localizable.get("alert.start.message", url));
                TLauncher.kill();
            }
        }
        return jfxStartPageLoader;
    }

    public WebView getWebView() {
        return this.view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(Object... objects) {
        U.log("[JFXStartPageLoader] ", objects);
    }
}
