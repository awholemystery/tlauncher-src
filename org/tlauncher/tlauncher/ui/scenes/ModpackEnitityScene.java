package org.tlauncher.tlauncher.ui.scenes;

import ch.qos.logback.core.CoreConstants;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton;
import org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableInstallButton;
import org.tlauncher.tlauncher.ui.modpack.GroupPanel;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.tlauncher.ui.swing.GameRadioButton;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackEnitityScene.class */
public class ModpackEnitityScene extends CompleteSubEntityScene {
    private static final String EMPTY = "EMPTY";
    private static final String ROWS = "ROWS";

    public ModpackEnitityScene(MainPane main) {
        super(main);
    }

    public void showModpackEntity(GameEntityDTO completeGameEntity) {
        showFullGameEntity(completeGameEntity, GameType.MODPACK);
        AbstractButton gameRadioButton = new GameRadioButton("modpack.complete.review.mod");
        gameRadioButton.setActionCommand(GameType.MOD.toString());
        AbstractButton gameRadioButton2 = new GameRadioButton("modpack.complete.review.resource");
        gameRadioButton2.setActionCommand(GameType.RESOURCEPACK.toString());
        AbstractButton gameRadioButton3 = new GameRadioButton("modpack.complete.review.map");
        gameRadioButton3.setActionCommand(GameType.MAP.toString());
        AbstractButton gameRadioButton4 = new GameRadioButton("modpack.button.shaderpack");
        gameRadioButton4.setActionCommand(GameType.SHADERPACK.toString());
        SwingUtil.changeFontFamily(gameRadioButton3, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(gameRadioButton4, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(gameRadioButton, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(gameRadioButton2, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        GroupPanel centerButtons = this.fullGameEntity.getCenterButtons();
        centerButtons.addInGroup(gameRadioButton, 3);
        centerButtons.addInGroup(gameRadioButton2, 4);
        centerButtons.addInGroup(gameRadioButton3, 5);
        centerButtons.addInGroup(gameRadioButton4, 6);
        final JPanel centerView = this.fullGameEntity.getCenterView();
        GameType.getSubEntities().forEach(t -> {
            final GameEntityTable table = new GameEntityTable(completeGameEntity, centerView);
            ScrollPane createScrollWrapper = ModpackScene.createScrollWrapper(table);
            final JPanel panel = new JPanel();
            CardLayout c = new CardLayout();
            panel.setLayout(c);
            LocalizableLabel emptyLabel = new LocalizableLabel("modpack.table.empty." + centerView.toLowerCase());
            emptyLabel.setHorizontalAlignment(0);
            emptyLabel.setAlignmentX(0.0f);
            SwingUtil.changeFontFamily(emptyLabel, FontTL.ROBOTO_BOLD, 18, ColorUtil.COLOR_16);
            panel.add(createScrollWrapper, ROWS);
            panel.add(emptyLabel, EMPTY);
            emptyLabel.setBounds(0, 160, MainPane.SIZE.width, 22);
            panel.setBounds(0, 0, MainPane.SIZE.width, 321);
            completeGameEntity.add(panel, centerView.toLowerCase());
            panel.addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.1
                public void componentShown(ComponentEvent e) {
                    if (table.getModel().getRowCount() > 0) {
                        return;
                    }
                    GameType gameType = centerView;
                    GameEntityDTO gameEntityDTO = completeGameEntity;
                    JPanel jPanel = panel;
                    GameEntityTable gameEntityTable = table;
                    CompletableFuture.runAsync(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002e: INVOKE  
                          (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0026: INVOKE  (r0v6 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                          (wrap: java.lang.Runnable : 0x0021: INVOKE_CUSTOM (r0v5 java.lang.Runnable A[REMOVE]) = 
                          (r6v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$1 A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$1), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r1v1 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                          (r2v1 'gameEntityDTO' org.tlauncher.modpack.domain.client.GameEntityDTO A[DONT_INLINE])
                          (r3v1 'jPanel' javax.swing.JPanel A[DONT_INLINE])
                          (r4v1 'gameEntityTable' org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable A[DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  
                          (r0 I:org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$1)
                          (r1 I:org.tlauncher.modpack.domain.client.share.GameType)
                          (r2 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                          (r3 I:javax.swing.JPanel)
                          (r4 I:org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable)
                         type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.1.lambda$componentShown$2(org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.GameEntityDTO, javax.swing.JPanel, org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable):void)
                         type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                          (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0029: INVOKE_CUSTOM (r1v2 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                         handle type: INVOKE_STATIC
                         lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                         call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.1.lambda$componentShown$3(java.lang.Throwable):java.lang.Void)
                         type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.1.componentShown(java.awt.event.ComponentEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackEnitityScene$1.class
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
                        org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r0 = r5
                        javax.swing.table.TableModel r0 = r0.getModel()
                        int r0 = r0.getRowCount()
                        if (r0 <= 0) goto L10
                        return
                    L10:
                        r0 = r6
                        r1 = r6
                        org.tlauncher.modpack.domain.client.share.GameType r1 = r6
                        r2 = r6
                        org.tlauncher.modpack.domain.client.GameEntityDTO r2 = r7
                        r3 = r6
                        javax.swing.JPanel r3 = r8
                        r4 = r6
                        org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r4 = r5
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
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.AnonymousClass1.componentShown(java.awt.event.ComponentEvent):void");
                }
            });
        });
        centerView.revalidate();
        centerView.repaint();
        gameRadioButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.2
            public void actionPerformed(ActionEvent e) {
                centerView.getLayout().show(centerView, GameType.MOD.toLowerCase());
            }
        });
        gameRadioButton2.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.3
            public void actionPerformed(ActionEvent e) {
                centerView.getLayout().show(centerView, GameType.RESOURCEPACK.toLowerCase());
            }
        });
        gameRadioButton3.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.4
            public void actionPerformed(ActionEvent e) {
                centerView.getLayout().show(centerView, GameType.MAP.toLowerCase());
            }
        });
        gameRadioButton4.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.5
            public void actionPerformed(ActionEvent e) {
                centerView.getLayout().show(centerView, GameType.SHADERPACK.toLowerCase());
            }
        });
        centerView.getLayout().show(centerView, "REVIEW");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackEnitityScene$RemoteEntityModel.class */
    public class RemoteEntityModel extends BaseSubtypeModel<CompleteSubEntityScene.BaseModelElement> {
        private SimpleDateFormat format;
        private GameType type;

        RemoteEntityModel(GameType type) {
            super();
            this.format = new SimpleDateFormat("dd MMMM YYYY", Localizable.get().getSelected());
            this.type = type;
        }

        public int getRowCount() {
            return this.list.size();
        }

        public int getColumnCount() {
            return 6;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return this.format.format(((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getEntity().getVersion().getUpdateDate());
                case 1:
                    return ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getEntity().getName();
                case 2:
                    return ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getEntity().getAuthor();
                case 3:
                    return ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getEntity().getVersion().getName();
                case 4:
                    return ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getEntity().getVersion().getType();
                case 5:
                    ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getModpackActButton().initButton();
                    return ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getModpackActButton();
                default:
                    return null;
            }
        }

        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    String line = Localizable.get("version.manager.editor.field.time");
                    return line.substring(0, line.length() - 1);
                case 1:
                    return Localizable.get("modpack.table.pack.element.name");
                case 2:
                    return Localizable.get("modpack.table.pack.element.author");
                case 3:
                    return Localizable.get("version.release");
                case 4:
                    String line2 = Localizable.get("version.manager.editor.field.type");
                    return line2.substring(0, line2.length() - 1);
                case 5:
                    return Localizable.get("modpack.table.pack.element.operation");
                default:
                    return CoreConstants.EMPTY_STRING;
            }
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.BaseSubtypeModel
        public GameEntityDTO getRowObject(int rowIndex) {
            return ((CompleteSubEntityScene.BaseModelElement) this.list.get(rowIndex)).getEntity();
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5) {
                return CompleteSubEntityScene.BaseModelElement.class;
            }
            return super.getColumnClass(columnIndex);
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 5;
        }

        public void addElements(List<GameEntityDTO> list) {
            ModpackComboBox modpackComboBox = TLauncher.getInstance().getFrame().mp.modpackScene.localmodpacks;
            for (GameEntityDTO entity : list) {
                this.list.add(new CompleteSubEntityScene.BaseModelElement(new ModpackTableInstallButton(entity, this.type, modpackComboBox), entity));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackEnitityScene$GameEntityTable.class */
    public class GameEntityTable extends CompleteSubEntityScene.ModpackTable {
        GameEntityTable(final GameEntityDTO parent, final GameType type) {
            super(new RemoteEntityModel(type));
            getSelectionModel().addListSelectionListener(new ListSelectionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.1
                public void valueChanged(ListSelectionEvent e) {
                    if (GameEntityTable.this.getSelectedRow() == -1) {
                        return;
                    }
                    int column = GameEntityTable.this.getSelectedColumn();
                    if (column != 5) {
                        GameEntityDTO gameEntity = GameEntityTable.this.getModel().getRowObject(GameEntityTable.this.getSelectedRow());
                        U.log("test12");
                        GameEntityDTO gameEntityDTO = parent;
                        GameType gameType = type;
                        CompletableFuture.runAsync(()
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0051: INVOKE  
                              (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0049: INVOKE  (r0v19 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                              (wrap: java.lang.Runnable : 0x0044: INVOKE_CUSTOM (r0v18 java.lang.Runnable A[REMOVE]) = 
                              (r5v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable$1 A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable$1), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                              (r0v14 'gameEntity' org.tlauncher.modpack.domain.client.GameEntityDTO A[D('gameEntity' org.tlauncher.modpack.domain.client.GameEntityDTO), DONT_INLINE])
                              (r2v2 'gameEntityDTO' org.tlauncher.modpack.domain.client.GameEntityDTO A[DONT_INLINE])
                              (r3v2 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                            
                             handle type: INVOKE_DIRECT
                             lambda: java.lang.Runnable.run():void
                             call insn: ?: INVOKE  
                              (r0 I:org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable$1)
                              (r1 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                              (r2 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                              (r3 I:org.tlauncher.modpack.domain.client.share.GameType)
                             type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.1.lambda$valueChanged$1(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void)
                             type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                              (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x004c: INVOKE_CUSTOM (r1v7 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                             handle type: INVOKE_STATIC
                             lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                             call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.1.lambda$valueChanged$2(java.lang.Throwable):java.lang.Void)
                             type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.1.valueChanged(javax.swing.event.ListSelectionEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackEnitityScene$GameEntityTable$1.class
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
                            Caused by: java.lang.IndexOutOfBoundsException: Index 3 out of bounds for length 3
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
                            	... 25 more
                            */
                        /*
                            this = this;
                            r0 = r5
                            org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r0 = org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.this
                            int r0 = r0.getSelectedRow()
                            r1 = -1
                            if (r0 != r1) goto Lc
                            return
                        Lc:
                            r0 = r5
                            org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r0 = org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.this
                            int r0 = r0.getSelectedColumn()
                            r7 = r0
                            r0 = r7
                            r1 = 5
                            if (r0 == r1) goto L55
                            r0 = r5
                            org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r0 = org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.this
                            javax.swing.table.TableModel r0 = r0.getModel()
                            org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$BaseSubtypeModel r0 = (org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.BaseSubtypeModel) r0
                            r1 = r5
                            org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r1 = org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.this
                            int r1 = r1.getSelectedRow()
                            org.tlauncher.modpack.domain.client.GameEntityDTO r0 = r0.getRowObject(r1)
                            r8 = r0
                            r0 = 1
                            java.lang.Object[] r0 = new java.lang.Object[r0]
                            r1 = r0
                            r2 = 0
                            java.lang.String r3 = "test12"
                            r1[r2] = r3
                            org.tlauncher.util.U.log(r0)
                            r0 = r5
                            r1 = r8
                            r2 = r5
                            org.tlauncher.modpack.domain.client.GameEntityDTO r2 = r6
                            r3 = r5
                            org.tlauncher.modpack.domain.client.share.GameType r3 = r7
                            void r0 = () -> { // java.lang.Runnable.run():void
                                r0.lambda$valueChanged$1(r1, r2, r3);
                            }
                            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                            void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                                return lambda$valueChanged$2(v0);
                            }
                            java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                        L55:
                            r0 = r5
                            org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene$GameEntityTable r0 = org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.this
                            javax.swing.ListSelectionModel r0 = r0.getSelectionModel()
                            r0.clearSelection()
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene.GameEntityTable.AnonymousClass1.valueChanged(javax.swing.event.ListSelectionEvent):void");
                    }
                });
                ModpackEnitityScene.this.manager.addGameListener(type, (GameEntityListener) getModel());
            }

            public void addElements(List<GameEntityDTO> list) {
                RemoteEntityModel rm = getModel();
                rm.addElements(list);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackEnitityScene$BaseSubtypeModel.class */
        public abstract class BaseSubtypeModel<T extends CompleteSubEntityScene.BaseModelElement> extends CompleteSubEntityScene.GameEntityTableModel {
            protected List<T> list;

            public abstract GameEntityDTO getRowObject(int i);

            private BaseSubtypeModel() {
                super();
                this.list = new ArrayList();
            }

            public T find(GameEntityDTO entity) {
                for (T el : this.list) {
                    if (entity.getId().equals(el.getEntity().getId())) {
                        return el;
                    }
                }
                return null;
            }

            @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void processingStarted(GameEntityDTO e, VersionDTO version) {
                CompleteSubEntityScene.BaseModelElement baseModelElement = find(e);
                if (baseModelElement != null) {
                    baseModelElement.getModpackActButton().setTypeButton(ModpackActButton.PROCESSING);
                }
            }

            @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
                CompleteSubEntityScene.BaseModelElement baseModelElement = find(e);
                if (baseModelElement != null) {
                    baseModelElement.getModpackActButton().reset();
                }
            }

            @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void installEntity(GameEntityDTO e, GameType type) {
                CompleteSubEntityScene.BaseModelElement baseModelElement = find(e);
                if (baseModelElement != null) {
                    baseModelElement.getModpackActButton().setTypeButton(ModpackActButton.REMOVE);
                }
            }

            @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void removeEntity(GameEntityDTO e) {
                CompleteSubEntityScene.BaseModelElement baseModelElement = find(e);
                if (baseModelElement != null) {
                    baseModelElement.getModpackActButton().setTypeButton(ModpackActButton.INSTALL);
                }
            }
        }
    }
