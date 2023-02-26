package org.tlauncher.tlauncher.ui.scenes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.entity.hot.AdditionalHotServer;
import org.tlauncher.tlauncher.entity.hot.HotBanner;
import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.BackgroundPanel;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.button.RoundImageButton;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.menu.PopupMenuView;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene.class */
public class AdditionalHostServerScene extends PseudoScene {
    private static final long serialVersionUID = 8975208936840346013L;
    private final PopupMenuView popupMenuView;
    private final HotServerManager hotServerManager;
    private final Integer rowHeight;
    private boolean loadOnce;
    private JTable table;
    private JButton back;
    private JLabel serverTitle;

    public AdditionalHostServerScene(MainPane mp) {
        super(mp);
        this.rowHeight = 48;
        this.hotServerManager = (HotServerManager) TLauncher.getInjector().getInstance(HotServerManager.class);
        JPanel panel = new BackgroundPanel("hot/hot-servers-background.png");
        panel.setSize(MainPane.SIZE.width, MainPane.SIZE.height);
        panel.setLocation(0, 0);
        add((Component) panel);
        this.popupMenuView = new PopupMenuView(this);
        this.back = new RoundImageButton(ImageCache.getImage("hot/back.png"), ImageCache.getImage("hot/back-active.png"));
        this.back.setBorder(BorderFactory.createEmptyBorder());
        this.serverTitle = new LocalizableLabel("server.page.title");
        this.table = new JTable();
        this.table.setRowHeight(this.rowHeight.intValue());
        this.table.setBorder(BorderFactory.createEmptyBorder());
        this.table.setOpaque(false);
        this.table.setShowGrid(false);
        this.table.getColumnModel().setColumnSelectionAllowed(true);
        this.table.setIntercellSpacing(new Dimension(0, 0));
        SwingUtil.changeFontFamily(this.serverTitle, FontTL.ROBOTO_MEDIUM, 20);
        this.serverTitle.setForeground(Color.WHITE);
        add((Component) this.back);
        add((Component) this.serverTitle);
        add((Component) this.table);
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.1
            public void componentShown(ComponentEvent e) {
                AdditionalHostServerScene.this.prepare();
            }
        });
        this.back.addActionListener(e -> {
            mp.setScene(mp.defaultScene);
        });
        RollOverListener lst = new RollOverListener(this.table);
        this.table.addMouseMotionListener(lst);
        this.table.addMouseListener(lst);
        this.popupMenuView.addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.2
            public void componentHidden(ComponentEvent e2) {
                AdditionalHostServerScene.this.table.clearSelection();
            }
        });
    }

    public void prepare() {
        prepareRemoteServerData(this.hotServerManager.getAdditionalHotServers().getList());
        if (this.loadOnce) {
            return;
        }
        this.loadOnce = true;
        int rows = this.hotServerManager.getAdditionalHotServers().getList().size();
        this.table.setSize(672, rows * this.rowHeight.intValue());
        this.back.setSize(new Dimension(187, 30));
        this.serverTitle.setSize(new Dimension((int) HttpStatus.SC_OK, 24));
        this.table.setLocation(187, (MainPane.SIZE.height - (rows * this.rowHeight.intValue())) / 2);
        this.back.setLocation((int) HttpStatus.SC_OK, this.table.getLocation().y - 26);
        this.serverTitle.setLocation(630, this.table.getLocation().y - 24);
        this.table.setModel(new TableModel(this.hotServerManager.getAdditionalHotServers().getList()));
        this.table.getColumnModel().getColumn(0).setPreferredWidth(632);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(40);
        this.table.getColumnModel().getColumn(0).setCellRenderer(new AdditionalServerRenderer());
        this.table.getColumnModel().getColumn(1).setCellRenderer(new Column1Renderer());
        AsyncThread.execute(() -> {
            try {
                HotBanner up = this.hotServerManager.getAdditionalHotServers().getUpBanner();
                if (Objects.nonNull(up)) {
                    RoundImageButton upButton = new RoundImageButton(up.getImage(), up.getMouseOnImage());
                    SwingUtilities.invokeLater(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002a: INVOKE  
                          (wrap: java.lang.Runnable : 0x0025: INVOKE_CUSTOM (r0v21 java.lang.Runnable A[REMOVE]) = 
                          (r5v0 'this' org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene A[D('this' org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r0v19 'upButton' org.tlauncher.tlauncher.ui.button.RoundImageButton A[D('upButton' org.tlauncher.tlauncher.ui.button.RoundImageButton), DONT_INLINE])
                          (r0v7 'up' org.tlauncher.tlauncher.entity.hot.HotBanner A[D('up' org.tlauncher.tlauncher.entity.hot.HotBanner), DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  
                          (r0 I:org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene)
                          (r1 I:org.tlauncher.tlauncher.ui.button.RoundImageButton)
                          (r2 I:org.tlauncher.tlauncher.entity.hot.HotBanner)
                         type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.lambda$null$2(org.tlauncher.tlauncher.ui.button.RoundImageButton, org.tlauncher.tlauncher.entity.hot.HotBanner):void)
                         type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.lambda$prepare$6():void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene.class
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
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                        	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:302)
                        	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
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
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	... 50 more
                        */
                    /*
                        this = this;
                        r0 = r5
                        org.tlauncher.tlauncher.managers.popup.menu.HotServerManager r0 = r0.hotServerManager     // Catch: java.lang.RuntimeException -> L5d
                        org.tlauncher.tlauncher.entity.hot.AdditionalHotServers r0 = r0.getAdditionalHotServers()     // Catch: java.lang.RuntimeException -> L5d
                        org.tlauncher.tlauncher.entity.hot.HotBanner r0 = r0.getUpBanner()     // Catch: java.lang.RuntimeException -> L5d
                        r6 = r0
                        r0 = r6
                        boolean r0 = java.util.Objects.nonNull(r0)     // Catch: java.lang.RuntimeException -> L5d
                        if (r0 == 0) goto L2d
                        org.tlauncher.tlauncher.ui.button.RoundImageButton r0 = new org.tlauncher.tlauncher.ui.button.RoundImageButton     // Catch: java.lang.RuntimeException -> L5d
                        r1 = r0
                        r2 = r6
                        java.lang.String r2 = r2.getImage()     // Catch: java.lang.RuntimeException -> L5d
                        r3 = r6
                        java.lang.String r3 = r3.getMouseOnImage()     // Catch: java.lang.RuntimeException -> L5d
                        r1.<init>(r2, r3)     // Catch: java.lang.RuntimeException -> L5d
                        r7 = r0
                        r0 = r5
                        r1 = r7
                        r2 = r6
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$null$2(r1, r2);
                        }     // Catch: java.lang.RuntimeException -> L5d
                        javax.swing.SwingUtilities.invokeLater(r0)     // Catch: java.lang.RuntimeException -> L5d
                    L2d:
                        r0 = r5
                        org.tlauncher.tlauncher.managers.popup.menu.HotServerManager r0 = r0.hotServerManager     // Catch: java.lang.RuntimeException -> L5d
                        org.tlauncher.tlauncher.entity.hot.AdditionalHotServers r0 = r0.getAdditionalHotServers()     // Catch: java.lang.RuntimeException -> L5d
                        org.tlauncher.tlauncher.entity.hot.HotBanner r0 = r0.getDownBanner()     // Catch: java.lang.RuntimeException -> L5d
                        r7 = r0
                        r0 = r7
                        boolean r0 = java.util.Objects.nonNull(r0)     // Catch: java.lang.RuntimeException -> L5d
                        if (r0 == 0) goto L5a
                        org.tlauncher.tlauncher.ui.button.RoundImageButton r0 = new org.tlauncher.tlauncher.ui.button.RoundImageButton     // Catch: java.lang.RuntimeException -> L5d
                        r1 = r0
                        r2 = r7
                        java.lang.String r2 = r2.getImage()     // Catch: java.lang.RuntimeException -> L5d
                        r3 = r7
                        java.lang.String r3 = r3.getMouseOnImage()     // Catch: java.lang.RuntimeException -> L5d
                        r1.<init>(r2, r3)     // Catch: java.lang.RuntimeException -> L5d
                        r8 = r0
                        r0 = r5
                        r1 = r8
                        r2 = r7
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$null$4(r1, r2);
                        }     // Catch: java.lang.RuntimeException -> L5d
                        javax.swing.SwingUtilities.invokeLater(r0)     // Catch: java.lang.RuntimeException -> L5d
                    L5a:
                        goto L69
                    L5d:
                        r6 = move-exception
                        r0 = 1
                        java.lang.Object[] r0 = new java.lang.Object[r0]
                        r1 = r0
                        r2 = 0
                        r3 = r6
                        r1[r2] = r3
                        org.tlauncher.util.U.log(r0)
                    L69:
                        r0 = r5
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$null$5();
                        }
                        javax.swing.SwingUtilities.invokeLater(r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.lambda$prepare$6():void");
                });
            }

            private void prepareRemoteServerData(List<AdditionalHotServer> list) {
                for (int i = 0; i < list.size(); i++) {
                    int finalI = i;
                    AsyncThread.execute(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0016: INVOKE  
                          (wrap: java.lang.Runnable : 0x0011: INVOKE_CUSTOM (r0v4 java.lang.Runnable A[REMOVE]) = 
                          (r4v0 'this' org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene A[D('this' org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r5v0 'list' java.util.List<org.tlauncher.tlauncher.entity.hot.AdditionalHotServer> A[D('list' java.util.List<org.tlauncher.tlauncher.entity.hot.AdditionalHotServer>), DONT_INLINE])
                          (r0v2 'finalI' int A[D('finalI' int), DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene), (r1 I:java.util.List), (r2 I:int) type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.lambda$prepareRemoteServerData$7(java.util.List, int):void)
                         type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.prepareRemoteServerData(java.util.List<org.tlauncher.tlauncher.entity.hot.AdditionalHotServer>):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene.class
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                        	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:195)
                        	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
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
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                        	... 21 more
                        */
                    /*
                        this = this;
                        r0 = 0
                        r6 = r0
                    L2:
                        r0 = r6
                        r1 = r5
                        int r1 = r1.size()
                        if (r0 >= r1) goto L1f
                        r0 = r6
                        r7 = r0
                        r0 = r4
                        r1 = r5
                        r2 = r7
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$prepareRemoteServerData$7(r1, r2);
                        }
                        org.tlauncher.util.async.AsyncThread.execute(r0)
                        int r6 = r6 + 1
                        goto L2
                    L1f:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene.prepareRemoteServerData(java.util.List):void");
                }

                public PopupMenuView getPopupMenuView() {
                    return this.popupMenuView;
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene$TableModel.class */
                public class TableModel extends AbstractTableModel {
                    private List<AdditionalHotServer> list;

                    TableModel(List<AdditionalHotServer> list) {
                        this.list = list;
                    }

                    public int getRowCount() {
                        return this.list.size();
                    }

                    public int getColumnCount() {
                        return 2;
                    }

                    public Object getValueAt(int row, int column) {
                        if (column == 0) {
                            return this.list.get(row);
                        }
                        return this.list.get(row).getTMonitoringLink();
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene$AdditionalServerRenderer.class */
                public class AdditionalServerRenderer extends DefaultTableCellRenderer {
                    private AdditionalServerRenderer() {
                    }

                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        JPanel panel;
                        JLabel image;
                        AdditionalHotServer s = (AdditionalHotServer) value;
                        if (Objects.isNull(s)) {
                            return null;
                        }
                        int gap = 0;
                        if (isSelected) {
                            panel = new BackgroundPanel(String.format("hot/%s-hot-servers-active.png", s.getImageNumber()));
                            gap = 1;
                        } else {
                            panel = new BackgroundPanel(String.format("hot/%s-hot-servers.png", s.getImageNumber()));
                        }
                        panel.setBorder(BorderFactory.createEmptyBorder());
                        JLabel shortDesc = new JLabel(s.getShortDescription());
                        JLabel desc = new JLabel(s.getAddDescription());
                        JLabel onlineTitle = new LocalizableLabel("server.page.online");
                        JLabel versionTitle = new LocalizableLabel("version.release");
                        JLabel online = new JLabel(s.getOnline().intValue() == -1 ? "???" : s.getOnline() + "/" + s.getMax());
                        JLabel startVersion = new JLabel(s.getVersionDescription());
                        SpringLayout spring = new SpringLayout();
                        panel.setLayout(spring);
                        if (!isSelected) {
                            if (Objects.nonNull(s.getImage())) {
                                image = new JLabel(new ImageIcon(s.getImage().getScaledInstance(44, 44, 4)));
                            } else {
                                image = new JLabel(ImageCache.getIcon("hot/tl-icon.png"));
                            }
                            spring.putConstraint("West", image, 1, "West", panel);
                            spring.putConstraint("East", image, 45, "West", panel);
                            spring.putConstraint("North", image, 1, "North", panel);
                            spring.putConstraint("South", image, -1, "South", panel);
                            panel.add(image);
                        }
                        spring.putConstraint("West", shortDesc, -564, "East", panel);
                        spring.putConstraint("East", shortDesc, -144, "East", panel);
                        spring.putConstraint("North", shortDesc, gap, "North", panel);
                        spring.putConstraint("South", shortDesc, gap + 24, "North", panel);
                        panel.add(shortDesc);
                        spring.putConstraint("West", desc, -564, "East", panel);
                        spring.putConstraint("East", desc, -144, "East", panel);
                        spring.putConstraint("North", desc, gap + 26, "North", panel);
                        spring.putConstraint("South", desc, (-gap) - 2, "South", panel);
                        panel.add(desc);
                        spring.putConstraint("West", onlineTitle, -142, "East", panel);
                        spring.putConstraint("East", onlineTitle, -62, "East", panel);
                        spring.putConstraint("North", onlineTitle, gap, "North", panel);
                        spring.putConstraint("South", onlineTitle, gap + 24, "North", panel);
                        panel.add(onlineTitle);
                        spring.putConstraint("West", online, -142, "East", panel);
                        spring.putConstraint("East", online, -62, "East", panel);
                        spring.putConstraint("North", online, gap + 26, "North", panel);
                        spring.putConstraint("South", online, gap + 46, "North", panel);
                        panel.add(online);
                        spring.putConstraint("West", versionTitle, -60, "East", panel);
                        spring.putConstraint("East", versionTitle, 0, "East", panel);
                        spring.putConstraint("North", versionTitle, gap, "North", panel);
                        spring.putConstraint("South", versionTitle, gap + 24, "North", panel);
                        panel.add(versionTitle);
                        spring.putConstraint("West", startVersion, -60, "East", panel);
                        spring.putConstraint("East", startVersion, 0, "East", panel);
                        spring.putConstraint("North", startVersion, gap + 26, "North", panel);
                        spring.putConstraint("South", startVersion, gap + 46, "North", panel);
                        panel.add(startVersion);
                        onlineTitle.setHorizontalAlignment(0);
                        versionTitle.setHorizontalAlignment(0);
                        online.setHorizontalAlignment(0);
                        startVersion.setHorizontalAlignment(0);
                        SwingUtil.changeFontFamily(shortDesc, FontTL.ROBOTO_BOLD, 16);
                        SwingUtil.changeFontFamily(desc, FontTL.ROBOTO_REGULAR, 12);
                        SwingUtil.changeFontFamily(onlineTitle, FontTL.ROBOTO_BOLD, 12);
                        SwingUtil.changeFontFamily(versionTitle, FontTL.ROBOTO_BOLD, 12);
                        SwingUtil.changeFontFamily(online, FontTL.ROBOTO_MEDIUM, 12);
                        SwingUtil.changeFontFamily(startVersion, FontTL.ROBOTO_MEDIUM, 12);
                        shortDesc.setForeground(Color.WHITE);
                        desc.setForeground(Color.WHITE);
                        onlineTitle.setForeground(Color.WHITE);
                        versionTitle.setForeground(Color.WHITE);
                        online.setForeground(Color.WHITE);
                        startVersion.setForeground(Color.WHITE);
                        if (isSelected) {
                            return panel;
                        }
                        JPanel jPanel = new JPanel(new BorderLayout(0, 0));
                        jPanel.setBorder(BorderFactory.createEmptyBorder(1, 13, 1, 0));
                        jPanel.add(panel, "Center");
                        jPanel.setOpaque(false);
                        return jPanel;
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene$Column1Renderer.class */
                public class Column1Renderer implements TableCellRenderer {
                    private Column1Renderer() {
                    }

                    private Component get(Object value, boolean active) {
                        JLabel label;
                        if (Objects.isNull(value)) {
                            return new JLabel();
                        }
                        if (active) {
                            label = new JLabel(ImageCache.getIcon("hot/hot-server-site-active.png"));
                        } else {
                            label = new JLabel(ImageCache.getIcon("hot/hot-server-site.png"));
                        }
                        return label;
                    }

                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        return get(value, isSelected);
                    }
                }

                /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AdditionalHostServerScene$RollOverListener.class */
                private class RollOverListener extends MouseInputAdapter {
                    JTable table;

                    RollOverListener(JTable table) {
                        this.table = table;
                    }

                    public void mouseExited(MouseEvent e) {
                        if (!AdditionalHostServerScene.this.popupMenuView.isVisible()) {
                            this.table.clearSelection();
                        }
                    }

                    public void mouseMoved(MouseEvent e) {
                        int row = this.table.rowAtPoint(e.getPoint());
                        int column = this.table.columnAtPoint(e.getPoint());
                        if (this.table.getSelectedRow() != row || this.table.getSelectedColumn() != column) {
                            this.table.changeSelection(row, column, false, false);
                        }
                        if (row == -1 && this.table.getSelectedRow() > -1) {
                            this.table.clearSelection();
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                        int row = this.table.rowAtPoint(e.getPoint());
                        int col = this.table.columnAtPoint(e.getPoint());
                        if (row >= 0 && col >= 0) {
                            Object value = this.table.getModel().getValueAt(row, col);
                            if (col == 1) {
                                if (Objects.nonNull(value)) {
                                    OS.openLink((String) value);
                                }
                            } else if (e.getPoint().getX() < 490.0d) {
                                AdditionalHotServer s = (AdditionalHotServer) value;
                                AdditionalHostServerScene.this.hotServerManager.processingEvent(s.getServerId());
                            }
                        }
                    }
                }
            }
