package org.tlauncher.tlauncher.ui.scenes;

import by.gdev.util.DesktopUtil;
import ch.qos.logback.core.CoreConstants;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.http.HttpStatus;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.SubModpackDTO;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.modpack.domain.client.share.GameEntitySort;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.button.StateModpackElementButton;
import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.listener.mods.DeferredDocumentListener;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel;
import org.tlauncher.tlauncher.ui.listener.mods.ModpackBoxListener;
import org.tlauncher.tlauncher.ui.listener.mods.ModpackSearchListener;
import org.tlauncher.tlauncher.ui.listener.mods.RightPanelAdjustmentListener;
import org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener;
import org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableHTMLLabel;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.tlauncher.ui.loc.modpack.ShadowButton;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.menu.ModpackPopup;
import org.tlauncher.tlauncher.ui.model.CategoryComboBoxModel;
import org.tlauncher.tlauncher.ui.modpack.ModpackCreation;
import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
import org.tlauncher.tlauncher.ui.modpack.filter.NameFilter;
import org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel;
import org.tlauncher.tlauncher.ui.server.BackPanel;
import org.tlauncher.tlauncher.ui.swing.GameRadioTextButton;
import org.tlauncher.tlauncher.ui.swing.ImageButton;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.renderer.CategoryListRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.CreationMinecraftTypeComboboxRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackGameVersionComboboxRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
import org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer;
import org.tlauncher.tlauncher.ui.ui.CategoryComboBoxUI;
import org.tlauncher.tlauncher.ui.ui.CreationMinecraftTypeComboboxUI;
import org.tlauncher.tlauncher.ui.ui.CreationModpackGameVersionComboboxUI;
import org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI;
import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene.class */
public class ModpackScene extends PseudoScene implements MinecraftListener, UpdateFavoriteValueListener {
    public static final int WIDTH_SEARCH_PANEL = 215;
    protected final ModpackComboBox localmodpacks;
    private final JComboBox<NameIdDTO> minecraftVersionTypes;
    private final JComboBox<GameVersionDTO> gameVersions;
    private final ExtendedPanel panel;
    private final JPanel layoutCenterPanel;
    private final GameEntityRightPanel modpacksPanel;
    private final GameEntityRightPanel modsPanel;
    private final GameEntityRightPanel resourcePackPanel;
    private final GameEntityRightPanel mapsPanel;
    private final GameEntityRightPanel shaderpacksPanel;
    private final ExtendedPanel entitiesPanel;
    private final Injector injector;
    private final GameEntityLeftPanel modLeftPanel;
    private final GameEntityLeftPanel mapLeftPanel;
    private final GameEntityLeftPanel shaderpackLeftPanel;
    private final GameEntityLeftPanel resourceLeftPanel;
    private final SearchPanel search;
    private final GameRadioTextButton modpacks;
    private final ItemListener modpackBoxListener;
    private final EditorCheckBox favoriteCheckBox;
    private final ModpackManager manager;
    private GameType current;
    private GameType last;
    private final ButtonGroup group;
    private final JComboBox<CategoryDTO> categoriesBox;
    private final JComboBox<GameEntitySort> sortBox;
    private final Map<GameType, JRadioButton> mapGameTypeAndRadioButton;
    private final ExecutorService modpackExecutorService;
    private final NameIdDTO anyVersionType;
    private final GameVersionDTO anyGameVersion;
    private final JLabel found;
    private final JPanel searchPanel;
    private NameIdDTO selectedVersionType;
    private GameVersionDTO selectedGameVersion;
    public static final Dimension SIZE = new Dimension(MainPane.SIZE.width, 585);
    public static final Color UP_BACKGROUND_PANEL_COLOR = new Color(60, 170, 252);
    public static final Color BACKGROUND_RIGHT_UNDER_PANEL = new Color(241, 241, 241);
    public static int LEFT_WIDTH = 234;
    public static int RIGHT_WIDTH = SIZE.width - LEFT_WIDTH;
    public static String EMPTY = "EMPTY";
    public static String NOT_EMPTY = "NOT_EMPTY";
    public static String PREPARING = "PREPARING";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$ElementCounterListener.class */
    public interface ElementCounterListener {
        void changeCounter(int i);
    }

    public ModpackScene(MainPane main) {
        super(main);
        this.localmodpacks = new ModpackComboBox();
        this.panel = new ExtendedPanel();
        this.modLeftPanel = new GameEntityLeftPanel();
        this.mapLeftPanel = new GameEntityLeftPanel();
        this.shaderpackLeftPanel = new GameEntityLeftPanel();
        this.resourceLeftPanel = new GameEntityLeftPanel();
        this.modpackBoxListener = new ModpackBoxListener();
        this.current = GameType.MODPACK;
        this.last = this.current;
        this.group = new ButtonGroup();
        this.mapGameTypeAndRadioButton = new HashMap();
        this.search = new SearchPanel();
        this.categoriesBox = new JComboBox<>();
        this.categoriesBox.setModel(new CategoryComboBoxModel(new CategoryDTO[0]));
        this.modpacksPanel = new GameEntityRightPanel(this.localmodpacks, GameType.MODPACK);
        this.modsPanel = new GameEntityRightPanel(this.localmodpacks, GameType.MOD);
        this.resourcePackPanel = new GameEntityRightPanel(this.localmodpacks, GameType.RESOURCEPACK);
        this.mapsPanel = new GameEntityRightPanel(this.localmodpacks, GameType.MAP);
        this.shaderpacksPanel = new GameEntityRightPanel(this.localmodpacks, GameType.SHADERPACK);
        this.injector = TLauncher.getInjector();
        this.modpackExecutorService = (ExecutorService) this.injector.getInstance(Key.get(ExecutorService.class, Names.named("modpackExecutorService")));
        this.anyVersionType = (NameIdDTO) this.injector.getInstance(Key.get(NameIdDTO.class, Names.named("anyVersionType")));
        this.anyGameVersion = (GameVersionDTO) this.injector.getInstance(Key.get(GameVersionDTO.class, Names.named("anyGameVersion")));
        this.layoutCenterPanel = new JPanel(new CardLayout(0, 0));
        this.manager = (ModpackManager) this.injector.getInstance(ModpackManager.class);
        this.localmodpacks.setPreferredSize(new Dimension((int) HttpStatus.SC_OK, 30));
        Component component = new ExtendedPanel() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
            public void paintComponent(Graphics g0) {
                g0.drawImage(ImageCache.getBufferedImage("modpack-top2-background.png"), 0, 0, (ImageObserver) null);
            }
        };
        component.setLayout(new FlowLayout(0, 0, 0));
        JLayeredPane layeredPane = new JLayeredPane() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.2
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        layeredPane.setSize(SIZE);
        this.searchPanel = new JPanel();
        this.searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        this.searchPanel.setVisible(false);
        this.searchPanel.addMouseMotionListener(new MouseMotionAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.3
        });
        this.searchPanel.setOpaque(true);
        this.searchPanel.setBackground(new Color(238, 238, 238));
        GridLayout gl = new GridLayout(9, 1);
        this.searchPanel.setLayout(gl);
        LocalizableLabel versionFieldType = new LocalizableLabel("version.manager.editor.field.type");
        versionFieldType.setHorizontalAlignment(0);
        this.minecraftVersionTypes = new JComboBox<>();
        DefaultComboBoxModel<NameIdDTO> dcm = new DefaultComboBoxModel<>();
        dcm.addElement(this.anyVersionType);
        this.minecraftVersionTypes.setModel(dcm);
        this.minecraftVersionTypes.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.minecraftVersionTypes.setRenderer(new CreationMinecraftTypeComboboxRenderer());
        this.minecraftVersionTypes.setUI(new CreationMinecraftTypeComboboxUI());
        JLabel versionGameLabel = new JLabel(Localizable.get("modpack.table.pack.element.version") + ":");
        versionGameLabel.setHorizontalAlignment(0);
        this.gameVersions = new JComboBox<>();
        this.gameVersions.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.gameVersions.setRenderer(new CreationModpackGameVersionComboboxRenderer());
        this.gameVersions.setUI(new CreationModpackGameVersionComboboxUI());
        LocalizableLabel modpackFilterLabel = new LocalizableLabel("modpack.filter.label");
        modpackFilterLabel.setHorizontalAlignment(0);
        this.favoriteCheckBox = new EditorCheckBox("modpack.favorite");
        this.favoriteCheckBox.setIconTextGap(14);
        this.favoriteCheckBox.setSelected(false);
        this.favoriteCheckBox.setHorizontalAlignment(0);
        this.found = new JLabel();
        this.found.setHorizontalAlignment(0);
        JButton reset = new UpdaterButton(ColorUtil.COLOR_213, ColorUtil.COLOR_195, "settings.reset.java");
        this.searchPanel.setBounds(SIZE.width - WIDTH_SEARCH_PANEL, 110, (int) WIDTH_SEARCH_PANEL, SIZE.height - 110);
        Component extendedPanel = new ExtendedPanel();
        this.entitiesPanel = new ExtendedPanel((LayoutManager) new CardLayout(0, 0));
        this.panel.setSize(SIZE);
        LayoutManager springLayout = new SpringLayout();
        this.panel.setLayout(springLayout);
        this.panel.setOpaque(true);
        extendedPanel.setLayout(new BorderLayout());
        extendedPanel.setOpaque(true);
        extendedPanel.setBackground(Color.WHITE);
        LocalizableLabel nameModpackLabel = new LocalizableLabel("modpack.button.modpack");
        nameModpackLabel.setHorizontalAlignment(0);
        nameModpackLabel.setForeground(Color.WHITE);
        LocalizableLabel categoryLabel = new LocalizableLabel("modpack.filter.label");
        categoryLabel.setHorizontalAlignment(0);
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setPreferredSize(new Dimension(98, 55));
        JButton create = new UpdaterFullButton(new Color(54, 153, 208), ColorUtil.BACKGROUND_COMBO_BOX_POPUP_SELECTED, "modpack.create.button", "create-modpack.png");
        create.setIconTextGap(16);
        JLabel sortLabel = new LocalizableLabel("modpack.sort.label");
        JPanel wrapper = new ExtendedPanel((LayoutManager) new FlowLayout(1, 0, 0));
        wrapper.setBorder(BorderFactory.createEmptyBorder(7, 0, 8, 0));
        this.sortBox = new JComboBox<>(GameEntitySort.getClientSortList());
        this.sortBox.setUI(new ModpackComboBoxUI() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.4
            @Override // org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                String name = Localizable.get("modpack." + ((GameEntitySort) ModpackScene.this.sortBox.getSelectedItem()).toString().toLowerCase(Locale.ROOT));
                paintText(g, bounds, name);
            }
        });
        wrapper.add(this.sortBox);
        this.sortBox.setBorder(BorderFactory.createLineBorder(ModpackComboxRenderer.LINE, 1));
        this.sortBox.setRenderer(new UserCategoryListRenderer());
        CategoryListRenderer categoryListRenderer = new CategoryListRenderer(this.categoriesBox);
        this.categoriesBox.setRenderer(categoryListRenderer);
        this.categoriesBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
        this.categoriesBox.setUI(new CategoryComboBoxUI());
        Component gameRadioTextButton = new GameRadioTextButton("modpack.button.mod");
        Component gameRadioTextButton2 = new GameRadioTextButton("modpack.button.shaderpack");
        this.modpacks = new GameRadioTextButton("modpack.button.modpack");
        Component gameRadioTextButton3 = new GameRadioTextButton("modpack.button.resourcepack");
        Component gameRadioTextButton4 = new GameRadioTextButton("modpack.button.map");
        this.mapGameTypeAndRadioButton.put(GameType.MOD, gameRadioTextButton);
        this.mapGameTypeAndRadioButton.put(GameType.MODPACK, this.modpacks);
        this.mapGameTypeAndRadioButton.put(GameType.RESOURCEPACK, gameRadioTextButton3);
        this.mapGameTypeAndRadioButton.put(GameType.MAP, gameRadioTextButton4);
        this.mapGameTypeAndRadioButton.put(GameType.SHADERPACK, gameRadioTextButton2);
        Collection<JRadioButton> values = this.mapGameTypeAndRadioButton.values();
        ButtonGroup buttonGroup = this.group;
        buttonGroup.getClass();
        values.forEach((v1) -> {
            r1.add(v1);
        });
        final Component imageButton = new ImageButton("settings-modpack.png");
        imageButton.setPreferredSize(new Dimension(66, 55));
        component.add(imageButton);
        component.add(Box.createHorizontalStrut(29));
        component.add(this.modpacks, Box.createHorizontalStrut(20));
        component.add(gameRadioTextButton, Box.createHorizontalStrut(20));
        component.add(gameRadioTextButton3, Box.createHorizontalStrut(20));
        component.add(gameRadioTextButton4, Box.createHorizontalStrut(20));
        component.add(gameRadioTextButton2, Box.createHorizontalStrut(0));
        component.add(Box.createHorizontalStrut(35));
        Component updaterButton = new UpdaterButton(ColorUtil.BLUE_MODPACK, ColorUtil.BACKGROUND_COMBO_BOX_POPUP_SELECTED, "modpack.filters");
        updaterButton.setForeground(Color.WHITE);
        updaterButton.setIconTextGap(15);
        updaterButton.setPreferredSize(new Dimension(192, 40));
        component.add(updaterButton);
        this.layoutCenterPanel.add(this.entitiesPanel, GameType.NOT_MODPACK.toLowerCase());
        CardLayout emptyRightLayout = new CardLayout();
        JPanel emptyRightPanel = new ExtendedPanel((LayoutManager) emptyRightLayout);
        emptyRightPanel.add(new EmptyRightView(GameType.MODPACK), EMPTY);
        emptyRightPanel.add(this.modpacksPanel, NOT_EMPTY);
        ScrollPane sp = createScrollWrapper(emptyRightPanel);
        sp.getVerticalScrollBar().addAdjustmentListener(new RightPanelAdjustmentListener(this.modpacksPanel, this));
        this.layoutCenterPanel.add(sp, GameType.MODPACK.toLowerCase());
        this.entitiesPanel.add((Component) new GameCenterPanel(this.modLeftPanel, this.modsPanel, GameType.MOD), (Object) GameType.MOD.toLowerCase());
        this.entitiesPanel.add((Component) new GameCenterPanel(this.resourceLeftPanel, this.resourcePackPanel, GameType.RESOURCEPACK), (Object) GameType.RESOURCEPACK.toLowerCase());
        this.entitiesPanel.add((Component) new GameCenterPanel(this.mapLeftPanel, this.mapsPanel, GameType.MAP), (Object) GameType.MAP.toLowerCase());
        this.entitiesPanel.add((Component) new GameCenterPanel(this.shaderpackLeftPanel, this.shaderpacksPanel, GameType.SHADERPACK), (Object) GameType.SHADERPACK.toLowerCase());
        extendedPanel.add(this.layoutCenterPanel, "Center");
        Component jPanel = new JPanel(new FlowLayout(0, 0, 0));
        jPanel.setBackground(new Color(60, 170, 232));
        BackPanel back = new UniverseBackPanel(new Color(54, 153, 208));
        back.setPreferredSize(new Dimension(66, 55));
        nameModpackLabel.setPreferredSize(new Dimension(114, 55));
        this.localmodpacks.setPreferredSize(new Dimension(172, 36));
        create.setPreferredSize(new Dimension(132, 36));
        this.search.setPreferredSize(new Dimension(194, 55));
        JPanel gup1 = new JPanel();
        gup1.setPreferredSize(new Dimension(1, 55));
        Color line = new Color(92, 190, 246);
        gup1.setBackground(line);
        JPanel gup2 = new JPanel();
        gup2.setPreferredSize(new Dimension(1, 55));
        gup2.setBackground(line);
        sortLabel.setPreferredSize(new Dimension((int) CoreConstants.CURLY_RIGHT, 55));
        sortLabel.setHorizontalAlignment(0);
        this.sortBox.setPreferredSize(new Dimension(192, 40));
        wrapper.setPreferredSize(new Dimension(192, 55));
        jPanel.add(back);
        jPanel.add(nameModpackLabel);
        jPanel.add(this.localmodpacks);
        jPanel.add(Box.createHorizontalStrut(16));
        jPanel.add(create);
        jPanel.add(Box.createHorizontalStrut(34));
        jPanel.add(gup1);
        jPanel.add(this.search);
        jPanel.add(gup2);
        jPanel.add(sortLabel);
        jPanel.add(wrapper);
        springLayout.putConstraint("West", jPanel, 0, "West", this.panel);
        springLayout.putConstraint("East", jPanel, 0, "East", this.panel);
        springLayout.putConstraint("North", jPanel, 0, "North", this.panel);
        springLayout.putConstraint("South", jPanel, 55, "North", this.panel);
        this.panel.add(jPanel);
        springLayout.putConstraint("West", component, 0, "West", this.panel);
        springLayout.putConstraint("East", component, 0, "East", this.panel);
        springLayout.putConstraint("North", component, 0, "South", jPanel);
        springLayout.putConstraint("South", component, 55, "South", jPanel);
        this.panel.add(component);
        springLayout.putConstraint("West", extendedPanel, 0, "West", this.panel);
        springLayout.putConstraint("East", extendedPanel, 0, "East", this.panel);
        springLayout.putConstraint("North", extendedPanel, 110, "North", this.panel);
        springLayout.putConstraint("South", extendedPanel, 0, "South", this.panel);
        this.panel.add(extendedPanel);
        layeredPane.add(this.searchPanel);
        layeredPane.add(this.panel);
        add((Component) layeredPane);
        this.searchPanel.add(versionFieldType);
        this.searchPanel.add(this.minecraftVersionTypes);
        this.searchPanel.add(versionGameLabel);
        this.searchPanel.add(this.gameVersions);
        this.searchPanel.add(modpackFilterLabel);
        this.searchPanel.add(this.categoriesBox);
        this.searchPanel.add(this.favoriteCheckBox);
        this.searchPanel.add(this.found);
        this.searchPanel.add(reset);
        SwingUtil.changeFontFamily(this.localmodpacks, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(this.search, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(this.sortBox, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(this.categoriesBox, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(updaterButton, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(nameModpackLabel, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(create, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(sortLabel, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(gameRadioTextButton, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(gameRadioTextButton2, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(this.modpacks, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(gameRadioTextButton3, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(gameRadioTextButton4, FontTL.ROBOTO_REGULAR, 14);
        SwingUtil.changeFontFamily(categoryLabel, FontTL.ROBOTO_MEDIUM, 14, Color.WHITE);
        SwingUtil.changeFontFamily(versionFieldType, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(this.minecraftVersionTypes, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(versionGameLabel, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(this.gameVersions, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(modpackFilterLabel, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(this.categoriesBox, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(this.favoriteCheckBox, FontTL.ROBOTO_MEDIUM, 14);
        SwingUtil.changeFontFamily(this.found, FontTL.ROBOTO_MEDIUM, 14);
        create.addActionListener(e -> {
            if (this.searchPanel.isVisible()) {
                this.searchPanel.setVisible(false);
            }
            CompletableFuture.runAsync(() -> {
                DesktopUtil.uncheckCall(() -> {
                    this.manager.fillVersionTypesAndGameVersion();
                    return null;
                });
            }, this.modpackExecutorService).handle(r, t -> {
                if (Objects.nonNull(t)) {
                    U.log(t);
                    Alert.showLocMessage("modpack.internet.update");
                    return null;
                }
                SwingUtilities.invokeLater(() -> {
                    new ModpackCreation(getMainPane().getRootFrame()).setVisible(true);
                });
                return null;
            });
        });
        create.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.5
            public void mouseEntered(MouseEvent e2) {
            }
        });
        this.modpacks.addActionListener(e2 -> {
            this.current = GameType.MODPACK;
        });
        gameRadioTextButton.addActionListener(e3 -> {
            this.current = GameType.MOD;
        });
        gameRadioTextButton3.addActionListener(e4 -> {
            this.current = GameType.RESOURCEPACK;
        });
        gameRadioTextButton4.addActionListener(e5 -> {
            this.current = GameType.MAP;
        });
        gameRadioTextButton2.addActionListener(e6 -> {
            this.current = GameType.SHADERPACK;
        });
        this.sortBox.addItemListener(e7 -> {
            fillGameEntitiesPanel(true);
        });
        imageButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.6
            public void mousePressed(MouseEvent e8) {
                if (SwingUtilities.isLeftMouseButton(e8)) {
                    JPopupMenu menu = new ModpackPopup(ModpackScene.this.localmodpacks);
                    menu.show(imageButton, e8.getX(), e8.getY());
                }
            }
        });
        final ActionListener modpackListener = e8 -> {
            SwingUtilities.invokeLater(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0008: INVOKE  
                  (wrap: java.lang.Runnable : 0x0003: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                  (r4v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackScene A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackScene), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r5v0 'mods' org.tlauncher.tlauncher.ui.swing.GameRadioTextButton A[D('mods' org.tlauncher.tlauncher.ui.swing.GameRadioTextButton), DONT_INLINE])
                  (r6v0 'gameRadioTextButton' org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel A[D('buttons' org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  
                  (r0 I:org.tlauncher.tlauncher.ui.scenes.ModpackScene)
                  (r1 I:org.tlauncher.tlauncher.ui.swing.GameRadioTextButton)
                  (r2 I:org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel)
                 type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.ModpackScene.lambda$null$11(org.tlauncher.tlauncher.ui.swing.GameRadioTextButton, org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel):void)
                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.scenes.ModpackScene.lambda$new$12(org.tlauncher.tlauncher.ui.swing.GameRadioTextButton, org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel, java.awt.event.ActionEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene.class
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:964)
                	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
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
                	... 28 more
                */
            /*
                this = this;
                r0 = r4
                r1 = r5
                r2 = r6
                void r0 = () -> { // java.lang.Runnable.run():void
                    r0.lambda$null$11(r1, r2);
                }
                javax.swing.SwingUtilities.invokeLater(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.ModpackScene.lambda$new$12(org.tlauncher.tlauncher.ui.swing.GameRadioTextButton, org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel, java.awt.event.ActionEvent):void");
        };
        this.localmodpacks.addPopupMenuListener(new PopupMenuListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.8
            public void popupMenuWillBecomeVisible(PopupMenuEvent e9) {
                ModpackScene.this.localmodpacks.addActionListener(modpackListener);
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e9) {
                ModpackScene.this.localmodpacks.removeActionListener(modpackListener);
            }

            public void popupMenuCanceled(PopupMenuEvent e9) {
                ModpackScene.this.localmodpacks.removeActionListener(modpackListener);
            }
        });
        this.manager.addGameListener(GameType.MOD, this.modLeftPanel);
        this.manager.addGameListener(GameType.MOD, this.modsPanel);
        this.manager.addGameListener(GameType.RESOURCEPACK, this.resourceLeftPanel);
        this.manager.addGameListener(GameType.RESOURCEPACK, this.resourcePackPanel);
        this.manager.addGameListener(GameType.MAP, this.mapLeftPanel);
        this.manager.addGameListener(GameType.MAP, this.mapsPanel);
        this.manager.addGameListener(GameType.SHADERPACK, this.shaderpackLeftPanel);
        this.manager.addGameListener(GameType.SHADERPACK, this.shaderpacksPanel);
        this.manager.addGameListener(GameType.MODPACK, this.localmodpacks);
        this.manager.addGameListener(GameType.MODPACK, this.modpacksPanel);
        this.mapGameTypeAndRadioButton.forEach(e9, v -> {
            v.addActionListener(l -> {
                this.searchPanel.setVisible(false);
                this.current = e;
                refillSearchKeys();
                fillLeftPanel(this.current);
                changeEntityView(this.current);
                fillGameEntitiesPanel(true);
            });
        });
        this.modpacks.setSelected(true);
        updaterButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.9
            public void mousePressed(MouseEvent e10) {
                CompletableFuture.runAsync(() -> {
                    DesktopUtil.uncheckCall(() -> {
                        ModpackScene.this.manager.fillVersionTypesAndGameVersion();
                        ModpackScene.this.manager.fillCategories();
                        return null;
                    });
                }, ModpackScene.this.modpackExecutorService).handle(r, t -> {
                    if (Objects.nonNull(t)) {
                        U.log(t);
                        Alert.showLocMessage("modpack.internet.update");
                        return null;
                    }
                    SwingUtilities.invokeLater(() -> {
                        ModpackScene.this.searchPanel.setVisible(!ModpackScene.this.searchPanel.isVisible());
                        if (ModpackScene.this.searchPanel.isVisible()) {
                            if (ModpackScene.this.minecraftVersionTypes.getModel().getSize() < 2) {
                                DefaultComboBoxModel<NameIdDTO> m = ModpackScene.this.minecraftVersionTypes.getModel();
                                List<NameIdDTO> minecraftVersionTypes = ModpackScene.this.manager.getMinecraftVersionTypes();
                                m.getClass();
                                minecraftVersionTypes.forEach((v1) -> {
                                    r1.addElement(v1);
                                });
                                ModpackScene.this.minecraftVersionTypes.setSelectedIndex(0);
                            }
                            NameIdDTO nid = (NameIdDTO) ModpackScene.this.minecraftVersionTypes.getModel().getElementAt(1);
                            DefaultComboBoxModel<GameVersionDTO> m2 = new DefaultComboBoxModel<>();
                            m2.addElement(ModpackScene.this.anyGameVersion);
                            ModpackScene.this.gameVersions.setModel(m2);
                            if (Objects.nonNull(nid) && ModpackScene.this.manager.getGameVersions().containsKey(nid)) {
                                m2.getClass();
                                ModpackScene.this.manager.getGameVersions().get(nid).forEach((v1) -> {
                                    r1.addElement(v1);
                                });
                            }
                            ModpackScene.this.minecraftVersionTypes.setSelectedItem(Objects.isNull(ModpackScene.this.selectedVersionType) ? ModpackScene.this.anyVersionType : ModpackScene.this.selectedVersionType);
                            ModpackScene.this.gameVersions.setSelectedItem(Objects.isNull(ModpackScene.this.selectedGameVersion) ? ModpackScene.this.anyGameVersion : ModpackScene.this.selectedGameVersion);
                            ModpackScene.this.fillCategories();
                        }
                    });
                    return null;
                });
            }
        });
        this.minecraftVersionTypes.addItemListener(item -> {
            NameIdDTO nid = (NameIdDTO) item.getItem();
            DefaultComboBoxModel<GameVersionDTO> m = new DefaultComboBoxModel<>();
            m.addElement(this.anyGameVersion);
            this.gameVersions.setModel(m);
            GameVersionDTO gtmp = this.selectedGameVersion;
            if (item.getStateChange() == 1 && this.manager.getGameVersions().containsKey(nid)) {
                List<GameVersionDTO> list = this.manager.getGameVersions().get(nid);
                m.getClass();
                list.forEach((v1) -> {
                    r1.addElement(v1);
                });
                NameIdDTO g = (NameIdDTO) item.getItem();
                if (this.anyVersionType.equals(g)) {
                    this.selectedVersionType = null;
                } else {
                    this.selectedVersionType = g;
                }
                if (list.contains(gtmp)) {
                    this.gameVersions.setSelectedItem(gtmp);
                } else {
                    this.selectedGameVersion = null;
                }
                fillGameEntitiesPanel(true);
            }
        });
        this.gameVersions.addItemListener(item2 -> {
            if (item2.getStateChange() == 1) {
                GameVersionDTO g = (GameVersionDTO) item2.getItem();
                if (this.anyGameVersion.equals(g)) {
                    this.selectedGameVersion = null;
                } else {
                    this.selectedGameVersion = g;
                }
                fillGameEntitiesPanel(true);
            }
        });
        this.favoriteCheckBox.addItemListener(item3 -> {
            if (item3.getStateChange() == 1) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        TLauncher.getInstance().getProfileManager().findUniqueTlauncherAccount();
                        fillGameEntitiesPanel(true);
                    } catch (RequiredTLAccountException e10) {
                        Alert.showLocError("modpack.right.panel.required.tl.account.title", Localizable.get("modpack.right.panel.required.tl.account", Localizable.get("loginform.button.settings.account")), null);
                        this.favoriteCheckBox.setSelected(false);
                    } catch (SelectedAnyOneTLAccountException e11) {
                        Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
                        this.favoriteCheckBox.setSelected(false);
                    }
                });
            }
        });
        back.addBackListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.10
            public void mousePressed(MouseEvent e10) {
                if (SwingUtilities.isLeftMouseButton(e10)) {
                    ModpackScene.this.searchPanel.setVisible(false);
                }
            }
        });
        this.localmodpacks.addItemListener(e10 -> {
            if (e10.getStateChange() == 1) {
                SwingUtilities.invokeLater(() -> {
                    if (this.searchPanel.isVisible()) {
                        this.searchPanel.setVisible(false);
                    }
                    refillSearchKeys();
                    fillGameEntitiesPanel(true);
                });
            }
        });
        this.categoriesBox.addActionListener(e11 -> {
            fillGameEntitiesPanel(true);
        });
        reset.addActionListener(a -> {
            resetSearch();
            this.minecraftVersionTypes.setSelectedItem(this.anyVersionType);
            this.gameVersions.setSelectedItem(this.anyGameVersion);
            this.favoriteCheckBox.setSelected(false);
            this.categoriesBox.setSelectedIndex(0);
        });
    }

    private void refillSearchKeys() {
        if (GameType.MODPACK.equals(this.current)) {
            resetSearch();
        } else if (isSelectedCompleteVersion()) {
            try {
                ModpackVersionDTO mv = (ModpackVersionDTO) getSelectedCompleteVersion().getModpack().getVersion();
                this.selectedGameVersion = this.manager.getGameVersion(mv);
                this.selectedVersionType = mv.findFirstMinecraftVersionType();
                this.categoriesBox.getModel().cleanAllSelection();
                this.favoriteCheckBox.setSelected(false);
            } catch (IOException e1) {
                U.log(e1);
            }
        } else {
            resetSearch();
        }
    }

    private void resetSearch() {
        this.selectedGameVersion = null;
        this.selectedVersionType = null;
        this.favoriteCheckBox.setSelected(false);
        this.categoriesBox.getModel().cleanAllSelection();
    }

    public static ScrollPane createScrollWrapper(JComponent panel) {
        ScrollPane pane = new ScrollPane((Component) panel, ScrollPane.ScrollBarPolicy.AS_NEEDED, ScrollPane.ScrollBarPolicy.NEVER);
        pane.getVerticalScrollBar().setUnitIncrement(5);
        pane.getVerticalScrollBar().setPreferredSize(new Dimension(13, 0));
        pane.setPreferredSize(new Dimension((int) HttpStatus.SC_OK, 100));
        pane.getVerticalScrollBar().setUI(new ModpackScrollBarUI());
        pane.setBorder(BorderFactory.createEmptyBorder());
        return pane;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createTextField(LocalizableTextField search, final DeferredDocumentListener listener) {
        search.getDocument().addDocumentListener(listener);
        search.addFocusListener(new FocusListener() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.11
            public void focusGained(FocusEvent e) {
                listener.start();
            }

            public void focusLost(FocusEvent e) {
                listener.stop();
            }
        });
    }

    public void prepareView(List<CompleteVersion> modpackVersions) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackScene A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackScene), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'modpackVersions' java.util.List<net.minecraft.launcher.versions.CompleteVersion> A[D('modpackVersions' java.util.List<net.minecraft.launcher.versions.CompleteVersion>), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.scenes.ModpackScene), (r1 I:java.util.List) type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.ModpackScene.lambda$prepareView$23(java.util.List):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.scenes.ModpackScene.prepareView(java.util.List<net.minecraft.launcher.versions.CompleteVersion>):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene.class
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
                r0.lambda$prepareView$23(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.ModpackScene.prepareView(java.util.List):void");
    }

    public void fillGameEntitiesPanel(boolean clean) {
        GameType gt = this.current;
        GameEntityRightPanel gerp = getRightPanelByType(gt);
        if (clean) {
            gerp.setNextPageIndex(0);
            gerp.setNextPage(true);
        }
        if (gerp.isProcessingRequest() || !gerp.isNextPage()) {
            return;
        }
        U.log("request");
        gerp.setProcessingRequest(true);
        GameVersionDTO selectGTmp = this.selectedGameVersion;
        NameIdDTO selectVTTmp = this.selectedVersionType;
        CompletableFuture.runAsync(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0065: INVOKE  
              (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x005a: INVOKE  (r0v18 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
              (wrap: java.lang.Runnable : 0x0051: INVOKE_CUSTOM (r0v17 java.lang.Runnable A[REMOVE]) = 
              (r7v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackScene A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackScene), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r0v1 'gt' org.tlauncher.modpack.domain.client.share.GameType A[D('gt' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
              (r0v15 'selectVTTmp' org.tlauncher.modpack.domain.client.share.NameIdDTO A[D('selectVTTmp' org.tlauncher.modpack.domain.client.share.NameIdDTO), DONT_INLINE])
              (r0v13 'selectGTmp' org.tlauncher.modpack.domain.client.GameVersionDTO A[D('selectGTmp' org.tlauncher.modpack.domain.client.GameVersionDTO), DONT_INLINE])
              (r0v3 'gerp' org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel A[D('gerp' org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel), DONT_INLINE])
              (r8v0 'clean' boolean A[D('clean' boolean), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  
              (r0 I:org.tlauncher.tlauncher.ui.scenes.ModpackScene)
              (r1 I:org.tlauncher.modpack.domain.client.share.GameType)
              (r2 I:org.tlauncher.modpack.domain.client.share.NameIdDTO)
              (r3 I:org.tlauncher.modpack.domain.client.GameVersionDTO)
              (r4 I:org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel)
              (r5 I:boolean)
             type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.ModpackScene.lambda$fillGameEntitiesPanel$26(org.tlauncher.modpack.domain.client.share.GameType, org.tlauncher.modpack.domain.client.share.NameIdDTO, org.tlauncher.modpack.domain.client.GameVersionDTO, org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel, boolean):void)
              (wrap: java.util.concurrent.ExecutorService : 0x0057: IGET  (r1v5 java.util.concurrent.ExecutorService A[REMOVE]) = 
              (r7v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackScene A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackScene), IMMUTABLE_TYPE, THIS])
             org.tlauncher.tlauncher.ui.scenes.ModpackScene.modpackExecutorService java.util.concurrent.ExecutorService)
             type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable, java.util.concurrent.Executor):java.util.concurrent.CompletableFuture)
              (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0060: INVOKE_CUSTOM (r1v7 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
              (r7v0 'this' org.tlauncher.tlauncher.ui.scenes.ModpackScene A[D('this' org.tlauncher.tlauncher.ui.scenes.ModpackScene), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r0v3 'gerp' org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel A[D('gerp' org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel), DONT_INLINE])
              (r0v1 'gt' org.tlauncher.modpack.domain.client.share.GameType A[D('gt' org.tlauncher.modpack.domain.client.share.GameType), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
             call insn: ?: INVOKE  
              (r1 I:org.tlauncher.tlauncher.ui.scenes.ModpackScene)
              (r2 I:org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel)
              (r3 I:org.tlauncher.modpack.domain.client.share.GameType)
              (v3 java.lang.Throwable)
             type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.ModpackScene.lambda$fillGameEntitiesPanel$28(org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel, org.tlauncher.modpack.domain.client.share.GameType, java.lang.Throwable):java.lang.Void)
             type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.scenes.ModpackScene.fillGameEntitiesPanel(boolean):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene.class
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
            Caused by: java.lang.IndexOutOfBoundsException: Index 5 out of bounds for length 5
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
            r0 = r7
            org.tlauncher.modpack.domain.client.share.GameType r0 = r0.current
            r9 = r0
            r0 = r7
            r1 = r9
            org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel r0 = r0.getRightPanelByType(r1)
            r10 = r0
            r0 = r8
            if (r0 == 0) goto L1c
            r0 = r10
            r1 = 0
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r0.setNextPageIndex(r1)
            r0 = r10
            r1 = 1
            r0.setNextPage(r1)
        L1c:
            r0 = r10
            boolean r0 = r0.isProcessingRequest()
            if (r0 != 0) goto L2a
            r0 = r10
            boolean r0 = r0.isNextPage()
            if (r0 != 0) goto L2b
        L2a:
            return
        L2b:
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1 = r0
            r2 = 0
            java.lang.String r3 = "request"
            r1[r2] = r3
            org.tlauncher.util.U.log(r0)
            r0 = r10
            r1 = 1
            r0.setProcessingRequest(r1)
            r0 = r7
            org.tlauncher.modpack.domain.client.GameVersionDTO r0 = r0.selectedGameVersion
            r11 = r0
            r0 = r7
            org.tlauncher.modpack.domain.client.share.NameIdDTO r0 = r0.selectedVersionType
            r12 = r0
            r0 = r7
            r1 = r9
            r2 = r12
            r3 = r11
            r4 = r10
            r5 = r8
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$fillGameEntitiesPanel$26(r1, r2, r3, r4, r5);
            }
            r1 = r7
            java.util.concurrent.ExecutorService r1 = r1.modpackExecutorService
            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0, r1)
            r1 = r7
            r2 = r10
            r3 = r9
            void r1 = (v3) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                return r1.lambda$fillGameEntitiesPanel$28(r2, r3, v3);
            }
            java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.ModpackScene.fillGameEntitiesPanel(boolean):void");
    }

    public void resetSelectedRightElement() {
        getRightPanelByType(this.current).filterRightPanel(this.current);
    }

    private void changeEntityView(GameType gameType) {
        if (this.last == GameType.MODPACK && gameType != GameType.MODPACK) {
            this.layoutCenterPanel.getLayout().show(this.layoutCenterPanel, GameType.NOT_MODPACK.toLowerCase());
        } else if (gameType == GameType.MODPACK) {
            this.layoutCenterPanel.getLayout().show(this.layoutCenterPanel, GameType.MODPACK.toLowerCase());
            this.last = gameType;
            return;
        }
        this.last = this.current;
        this.entitiesPanel.getLayout().show(this.entitiesPanel, gameType.toLowerCase());
    }

    private GameEntityRightPanel getRightPanelByType(GameType type) {
        switch (type) {
            case MOD:
                return this.modsPanel;
            case MAP:
                return this.mapsPanel;
            case RESOURCEPACK:
                return this.resourcePackPanel;
            case MODPACK:
                return this.modpacksPanel;
            case SHADERPACK:
                return this.shaderpacksPanel;
            default:
                throw new NullPointerException();
        }
    }

    private void fillLeftPanel(GameType gt) {
        if (gt == GameType.MODPACK) {
            return;
        }
        if (this.localmodpacks.getSelectedIndex() < 1) {
            getLeftPanelByType(gt).cleanLeftPanel();
            getLeftPanelByType(gt).fireCounterChanged();
        } else {
            this.manager.checkFolderSubGameEntity(this.localmodpacks.getSelectedValue(), gt);
            ModpackVersionDTO version = (ModpackVersionDTO) this.localmodpacks.getSelectedValue().getModpack().getVersion();
            getLeftPanelByType(gt).addElements(version.getByType(gt), gt);
        }
        getLeftPanelByType(gt).revalidate();
        getLeftPanelByType(gt).repaint();
    }

    private GameEntityLeftPanel getLeftPanelByType(GameType type) {
        switch (type) {
            case MOD:
                return this.modLeftPanel;
            case MAP:
                return this.mapLeftPanel;
            case RESOURCEPACK:
                return this.resourceLeftPanel;
            case MODPACK:
            default:
                throw new NullPointerException();
            case SHADERPACK:
                return this.shaderpackLeftPanel;
        }
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        super.onResize();
        this.panel.setLocation((getWidth() / 2) - (this.panel.getWidth() / 2), ((getHeight() - LoginForm.LOGIN_SIZE.height) / 2) - (this.panel.getHeight() / 2));
    }

    public void prepareLocalModpack(List<CompleteVersion> modpackVersions) {
        int index = TLauncher.getInstance().getConfiguration().getInteger("modpack.combobox.index");
        this.localmodpacks.removeAllItems();
        for (CompleteVersion v : modpackVersions) {
            this.localmodpacks.addItem(v);
        }
        if (this.localmodpacks.getModel().getSize() > index) {
            this.localmodpacks.setSelectedIndex(index);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.scenes.PseudoScene, org.tlauncher.tlauncher.ui.swing.AnimatedVisibility
    public void setShown(boolean shown, boolean animate) {
        if (shown) {
            this.localmodpacks.addItemListener(this.modpackBoxListener);
        } else {
            this.localmodpacks.removeItemListener(this.modpackBoxListener);
        }
        super.setShown(shown, animate);
    }

    @Override // org.tlauncher.tlauncher.ui.scenes.PseudoScene, org.tlauncher.tlauncher.ui.swing.AnimatedVisibility
    public void setShown(boolean shown) {
        super.setShown(shown);
    }

    public CompleteVersion getSelectedCompleteVersion() {
        return this.localmodpacks.getSelectedValue();
    }

    public boolean isSelectedCompleteVersion() {
        return this.localmodpacks.getSelectedIndex() > 0;
    }

    public CompleteVersion getCompleteVersion(ModpackDTO dto, VersionDTO versionDTO) {
        return this.localmodpacks.findByModpack(dto, versionDTO);
    }

    private void prepareRightPanel(GameType gt) {
        getRightPanelByType(gt).filterRightPanel(gt);
        fillLeftPanel(gt);
        changeEntityView(gt);
    }

    public GameType getCurrent() {
        return this.current;
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
        if (this.current != GameType.MODPACK) {
            fillLeftPanel(this.current);
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$UserCategory1.class */
    public enum UserCategory1 {
        POPULATE_MONTH,
        FAVORITE,
        NAME,
        POPULATE_ALL_TIME,
        DATE;

        @Override // java.lang.Enum
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$EmptyRightView.class */
    public class EmptyRightView extends ExtendedPanel {
        LocalizableHTMLLabel jLabel;
        private final GameType gameType;

        public EmptyRightView(GameType gameType) {
            this.gameType = gameType;
            setLayout(new BorderLayout());
            this.jLabel = new LocalizableHTMLLabel(CoreConstants.EMPTY_STRING) { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.EmptyRightView.1
                @Override // org.tlauncher.tlauncher.ui.loc.LocalizableLabel, org.tlauncher.tlauncher.ui.loc.LocalizableComponent
                public void updateLocale() {
                    EmptyRightView.this.updateText();
                }
            };
            this.jLabel.setHorizontalAlignment(0);
            this.jLabel.setAlignmentY(0.0f);
            SwingUtil.setFontSize(this.jLabel, 18.0f, 1);
            add((Component) this.jLabel, "Center");
            updateText();
        }

        public void updateText() {
            String value = Localizable.get("modpack.criteria.not.found." + this.gameType.toLowerCase());
            String additional = CoreConstants.EMPTY_STRING;
            if (!this.gameType.equals(GameType.MODPACK) && ModpackScene.this.localmodpacks.getSelectedIndex() > 0) {
                additional = Localizable.get("modpack.search.without.modpack", Localizable.get("modpack.local.box.default"));
            }
            String text = value + additional;
            this.jLabel.setText(String.format("<div WIDTH=%d><center>%s</center></div>", 600, text));
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$PreparingRightView.class */
    private class PreparingRightView extends EmptyRightView {
        public PreparingRightView() {
            super(null);
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.ModpackScene.EmptyRightView
        public void updateText() {
            this.jLabel.setText(String.format("<div WIDTH=%d><center>%s</center></div>", 600, Localizable.get("autoupdater.preparing")));
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$GameCenterPanel.class */
    class GameCenterPanel extends ExtendedPanel {
        Color color241 = new Color(241, 241, 241);
        private final String COLLAPSE = "0";
        private final String NOT_COLLAPSE = "1";

        public GameCenterPanel(GameEntityLeftPanel leftEntityPanel, GameEntityRightPanel rightPanel, GameType gameType) {
            setLayout(new BorderLayout(0, 0));
            ShadowButton collapse = new ShadowButton(this.color241, ColorUtil.COLOR_215, "modpack.element.collapse", "left-array-collapse.png");
            collapse.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            collapse.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 39));
            collapse.setIconTextGap(15);
            collapse.setForeground(Color.BLACK);
            JButton unCollapse = new ImageUdaterButton(new Color((int) HttpStatus.SC_OK, (int) HttpStatus.SC_OK, (int) HttpStatus.SC_OK), new Color((int) HttpStatus.SC_OK, (int) HttpStatus.SC_OK, (int) HttpStatus.SC_OK), "un-collapse-arrow.png", "un-collapse-arrow-up.png");
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setBackground(Color.WHITE);
            JPanel search = new JPanel(new BorderLayout());
            search.setBackground(Color.WHITE);
            LocalizableTextField field = new LocalizableTextField("modpack.search.textfield." + gameType.toLowerCase()) { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.GameCenterPanel.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
                public void onFocusLost() {
                    super.onFocusLost();
                    setForeground(Color.BLACK);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
                public void onFocusGained() {
                    super.onFocusGained();
                    setForeground(Color.BLACK);
                }

                @Override // org.tlauncher.tlauncher.ui.loc.LocalizableTextField, org.tlauncher.tlauncher.ui.loc.LocalizableComponent
                public void updateLocale() {
                    super.updateLocale();
                    setForeground(Color.BLACK);
                }
            };
            JLabel searchLabel = new JLabel(ImageCache.getNativeIcon("search-left-panel.png"));
            searchLabel.setPreferredSize(new Dimension(38, 38));
            search.setPreferredSize(new Dimension(0, 38));
            field.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 7));
            search.add(field, "Center");
            search.add(searchLabel, "West");
            leftPanel.add(search, "North");
            CardLayout emptyLayout = new CardLayout();
            JPanel emptyPanel = new ExtendedPanel((LayoutManager) emptyLayout);
            emptyPanel.add(new EmptyGameEntityView(gameType), ModpackScene.EMPTY);
            JScrollPane pane = ModpackScene.createScrollWrapper(leftEntityPanel);
            pane.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, getPreferredSize().height));
            emptyPanel.add(pane, ModpackScene.NOT_EMPTY);
            leftEntityPanel.addCounterListener(currentCounter -> {
                if (currentCounter == 0) {
                    emptyLayout.show(emptyPanel, ModpackScene.EMPTY);
                } else {
                    emptyLayout.show(emptyPanel, ModpackScene.NOT_EMPTY);
                }
            });
            leftEntityPanel.fireCounterChanged();
            leftPanel.add(emptyPanel, "Center");
            leftPanel.add(collapse, "South");
            final CardLayout card = new CardLayout();
            final JPanel leftCenterPanel = new JPanel(card);
            leftCenterPanel.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 0));
            leftCenterPanel.add(leftPanel, "1");
            leftCenterPanel.add(unCollapse, "0");
            add((Component) leftCenterPanel, "West");
            SwingUtil.changeFontFamily(field, FontTL.ROBOTO_REGULAR, 14, new Color(16, 16, 16));
            SwingUtil.changeFontFamily(collapse, FontTL.ROBOTO_BOLD, 14);
            CardLayout emptyRightLayout = new CardLayout();
            JPanel emptyRightPanel = new ExtendedPanel((LayoutManager) emptyRightLayout);
            EmptyRightView emptyRightView = new EmptyRightView(gameType);
            emptyRightPanel.add(new PreparingRightView(), ModpackScene.PREPARING);
            emptyRightPanel.add(emptyRightView, ModpackScene.EMPTY);
            emptyRightPanel.add(rightPanel, ModpackScene.NOT_EMPTY);
            emptyRightLayout.show(emptyRightPanel, ModpackScene.PREPARING);
            ScrollPane sp = ModpackScene.createScrollWrapper(emptyRightPanel);
            add((Component) sp, "Center");
            sp.getVerticalScrollBar().addAdjustmentListener(new RightPanelAdjustmentListener(rightPanel, ModpackScene.this));
            card.show(leftCenterPanel, "1");
            DeferredDocumentListener listener = new DeferredDocumentListener(500, e -> {
                if (ModpackScene.this.localmodpacks.getSelectedIndex() > 0) {
                    List<? extends GameEntityDTO> list = ((ModpackVersionDTO) ModpackScene.this.localmodpacks.getSelectedValue().getModpack().getVersion()).getByType(gameType);
                    String text = gameType.getValue();
                    if (text == null || text.isEmpty()) {
                        field.addElements(list, gameType);
                    } else {
                        List<GameEntityDTO> res = new BaseModpackFilter(new NameFilter(text)).findAll(list);
                        field.addElements(res, gameType);
                    }
                    field.revalidate();
                    field.repaint();
                }
            }, false);
            collapse.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.GameCenterPanel.2
                public void mousePressed(MouseEvent e2) {
                    if (SwingUtilities.isLeftMouseButton(e2)) {
                        card.show(leftCenterPanel, "0");
                        leftCenterPanel.setPreferredSize(new Dimension(30, 0));
                        GameCenterPanel.this.revalidate();
                        GameCenterPanel.this.repaint();
                    }
                }
            });
            ModpackScene.this.createTextField(field, listener);
            unCollapse.addActionListener(e2 -> {
                card.show(card, "1");
                card.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 0));
                revalidate();
                repaint();
            });
            ModpackScene.this.localmodpacks.addItemListener(e3 -> {
                if (e3.getStateChange() == 1) {
                    emptyRightView.updateText();
                }
            });
        }

        /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$GameCenterPanel$EmptyGameEntityView.class */
        private class EmptyGameEntityView extends ExtendedPanel implements LocalizableComponent {
            private final JLabel notHaveSumEntities;
            private final JLabel intallThem;
            private final GameType gameType;

            public EmptyGameEntityView(GameType gameType) {
                this.gameType = gameType;
                setLayout(new FlowLayout(0, 0, 0));
                JLabel image = new JLabel(ImageCache.getIcon("empty-left-panel.png", 225, 165));
                image.setHorizontalAlignment(0);
                this.notHaveSumEntities = new LocalizableLabel("modpack.left.empty.mod.label." + gameType.toLowerCase());
                this.intallThem = new LocalizableLabel("modpack.left.fast.install");
                this.notHaveSumEntities.setHorizontalAlignment(0);
                this.intallThem.setHorizontalAlignment(0);
                this.notHaveSumEntities.setVerticalAlignment(3);
                this.intallThem.setVerticalAlignment(1);
                this.notHaveSumEntities.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 40));
                this.intallThem.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 40));
                SwingUtil.changeFontFamily(this.notHaveSumEntities, FontTL.ROBOTO_BOLD, 14);
                SwingUtil.changeFontFamily(this.intallThem, FontTL.ROBOTO_BOLD, 14);
                setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
                add((Component) image);
                add((Component) this.notHaveSumEntities);
                add((Component) this.intallThem);
            }

            @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
            public void updateLocale() {
                this.notHaveSumEntities.setText("modpack.left.empty.mod.label." + this.gameType.toLowerCase());
                this.intallThem.setText("modpack.left.fast.install");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$GameLeftElement.class */
    public class GameLeftElement extends ExtendedPanel implements UpdateGameListener {
        private final StateModpackElementButton clickButton;
        private final SubModpackDTO entity;
        private final GameType type;
        private final JLabel leftLabel;
        private final JLabel name = new JLabel();
        private final Dimension DEFAULT_SIZE = new Dimension(ModpackScene.LEFT_WIDTH, 39);
        private final Color MOUSE_UNDER_BACKGROUND = new Color(242, 242, 242);
        private final Color BACKGROUND_COLOR = new Color(235, 235, 235);

        public GameLeftElement(SubModpackDTO e, final GameType type) {
            this.type = type;
            setPreferredSize(this.DEFAULT_SIZE);
            setMaximumSize(this.DEFAULT_SIZE);
            setMinimumSize(this.DEFAULT_SIZE);
            setLayout(new BorderLayout());
            this.entity = e;
            SpringLayout layout = new SpringLayout();
            JPanel wrapperEast = new ExtendedPanel((LayoutManager) layout);
            wrapperEast.setPreferredSize(new Dimension(67, 39));
            this.clickButton = new StateModpackElementButton(e, type);
            if (this.entity.isUserInstall()) {
                this.leftLabel = new JLabel(ImageCache.getIcon("modpack-element-left-hanlde.png"));
            } else {
                this.leftLabel = new JLabel(ImageCache.getIcon("modpack-element-left.png"));
            }
            layout.putConstraint("West", this.clickButton, 0, "West", wrapperEast);
            layout.putConstraint("East", this.clickButton, -16, "East", wrapperEast);
            layout.putConstraint("North", this.clickButton, 10, "North", wrapperEast);
            layout.putConstraint("South", this.clickButton, -9, "South", wrapperEast);
            wrapperEast.add(this.clickButton);
            this.leftLabel.setPreferredSize(new Dimension(39, 29));
            add((Component) this.leftLabel, "West");
            final Border nameBorder = BorderFactory.createEmptyBorder(10, 1, 10, 1);
            this.name.setBorder(nameBorder);
            add((Component) this.name, "Center");
            if (GameType.MAP != type) {
                add((Component) wrapperEast, "East");
            }
            initGameEntity();
            setOpaque(true);
            SwingUtil.changeFontFamily(this.name, FontTL.ROBOTO_BOLD, 14, ColorUtil.COLOR_16);
            MouseAdapter click = new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.GameLeftElement.1
                public void mousePressed(MouseEvent e2) {
                    if (!SwingUtilities.isRightMouseButton(e2)) {
                        if (!GameLeftElement.this.entity.isUserInstall()) {
                            ModpackScene.this.manager.showFullGameEntity(GameLeftElement.this.entity, type);
                            return;
                        }
                        return;
                    }
                    JPopupMenu popupMenu = new ModpackPopup();
                    JMenuItem delete = new ModpackPopup.ModpackMenuItem("modpack.popup.delete");
                    popupMenu.add(delete);
                    GameType gameType = type;
                    delete.addActionListener(e1 -> {
                        popupMenu.setVisible(false);
                        List<GameEntityDTO> list = ModpackScene.this.manager.findDependenciesFromGameEntityDTO(GameLeftElement.this.entity);
                        StringBuilder b = ModpackUtil.buildMessage(list);
                        if (list.isEmpty()) {
                            ModpackScene.this.manager.removeEntity(GameLeftElement.this.entity, GameLeftElement.this.entity.getVersion(), popupMenu, false);
                        } else if (Alert.showQuestion(CoreConstants.EMPTY_STRING, Localizable.get("modpack.left.element.remove.question", GameLeftElement.this.entity.getName(), b.toString()))) {
                            ModpackScene.this.manager.removeEntity(GameLeftElement.this.entity, GameLeftElement.this.entity.getVersion(), popupMenu, false);
                        }
                    });
                    popupMenu.show(e2.getComponent(), e2.getX(), e2.getY());
                }
            };
            MouseAdapter backgroundListener = new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.GameLeftElement.2
                public void mouseEntered(MouseEvent e2) {
                    GameLeftElement.this.setBackground(GameLeftElement.this.MOUSE_UNDER_BACKGROUND);
                }

                public void mouseExited(MouseEvent e2) {
                    GameLeftElement.this.setBackground(GameLeftElement.this.BACKGROUND_COLOR);
                }
            };
            final AtomicInteger padding = new AtomicInteger();
            final Timer timer = new Timer(30, e12 -> {
                int width = this.name.getWidth();
                if (padding.get() + width < 0) {
                    padding.set(width - 20);
                }
                this.name.setBorder(new EmptyBorder(0, padding.getAndDecrement(), 0, 0));
            });
            this.name.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.GameLeftElement.3
                public void mouseEntered(MouseEvent e2) {
                    padding.set(0);
                    if (GameLeftElement.this.name.getPreferredSize().width > 120) {
                        timer.restart();
                    }
                }

                public void mouseExited(MouseEvent e2) {
                    timer.stop();
                    GameLeftElement.this.name.setBorder(nameBorder);
                }
            });
            addMouseListenerOriginally(click);
            this.name.addMouseListener(click);
            this.leftLabel.addMouseListener(click);
            addMouseListener(backgroundListener);
            addMouseListenerOriginally(backgroundListener);
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener
        public void initGameEntity() {
            this.name.setText(this.entity.getName());
            if (this.type != GameType.MAP) {
                this.clickButton.setState(this.entity.getStateGameElement());
            }
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener
        public void processingActivation() {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener
        public void processingInstall() {
        }

        public SubModpackDTO getEntity() {
            return this.entity;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Rectangle rec = getVisibleRect();
            SwingUtil.paintShadowLine(rec, g, getBackground().getRed(), 12);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$SearchPanel.class */
    public class SearchPanel extends JPanel {
        private final LocalizableTextField field;

        public SearchPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(194, 55));
            SpringLayout spring = new SpringLayout();
            setLayout(spring);
            this.field = new LocalizableTextField("modpack.search.text") { // from class: org.tlauncher.tlauncher.ui.scenes.ModpackScene.SearchPanel.1
                public void setBackColor() {
                    if (!OS.is(OS.LINUX)) {
                        setForeground(Color.WHITE);
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
                public void onFocusLost() {
                    super.onFocusLost();
                    setBackColor();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
                public void onFocusGained() {
                    super.onFocusGained();
                    setBackColor();
                }

                @Override // org.tlauncher.tlauncher.ui.loc.LocalizableTextField, org.tlauncher.tlauncher.ui.loc.LocalizableComponent
                public void updateLocale() {
                    super.updateLocale();
                    setBackColor();
                }
            };
            this.field.setOpaque(false);
            this.field.setBorder(BorderFactory.createEmptyBorder());
            this.field.setCaretColor(Color.WHITE);
            if (!OS.is(OS.LINUX)) {
                SwingUtil.changeFontFamily(this.field, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
            }
            spring.putConstraint("West", this.field, 17, "West", this);
            spring.putConstraint("East", this.field, -50, "East", this);
            spring.putConstraint("North", this.field, 17, "North", this);
            spring.putConstraint("South", this.field, -20, "South", this);
            add(this.field);
            Color lineYellow = new Color(254, 254, 168);
            JPanel linePanel = new JPanel();
            linePanel.setBackground(lineYellow);
            spring.putConstraint("West", linePanel, 17, "West", this);
            spring.putConstraint("East", linePanel, -17, "East", this);
            spring.putConstraint("North", linePanel, -12, "South", this);
            spring.putConstraint("South", linePanel, -11, "South", this);
            add(linePanel);
            JLabel image = new JLabel(ImageCache.getNativeIcon("search-modpack-element.png"));
            spring.putConstraint("West", image, -35, "East", this);
            spring.putConstraint("East", image, -19, "East", this);
            spring.putConstraint("North", image, 18, "North", this);
            spring.putConstraint("South", image, -21, "South", this);
            add(image);
            DeferredDocumentListener listener = new DeferredDocumentListener(1000, new ModpackSearchListener(ModpackScene.this, CoreConstants.EMPTY_STRING, this.field), false);
            ModpackScene.this.createTextField(this.field, listener);
        }

        public boolean isNotEmpty() {
            return (this.field.getValue() == null || this.field.getValue().isEmpty()) ? false : true;
        }

        public String getSearchLine() {
            return this.field.getValue();
        }

        public void reset() {
            this.field.setValue((Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/ModpackScene$GameEntityLeftPanel.class */
    public class GameEntityLeftPanel extends GameEntityPanel {
        private final List<ElementCounterListener> observer;

        public GameEntityLeftPanel() {
            setLayout(new BoxLayout(this, 1));
            this.observer = new ArrayList();
        }

        public void addElements(List<? extends SubModpackDTO> list, GameType type) {
            cleanLeftPanel();
            list.stream().forEach(e -> {
                add((Component) new GameLeftElement(type, type));
            });
            fireCounterChanged();
        }

        protected void cleanLeftPanel() {
            for (Component c : Arrays.asList(getComponents())) {
                if (c instanceof GameLeftElement) {
                    remove(c);
                }
            }
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void activationStarted(GameEntityDTO e) {
            for (Component component : Arrays.asList(getComponents())) {
                if (component instanceof GameLeftElement) {
                    ((GameLeftElement) component).processingActivation();
                }
            }
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void removeEntity(GameEntityDTO e) {
            GameLeftElement elem = find(e);
            if (elem != null) {
                remove(elem);
                revalidate();
                repaint();
            }
            fireCounterChanged();
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void activation(GameEntityDTO e) {
            for (Component component : Arrays.asList(getComponents())) {
                if (component instanceof GameLeftElement) {
                    ((GameLeftElement) component).initGameEntity();
                }
            }
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void activationError(GameEntityDTO e, Throwable t) {
            for (Component component : Arrays.asList(getComponents())) {
                if (component instanceof GameLeftElement) {
                    ((GameLeftElement) component).initGameEntity();
                }
            }
        }

        private GameLeftElement find(GameEntityDTO e) {
            for (Component component : Arrays.asList(getComponents())) {
                if (component instanceof GameLeftElement) {
                    GameLeftElement el = (GameLeftElement) component;
                    if (el.getEntity().getId().equals(e.getId())) {
                        return el;
                    }
                }
            }
            return null;
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void installEntity(GameEntityDTO e, GameType type) {
            add((Component) new GameLeftElement((SubModpackDTO) e, type));
            revalidate();
            repaint();
            fireCounterChanged();
        }

        public void fireCounterChanged() {
            for (ElementCounterListener el : this.observer) {
                el.changeCounter(getComponentCount());
            }
        }

        public void addCounterListener(ElementCounterListener l) {
            this.observer.add(l);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillCategories() {
        this.categoriesBox.setModel(new CategoryComboBoxModel(this.manager.getLocalCategories(this.current)));
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener
    public void updateFavoriteValue() {
        SwingUtilities.invokeLater(() -> {
            getRightPanelByType(this.current).filterRightPanel(this.current);
            repaint();
        });
    }
}
