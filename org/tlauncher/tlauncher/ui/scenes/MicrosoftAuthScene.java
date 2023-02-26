package org.tlauncher.tlauncher.ui.scenes;

import com.sun.webkit.network.CookieManager;
import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.Field;
import java.net.CookieHandler;
import java.util.Map;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.handlers.ExceptionHandler;
import org.tlauncher.tlauncher.minecraft.user.oauth.OAuthApplication;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/MicrosoftAuthScene.class */
public class MicrosoftAuthScene extends PseudoScene {
    public static final Dimension SIZE = new Dimension(MainPane.SIZE.width, MainPane.SIZE.height);
    private final ExtendedPanel base;
    private WebEngine engine;
    private final MicrosoftBrowerPanel panel;

    public MicrosoftAuthScene(MainPane main) {
        super(main);
        this.base = new ExtendedPanel();
        this.panel = new MicrosoftBrowerPanel();
        this.panel.setPreferredSize(new Dimension(SIZE.getSize().width - 250, SIZE.getSize().height - 80));
        this.base.add((Component) this.panel);
        this.base.setSize(SIZE);
        add((Component) this.base);
    }

    @Override // org.tlauncher.tlauncher.ui.scenes.PseudoScene, org.tlauncher.tlauncher.ui.swing.AnimatedVisibility
    public void setShown(boolean shown) {
        if (shown) {
            Platform.runLater(() -> {
                try {
                    cleanCookie();
                    OAuthApplication m = OAuthApplication.TLAUNCHER_PARAMETERS;
                    String url = String.format("%s?client_id=%s&response_type=code&redirect_uri=%s&scope=%s", m.getBasicURL(), m.getClientId(), m.getRedirectURL(), m.getScope());
                    this.engine.load(url);
                } catch (Throwable e) {
                    U.log(e);
                }
            });
        }
        super.setShown(shown);
    }

    private void cleanCookie() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        CookieManager cookieManager = CookieHandler.getDefault();
        Field f = cookieManager.getClass().getDeclaredField("store");
        f.setAccessible(true);
        Object cookieStore = f.get(cookieManager);
        Field bucketsField = Class.forName("com.sun.webkit.network.CookieStore").getDeclaredField("buckets");
        bucketsField.setAccessible(true);
        Map buckets = (Map) bucketsField.get(cookieStore);
        f.setAccessible(true);
        buckets.clear();
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/MicrosoftAuthScene$MicrosoftBrowerPanel.class */
    private class MicrosoftBrowerPanel extends JFXPanel {
        public MicrosoftBrowerPanel() {
            Platform.runLater(() -> {
                Thread.currentThread().setUncaughtExceptionHandler(ExceptionHandler.getInstance());
                Group group = new Group();
                Scene scene = new Scene(group);
                setScene(scene);
                WebView view = new WebView();
                view.setContextMenuEnabled(false);
                MicrosoftAuthScene.this.engine = view.getEngine();
                MicrosoftAuthScene.this.engine.setOnAlert(event -> {
                    Alert.showMessage(MicrosoftAuthScene.this.engine.getTitle(), (String) event.getData());
                });
                group.getChildren().add(view);
                view.getEngine().locationProperty().addListener(obs, oldLocation, newLocation -> {
                    U.log("new locathion is " + newLocation);
                    SwingUtilities.invokeLater(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0024: INVOKE  
                          (wrap: java.lang.Runnable : 0x001f: INVOKE_CUSTOM (r0v3 java.lang.Runnable A[REMOVE]) = 
                          (r6v0 'this' org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene$MicrosoftBrowerPanel A[D('this' org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene$MicrosoftBrowerPanel), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r9v0 'newLocation' java.lang.String A[D('newLocation' java.lang.String), DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene$MicrosoftBrowerPanel), (r1 I:java.lang.String) type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene.MicrosoftBrowerPanel.lambda$null$1(java.lang.String):void)
                         type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene.MicrosoftBrowerPanel.lambda$null$2(javafx.beans.value.ObservableValue, java.lang.String, java.lang.String):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/MicrosoftAuthScene$MicrosoftBrowerPanel.class
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                        	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:964)
                        	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                        	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:964)
                        	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
                        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
                        	at java.base/java.util.ArrayList.forEach(Unknown Source)
                        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
                        	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
                        Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
                        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
                        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
                        	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
                        	at java.base/java.util.Objects.checkIndex(Unknown Source)
                        	at java.base/java.util.ArrayList.get(Unknown Source)
                        	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
                        	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	... 53 more
                        */
                    /*
                        this = this;
                        r0 = 1
                        java.lang.Object[] r0 = new java.lang.Object[r0]
                        r1 = r0
                        r2 = 0
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder
                        r4 = r3
                        r4.<init>()
                        java.lang.String r4 = "new locathion is "
                        java.lang.StringBuilder r3 = r3.append(r4)
                        r4 = r9
                        java.lang.StringBuilder r3 = r3.append(r4)
                        java.lang.String r3 = r3.toString()
                        r1[r2] = r3
                        org.tlauncher.util.U.log(r0)
                        r0 = r6
                        r1 = r9
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$null$1(r1);
                        }
                        javax.swing.SwingUtilities.invokeLater(r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene.MicrosoftBrowerPanel.lambda$null$2(javafx.beans.value.ObservableValue, java.lang.String, java.lang.String):void");
                });
            });
        }
    }
}
