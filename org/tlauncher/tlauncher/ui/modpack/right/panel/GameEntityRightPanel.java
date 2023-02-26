package org.tlauncher.tlauncher.ui.modpack.right.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.listener.BlockClickListener;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
import org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener;
import org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton;
import org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/right/panel/GameEntityRightPanel.class */
public class GameEntityRightPanel extends JTable implements GameEntityListener {
    private final ModpackComboBox localmodpacks;
    private final GameType type;
    private static final int HEIGHT_RIGHT_ELEMENT = 159;
    private boolean nextPage;
    private boolean processingRequest;
    private List<Long> changeableElements = new ArrayList();
    private Integer nextPageIndex = 0;
    int currentSelectedIndex = -1;

    public Integer getNextPageIndex() {
        return this.nextPageIndex;
    }

    public void setNextPageIndex(Integer nextPageIndex) {
        this.nextPageIndex = nextPageIndex;
    }

    public boolean isNextPage() {
        return this.nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isProcessingRequest() {
        return this.processingRequest;
    }

    public void setProcessingRequest(boolean processingRequest) {
        this.processingRequest = processingRequest;
    }

    public GameEntityRightPanel(ModpackComboBox localmodpacks, GameType type) {
        this.localmodpacks = localmodpacks;
        this.type = type;
        setBackground(ColorUtil.COLOR_233);
        setRowHeight(HEIGHT_RIGHT_ELEMENT);
        setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setDefaultEditor(GameRightElement.class, new RightRenderer());
        setDefaultRenderer(GameRightElement.class, new RightRenderer());
        setModel(new RightTableModel());
        MouseAdapter mouse = new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel.1
            public void mouseMoved(MouseEvent e) {
                check(e);
            }

            public void mouseWheelMoved(MouseWheelEvent e) {
                GameEntityRightPanel.this.getParent().dispatchEvent(e);
                check(e);
            }

            private void check(MouseEvent e) {
                int i = GameEntityRightPanel.this.rowAtPoint(e.getPoint());
                if (i != -1 && i != GameEntityRightPanel.this.currentSelectedIndex) {
                    GameEntityRightPanel.this.editCellAt(i, 0);
                    GameEntityRightPanel.this.currentSelectedIndex = i;
                }
            }
        };
        addMouseWheelListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void addElements(List<GameEntityDTO> list, boolean clean) {
        RightTableModel<GameEntityDTO> rightTableModel = getModel();
        rightTableModel.addElements(list, clean);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void processingStarted(GameEntityDTO e, VersionDTO version) {
        this.changeableElements.add(e.getId());
        for (Component component : Arrays.asList(getComponents())) {
            if (component instanceof GameRightElement) {
                GameRightElement el = (GameRightElement) component;
                if (el.getEntity().getId().equals(e.getId())) {
                    el.processingInstall();
                }
            }
        }
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(GameEntityDTO e, GameType type) {
        this.changeableElements.remove(e.getId());
        repaint();
        updateRow();
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
        this.changeableElements.remove(e.getId());
        GameRightElement elem = find(e);
        if (elem == null) {
            return;
        }
        elem.modpackActButton.reset();
    }

    private GameRightElement find(GameEntityDTO e) {
        for (Component component : Arrays.asList(getComponents())) {
            if (component instanceof GameRightElement) {
                GameRightElement el = (GameRightElement) component;
                if (el.getEntity().getId().equals(e.getId())) {
                    return el;
                }
            }
        }
        return null;
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void populateStatus(GameEntityDTO e, GameType type, boolean state) {
        GameRightElement elem = find(e);
        if (elem != null) {
            elem.getStatusStarButton().setStatus(state);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeEntity(GameEntityDTO e) {
        this.changeableElements.remove(e.getId());
        repaint();
        updateRow();
    }

    void updateRow() {
        int row = getEditingRow();
        if (row >= 0) {
            SwingUtilities.invokeLater(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0010: INVOKE  
                  (wrap: java.lang.Runnable : 0x000b: INVOKE_CUSTOM (r0v4 java.lang.Runnable A[REMOVE]) = 
                  (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel A[D('this' org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r0v1 'row' int A[D('row' int), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel), (r1 I:int) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel.lambda$updateRow$0(int):void)
                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel.updateRow():void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/right/panel/GameEntityRightPanel.class
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
                	... 23 more
                */
            /*
                this = this;
                r0 = r3
                int r0 = r0.getEditingRow()
                r4 = r0
                r0 = r4
                if (r0 < 0) goto L13
                r0 = r3
                r1 = r4
                void r0 = () -> { // java.lang.Runnable.run():void
                    r0.lambda$updateRow$0(r1);
                }
                javax.swing.SwingUtilities.invokeLater(r0)
            L13:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel.updateRow():void");
        }

        /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/right/panel/GameEntityRightPanel$GameRightElement.class */
        public class GameRightElement extends CompleteSubEntityScene.DescriptionGamePanel implements UpdateGameListener {
            private GameEntityDTO entity;
            private int row;
            private GameRightButton modpackActButton;

            public GameEntityDTO getEntity() {
                return this.entity;
            }

            public GameRightElement(GameEntityDTO entity, GameType type, int row) {
                super(entity, type);
                Component[] components;
                this.row = row;
                this.description.setVisible(true);
                this.entity = entity;
                this.modpackActButton = new GameRightButton(entity, type, GameEntityRightPanel.this.localmodpacks) { // from class: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel.GameRightElement.1
                    @Override // org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton
                    public void updateRow() {
                        GameEntityRightPanel.this.repaint();
                    }
                };
                JLabel shadow = new JLabel() { // from class: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel.GameRightElement.2
                    protected void paintComponent(Graphics g) {
                        Rectangle rec = getBounds();
                        SwingUtil.paintShadowLine(rec, g, getParent().getBackground().getRed() - 14, 14);
                    }
                };
                shadow.setBackground(Color.green);
                this.descriptionLayout.putConstraint("West", shadow, 0, "West", this);
                this.descriptionLayout.putConstraint("East", shadow, 0, "East", this);
                this.descriptionLayout.putConstraint("North", shadow, 0, "North", this);
                this.descriptionLayout.putConstraint("South", shadow, 14, "North", this);
                add(shadow);
                this.modpackActButton.initButton();
                this.imagePanel.addMoapckActButton(this.modpackActButton);
                this.descriptionLayout.putConstraint("West", this.imagePanel, 27, "West", this);
                this.descriptionLayout.putConstraint("East", this.imagePanel, 138, "West", this);
                C1MouseBackgroundListener adapter = new C1MouseBackgroundListener(entity, type);
                addMouseListener(adapter);
                for (Component comp : getComponents()) {
                    comp.removeMouseListener(adapter);
                }
                this.description.addMouseListener(adapter);
            }

            /* renamed from: org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel$GameRightElement$1MouseBackgroundListener  reason: invalid class name */
            /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/right/panel/GameEntityRightPanel$GameRightElement$1MouseBackgroundListener.class */
            class C1MouseBackgroundListener extends MouseAdapter implements BlockClickListener {
                final /* synthetic */ GameEntityDTO val$entity;
                final /* synthetic */ GameType val$type;

                C1MouseBackgroundListener(GameEntityDTO gameEntityDTO, GameType gameType) {
                    this.val$entity = gameEntityDTO;
                    this.val$type = gameType;
                }

                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == 1) {
                        ((ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class)).showFullGameEntity(this.val$entity, this.val$type);
                    }
                }
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener
            public void processingActivation() {
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener
            public void processingInstall() {
                this.modpackActButton.setTypeButton(ModpackActButton.PROCESSING);
                GameEntityRightPanel.this.getModel().fireTableCellUpdated(this.row, 0);
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener
            public void initGameEntity() {
            }
        }

        public void filterRightPanel(GameType current) {
            Container container = getParent();
            CardLayout cardLayout = container.getLayout();
            if (getModel().getRowCount() > 0) {
                cardLayout.show(container, ModpackScene.NOT_EMPTY);
            } else {
                cardLayout.show(container, ModpackScene.EMPTY);
            }
            if (isEditing()) {
                this.cellEditor.cancelCellEditing();
            }
            this.currentSelectedIndex = -1;
        }

        /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/right/panel/GameEntityRightPanel$RightRenderer.class */
        private class RightRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
            public RightRenderer() {
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (Objects.isNull(value)) {
                    return null;
                }
                GameRightElement el = new GameRightElement((GameEntityDTO) value, GameEntityRightPanel.this.type, row);
                if (GameEntityRightPanel.this.changeableElements.contains(((GameEntityDTO) value).getId())) {
                    el.modpackActButton.setTypeButton(ModpackActButton.PROCESSING);
                }
                el.setBackground(ColorUtil.COLOR_247);
                return el;
            }

            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                if (Objects.isNull(value)) {
                    return null;
                }
                GameRightElement el = new GameRightElement((GameEntityDTO) value, GameEntityRightPanel.this.type, row);
                if (GameEntityRightPanel.this.changeableElements.contains(((GameEntityDTO) value).getId())) {
                    el.modpackActButton.setTypeButton(ModpackActButton.PROCESSING);
                }
                el.setBackground(Color.WHITE);
                return el;
            }

            public Object getCellEditorValue() {
                return null;
            }
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void installEntity(CompleteVersion e) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void activationStarted(GameEntityDTO e) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void activation(GameEntityDTO e) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void activationError(GameEntityDTO e, Throwable t) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void removeCompleteVersion(CompleteVersion e) {
        }

        public Class<?> getColumnClass(int column) {
            return GameRightElement.class;
        }
    }
