package org.tlauncher.tlauncher.ui.modpack;

import by.gdev.http.download.service.FileCacheService;
import ch.qos.logback.core.CoreConstants;
import com.google.inject.Injector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.icon.ImageIconPicturePosition;
import org.tlauncher.tlauncher.ui.swing.renderer.PictureListRenderer;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/PicturePanel.class */
public class PicturePanel extends ExtendedPanel {
    private JButton previousPicture;
    private JButton nextPicture;
    private int current;
    private LocalizableLabel downloadingLabel;
    private ExecutorService executorService;
    private JList<ImageIcon> list = new JList<>();
    private final int WIDHT_BUTTON = 64;
    private Dimension buttonSize = new Dimension(64, (int) TarConstants.PREFIXLEN);
    private List<ImageIconPicturePosition> cache = Collections.synchronizedList(new ArrayList());

    public PicturePanel(final GameEntityDTO g, final GameType type) {
        Injector inj = TLauncher.getInjector();
        final ModpackManager m = (ModpackManager) inj.getInstance(ModpackManager.class);
        this.executorService = (ExecutorService) inj.getInstance(ExecutorService.class);
        final FileCacheService fileCacheService = (FileCacheService) inj.getInstance(FileCacheService.class);
        setPreferredSize(new Dimension(1050, 318));
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setOpaque(false);
        this.previousPicture = new ImageUdaterButton(Color.WHITE, Color.WHITE, "previous-arrow.png", "previous-arrow-under.png");
        this.nextPicture = new ImageUdaterButton(Color.WHITE, Color.WHITE, "next-arrow.png", "next-arrow-under.png");
        this.downloadingLabel = new LocalizableLabel("loginform.loading");
        this.downloadingLabel.setHorizontalAlignment(0);
        this.downloadingLabel.setVerticalAlignment(0);
        SwingUtil.setFontSize(this.downloadingLabel, 16.0f);
        this.list.setPreferredSize(new Dimension(ModpackScene.SIZE.width - 128, 190));
        this.list.setOpaque(false);
        this.list.setLayoutOrientation(2);
        this.list.setVisibleRowCount(1);
        this.nextPicture.setPreferredSize(this.buttonSize);
        this.previousPicture.setPreferredSize(this.buttonSize);
        this.list.setCellRenderer(new PictureListRenderer());
        this.list.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.PicturePanel.1
            public void mousePressed(MouseEvent e) {
                if (PicturePanel.this.list.getModel().getSize() > 0 && PicturePanel.this.current != -1) {
                    BigPictureObserver pictureObserver = new BigPictureObserver(TLauncher.getInstance().getFrame(), CoreConstants.EMPTY_STRING, ((ImageIconPicturePosition) PicturePanel.this.cache.get(PicturePanel.this.current + PicturePanel.this.list.locationToIndex(e.getPoint()))).getPosition(), type, g);
                    pictureObserver.setVisible(true);
                }
            }
        });
        this.previousPicture.setOpaque(false);
        this.nextPicture.setOpaque(false);
        this.nextPicture.addActionListener(e -> {
            updateNext();
        });
        this.previousPicture.addActionListener(e2 -> {
            updatePrevious();
        });
        add((Component) this.downloadingLabel, "Center");
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.PicturePanel.2
            public void componentShown(ComponentEvent e3) {
                if (!PicturePanel.this.contains(PicturePanel.this.downloadingLabel)) {
                    return;
                }
                ModpackManager modpackManager = m;
                GameType gameType = type;
                GameEntityDTO gameEntityDTO = g;
                FileCacheService fileCacheService2 = fileCacheService;
                CompletableFuture.runAsync(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0030: INVOKE  
                      (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0028: INVOKE  (r0v5 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                      (wrap: java.lang.Runnable : 0x0023: INVOKE_CUSTOM (r0v4 java.lang.Runnable A[REMOVE]) = 
                      (r6v0 'this' org.tlauncher.tlauncher.ui.modpack.PicturePanel$2 A[D('this' org.tlauncher.tlauncher.ui.modpack.PicturePanel$2), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r1v4 'modpackManager' org.tlauncher.tlauncher.managers.ModpackManager A[DONT_INLINE])
                      (r2v1 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                      (r3v1 'gameEntityDTO' org.tlauncher.modpack.domain.client.GameEntityDTO A[DONT_INLINE])
                      (r4v1 'fileCacheService2' by.gdev.http.download.service.FileCacheService A[DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  
                      (r0 I:org.tlauncher.tlauncher.ui.modpack.PicturePanel$2)
                      (r1 I:org.tlauncher.tlauncher.managers.ModpackManager)
                      (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
                      (r3 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                      (r4 I:by.gdev.http.download.service.FileCacheService)
                     type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.PicturePanel.2.lambda$componentShown$2(org.tlauncher.tlauncher.managers.ModpackManager, org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.GameEntityDTO, by.gdev.http.download.service.FileCacheService):void)
                     type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                      (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x002b: INVOKE_CUSTOM (r1v5 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                     handle type: INVOKE_STATIC
                     lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                     call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.modpack.PicturePanel.2.lambda$componentShown$3(java.lang.Throwable):java.lang.Void)
                     type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.PicturePanel.2.componentShown(java.awt.event.ComponentEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/PicturePanel$2.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
                    Caused by: java.lang.IndexOutOfBoundsException: Index 4 out of bounds for length 4
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
                    	... 19 more
                    */
                /*
                    this = this;
                    r0 = r6
                    org.tlauncher.tlauncher.ui.modpack.PicturePanel r0 = org.tlauncher.tlauncher.ui.modpack.PicturePanel.this
                    r1 = r6
                    org.tlauncher.tlauncher.ui.modpack.PicturePanel r1 = org.tlauncher.tlauncher.ui.modpack.PicturePanel.this
                    org.tlauncher.tlauncher.ui.loc.LocalizableLabel r1 = org.tlauncher.tlauncher.ui.modpack.PicturePanel.access$300(r1)
                    boolean r0 = r0.contains(r1)
                    if (r0 != 0) goto L12
                    return
                L12:
                    r0 = r6
                    r1 = r6
                    org.tlauncher.tlauncher.managers.ModpackManager r1 = r5
                    r2 = r6
                    org.tlauncher.modpack.domain.client.share.GameType r2 = r6
                    r3 = r6
                    org.tlauncher.modpack.domain.client.GameEntityDTO r3 = r7
                    r4 = r6
                    by.gdev.http.download.service.FileCacheService r4 = r8
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$componentShown$2(r1, r2, r3, r4);
                    }
                    java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                    void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                        return lambda$componentShown$3(v0);
                    }
                    java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.PicturePanel.AnonymousClass2.componentShown(java.awt.event.ComponentEvent):void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterDownload() {
        SwingUtilities.invokeLater(() -> {
            if (this.cache.size() == 1) {
                this.list.setBorder(BorderFactory.createEmptyBorder(64, (int) HttpStatus.SC_NOT_MODIFIED, 0, 0));
            } else {
                this.list.setBorder(BorderFactory.createEmptyBorder(64, 0, 0, 0));
            }
            if (this.cache.size() > 3) {
                add((Component) this.previousPicture, "West");
                add((Component) this.nextPicture, "East");
            } else {
                setBorder(BorderFactory.createEmptyBorder(0, 64, 0, 64));
            }
            remove(this.downloadingLabel);
            if (this.cache.isEmpty()) {
                JLabel l = new LocalizableLabel("modpack.complete.picture.empty");
                l.setHorizontalAlignment(0);
                l.setVerticalAlignment(0);
                SwingUtil.setFontSize(l, 16.0f);
                add((Component) l, "Center");
            } else {
                add((Component) this.list, "Center");
                updateData();
            }
            revalidate();
            repaint();
        });
    }

    private void updateNext() {
        int enable = (this.cache.size() - 3) - this.current;
        if (enable > 0) {
            this.current++;
            updateData();
        }
    }

    private void updatePrevious() {
        if (this.current > 0) {
            this.current--;
            updateData();
        }
    }

    private void updateData() {
        DefaultListModel<ImageIcon> page = new DefaultListModel<>();
        int i = this.current;
        for (int j = 0; i < this.cache.size() && j < 3; j++) {
            page.addElement(this.cache.get(i));
            i++;
        }
        this.list.setModel(page);
        this.nextPicture.setPreferredSize(this.buttonSize);
        this.previousPicture.setPreferredSize(this.buttonSize);
        repaint();
    }
}
