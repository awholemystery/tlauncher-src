package org.tlauncher.tlauncher.ui.button;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.apache.http.client.ClientProtocolException;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.listener.BlockClickListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/button/StatusStarButton.class */
public class StatusStarButton extends JLabel {
    private static final long serialVersionUID = 8192841246854925487L;
    private volatile boolean status;
    private ModpackManager manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
    private static final Object OBJECT = new Object();
    private GameEntityDTO entityDTO;

    public StatusStarButton(final GameEntityDTO entityDTO, final GameType type) {
        this.entityDTO = entityDTO;
        updateStatus();
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.button.StatusStarButton.1
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    GameEntityDTO gameEntityDTO = entityDTO;
                    GameType gameType = type;
                    CompletableFuture.runAsync(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0027: INVOKE  
                          (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x001f: INVOKE  (r0v4 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                          (wrap: java.lang.Runnable : 0x0010: INVOKE_CUSTOM (r0v3 java.lang.Runnable A[REMOVE]) = 
                          (r4v0 'this' org.tlauncher.tlauncher.ui.button.StatusStarButton$1 A[D('this' org.tlauncher.tlauncher.ui.button.StatusStarButton$1), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r1v1 'gameEntityDTO' org.tlauncher.modpack.domain.client.GameEntityDTO A[DONT_INLINE])
                          (r2v1 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  
                          (r0 I:org.tlauncher.tlauncher.ui.button.StatusStarButton$1)
                          (r1 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                          (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
                         type: DIRECT call: org.tlauncher.tlauncher.ui.button.StatusStarButton.1.lambda$mousePressed$1(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void)
                          (wrap: java.util.concurrent.ExecutorService : 0x001c: INVOKE  (r1v5 java.util.concurrent.ExecutorService A[REMOVE]) = 
                          (wrap: org.tlauncher.tlauncher.managers.ModpackManager : 0x0019: IGET  (r1v4 org.tlauncher.tlauncher.managers.ModpackManager A[REMOVE]) = 
                          (wrap: org.tlauncher.tlauncher.ui.button.StatusStarButton : 0x0016: IGET  (r1v3 org.tlauncher.tlauncher.ui.button.StatusStarButton A[REMOVE]) = 
                          (r4v0 'this' org.tlauncher.tlauncher.ui.button.StatusStarButton$1 A[D('this' org.tlauncher.tlauncher.ui.button.StatusStarButton$1), IMMUTABLE_TYPE, THIS])
                         org.tlauncher.tlauncher.ui.button.StatusStarButton.1.this$0 org.tlauncher.tlauncher.ui.button.StatusStarButton)
                         org.tlauncher.tlauncher.ui.button.StatusStarButton.manager org.tlauncher.tlauncher.managers.ModpackManager)
                         type: VIRTUAL call: org.tlauncher.tlauncher.managers.ModpackManager.getModpackExecutorService():java.util.concurrent.ExecutorService)
                         type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable, java.util.concurrent.Executor):java.util.concurrent.CompletableFuture)
                          (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0022: INVOKE_CUSTOM (r1v6 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                         handle type: INVOKE_STATIC
                         lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                         call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.button.StatusStarButton.1.lambda$mousePressed$3(java.lang.Throwable):java.lang.Void)
                         type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.button.StatusStarButton.1.mousePressed(java.awt.event.MouseEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/button/StatusStarButton$1.class
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                        	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
                        Caused by: java.lang.IndexOutOfBoundsException: Index 2 out of bounds for length 2
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
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	... 23 more
                        */
                    /*
                        this = this;
                        r0 = r5
                        boolean r0 = javax.swing.SwingUtilities.isLeftMouseButton(r0)
                        if (r0 == 0) goto L2b
                        r0 = r4
                        r1 = r4
                        org.tlauncher.modpack.domain.client.GameEntityDTO r1 = r5
                        r2 = r4
                        org.tlauncher.modpack.domain.client.share.GameType r2 = r6
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$mousePressed$1(r1, r2);
                        }
                        r1 = r4
                        org.tlauncher.tlauncher.ui.button.StatusStarButton r1 = org.tlauncher.tlauncher.ui.button.StatusStarButton.this
                        org.tlauncher.tlauncher.managers.ModpackManager r1 = org.tlauncher.tlauncher.ui.button.StatusStarButton.access$000(r1)
                        java.util.concurrent.ExecutorService r1 = r1.getModpackExecutorService()
                        java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0, r1)
                        void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                            return lambda$mousePressed$3(v0);
                        }
                        java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                    L2b:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.button.StatusStarButton.AnonymousClass1.mousePressed(java.awt.event.MouseEvent):void");
                }
            });
        }

        public void setStatus(boolean status) {
            this.status = status;
            setIcon(ImageCache.getIcon("star-" + status + ".png"));
        }

        public void updateStatus() {
            setStatus(this.manager.getFavoriteGameEntitiesByAccount().contains(this.entityDTO.getId()));
        }

        public synchronized void addMouseListener(MouseListener l) {
            if (l instanceof BlockClickListener) {
                return;
            }
            super.addMouseListener(l);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void syncSending(GameEntityDTO entityDTO, GameType type) throws ClientProtocolException, IOException, RequiredTLAccountException {
            synchronized (OBJECT) {
                try {
                    try {
                        if (this.status) {
                            this.manager.deleteFavoriteGameEntities(entityDTO, type);
                        } else {
                            this.manager.addFavoriteGameEntities(entityDTO, type);
                        }
                        setStatus(!this.status);
                    } catch (SelectedAnyOneTLAccountException e) {
                        Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (RequiredTLAccountException e3) {
                    Alert.showLocError("modpack.right.panel.required.tl.account.title", Localizable.get("modpack.right.panel.required.tl.account", Localizable.get("loginform.button.settings.account")), null);
                }
            }
        }
    }
