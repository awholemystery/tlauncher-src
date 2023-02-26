package org.tlauncher.tlauncher.ui.modpack;

import ch.qos.logback.core.CoreConstants;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.controller.JavaMinecraftController;
import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.settings.SettingElement;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.text.LocalizableTextArea;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ConfigurationJavaFrame.class */
public class ConfigurationJavaFrame extends TemlateModpackFrame {
    private LocalizableTextField path;
    private LocalizableTextArea args;
    private LocalizableTextField name;
    private JTable addedJava;
    private JScrollPane scroll;
    private CardLayout cradLayout;
    JPanel topPanel;
    JPanel bottomPanel;
    @Inject
    private JavaMinecraftController controller;
    private volatile MinecraftJava.CompleteMinecraftJava j;

    public ConfigurationJavaFrame() {
        super(TLauncher.getInstance().getFrame(), "settings.select.java", new Dimension(1000, 480));
        this.j = new MinecraftJava.CompleteMinecraftJava();
        Font font = new JButton().getFont().deriveFont(1, 13.0f);
        this.addedJava = new JTable();
        this.scroll = new JScrollPane(this.addedJava, 20, 31);
        JavaTableModel d = new JavaTableModel();
        ExtendedPanel common = new ExtendedPanel();
        ExtendedPanel buttonsBottom = new ExtendedPanel();
        this.cradLayout = new CardLayout();
        this.topPanel = new JPanel(this.cradLayout);
        GridBagConstraints c = new GridBagConstraints();
        JButton addJava = new UpdaterButton(UpdaterButton.GRAY_COLOR, "explorer.browse");
        JButton reset = new UpdaterButton(new Color(222, 64, 43), new Color(222, 31, 8), Color.WHITE, "settings.reset.java");
        final UpdaterButton save = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.save");
        this.path = new LocalizableTextField();
        this.name = new LocalizableTextField("settings.java.name");
        this.args = new LocalizableTextArea("settings.java.args", 2, 0);
        this.path.setPlaceholder("settings.java.choose", Localizable.get("explorer.browse"));
        JLabel pathLabel = new LocalizableLabel("settings.java.path");
        JLabel jvmArgs = new LocalizableLabel("settings.java.args.jvm");
        JLabel nameJava = new LocalizableLabel("modpack.table.pack.element.name");
        JLabel javaNotAdded = new LocalizableLabel("settings.java.not.added");
        this.addedJava.setRowHeight(50);
        this.addedJava.setShowVerticalLines(false);
        this.addedJava.setTableHeader((JTableHeader) null);
        this.addedJava.setModel(d);
        this.addedJava.getColumnModel().getColumn(0).setPreferredWidth(880);
        this.addedJava.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.addedJava.getColumnModel().getColumn(2).setPreferredWidth(60);
        this.addedJava.setDefaultEditor(MinecraftJava.CompleteMinecraftJava.class, new JavaCellRenderAndEditor());
        this.addedJava.setDefaultRenderer(MinecraftJava.CompleteMinecraftJava.class, new JavaCellRenderAndEditor());
        this.addedJava.getSelectionModel().addListSelectionListener(new ListSelectionListener() { // from class: org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame.1
            public void valueChanged(ListSelectionEvent e) {
                int row = ConfigurationJavaFrame.this.addedJava.getSelectedRow();
                int column = ConfigurationJavaFrame.this.addedJava.getSelectedColumn();
                TableCellEditor cellEditor = ConfigurationJavaFrame.this.addedJava.getCellEditor();
                if (row == -1) {
                    return;
                }
                if (Objects.nonNull(cellEditor)) {
                    cellEditor.cancelCellEditing();
                }
                ConfigurationJavaFrame.this.addedJava.removeRowSelectionInterval(row, row);
                if (column == 2) {
                    ConfigurationJavaFrame.this.controller.remove((MinecraftJava.CompleteMinecraftJava) ConfigurationJavaFrame.this.addedJava.getModel().getValueAt(row, 0));
                } else if (column == 1) {
                    ConfigurationJavaFrame.this.j = (MinecraftJava.CompleteMinecraftJava) ConfigurationJavaFrame.this.addedJava.getModel().getValueAt(row, 0);
                    ConfigurationJavaFrame.this.path.setValue((Object) ConfigurationJavaFrame.this.j.getPath());
                    ConfigurationJavaFrame.this.name.setValue((Object) ConfigurationJavaFrame.this.j.getName());
                    ConfigurationJavaFrame.this.args.setText((String) ConfigurationJavaFrame.this.j.getArgs().stream().collect(Collectors.joining(" ")));
                }
            }
        });
        pathLabel.setFont(SettingElement.LABEL_FONT);
        jvmArgs.setFont(SettingElement.LABEL_FONT);
        nameJava.setFont(SettingElement.LABEL_FONT);
        javaNotAdded.setHorizontalAlignment(0);
        javaNotAdded.setAlignmentY(0.0f);
        SwingUtil.setFontSize(javaNotAdded, 18.0f, 1);
        save.setForeground(Color.WHITE);
        save.setFont(font);
        reset.setForeground(Color.WHITE);
        reset.setFont(font);
        save.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame.2
            public void mouseEntered(MouseEvent e) {
                save.setBackground(ColorUtil.COLOR_204);
            }

            public void mouseExited(MouseEvent e) {
                save.setBackground(save.getBackgroundColor());
            }
        });
        common.setLayout(new BoxLayout(common, 1));
        buttonsBottom.add((Component) reset, (Component) save);
        this.topPanel.add(javaNotAdded, ModpackScene.EMPTY);
        this.topPanel.add(this.scroll, ModpackScene.NOT_EMPTY);
        common.add((Component) this.topPanel);
        this.bottomPanel = new ExtendedPanel();
        this.bottomPanel.setLayout(new GridBagLayout());
        c.weightx = 0.1d;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = 17;
        c.insets = new Insets(0, 10, 0, 10);
        pathLabel.setHorizontalTextPosition(2);
        this.bottomPanel.add(pathLabel, c);
        c.gridy = 2;
        jvmArgs.setHorizontalTextPosition(2);
        this.bottomPanel.add(nameJava, c);
        c.gridy = 3;
        nameJava.setHorizontalTextPosition(2);
        this.bottomPanel.add(jvmArgs, c);
        c.insets = new Insets(5, 0, 5, 10);
        c.fill = 2;
        c.weighty = 0.0d;
        c.weightx = 0.8d;
        c.gridx = 1;
        c.gridy = 1;
        this.bottomPanel.add(this.path, c);
        c.gridy = 2;
        this.bottomPanel.add(this.name, c);
        c.gridy = 3;
        this.bottomPanel.add(this.args, c);
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = 0;
        c.gridy = 4;
        c.anchor = 10;
        c.gridwidth = 3;
        c.gridx = 0;
        this.bottomPanel.add(buttonsBottom, c);
        c.gridwidth = 1;
        c.weightx = 0.0d;
        c.weighty = 0.0d;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 10);
        this.bottomPanel.add(addJava, c);
        common.add((Component) this.bottomPanel);
        addCenter(common);
        addJava.addActionListener(e -> {
            FileChooser fileChooser = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
            fileChooser.setFileSelectionMode(1);
            if (Objects.nonNull(this.path.getValue())) {
                fileChooser.setCurrentDirectory(new File(this.path.getValue()));
            } else {
                fileChooser.setCurrentDirectory(OS.buildJAVAFolder().toFile());
            }
            int result = fileChooser.showDialog(this);
            if (result == 0) {
                File f = fileChooser.getSelectedFile();
                this.path.setValue((Object) f.getAbsolutePath());
                List<String> list1 = new ArrayList<>();
                MinecraftUtil.configureG1GC(list1);
                this.args.setText((String) list1.stream().collect(Collectors.joining(" ")));
                this.name.setValue((Object) f.getAbsoluteFile().getName());
            }
        });
        save.addActionListener(e2 -> {
            String java = OS.appendBootstrapperJvm2(this.path.getValue());
            if (Objects.isNull(this.path.getValue())) {
                Alert.showError(CoreConstants.EMPTY_STRING, Localizable.get("review.message.fill") + " " + Localizable.get("settings.java.path"));
            } else if (Files.notExists(Paths.get(java, new String[0]), new LinkOption[0])) {
                Alert.showError(CoreConstants.EMPTY_STRING, Localizable.get("settings.java.not.proper.path", java));
            } else if (StringUtils.isBlank(this.name.getValue())) {
                Alert.showError(CoreConstants.EMPTY_STRING, Localizable.get("review.message.fill") + " " + Localizable.get("modpack.table.pack.element.name"));
            } else {
                this.j.setPath(this.path.getValue());
                this.j.setName(this.name.getValue());
                this.j.setArgs(Arrays.asList(this.args.getText().split(" ")));
                this.controller.add(this.j);
                resetForm();
            }
        });
        reset.addActionListener(e3 -> {
            resetForm();
        });
        addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame.3
            public void componentShown(ComponentEvent e4) {
                ConfigurationJavaFrame.this.controller.notifyListeners();
            }
        });
    }

    @Subscribe
    public void applicationEvent(MinecraftJava event) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame A[D('this' org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'event' org.tlauncher.tlauncher.entity.minecraft.MinecraftJava A[D('event' org.tlauncher.tlauncher.entity.minecraft.MinecraftJava), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame)
              (r1 I:org.tlauncher.tlauncher.entity.minecraft.MinecraftJava)
             type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame.lambda$applicationEvent$3(org.tlauncher.tlauncher.entity.minecraft.MinecraftJava):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame.applicationEvent(org.tlauncher.tlauncher.entity.minecraft.MinecraftJava):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ConfigurationJavaFrame.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
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
            	... 15 more
            */
        /*
            this = this;
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$applicationEvent$3(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame.applicationEvent(org.tlauncher.tlauncher.entity.minecraft.MinecraftJava):void");
    }

    private void resetForm() {
        this.path.setValue((Object) CoreConstants.EMPTY_STRING);
        this.name.setValue((Object) CoreConstants.EMPTY_STRING);
        this.args.setText(CoreConstants.EMPTY_STRING);
        this.j = new MinecraftJava.CompleteMinecraftJava();
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ConfigurationJavaFrame$JavaTableModel.class */
    private class JavaTableModel extends AbstractTableModel {
        private List<MinecraftJava.CompleteMinecraftJava> list = new ArrayList();

        public JavaTableModel() {
        }

        public void setList(List<MinecraftJava.CompleteMinecraftJava> list) {
            this.list = list;
        }

        public String toString() {
            return "ConfigurationJavaFrame.JavaTableModel(list=" + getList() + ")";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof JavaTableModel) {
                JavaTableModel other = (JavaTableModel) o;
                if (other.canEqual(this) && super/*java.lang.Object*/.equals(o)) {
                    Object this$list = getList();
                    Object other$list = other.getList();
                    return this$list == null ? other$list == null : this$list.equals(other$list);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof JavaTableModel;
        }

        public int hashCode() {
            int result = super/*java.lang.Object*/.hashCode();
            Object $list = getList();
            return (result * 59) + ($list == null ? 43 : $list.hashCode());
        }

        public List<MinecraftJava.CompleteMinecraftJava> getList() {
            return this.list;
        }

        public Object getValueAt(int row, int column) {
            return this.list.get(row);
        }

        public int getRowCount() {
            return this.list.size();
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (rowIndex == 0) {
                return super.isCellEditable(rowIndex, columnIndex);
            }
            return true;
        }

        public int getColumnCount() {
            return 3;
        }

        public Class<?> getColumnClass(int columnIndex) {
            return MinecraftJava.CompleteMinecraftJava.class;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ConfigurationJavaFrame$JavaCellRenderAndEditor.class */
    public class JavaCellRenderAndEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
        public JavaCellRenderAndEditor() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return buildComponent(row, column, (MinecraftJava.CompleteMinecraftJava) value);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return buildComponent(row, column, (MinecraftJava.CompleteMinecraftJava) value);
        }

        public Object getCellEditorValue() {
            return null;
        }

        private Component buildComponent(int row, int column, MinecraftJava.CompleteMinecraftJava v) {
            switch (column) {
                case 0:
                    JLabel l = new JLabel((row + 1) + ") " + Localizable.get("modpack.table.pack.element.name") + ": " + v.getName() + ". " + Localizable.get("settings.java.path") + ": " + v.getPath());
                    l.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                    SwingUtil.changeFontFamily(l, FontTL.ROBOTO_MEDIUM, 13);
                    return l;
                case 1:
                    return new ImageUdaterButton(Color.WHITE, "gear.png");
                case 2:
                    return new ImageUdaterButton(Color.WHITE, "remove.png");
                default:
                    return null;
            }
        }
    }
}
