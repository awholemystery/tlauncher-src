package org.tlauncher.tlauncher.ui.settings;

import ch.qos.logback.core.CoreConstants;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import net.minecraft.launcher.process.JavaProcessLauncher;
import org.apache.commons.io.IOUtils;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/ResetView.class */
public class ResetView extends JPanel {
    private JButton resetAgain;

    public ResetView() {
        SpringLayout spring = new SpringLayout();
        setLayout(spring);
        setPreferredSize(new Dimension(420, 280));
        JLabel label = new LocalizableLabel("settings.reset.message");
        JLabel mapsNotRemove = new LocalizableLabel("settings.reset.not.remove.maps");
        mapsNotRemove.setHorizontalAlignment(0);
        mapsNotRemove.setForeground(new Color(255, 58, 66));
        this.resetAgain = new UpdaterButton(new Color(222, 64, 43), new Color(222, 31, 8), Color.WHITE, "settings.reset.button.start");
        SwingUtil.setFontSize(this.resetAgain, 13.0f, 1);
        LocalizableCheckbox tlSettings = new EditorCheckBox("settings.reset.remove.tlauncher.settings");
        tlSettings.setIconTextGap(15);
        tlSettings.setSelected(true);
        LocalizableCheckbox minecraft = new EditorCheckBox("settings.reset.remove.minecraft.folder");
        minecraft.setIconTextGap(15);
        minecraft.setSelected(true);
        LocalizableCheckbox versions = new EditorCheckBox("settings.reset.remove.versions");
        versions.setIconTextGap(15);
        versions.setSelected(true);
        spring.putConstraint("North", label, 10, "North", this);
        spring.putConstraint("West", label, 10, "West", this);
        spring.putConstraint("South", label, 40, "North", this);
        spring.putConstraint("East", label, -10, "East", this);
        add(label);
        spring.putConstraint("North", tlSettings, 10, "South", label);
        spring.putConstraint("West", tlSettings, 10, "West", this);
        spring.putConstraint("South", tlSettings, 40, "South", label);
        spring.putConstraint("East", tlSettings, -10, "East", this);
        add(tlSettings);
        spring.putConstraint("North", versions, 10, "South", tlSettings);
        spring.putConstraint("West", versions, 10, "West", this);
        spring.putConstraint("South", versions, 40, "South", tlSettings);
        spring.putConstraint("East", versions, -10, "East", this);
        add(versions);
        spring.putConstraint("North", minecraft, 10, "South", versions);
        spring.putConstraint("West", minecraft, 10, "West", this);
        spring.putConstraint("South", minecraft, 40, "South", versions);
        spring.putConstraint("East", minecraft, -10, "East", this);
        add(minecraft);
        spring.putConstraint("North", mapsNotRemove, 30, "South", minecraft);
        spring.putConstraint("West", mapsNotRemove, 0, "West", this);
        spring.putConstraint("South", mapsNotRemove, 55, "South", minecraft);
        spring.putConstraint("East", mapsNotRemove, 0, "East", this);
        add(mapsNotRemove);
        spring.putConstraint("North", this.resetAgain, -50, "South", this);
        spring.putConstraint("West", this.resetAgain, 50, "West", this);
        spring.putConstraint("South", this.resetAgain, -10, "South", this);
        spring.putConstraint("East", this.resetAgain, -50, "East", this);
        add(this.resetAgain);
        this.resetAgain.addActionListener(e1 -> {
            if (Alert.showLocQuestion("settings.reset.question.confirm")) {
                AsyncThread.execute(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0011: INVOKE  
                      (wrap: java.lang.Runnable : 0x000c: INVOKE_CUSTOM (r0v3 java.lang.Runnable A[REMOVE]) = 
                      (r5v0 'this' org.tlauncher.tlauncher.ui.settings.ResetView A[D('this' org.tlauncher.tlauncher.ui.settings.ResetView), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r6v0 'tlSettings' org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox A[D('tlSettings' org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox), DONT_INLINE])
                      (r7v0 'tlSettings' org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox A[D('minecraft' org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox), DONT_INLINE])
                      (r8v0 'minecraft' org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox A[D('versions' org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  
                      (r0 I:org.tlauncher.tlauncher.ui.settings.ResetView)
                      (r1 I:org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox)
                      (r2 I:org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox)
                      (r3 I:org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox)
                     type: DIRECT call: org.tlauncher.tlauncher.ui.settings.ResetView.lambda$null$0(org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox):void)
                     type: STATIC call: org.tlauncher.util.async.AsyncThread.execute(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.settings.ResetView.lambda$new$1(org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, java.awt.event.ActionEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/ResetView.class
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
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                    	... 42 more
                    */
                /*
                    this = this;
                    java.lang.String r0 = "settings.reset.question.confirm"
                    boolean r0 = org.tlauncher.tlauncher.ui.alert.Alert.showLocQuestion(r0)
                    if (r0 == 0) goto L14
                    r0 = r5
                    r1 = r6
                    r2 = r7
                    r3 = r8
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$null$0(r1, r2, r3);
                    }
                    org.tlauncher.util.async.AsyncThread.execute(r0)
                L14:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.settings.ResetView.lambda$new$1(org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox, java.awt.event.ActionEvent):void");
            });
        }

        public JButton getResetAgain() {
            return this.resetAgain;
        }

        private void reset(LocalizableCheckbox tlSettings, LocalizableCheckbox minecraft, LocalizableCheckbox versions) {
            List<File> files = new ArrayList<>();
            JavaProcessLauncher p = Bootstrapper.restartLauncher();
            if (versions.isSelected()) {
                File dir2 = new File(MinecraftUtil.getWorkingDirectory(), PathAppUtil.VERSION_DIRECTORY);
                if (dir2.exists()) {
                    files.add(dir2);
                }
            }
            if (minecraft.isSelected()) {
                try {
                    File dir3 = MinecraftUtil.getWorkingDirectory();
                    if (dir3.exists()) {
                        files.addAll((Collection) IOUtils.readLines(getClass().getResourceAsStream("/removedFolders.txt")).stream().map(e -> {
                            return new File(dir3, e);
                        }).filter((v0) -> {
                            return v0.exists();
                        }).collect(Collectors.toList()));
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (tlSettings.isSelected()) {
                File tlFolder = MinecraftUtil.getTLauncherFile(CoreConstants.EMPTY_STRING);
                File[] listFiles = tlFolder.listFiles(pathname -> {
                    return !pathname.toString().endsWith("jvms");
                });
                files.addAll(Lists.newArrayList(listFiles));
                TLauncher.getInstance().getConfiguration().clear();
                TLauncher.getInstance().getConfiguration().store();
            }
            files.forEach(f -> {
                if (f.isDirectory()) {
                    FileUtil.deleteDirectory(f);
                } else {
                    FileUtil.deleteFile(f);
                }
            });
            try {
                p.start();
                TLauncher.kill();
            } catch (IOException e3) {
                U.log(e3);
            }
        }
    }
