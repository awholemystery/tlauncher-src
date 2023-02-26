package org.tlauncher.tlauncher.ui.modpack;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.tlauncher.ui.swing.renderer.CreationMinecraftTypeComboboxRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackForgeComboboxRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackGameVersionComboboxRenderer;
import org.tlauncher.tlauncher.ui.ui.CreationMinecraftTypeComboboxUI;
import org.tlauncher.tlauncher.ui.ui.CreationModpackForgeComboboxUI;
import org.tlauncher.tlauncher.ui.ui.CreationModpackGameVersionComboboxUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackCreation.class */
public class ModpackCreation extends TemlateModpackFrame {
    private final LocalizableTextField versionName;
    private final JComboBox<GameVersionDTO> gameVersions;
    private final JComboBox<NameIdDTO> versionNameValue;
    private JComboBox<NameIdDTO> minecraftVersionTypes;
    private JButton create;
    private JCheckBox box;
    private JButton cancel;
    private EditorCheckBox tlskinCapeModBox;
    private static final Dimension maxSize = new Dimension(572, 479);
    private static final Dimension minSize = new Dimension(572, 374);
    private JLabel question;
    private Configuration c;
    private ModpackManager modpackManager;

    public ModpackCreation(JFrame parent) {
        super(parent, "modpack.creation.modpack", new Dimension(572, 479));
        this.modpackManager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        this.question = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
        this.c = TLauncher.getInstance().getConfiguration();
        this.question.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.1
            public void mousePressed(MouseEvent e) {
                ModpackCreation.this.setVisible(false);
                Alert.showLocMessage(ModpackCreation.this.c.get(TestEnvironment.WARMING_MESSAGE));
                ModpackCreation.this.setVisible(true);
            }
        });
        SpringLayout spring = new SpringLayout();
        JPanel panel = new JPanel(spring);
        panel.setBorder(new EmptyBorder(0, 0, 33, 0));
        addCenter(panel);
        panel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(Localizable.get("modpack.creation.name"));
        nameLabel.setForeground(ColorUtil.COLOR_149);
        JLabel selectedMemory = new LocalizableLabel("settings.java.memory.label");
        JLabel type = new JLabel(Localizable.get("version.manager.editor.field.type"));
        JLabel memorySettings = new LocalizableLabel("modpack.config.memory.title");
        JLabel tlskinCapeModLabel = new LocalizableLabel("modpack.config.system.label");
        this.box = new EditorCheckBox("modpack.config.memory.box");
        this.box.setIconTextGap(14);
        this.tlskinCapeModBox = new EditorCheckBox("modpack.config.skin.use");
        this.tlskinCapeModBox.setIconTextGap(14);
        this.tlskinCapeModBox.setSelected(true);
        JLabel versionGameLabel = new JLabel(Localizable.get("modpack.table.pack.element.version") + ":");
        JLabel versionLabel = new JLabel(Localizable.get("version.name.v1") + ":");
        this.create = new UpdaterFullButton(COLOR_0_174_239, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.create.button", "modpack-creation.button.png");
        this.create.setEnabled(false);
        this.create.setBorder(new EmptyBorder(0, 29, 0, 0));
        this.create.setIconTextGap(42);
        this.cancel = new UpdaterFullButton(new Color(208, 43, 43), new Color(180, 39, 39), "loginform.enter.cancel", "modpack-cancel-button.png");
        this.cancel.setBorder(new EmptyBorder(0, 29, 0, 0));
        this.cancel.setIconTextGap(42);
        this.gameVersions = new JComboBox<>();
        this.gameVersions.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.gameVersions.setRenderer(new CreationModpackGameVersionComboboxRenderer());
        this.gameVersions.setUI(new CreationModpackGameVersionComboboxUI());
        this.minecraftVersionTypes = new JComboBox<>(this.modpackManager.getMinecraftVersionTypes().toArray(new NameIdDTO[0]));
        this.minecraftVersionTypes.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.minecraftVersionTypes.setRenderer(new CreationMinecraftTypeComboboxRenderer());
        this.minecraftVersionTypes.setUI(new CreationMinecraftTypeComboboxUI());
        this.versionNameValue = new JComboBox<>();
        this.versionNameValue.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.versionNameValue.setRenderer(new CreationModpackForgeComboboxRenderer());
        this.versionNameValue.setUI(new CreationModpackForgeComboboxUI());
        this.versionName = new LocalizableTextField("modpack.creation.input.name") { // from class: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
            public void onFocusLost() {
                super.onFocusLost();
                if (super.getValue() == null) {
                    ModpackCreation.this.versionName.setForeground(ColorUtil.COLOR_202);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
            public void onFocusGained() {
                super.onFocusGained();
                ModpackCreation.this.versionName.setForeground(ColorUtil.COLOR_25);
            }
        };
        this.versionName.setHorizontalAlignment(0);
        this.versionName.setBorder(BorderFactory.createLineBorder(ColorUtil.COLOR_149, 1));
        SliderModpackPanel slider = new SliderModpackPanel(new Dimension(534, 80));
        SwingUtil.changeFontFamily(type, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(nameLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(versionLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(versionGameLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
        SwingUtil.changeFontFamily(memorySettings, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(selectedMemory, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(tlskinCapeModLabel, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.tlskinCapeModBox, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.create, FontTL.ROBOTO_BOLD, 12, Color.WHITE);
        SwingUtil.changeFontFamily(this.box, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.minecraftVersionTypes, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(this.gameVersions, FontTL.ROBOTO_BOLD, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(this.versionNameValue, FontTL.ROBOTO_BOLD, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(this.cancel, FontTL.ROBOTO_BOLD, 12, Color.WHITE);
        SwingUtil.changeFontFamily(this.versionName, FontTL.ROBOTO_BOLD, 18, ColorUtil.COLOR_202);
        spring.putConstraint("West", nameLabel, 253, "West", panel);
        spring.putConstraint("East", nameLabel, 353, "West", panel);
        spring.putConstraint("North", nameLabel, 23, "North", panel);
        spring.putConstraint("South", nameLabel, 23 + 18, "North", panel);
        panel.add(nameLabel);
        spring.putConstraint("West", this.versionName, 29, "West", panel);
        spring.putConstraint("East", this.versionName, -27, "East", panel);
        spring.putConstraint("North", this.versionName, 2, "South", nameLabel);
        spring.putConstraint("South", this.versionName, 46, "South", nameLabel);
        panel.add(this.versionName);
        spring.putConstraint("West", type, 32, "West", panel);
        spring.putConstraint("East", type, 139, "West", panel);
        spring.putConstraint("North", type, 18, "South", this.versionName);
        spring.putConstraint("South", type, 18 + 18, "South", this.versionName);
        panel.add(type);
        spring.putConstraint("West", versionGameLabel, 162, "West", panel);
        spring.putConstraint("East", versionGameLabel, 289, "West", panel);
        spring.putConstraint("North", versionGameLabel, 18, "South", this.versionName);
        spring.putConstraint("South", versionGameLabel, 18 + 18, "South", this.versionName);
        panel.add(versionGameLabel);
        spring.putConstraint("West", versionLabel, 332, "West", panel);
        spring.putConstraint("East", versionLabel, 545, "West", panel);
        spring.putConstraint("North", versionLabel, 18, "South", this.versionName);
        spring.putConstraint("South", versionLabel, 18 + 18, "South", this.versionName);
        panel.add(versionLabel);
        spring.putConstraint("West", this.minecraftVersionTypes, 29, "West", panel);
        spring.putConstraint("East", this.minecraftVersionTypes, 139, "West", panel);
        spring.putConstraint("North", this.minecraftVersionTypes, 5, "South", versionLabel);
        spring.putConstraint("South", this.minecraftVersionTypes, 49, "South", versionLabel);
        panel.add(this.minecraftVersionTypes);
        spring.putConstraint("West", this.gameVersions, 159, "West", panel);
        spring.putConstraint("East", this.gameVersions, 309, "West", panel);
        spring.putConstraint("North", this.gameVersions, 5, "South", versionLabel);
        spring.putConstraint("South", this.gameVersions, 49, "South", versionLabel);
        panel.add(this.gameVersions);
        spring.putConstraint("West", this.versionNameValue, 329, "West", panel);
        spring.putConstraint("East", this.versionNameValue, 545, "West", panel);
        spring.putConstraint("North", this.versionNameValue, 5, "South", versionLabel);
        spring.putConstraint("South", this.versionNameValue, 49, "South", versionLabel);
        panel.add(this.versionNameValue);
        spring.putConstraint("West", memorySettings, 29, "West", panel);
        spring.putConstraint("East", memorySettings, 229, "West", panel);
        spring.putConstraint("North", memorySettings, 15, "South", this.versionNameValue);
        spring.putConstraint("South", memorySettings, 32, "South", this.versionNameValue);
        panel.add(memorySettings);
        spring.putConstraint("West", this.box, 179, "West", panel);
        spring.putConstraint("East", this.box, 529, "West", panel);
        spring.putConstraint("North", this.box, 15, "South", this.versionNameValue);
        spring.putConstraint("South", this.box, 32, "South", this.versionNameValue);
        panel.add(this.box);
        spring.putConstraint("West", tlskinCapeModLabel, 29, "West", panel);
        spring.putConstraint("East", tlskinCapeModLabel, 229, "West", this.versionNameValue);
        spring.putConstraint("North", tlskinCapeModLabel, 10, "South", memorySettings);
        spring.putConstraint("South", tlskinCapeModLabel, 27, "South", memorySettings);
        panel.add(tlskinCapeModLabel);
        spring.putConstraint("West", this.tlskinCapeModBox, 179, "West", panel);
        spring.putConstraint("East", this.tlskinCapeModBox, 529, "West", panel);
        spring.putConstraint("North", this.tlskinCapeModBox, 10, "South", memorySettings);
        spring.putConstraint("South", this.tlskinCapeModBox, 27, "South", memorySettings);
        panel.add(this.tlskinCapeModBox);
        spring.putConstraint("West", this.create, 29, "West", panel);
        spring.putConstraint("East", this.create, 267, "West", panel);
        spring.putConstraint("North", this.create, -43, "South", panel);
        spring.putConstraint("South", this.create, 0, "South", panel);
        panel.add(this.create);
        spring.putConstraint("West", this.cancel, (int) HttpStatus.SC_TEMPORARY_REDIRECT, "West", panel);
        spring.putConstraint("East", this.cancel, 545, "West", panel);
        spring.putConstraint("North", this.cancel, -43, "South", panel);
        spring.putConstraint("South", this.cancel, 0, "South", panel);
        panel.add(this.cancel);
        this.create.addActionListener(e -> {
            String name = this.versionName.getValue();
            try {
                Paths.get(name, new String[0]);
                if (!this.modpackManager.checkNameVersion(Lists.newArrayList(new String[]{name}))) {
                    setVisible(false);
                    Alert.showLocWarning("modpack.config.memory.message");
                    setVisible(true);
                    return;
                }
                ModpackDTO modpackDTO = new ModpackDTO();
                modpackDTO.setId(Long.valueOf(-U.n()));
                ModpackVersionDTO v = new ModpackVersionDTO();
                v.setId(Long.valueOf((-U.n()) - 1));
                modpackDTO.setName(name);
                GameVersionDTO g = (GameVersionDTO) this.gameVersions.getSelectedItem();
                v.setGameVersionDTO(g);
                v.setName("1.0");
                v.setMinecraftVersionTypes(Lists.newArrayList(new NameIdDTO[]{(NameIdDTO) this.minecraftVersionTypes.getSelectedItem()}));
                v.setMinecraftVersionName((NameIdDTO) this.versionNameValue.getSelectedItem());
                modpackDTO.setVersion(v);
                if (!this.box.isSelected()) {
                    modpackDTO.setModpackMemory(true);
                    modpackDTO.setMemory(slider.getValue());
                }
                this.modpackManager.createModpack(name, modpackDTO, this.tlskinCapeModBox.isSelected());
                setVisible(false);
            } catch (NullPointerException | InvalidPathException e) {
                this.versionName.setBorder(BorderFactory.createLineBorder(new Color(255, 62, 62), 1));
            }
        });
        this.box.addItemListener(e2 -> {
            if (selectedMemory.getStateChange() == 2) {
                setCenter(maxSize);
                spring.putConstraint("West", spring, 12, "West", slider);
                spring.putConstraint("East", spring, -13, "East", slider);
                spring.putConstraint("North", spring, 259, "North", slider);
                spring.putConstraint("South", spring, 339, "South", slider);
                slider.add(spring);
                spring.putConstraint("West", panel, 29, "West", slider);
                spring.putConstraint("East", panel, 29 + SwingUtil.getWidthText(panel, panel.getText()) + 5, "West", slider);
                spring.putConstraint("North", panel, 245, "North", slider);
                spring.putConstraint("South", panel, (int) TarConstants.VERSION_OFFSET, "North", slider);
                slider.add(panel);
                spring.putConstraint("West", this.question, 2, "East", panel);
                spring.putConstraint("East", this.question, 25, "East", panel);
                spring.putConstraint("North", this.question, -153, "South", slider);
                spring.putConstraint("South", this.question, -133, "South", slider);
                slider.add(this.question);
                setCenter(maxSize);
                if (!this.c.isExist(TestEnvironment.WARMING_MESSAGE)) {
                    this.question.setVisible(false);
                    return;
                }
                return;
            }
            slider.remove(panel);
            slider.remove(spring);
            slider.remove(this.question);
            setCenter(minSize);
        });
        this.cancel.addActionListener(e3 -> {
            setVisible(false);
        });
        this.box.setSelected(true);
        this.minecraftVersionTypes.addItemListener(item -> {
            if (item.getStateChange() == 1) {
                getGameVersionsFields((NameIdDTO) item.getItem());
            }
        });
        this.gameVersions.addItemListener(e4 -> {
            if (e4.getStateChange() == 1) {
                CompletableFuture.runAsync(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0017: INVOKE  
                      (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x000f: INVOKE  (r0v4 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                      (wrap: java.lang.Runnable : 0x000a: INVOKE_CUSTOM (r0v3 java.lang.Runnable A[REMOVE]) = 
                      (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation A[D('this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r4v0 'e4' java.awt.event.ItemEvent A[D('e' java.awt.event.ItemEvent), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.ModpackCreation), (r1 I:java.awt.event.ItemEvent) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$null$5(java.awt.event.ItemEvent):void)
                     type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                      (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0012: INVOKE_CUSTOM (r1v2 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                     handle type: INVOKE_STATIC
                     lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                     call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$null$6(java.lang.Throwable):java.lang.Void)
                     type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$new$7(java.awt.event.ItemEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackCreation.class
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
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                    	... 42 more
                    */
                /*
                    this = this;
                    r0 = r4
                    int r0 = r0.getStateChange()
                    r1 = 1
                    if (r0 != r1) goto L1b
                    r0 = r3
                    r1 = r4
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$null$5(r1);
                    }
                    java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                    void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                        return lambda$null$6(v0);
                    }
                    java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                L1b:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$new$7(java.awt.event.ItemEvent):void");
            });
            getGameVersionsFields((NameIdDTO) this.minecraftVersionTypes.getSelectedItem());
        }

        private void getGameVersionsFields(NameIdDTO nameIdDTO) {
            this.create.setEnabled(false);
            setStatusTlSkinCapeModBox(false);
            CompletableFuture.runAsync(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001c: INVOKE  
                  (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0014: INVOKE  (r0v5 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                  (wrap: java.lang.Runnable : 0x000f: INVOKE_CUSTOM (r0v4 java.lang.Runnable A[REMOVE]) = 
                  (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation A[D('this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r4v0 'nameIdDTO' org.tlauncher.modpack.domain.client.share.NameIdDTO A[D('nameIdDTO' org.tlauncher.modpack.domain.client.share.NameIdDTO), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.ModpackCreation), (r1 I:org.tlauncher.modpack.domain.client.share.NameIdDTO) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$getGameVersionsFields$10(org.tlauncher.modpack.domain.client.share.NameIdDTO):void)
                 type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                  (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0017: INVOKE_CUSTOM (r1v3 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                 handle type: INVOKE_STATIC
                 lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                 call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$getGameVersionsFields$11(java.lang.Throwable):java.lang.Void)
                 type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.getGameVersionsFields(org.tlauncher.modpack.domain.client.share.NameIdDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackCreation.class
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
                r0 = r3
                javax.swing.JButton r0 = r0.create
                r1 = 0
                r0.setEnabled(r1)
                r0 = r3
                r1 = 0
                r0.setStatusTlSkinCapeModBox(r1)
                r0 = r3
                r1 = r4
                void r0 = () -> { // java.lang.Runnable.run():void
                    r0.lambda$getGameVersionsFields$10(r1);
                }
                java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                    return lambda$getGameVersionsFields$11(v0);
                }
                java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.getGameVersionsFields(org.tlauncher.modpack.domain.client.share.NameIdDTO):void");
        }

        private void updateVersions(GameVersionDTO gameVersionDTO) throws IOException {
            NameIdDTO minecraftVersionType = (NameIdDTO) this.minecraftVersionTypes.getSelectedItem();
            NameIdDTO[] list = (NameIdDTO[]) this.modpackManager.getVersionsByGameVersionAndMinecraftVersionType(gameVersionDTO.getId(), minecraftVersionType).toArray(new NameIdDTO[0]);
            SwingUtilities.invokeLater(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002b: INVOKE  
                  (wrap: java.lang.Runnable : 0x0026: INVOKE_CUSTOM (r0v10 java.lang.Runnable A[REMOVE]) = 
                  (r4v0 'this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation A[D('this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r0v8 'list' org.tlauncher.modpack.domain.client.share.NameIdDTO[] A[D('list' org.tlauncher.modpack.domain.client.share.NameIdDTO[]), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.ModpackCreation), (r1 I:org.tlauncher.modpack.domain.client.share.NameIdDTO[]) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$updateVersions$12(org.tlauncher.modpack.domain.client.share.NameIdDTO[]):void)
                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.updateVersions(org.tlauncher.modpack.domain.client.GameVersionDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackCreation.class
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
                r0 = r4
                javax.swing.JComboBox<org.tlauncher.modpack.domain.client.share.NameIdDTO> r0 = r0.minecraftVersionTypes
                java.lang.Object r0 = r0.getSelectedItem()
                org.tlauncher.modpack.domain.client.share.NameIdDTO r0 = (org.tlauncher.modpack.domain.client.share.NameIdDTO) r0
                r6 = r0
                r0 = r4
                org.tlauncher.tlauncher.managers.ModpackManager r0 = r0.modpackManager
                r1 = r5
                java.lang.Long r1 = r1.getId()
                r2 = r6
                java.util.List r0 = r0.getVersionsByGameVersionAndMinecraftVersionType(r1, r2)
                r1 = 0
                org.tlauncher.modpack.domain.client.share.NameIdDTO[] r1 = new org.tlauncher.modpack.domain.client.share.NameIdDTO[r1]
                java.lang.Object[] r0 = r0.toArray(r1)
                org.tlauncher.modpack.domain.client.share.NameIdDTO[] r0 = (org.tlauncher.modpack.domain.client.share.NameIdDTO[]) r0
                r7 = r0
                r0 = r4
                r1 = r7
                void r0 = () -> { // java.lang.Runnable.run():void
                    r0.lambda$updateVersions$12(r1);
                }
                javax.swing.SwingUtilities.invokeLater(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.updateVersions(org.tlauncher.modpack.domain.client.GameVersionDTO):void");
        }

        private void updateSkinBoxButton(GameVersionDTO gameVersionDTO) {
            GameEntityDTO g = new GameEntityDTO();
            g.setId(ModDTO.TL_SKIN_CAPE_ID);
            NameIdDTO minecraftVersionType = (NameIdDTO) this.minecraftVersionTypes.getSelectedItem();
            try {
                this.modpackManager.getInstallingGameEntity(GameType.MOD, g, null, gameVersionDTO, (NameIdDTO) this.minecraftVersionTypes.getSelectedItem());
                setStatusTlSkinCapeModBox(true);
            } catch (IOException e) {
                U.log("not found tl skin cape for", gameVersionDTO.getName(), minecraftVersionType.getName());
            }
        }

        private void setStatusTlSkinCapeModBox(boolean flag) {
            SwingUtilities.invokeLater(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
                  (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                  (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation A[D('this' org.tlauncher.tlauncher.ui.modpack.ModpackCreation), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r4v0 'flag' boolean A[D('flag' boolean), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.ModpackCreation), (r1 I:boolean) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.lambda$setStatusTlSkinCapeModBox$13(boolean):void)
                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.setStatusTlSkinCapeModBox(boolean):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ModpackCreation.class
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
                    r0.lambda$setStatusTlSkinCapeModBox$13(r1);
                }
                javax.swing.SwingUtilities.invokeLater(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.ModpackCreation.setStatusTlSkinCapeModBox(boolean):void");
        }
    }
