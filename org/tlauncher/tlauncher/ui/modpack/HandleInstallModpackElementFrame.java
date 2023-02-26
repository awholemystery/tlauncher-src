package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.explorer.filters.FilesAndExtentionFilter;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.swing.GameInstallRadioButton;
import org.tlauncher.tlauncher.ui.swing.GameRadioTextButton;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/HandleInstallModpackElementFrame.class */
public class HandleInstallModpackElementFrame extends TemlateModpackFrame {
    private GameType type;
    private static final Dimension DEFAULT_SIZE = new Dimension(572, 470);
    private File[] files;
    private LocalizableLabel installToModpack;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/HandleInstallModpackElementFrame$HandleListener.class */
    public interface HandleListener {
        void installedSuccess();

        void processError(Throwable th);
    }

    public HandleInstallModpackElementFrame(JFrame parent, CompleteVersion version) {
        super(parent, "modpack.install.handle.title", DEFAULT_SIZE);
        this.type = GameType.MOD;
        SpringLayout spring = new SpringLayout();
        JPanel panel = new JPanel(spring);
        ButtonGroup group = new ButtonGroup();
        addCenter(panel);
        FileChooser chooser = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FilesAndExtentionFilter("zip,rar,jar", ArchiveStreamFactory.ZIP, "rar", ArchiveStreamFactory.JAR));
        GameRadioTextButton mods = new GameInstallRadioButton("modpack.button.mod");
        GameRadioTextButton texturePacks = new GameInstallRadioButton("modpack.button.resourcepack");
        GameRadioTextButton maps = new GameInstallRadioButton("modpack.button.map");
        GameRadioTextButton shaderpacks = new GameInstallRadioButton("modpack.button.shaderpack");
        UpdaterButton chooseFiles = new UpdaterButton(ColorUtil.COLOR_215, "modpack.explorer.files");
        this.installToModpack = new LocalizableLabel();
        LocalizableLabel warningLabel = new LocalizableLabel("modpack.install.handle.warning");
        JButton install = new UpdaterButton(BLUE_COLOR, BLUE_COLOR_UNDER, "loginform.enter.install");
        this.installToModpack.setHorizontalAlignment(0);
        warningLabel.setHorizontalAlignment(0);
        group.add(mods);
        group.add(texturePacks);
        group.add(maps);
        group.add(shaderpacks);
        SwingUtil.changeFontFamily(mods, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(texturePacks, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(maps, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(shaderpacks, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
        SwingUtil.changeFontFamily(chooseFiles, FontTL.ROBOTO_MEDIUM, 12, ColorUtil.get(96));
        SwingUtil.changeFontFamily(this.installToModpack, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(warningLabel, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
        SwingUtil.changeFontFamily(install, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        warningLabel.setBackground(ColorUtil.COLOR_244);
        panel.setBackground(Color.WHITE);
        warningLabel.setOpaque(true);
        spring.putConstraint("West", mods, 0, "West", panel);
        spring.putConstraint("East", mods, 143, "West", panel);
        spring.putConstraint("North", mods, 0, "North", panel);
        spring.putConstraint("South", mods, 58, "North", panel);
        panel.add(mods);
        spring.putConstraint("West", texturePacks, 143, "West", panel);
        spring.putConstraint("East", texturePacks, 286, "West", panel);
        spring.putConstraint("North", texturePacks, 0, "North", panel);
        spring.putConstraint("South", texturePacks, 58, "North", panel);
        panel.add(texturePacks);
        spring.putConstraint("West", maps, 286, "West", panel);
        spring.putConstraint("East", maps, 429, "West", panel);
        spring.putConstraint("North", maps, 0, "North", panel);
        spring.putConstraint("South", maps, 58, "North", panel);
        panel.add(maps);
        spring.putConstraint("West", shaderpacks, 429, "West", panel);
        spring.putConstraint("East", shaderpacks, 0, "East", panel);
        spring.putConstraint("North", shaderpacks, 0, "North", panel);
        spring.putConstraint("South", shaderpacks, 58, "North", panel);
        panel.add(shaderpacks);
        spring.putConstraint("West", chooseFiles, 179, "West", panel);
        spring.putConstraint("East", chooseFiles, -177, "East", panel);
        spring.putConstraint("North", chooseFiles, 97, "North", panel);
        spring.putConstraint("South", chooseFiles, 135, "North", panel);
        panel.add(chooseFiles);
        spring.putConstraint("West", this.installToModpack, 29, "West", panel);
        spring.putConstraint("East", this.installToModpack, -27, "East", panel);
        spring.putConstraint("North", this.installToModpack, (int) TarConstants.LF_OFFSET, "North", panel);
        spring.putConstraint("South", this.installToModpack, 182, "North", panel);
        panel.add(this.installToModpack);
        spring.putConstraint("West", warningLabel, 0, "West", panel);
        spring.putConstraint("East", warningLabel, 0, "East", panel);
        spring.putConstraint("North", warningLabel, 208, "North", panel);
        spring.putConstraint("South", warningLabel, 326, "North", panel);
        panel.add(warningLabel);
        spring.putConstraint("West", install, (int) HttpStatus.SC_RESET_CONTENT, "West", panel);
        spring.putConstraint("East", install, 368, "West", panel);
        spring.putConstraint("North", install, -68, "South", panel);
        spring.putConstraint("South", install, -29, "South", panel);
        panel.add(install);
        mods.addItemListener(e -> {
            if (1 == version.getStateChange()) {
                chooseFiles.setText(Localizable.get("modpack.explorer.files"));
                this.files = null;
                this.type = GameType.MOD;
                updateInfoLabel(chooseFiles);
            }
        });
        maps.addItemListener(e2 -> {
            if (1 == version.getStateChange()) {
                chooseFiles.setText(Localizable.get("modpack.explorer.files"));
                this.files = null;
                this.type = GameType.MAP;
                updateInfoLabel(chooseFiles);
            }
        });
        texturePacks.addItemListener(e3 -> {
            if (1 == version.getStateChange()) {
                chooseFiles.setText(Localizable.get("modpack.explorer.files"));
                this.files = null;
                this.type = GameType.RESOURCEPACK;
                updateInfoLabel(chooseFiles);
            }
        });
        shaderpacks.addItemListener(e4 -> {
            if (1 == version.getStateChange()) {
                chooseFiles.setText(Localizable.get("modpack.explorer.files"));
                this.files = null;
                this.type = GameType.SHADERPACK;
                updateInfoLabel(chooseFiles);
            }
        });
        chooseFiles.addActionListener(e5 -> {
            chooseFiles.setBorder(BorderFactory.createEmptyBorder());
            try {
                int result = chooseFiles.showDialog(this, Localizable.get("modpack.explorer.files"));
                if (result == 0) {
                    this.files = chooseFiles.getSelectedFiles();
                    chooseFiles.setText(Localizable.get("explorer.backup.file.chosen"));
                }
            } catch (NullPointerException ex) {
                U.log(ex);
            }
        });
        install.addActionListener(e6 -> {
            if (this.files == null) {
                chooseFiles.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            } else {
                AsyncThread.execute(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001a: INVOKE  
                      (wrap: java.lang.Runnable : 0x0015: INVOKE_CUSTOM (r0v3 java.lang.Runnable A[REMOVE]) = 
                      (r4v0 'this' org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame A[D('this' org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r6v0 'chooseFiles' net.minecraft.launcher.versions.CompleteVersion A[D('version' net.minecraft.launcher.versions.CompleteVersion), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  
                      (r0 I:org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame)
                      (r1 I:net.minecraft.launcher.versions.CompleteVersion)
                     type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame.lambda$null$5(net.minecraft.launcher.versions.CompleteVersion):void)
                     type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame.lambda$new$6(org.tlauncher.tlauncher.ui.loc.UpdaterButton, net.minecraft.launcher.versions.CompleteVersion, java.awt.event.ActionEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/HandleInstallModpackElementFrame.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
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
                    	... 40 more
                    */
                /*
                    this = this;
                    r0 = r4
                    java.io.File[] r0 = r0.files
                    if (r0 != 0) goto L13
                    r0 = r5
                    java.awt.Color r1 = java.awt.Color.RED
                    r2 = 1
                    javax.swing.border.Border r1 = javax.swing.BorderFactory.createLineBorder(r1, r2)
                    r0.setBorder(r1)
                    return
                L13:
                    r0 = r4
                    r1 = r6
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$null$5(r1);
                    }
                    org.tlauncher.util.async.AsyncThread.execute(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame.lambda$new$6(org.tlauncher.tlauncher.ui.loc.UpdaterButton, net.minecraft.launcher.versions.CompleteVersion, java.awt.event.ActionEvent):void");
            });
            mods.setSelected(true);
        }

        private void updateInfoLabel(CompleteVersion version) {
            this.installToModpack.setText(Localizable.get("modpack.install.handle." + this.type.toString().toLowerCase()).replace("modpack.name", version.getModpack().getName()));
        }
    }
