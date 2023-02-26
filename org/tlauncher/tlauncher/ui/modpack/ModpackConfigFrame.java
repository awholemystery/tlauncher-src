package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.http.HttpStatus;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
import org.tlauncher.tlauncher.controller.ModpackConfig;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackForgeComboboxRenderer;
import org.tlauncher.tlauncher.ui.ui.CreationModpackForgeComboboxUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackConfigFrame.class */
public class ModpackConfigFrame extends TemlateModpackFrame {
    private ModpackConfig controller;
    private JCheckBox box;
    private JTextField modpackName;
    private JComboBox<NameIdDTO> minecraftVersion;
    private SliderModpackPanel slider;
    private JButton save;
    private JButton open;
    private JButton remove;
    private EditorCheckBox skinCheckBox;
    private static final Dimension maxSize = new Dimension(572, 451);
    private static final Dimension minSize = new Dimension(572, 350);
    private ModpackManager manager;
    private final JPanel panel;
    private final JLabel selectedMemory;
    private JLabel question;
    private Configuration c;
    private Color colorButton;

    public ModpackConfigFrame(JFrame parent, CompleteVersion version) {
        super(parent, "modpack.config.title", minSize);
        this.colorButton = new Color(0, 174, 239);
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        this.controller = (ModpackConfig) TLauncher.getInjector().getInstance(ModpackConfig.class);
        this.question = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
        this.c = TLauncher.getInstance().getConfiguration();
        this.question.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame.1
            public void mousePressed(MouseEvent e) {
                ModpackConfigFrame.this.setVisible(false);
                Alert.showLocMessage(ModpackConfigFrame.this.c.get(TestEnvironment.WARMING_MESSAGE));
                ModpackConfigFrame.this.setVisible(true);
            }
        });
        JLabel memorySettings = new LocalizableLabel("modpack.config.memory.title");
        JLabel optifineLabel = new LocalizableLabel("modpack.config.system.label");
        Border empty = BorderFactory.createLineBorder(ColorUtil.COLOR_149, 1);
        CompoundBorder border = new CompoundBorder(empty, BorderFactory.createEmptyBorder(0, 15, 0, 0));
        JLabel nameLabel = new LocalizableLabel("modpack.creation.name");
        JLabel forgeVersionLabel = new JLabel(Localizable.get("version.name.v1") + ":");
        JLabel minecraftVersinoTypeLabel = new LocalizableLabel("version.manager.editor.field.type");
        JLabel gameVersion = new LocalizableLabel("modpack.config.game.version");
        minecraftVersinoTypeLabel.setVerticalAlignment(0);
        gameVersion.setVerticalAlignment(0);
        this.selectedMemory = new LocalizableLabel("settings.java.memory.label");
        ModpackVersionDTO modpackVersionDTO = (ModpackVersionDTO) version.getModpack().getVersion();
        GameVersionDTO gameVersionDTO = modpackVersionDTO.getGameVersionDTO();
        this.minecraftVersion = new JComboBox<>();
        this.minecraftVersion.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.minecraftVersion.setRenderer(new CreationModpackForgeComboboxRenderer());
        this.minecraftVersion.setUI(new CreationModpackForgeComboboxUI());
        this.box = new EditorCheckBox("modpack.config.memory.box");
        this.box.setIconTextGap(14);
        this.skinCheckBox = new EditorCheckBox("modpack.config.skin.use");
        this.skinCheckBox.setIconTextGap(14);
        if (ModpackUtil.useSkinMod(version)) {
            this.skinCheckBox.setSelected(true);
        } else if (gameVersionDTO == null) {
            this.skinCheckBox.setSelected(false);
            this.skinCheckBox.addItemListener(e -> {
                if (e.getStateChange() == 1) {
                    setVisible(false);
                    Alert.showLocMessage("modpack.internet.update");
                    this.skinCheckBox.setSelected(false);
                    setVisible(true);
                }
            });
        }
        this.modpackName = new JTextField(version.getID());
        this.modpackName.setBorder(border);
        this.modpackName.setForeground(ColorUtil.COLOR_25);
        JLabel gameVersionValue = new JLabel(Objects.nonNull(gameVersionDTO) ? gameVersionDTO.getName() : modpackVersionDTO.getGameVersion());
        NameIdDTO mvt = modpackVersionDTO.findFirstMinecraftVersionType();
        JLabel minecraftVersionTypeValue = new LocalizableLabel("modpack.version." + (Objects.nonNull(mvt) ? mvt.getName() : "forge"));
        minecraftVersionTypeValue.setBorder(border);
        gameVersionValue.setBorder(border);
        this.slider = new SliderModpackPanel(new Dimension(534, 80));
        if (version.getModpack().isModpackMemory()) {
            this.slider.setValue(version.getModpack().getMemory());
        } else {
            this.slider.setValue(TLauncher.getInstance().getConfiguration().getInteger(MinecraftSettings.MINECRAFT_SETTING_RAM));
        }
        this.save = new UpdaterFullButton(this.colorButton, BLUE_COLOR_UNDER, "settings.save", "save-modpack.png");
        this.save.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        this.save.setIconTextGap(19);
        this.open = new UpdaterFullButton(this.colorButton, BLUE_COLOR_UNDER, "modpack.open.folder", "open-modpack.png");
        this.open.setIconTextGap(10);
        this.remove = new UpdaterFullButton(new Color(208, 43, 43), new Color(180, 39, 39), "modpack.popup.delete", "modpack-dustbin.png");
        this.remove.setBorder(BorderFactory.createEmptyBorder(0, 19, 0, 0));
        this.remove.setIconTextGap(15);
        SpringLayout spring = new SpringLayout();
        this.panel = new JPanel(spring);
        this.panel.setBackground(Color.WHITE);
        this.panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 21, 0));
        addCenter(this.panel);
        SwingUtil.changeFontFamily(nameLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(gameVersion, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(minecraftVersinoTypeLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(gameVersionValue, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(minecraftVersionTypeValue, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(this.modpackName, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(forgeVersionLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(this.minecraftVersion, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(this.box, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.skinCheckBox, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(memorySettings, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(optifineLabel, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.selectedMemory, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.save, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
        SwingUtil.changeFontFamily(this.open, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
        SwingUtil.changeFontFamily(this.remove, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
        spring.putConstraint("West", nameLabel, 30, "West", this.panel);
        spring.putConstraint("East", nameLabel, 267, "West", this.panel);
        spring.putConstraint("North", nameLabel, -3, "North", this.panel);
        spring.putConstraint("South", nameLabel, 15, "North", this.panel);
        this.panel.add(nameLabel);
        spring.putConstraint("West", this.modpackName, 29, "West", this.panel);
        spring.putConstraint("East", this.modpackName, -27, "East", this.panel);
        spring.putConstraint("North", this.modpackName, 19, "North", this.panel);
        spring.putConstraint("South", this.modpackName, 63, "North", this.panel);
        this.panel.add(this.modpackName);
        spring.putConstraint("West", minecraftVersinoTypeLabel, 32, "West", this.panel);
        spring.putConstraint("East", minecraftVersinoTypeLabel, 139, "West", this.panel);
        spring.putConstraint("North", minecraftVersinoTypeLabel, 10, "South", this.modpackName);
        spring.putConstraint("South", minecraftVersinoTypeLabel, 28, "South", this.modpackName);
        this.panel.add(minecraftVersinoTypeLabel);
        spring.putConstraint("West", gameVersion, 162, "West", this.panel);
        spring.putConstraint("East", gameVersion, 289, "West", this.panel);
        spring.putConstraint("North", gameVersion, 10, "South", this.modpackName);
        spring.putConstraint("South", gameVersion, 28, "South", this.modpackName);
        this.panel.add(gameVersion);
        spring.putConstraint("West", forgeVersionLabel, 332, "West", this.panel);
        spring.putConstraint("East", forgeVersionLabel, -27, "East", this.panel);
        spring.putConstraint("North", forgeVersionLabel, 10, "South", this.modpackName);
        spring.putConstraint("South", forgeVersionLabel, 28, "South", this.modpackName);
        this.panel.add(forgeVersionLabel);
        spring.putConstraint("West", minecraftVersionTypeValue, 29, "West", this.panel);
        spring.putConstraint("East", minecraftVersionTypeValue, 139, "West", this.panel);
        spring.putConstraint("North", minecraftVersionTypeValue, 4, "South", gameVersion);
        spring.putConstraint("South", minecraftVersionTypeValue, 48, "South", gameVersion);
        this.panel.add(minecraftVersionTypeValue);
        spring.putConstraint("West", gameVersionValue, 159, "West", this.panel);
        spring.putConstraint("East", gameVersionValue, 309, "West", this.panel);
        spring.putConstraint("North", gameVersionValue, 4, "South", gameVersion);
        spring.putConstraint("South", gameVersionValue, 48, "South", gameVersion);
        this.panel.add(gameVersionValue);
        spring.putConstraint("West", this.minecraftVersion, 329, "West", this.panel);
        spring.putConstraint("East", this.minecraftVersion, -27, "East", this.panel);
        spring.putConstraint("North", this.minecraftVersion, 4, "South", forgeVersionLabel);
        spring.putConstraint("South", this.minecraftVersion, 48, "South", forgeVersionLabel);
        this.panel.add(this.minecraftVersion);
        spring.putConstraint("West", optifineLabel, 29, "West", this.panel);
        spring.putConstraint("East", optifineLabel, 169, "East", this.panel);
        spring.putConstraint("North", optifineLabel, 15, "South", this.minecraftVersion);
        spring.putConstraint("South", optifineLabel, 33, "South", this.minecraftVersion);
        this.panel.add(optifineLabel);
        spring.putConstraint("West", this.skinCheckBox, 179, "West", this.panel);
        spring.putConstraint("East", this.skinCheckBox, -27, "East", this.panel);
        spring.putConstraint("North", this.skinCheckBox, 15, "South", this.minecraftVersion);
        spring.putConstraint("South", this.skinCheckBox, 33, "South", this.minecraftVersion);
        this.panel.add(this.skinCheckBox);
        spring.putConstraint("West", memorySettings, 29, "West", this.panel);
        spring.putConstraint("East", memorySettings, 169, "East", this.panel);
        spring.putConstraint("North", memorySettings, 10, "South", this.skinCheckBox);
        spring.putConstraint("South", memorySettings, 33, "South", this.skinCheckBox);
        this.panel.add(memorySettings);
        spring.putConstraint("West", this.box, 179, "West", this.panel);
        spring.putConstraint("East", this.box, -27, "East", this.panel);
        spring.putConstraint("North", this.box, 10, "South", this.skinCheckBox);
        spring.putConstraint("South", this.box, 33, "South", this.skinCheckBox);
        this.panel.add(this.box);
        spring.putConstraint("West", this.save, 29, "West", this.panel);
        spring.putConstraint("East", this.save, 168, "West", this.panel);
        spring.putConstraint("North", this.save, -43, "South", this.panel);
        spring.putConstraint("South", this.save, 0, "South", this.panel);
        this.panel.add(this.save);
        spring.putConstraint("West", this.open, 185, "West", this.panel);
        spring.putConstraint("East", this.open, 390, "West", this.panel);
        spring.putConstraint("North", this.open, -43, "South", this.panel);
        spring.putConstraint("South", this.open, 0, "South", this.panel);
        this.panel.add(this.open);
        spring.putConstraint("West", this.remove, (int) HttpStatus.SC_NOT_ACCEPTABLE, "West", this.panel);
        spring.putConstraint("East", this.remove, -27, "East", this.panel);
        spring.putConstraint("North", this.remove, -43, "South", this.panel);
        spring.putConstraint("South", this.remove, 0, "South", this.panel);
        this.panel.add(this.remove);
        this.box.addItemListener(e2 -> {
            updateState(spring);
        });
        this.box.setSelected(!version.getModpack().isModpackMemory());
        this.save.addActionListener(e3 -> {
            if (!this.box.isSelected()) {
                version.getModpack().setMemory(this.slider.getValue());
            }
            version.getModpack().setModpackMemory(!this.box.isSelected());
            this.controller.save(version, this.modpackName.getText(), this.skinCheckBox.isSelected(), (NameIdDTO) this.minecraftVersion.getSelectedItem());
            setVisible(false);
        });
        this.open.addActionListener(e4 -> {
            this.controller.open(version);
            setVisible(false);
        });
        this.remove.addActionListener(e5 -> {
            this.controller.remove(version);
            setVisible(false);
        });
        updateState(spring);
        addedVersions(modpackVersionDTO);
    }

    private void addedVersions(ModpackVersionDTO mv) {
        CompletableFuture.runAsync(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0011: INVOKE  
              (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0007: INVOKE  (r0v2 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame A[D('this' org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r5v0 'mv' org.tlauncher.modpack.domain.client.version.ModpackVersionDTO A[D('mv' org.tlauncher.modpack.domain.client.version.ModpackVersionDTO), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame)
              (r1 I:org.tlauncher.modpack.domain.client.version.ModpackVersionDTO)
             type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame.lambda$addedVersions$6(org.tlauncher.modpack.domain.client.version.ModpackVersionDTO):void)
             type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
              (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x000c: INVOKE_CUSTOM (r1v2 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
              (r4v0 'this' org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame A[D('this' org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r5v0 'mv' org.tlauncher.modpack.domain.client.version.ModpackVersionDTO A[D('mv' org.tlauncher.modpack.domain.client.version.ModpackVersionDTO), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
             call insn: ?: INVOKE  
              (r1 I:org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame)
              (r2 I:org.tlauncher.modpack.domain.client.version.ModpackVersionDTO)
              (v2 java.lang.Throwable)
             type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame.lambda$addedVersions$7(org.tlauncher.modpack.domain.client.version.ModpackVersionDTO, java.lang.Throwable):java.lang.Void)
             type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame.addedVersions(org.tlauncher.modpack.domain.client.version.ModpackVersionDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackConfigFrame.class
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
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 15 more
            */
        /*
            this = this;
            r0 = r4
            r1 = r5
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$addedVersions$6(r1);
            }
            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
            r1 = r4
            r2 = r5
            void r1 = (v2) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                return r1.lambda$addedVersions$7(r2, v2);
            }
            java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
            r0 = r4
            r1 = r5
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$addedVersions$9(r1);
            }
            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame.addedVersions(org.tlauncher.modpack.domain.client.version.ModpackVersionDTO):void");
    }

    private void updateState(SpringLayout spring) {
        if (this.box.isSelected()) {
            this.panel.remove(this.selectedMemory);
            this.panel.remove(this.slider);
            this.panel.remove(this.question);
            setCenter(minSize);
        } else {
            spring.putConstraint("West", this.slider, 12, "West", this.panel);
            spring.putConstraint("East", this.slider, -13, "East", this.panel);
            spring.putConstraint("North", this.slider, -130, "South", this.panel);
            spring.putConstraint("South", this.slider, -60, "South", this.panel);
            this.panel.add(this.slider);
            spring.putConstraint("West", this.selectedMemory, 29, "West", this.panel);
            spring.putConstraint("East", this.selectedMemory, 29 + SwingUtil.getWidthText(this.selectedMemory, this.selectedMemory.getText()) + 5, "West", this.panel);
            spring.putConstraint("North", this.selectedMemory, -150, "South", this.panel);
            spring.putConstraint("South", this.selectedMemory, -132, "South", this.panel);
            this.panel.add(this.selectedMemory);
            spring.putConstraint("West", this.question, 2, "East", this.selectedMemory);
            spring.putConstraint("East", this.question, 25, "East", this.selectedMemory);
            spring.putConstraint("North", this.question, -151, "South", this.panel);
            spring.putConstraint("South", this.question, -131, "South", this.panel);
            this.panel.add(this.question);
            setCenter(maxSize);
            if (!this.c.isExist(TestEnvironment.WARMING_MESSAGE)) {
                this.question.setVisible(false);
            }
        }
        this.panel.revalidate();
        this.panel.repaint();
    }

    private void updateSkinBoxButton(GameVersionDTO gameVersionDTO, ModpackVersionDTO mv) {
        if (this.skinCheckBox.isSelected()) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            this.skinCheckBox.setEnabled(false);
        });
        GameEntityDTO g = new GameEntityDTO();
        g.setId(ModDTO.TL_SKIN_CAPE_ID);
        try {
            this.manager.getInstallingGameEntity(GameType.MOD, g, null, gameVersionDTO, mv.findFirstMinecraftVersionType());
            SwingUtilities.invokeLater(() -> {
                this.skinCheckBox.setEnabled(true);
            });
        } catch (IOException e) {
            U.log("not found tl skin cape for", gameVersionDTO.getName(), mv.findFirstMinecraftVersionType());
        }
    }
}
