package org.tlauncher.tlauncher.ui.settings;

import ch.qos.logback.core.CoreConstants;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.controller.JavaMinecraftController;
import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
import org.tlauncher.tlauncher.managers.VersionLists;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.converter.MinecraftJavaConverter;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
import org.tlauncher.tlauncher.ui.editor.EditorFileField;
import org.tlauncher.tlauncher.ui.editor.EditorResolutionField;
import org.tlauncher.tlauncher.ui.editor.EditorTextField;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/MinecraftSettings.class */
public class MinecraftSettings extends PageSettings {
    public static final String MINECRAFT_SETTING_RAM = "minecraft.memory.ram2";
    SettingsMemorySlider slider;
    @Inject
    private JavaMinecraftController controller;
    private EditorComboBox<MinecraftJava.CompleteMinecraftJava> javaComboBox;
    public static final MinecraftJava.CompleteMinecraftJava defaultJava = MinecraftJava.CompleteMinecraftJava.create(0L, "settings.default", CoreConstants.EMPTY_STRING, new ArrayList());
    @Inject
    private MinecraftJavaConverter converter;

    public MinecraftSettings() {
        setOpaque(false);
    }

    private ExtendedPanel doublePanel(JComponent com1, int gap, JComponent comp2) {
        ExtendedPanel extendedPanel = new ExtendedPanel((LayoutManager) new FlowLayout(0, 0, 0));
        extendedPanel.add((Component) com1);
        extendedPanel.add(Box.createHorizontalStrut(gap));
        extendedPanel.add((Component) comp2);
        return extendedPanel;
    }

    private ExtendedPanel createBoxes(List<EditorCheckBox> list) {
        ExtendedPanel extendedPanel = new ExtendedPanel();
        extendedPanel.setLayout(new BoxLayout(extendedPanel, 1));
        for (EditorCheckBox box : list) {
            extendedPanel.add((Component) box);
        }
        return extendedPanel;
    }

    @Inject
    public void initGuice() {
        SpringLayout springLayout = new SpringLayout();
        FileChooser fileChooser = (FileChooser) TLauncher.getInjector().getInstance(FileChooser.class);
        fileChooser.setFileSelectionMode(1);
        EditorFileField editorFileField = new EditorFileField("settings.client.gamedir.prompt", fileChooser) { // from class: org.tlauncher.tlauncher.ui.settings.MinecraftSettings.1
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFileField, org.tlauncher.tlauncher.ui.editor.EditorField
            public boolean isValueValid() {
                try {
                    File f = new File(getSettingsValue(), "testChooserFolder");
                    FileUtil.createFolder(f);
                    FileUtil.deleteDirectory(f);
                    return super.isValueValid();
                } catch (IOException e) {
                    Alert.showLocError("settings.client.gamedir.noaccess", e);
                    return false;
                }
            }
        };
        EditorResolutionField editorResolutionField = new EditorResolutionField("settings.client.resolution.width", "settings.client.resolution.height", this.global.getDefaultClientWindowSize(), false);
        EditorCheckBox box = new EditorCheckBox("settings.client.resolution.fullscreen");
        List<EditorCheckBox> versions = new ArrayList<>();
        List<HandlerSettings> settings = new ArrayList<>();
        EditorTextField jvmArguments = new EditorTextField("settings.java.args.jvm", true);
        Component editorTextField = new EditorTextField("settings.java.args.minecraft", true);
        ExtendedPanel argPanel1 = new ExtendedPanel();
        argPanel1.setLayout(new BoxLayout(argPanel1, 1));
        Component updaterButton = new UpdaterButton(UpdaterButton.GRAY_COLOR, "settings.change");
        this.javaComboBox = new EditorComboBox<>(this.converter, null);
        this.slider = new SettingsMemorySlider();
        EditorFieldChangeListener changeListener = new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.MinecraftSettings.2
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldValue, String newValue) {
                TLauncher.getInstance().getVersionManager().updateVersionList();
            }
        };
        BorderPanel b = new BorderPanel(10, 0);
        b.add((Component) this.javaComboBox, (Object) "Center");
        b.add(updaterButton, "East");
        b.setPreferredSize(new Dimension(0, 21));
        argPanel1.add((Component) b);
        for (ReleaseType releaseType : ReleaseType.getDefinable()) {
            EditorCheckBox editorCheckBox = new EditorCheckBox("settings.versions." + releaseType);
            HandlerSettings handlerSettings = new HandlerSettings("minecraft.versions." + releaseType, editorCheckBox, changeListener);
            addHandler(handlerSettings);
            settings.add(handlerSettings);
            versions.add(editorCheckBox);
        }
        EditorCheckBox oldRelease = new EditorCheckBox("settings.versions.sub." + ReleaseType.SubType.OLD_RELEASE);
        HandlerSettings handlerSettings2 = new HandlerSettings("minecraft.versions.sub." + ReleaseType.SubType.OLD_RELEASE, oldRelease, changeListener);
        addHandler(handlerSettings2);
        settings.add(handlerSettings2);
        versions.add(2, oldRelease);
        SettingElement directorySettings = new SettingElement("settings.client.gamedir.label", editorFileField, 31);
        SettingElement resolution = new SettingElement("settings.client.resolution.label", doublePanel(editorResolutionField, 16, box), 21);
        SettingElement versionList = new SettingElement("settings.versions.label", createBoxes(versions), 121);
        SettingElement javaPath = new SettingElement("settings.java.path.label", argPanel1, 0);
        SettingElement memory = new SettingElement("settings.java.memory.label", (JComponent) this.slider, 84, 10, 1);
        ExtendedPanel argPanel = new ExtendedPanel();
        argPanel.setLayout(new BoxLayout(argPanel, 1));
        argPanel.add(Box.createVerticalStrut(9));
        argPanel.add(editorTextField);
        SettingElement arguments = new SettingElement("version.manager.editor.field.minecraftArguments", argPanel, 0);
        setLayout(springLayout);
        springLayout.putConstraint("North", directorySettings, 0, "North", this);
        springLayout.putConstraint("West", directorySettings, 0, "West", this);
        springLayout.putConstraint("South", directorySettings, 27, "North", this);
        springLayout.putConstraint("East", directorySettings, 0, "East", this);
        add((Component) directorySettings);
        springLayout.putConstraint("North", resolution, 17, "South", directorySettings);
        springLayout.putConstraint("West", resolution, 0, "West", this);
        springLayout.putConstraint("South", resolution, 43, "South", directorySettings);
        springLayout.putConstraint("East", resolution, 0, "East", directorySettings);
        add((Component) resolution);
        springLayout.putConstraint("North", versionList, 6, "South", resolution);
        springLayout.putConstraint("West", versionList, 0, "West", this);
        springLayout.putConstraint("South", versionList, 138, "South", resolution);
        springLayout.putConstraint("East", versionList, 0, "East", directorySettings);
        add((Component) versionList);
        springLayout.putConstraint("North", arguments, 2, "South", versionList);
        springLayout.putConstraint("West", arguments, 0, "West", this);
        springLayout.putConstraint("South", arguments, 32, "North", arguments);
        springLayout.putConstraint("East", arguments, 0, "East", directorySettings);
        add((Component) arguments);
        springLayout.putConstraint("West", javaPath, 0, "West", directorySettings);
        springLayout.putConstraint("East", javaPath, 0, "East", directorySettings);
        springLayout.putConstraint("North", javaPath, 12, "South", arguments);
        springLayout.putConstraint("South", javaPath, 23, "North", javaPath);
        add((Component) javaPath);
        springLayout.putConstraint("North", memory, 18, "South", javaPath);
        springLayout.putConstraint("South", memory, 100, "North", memory);
        springLayout.putConstraint("West", memory, 0, "West", directorySettings);
        springLayout.putConstraint("East", memory, 0, "East", directorySettings);
        add((Component) memory);
        EditorFieldChangeListener editorFieldChangeListener = new EditorFieldChangeListener() { // from class: org.tlauncher.tlauncher.ui.settings.MinecraftSettings.3
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener
            public void onChange(String oldValue, String newValue) {
                if (!MinecraftSettings.this.tlauncher.isReady()) {
                    return;
                }
                try {
                    ((VersionLists) MinecraftSettings.this.tlauncher.getManager().getComponent(VersionLists.class)).updateLocal();
                    MinecraftSettings.this.tlauncher.getVersionManager().asyncRefresh();
                    MinecraftSettings.this.tlauncher.getProfileManager().asyncRefresh();
                } catch (IOException e) {
                    Alert.showLocError("settings.client.gamedir.noaccess", e);
                }
            }
        };
        updaterButton.addActionListener(e -> {
            ConfigurationJavaFrame frame = (ConfigurationJavaFrame) TLauncher.getInjector().getInstance(ConfigurationJavaFrame.class);
            frame.setVisible(true);
        });
        addHandler(new HandlerSettings("minecraft.gamedir", editorFileField, editorFieldChangeListener));
        addHandler(new HandlerSettings("minecraft.size", editorResolutionField));
        addHandler(new HandlerSettings("minecraft.fullscreen", box));
        addHandler(new HandlerSettings("minecraft.javaargs", jvmArguments));
        addHandler(new HandlerSettings("minecraft.args", editorTextField));
        addHandler(new HandlerSettings(MINECRAFT_SETTING_RAM, this.slider));
        addHandler(new HandlerSettings("minecraft.args", editorTextField));
        addHandler(new HandlerSettings(JavaMinecraftController.SELECTED_JAVA_KEY, this.javaComboBox));
    }

    @Override // org.tlauncher.tlauncher.ui.settings.PageSettings, org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface
    public void init() {
        this.controller.notifyListeners();
        this.slider.initMemoryQuestion();
        super.init();
    }

    @Subscribe
    public void applicationEvent(MinecraftJava event) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.settings.MinecraftSettings A[D('this' org.tlauncher.tlauncher.ui.settings.MinecraftSettings), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'event' org.tlauncher.tlauncher.entity.minecraft.MinecraftJava A[D('event' org.tlauncher.tlauncher.entity.minecraft.MinecraftJava), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.ui.settings.MinecraftSettings)
              (r1 I:org.tlauncher.tlauncher.entity.minecraft.MinecraftJava)
             type: DIRECT call: org.tlauncher.tlauncher.ui.settings.MinecraftSettings.lambda$applicationEvent$1(org.tlauncher.tlauncher.entity.minecraft.MinecraftJava):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.settings.MinecraftSettings.applicationEvent(org.tlauncher.tlauncher.entity.minecraft.MinecraftJava):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/MinecraftSettings.class
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
                r0.lambda$applicationEvent$1(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.settings.MinecraftSettings.applicationEvent(org.tlauncher.tlauncher.entity.minecraft.MinecraftJava):void");
    }
}
