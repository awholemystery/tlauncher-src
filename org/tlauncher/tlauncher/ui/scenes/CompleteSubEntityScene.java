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
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.compress.harmony.pack200.PackingOptions;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.site.CommonPage;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.button.StatusStarButton;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
import org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel;
import org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener;
import org.tlauncher.tlauncher.ui.listener.mods.VersionsAdjustmentListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton;
import org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton;
import org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableVersionButton;
import org.tlauncher.tlauncher.ui.loc.modpack.UpInstallButton;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.menu.ModpackCategoryPopupMenu;
import org.tlauncher.tlauncher.ui.modpack.DiscussionPanel;
import org.tlauncher.tlauncher.ui.modpack.GroupPanel;
import org.tlauncher.tlauncher.ui.modpack.PicturePanel;
import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
import org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm;
import org.tlauncher.tlauncher.ui.server.BackPanel;
import org.tlauncher.tlauncher.ui.swing.GameRadioButton;
import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
import org.tlauncher.tlauncher.ui.swing.TextWrapperLabel;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.renderer.JTableButtonRenderer;
import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene.class */
public class CompleteSubEntityScene extends PseudoScene implements UpdateFavoriteValueListener {
    protected final ExtendedPanel panel;
    private JLayeredPane layeredPane;
    protected final String REVIEW_S = "REVIEW";
    protected final String VERSION_S = "VERSIONS";
    protected final String PICTURES_S = "PICTURES";
    protected final String DISCUSSION_S = "DISCUSSION";
    protected final ModpackManager manager;
    protected FullGameEntity fullGameEntity;
    public static final int BUTTON_PANEL_SUB_VIEW = 130;
    private static final int activeColumn = 5;
    private CommentCreationForm commentCreationForm;
    public static final AtomicBoolean b = new AtomicBoolean(false);
    private static Executor singleDownloadExecutor = (Executor) TLauncher.getInjector().getInstance(Key.get(Executor.class, Names.named("singleDownloadExecutor")));

    public CompleteSubEntityScene(MainPane main) {
        super(main);
        this.panel = new ExtendedPanel((LayoutManager) new GridLayout(1, 1, 0, 0));
        this.REVIEW_S = "REVIEW";
        this.VERSION_S = "VERSIONS";
        this.PICTURES_S = "PICTURES";
        this.DISCUSSION_S = "DISCUSSION";
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        this.layeredPane = new JLayeredPane() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.1
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        this.panel.setOpaque(true);
        this.panel.setForeground(Color.WHITE);
        this.panel.setSize(ModpackScene.SIZE);
        this.layeredPane.setSize(ModpackScene.SIZE);
        this.commentCreationForm = (CommentCreationForm) TLauncher.getInjector().getInstance(CommentCreationForm.class);
        this.layeredPane.add(this.commentCreationForm);
        this.layeredPane.add(this.panel);
        add((Component) this.layeredPane);
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        super.onResize();
        this.layeredPane.setLocation((getWidth() / 2) - (this.layeredPane.getWidth() / 2), ((getHeight() - LoginForm.LOGIN_SIZE.height) / 2) - (this.layeredPane.getHeight() / 2));
    }

    public void showFullGameEntity(GameEntityDTO gameEntityDTO, final GameType type) {
        clean(type);
        U.debug("open " + gameEntityDTO.getName() + " " + U.memoryStatus());
        BackPanel backPanel = new BackPanel(CoreConstants.EMPTY_STRING, new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.2
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    CompleteSubEntityScene.this.getMainPane().setScene(CompleteSubEntityScene.this.getMainPane().modpackScene);
                    CompleteSubEntityScene.this.clean(type);
                    SwingUtilities.invokeLater(() -> {
                        CompleteSubEntityScene.this.getMainPane().modpackScene.resetSelectedRightElement();
                    });
                }
            }
        }, ImageCache.getIcon("back-arrow.png"));
        this.fullGameEntity = new FullGameEntity(gameEntityDTO, backPanel, type);
        this.panel.removeAll();
        this.panel.add((Component) this.fullGameEntity);
        if (getMainPane().getScene() != this) {
            getMainPane().setScene(this);
            return;
        }
        this.panel.revalidate();
        this.panel.repaint();
    }

    protected void clean(GameType type) {
        if (this.fullGameEntity != null) {
            this.panel.remove(this.fullGameEntity);
            this.fullGameEntity.clearContent();
            this.manager.removeGameListener(type, this.fullGameEntity);
            this.fullGameEntity = null;
        }
    }

    public void showModpackElement(GameEntityDTO completeGameEntity, final GameType type) {
        clean(type);
        BackPanel backPanel = new BackPanel(CoreConstants.EMPTY_STRING, new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.3
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    CompleteSubEntityScene.this.getMainPane().setScene(CompleteSubEntityScene.this.getMainPane().modpackEnitityScene);
                    CompleteSubEntityScene.this.clean(type);
                }
            }
        }, ImageCache.getIcon("back-arrow.png"));
        this.fullGameEntity = new FullGameEntity(completeGameEntity, backPanel, type);
        this.panel.add((Component) this.fullGameEntity);
        revalidate();
        repaint();
        getMainPane().setScene(this);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$ImagePanel.class */
    public static class ImagePanel extends JLayeredPane {
        private GameRightButton gameRightButton;
        private static final ConcurrentLinkedDeque<List<String>> images = new ConcurrentLinkedDeque<>();
        private final GameEntityDTO entity;
        private final JLabel label;

        public ImagePanel(GameEntityDTO entity) {
            this.entity = entity;
            setOpaque(true);
            this.label = new JLabel() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.ImagePanel.1
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImagePanel.this.initPicture(g);
                }
            };
            this.label.setOpaque(true);
            add(this.label, 1);
            this.label.setBounds(0, 0, 111, 111);
        }

        public void addMoapckActButton(GameRightButton actButton) {
            this.gameRightButton = actButton;
            add(actButton, 0);
            actButton.setBounds(10, 80, 90, 23);
        }

        public void initPicture(Graphics g) {
            if (this.entity.getPicture() != null) {
                try {
                    List<String> picture = ModpackUtil.getPictureURL(this.entity.getPicture(), "logo");
                    Optional<String> op = picture.stream().filter(ImageCache::imageInCache).findFirst();
                    if (!op.isPresent()) {
                        images.push(picture);
                        if (images.size() == 1) {
                            loadImages();
                        }
                    } else {
                        BufferedImage loadImage = ImageCache.loadImage(picture, false);
                        if (loadImage != null) {
                            g.drawImage(loadImage, 0, 0, (ImageObserver) null);
                        }
                    }
                } catch (Exception e) {
                    U.log(e);
                }
            }
        }

        private void loadImages() {
            CompletableFuture.runAsync(() -> {
                try {
                    Set<List<String>> set = new HashSet<>();
                    Thread.sleep(100L);
                    int size = images.size() > 3 ? 3 : images.size();
                    for (int i = 0; i < size; i++) {
                        set.add(images.pop());
                    }
                    images.clear();
                    List<CompletableFuture<Void>> c = new ArrayList<>();
                    for (List<String> list : set) {
                        c.add(CompletableFuture.runAsync(() -> {
                            try {
                                ImageCache.loadImage(list, false);
                            } catch (Throwable e) {
                                U.log(e);
                            }
                        }));
                    }
                    try {
                        CompletableFuture.allOf((CompletableFuture[]) c.toArray(new CompletableFuture[0])).get();
                    } catch (InterruptedException | ExecutionException e) {
                        U.log("problem with pictures", e);
                    }
                    if (this.gameRightButton != null) {
                        this.gameRightButton.updateRow();
                    }
                    this.label.repaint();
                } catch (Exception e2) {
                    U.log(e2);
                }
            }, CompleteSubEntityScene.singleDownloadExecutor);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$DescriptionGamePanel.class */
    public static class DescriptionGamePanel extends JPanel {
        protected StatusStarButton statusStarButton;
        protected JLabel name;
        protected JTextArea description;
        protected JLabel downloadLabel;
        protected JLabel updateLabel;
        protected JLabel gameVersion;
        protected ImagePanel imagePanel;
        private final int gupPair = 30;
        private final int gup = 5;
        private final SimpleDateFormat format = new SimpleDateFormat("dd MMMM YYYY", Localizable.get().getSelected());
        protected SpringLayout descriptionLayout = new SpringLayout();

        public StatusStarButton getStatusStarButton() {
            return this.statusStarButton;
        }

        public DescriptionGamePanel(GameEntityDTO entity, GameType type) {
            this.statusStarButton = new StatusStarButton(entity, type);
            setLayout(this.descriptionLayout);
            this.imagePanel = new ImagePanel(entity);
            ExtendedPanel descriptionEntityPanel = new ExtendedPanel();
            this.name = new JLabel(entity.getName());
            JLabel authorLabel = new LocalizableLabel("modpack.complete.author");
            JLabel authorValue = new JLabel(entity.getAuthor());
            this.downloadLabel = new LocalizableLabel("modpack.description.download");
            JLabel downloadValue = new JLabel(getStringDownloadingCount(entity.getDownloadALL()));
            this.updateLabel = new LocalizableLabel("modpack.description.date");
            JLabel updateValue = new JLabel(this.format.format(new Date(entity.getUpdate().longValue())));
            this.gameVersion = new JLabel();
            JLabel gameVersionLabel = new LocalizableLabel("modpack.creation.version.game");
            if (Objects.isNull(entity.getLastGameVersion())) {
                this.gameVersion.setVisible(false);
                gameVersionLabel.setVisible(false);
            } else {
                this.gameVersion.setText(entity.getLastGameVersion().getName());
            }
            this.description = new TextWrapperLabel(entity.getShortDescription());
            this.description.setVisible(false);
            authorValue.setHorizontalAlignment(2);
            downloadValue.setHorizontalAlignment(2);
            updateValue.setHorizontalAlignment(2);
            this.gameVersion.setHorizontalAlignment(2);
            SwingUtil.changeFontFamily(this.name, FontTL.ROBOTO_BOLD, 18);
            SwingUtil.changeFontFamily(authorLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_193);
            SwingUtil.changeFontFamily(this.downloadLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(gameVersionLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(this.updateLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(authorValue, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
            SwingUtil.changeFontFamily(downloadValue, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
            SwingUtil.changeFontFamily(updateValue, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
            SwingUtil.changeFontFamily(this.gameVersion, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
            SwingUtil.changeFontFamily(this.description, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_64);
            SpringLayout descriptionSpring = new SpringLayout();
            descriptionEntityPanel.setLayout(descriptionSpring);
            this.descriptionLayout.putConstraint("West", this.imagePanel, 66, "West", this);
            this.descriptionLayout.putConstraint("East", this.imagePanel, 177, "West", this);
            this.descriptionLayout.putConstraint("North", this.imagePanel, 25, "North", this);
            this.descriptionLayout.putConstraint("South", this.imagePanel, -25, "South", this);
            add(this.imagePanel);
            this.descriptionLayout.putConstraint("West", descriptionEntityPanel, 13, "East", this.imagePanel);
            this.descriptionLayout.putConstraint("East", descriptionEntityPanel, 0, "East", this);
            this.descriptionLayout.putConstraint("North", descriptionEntityPanel, 25, "North", this);
            this.descriptionLayout.putConstraint("South", descriptionEntityPanel, -20, "South", this);
            add(descriptionEntityPanel);
            descriptionSpring.putConstraint("West", this.name, 0, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("East", this.name, 250, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("North", this.name, 0, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", this.name, 23, "North", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) this.name);
            descriptionSpring.putConstraint("West", authorLabel, 0, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("East", authorLabel, authorLabel.getPreferredSize().width, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("North", authorLabel, 23, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", authorLabel, 42, "North", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) authorLabel);
            descriptionSpring.putConstraint("West", authorValue, 5, "East", authorLabel);
            descriptionSpring.putConstraint("East", authorValue, 0, "East", descriptionEntityPanel);
            descriptionSpring.putConstraint("North", authorValue, 23, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", authorValue, 42, "North", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) authorValue);
            descriptionSpring.putConstraint("West", this.description, 0, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("East", this.description, -100, "East", descriptionEntityPanel);
            descriptionSpring.putConstraint("North", this.description, 46, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", this.description, 85, "North", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) this.description);
            descriptionSpring.putConstraint("West", this.downloadLabel, 0, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("East", this.downloadLabel, this.downloadLabel.getPreferredSize().width, "West", descriptionEntityPanel);
            descriptionSpring.putConstraint("North", this.downloadLabel, 90, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", this.downloadLabel, 0, "South", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) this.downloadLabel);
            descriptionSpring.putConstraint("West", downloadValue, 5, "East", this.downloadLabel);
            descriptionSpring.putConstraint("East", downloadValue, downloadValue.getPreferredSize().width + 5, "East", this.downloadLabel);
            descriptionSpring.putConstraint("North", downloadValue, 90, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", downloadValue, 0, "South", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) downloadValue);
            descriptionSpring.putConstraint("West", this.updateLabel, 30, "East", downloadValue);
            descriptionSpring.putConstraint("East", this.updateLabel, 30 + this.updateLabel.getPreferredSize().width, "East", downloadValue);
            descriptionSpring.putConstraint("North", this.updateLabel, 90, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", this.updateLabel, 0, "South", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) this.updateLabel);
            descriptionSpring.putConstraint("West", updateValue, 5, "East", this.updateLabel);
            descriptionSpring.putConstraint("East", updateValue, updateValue.getPreferredSize().width + 5, "East", this.updateLabel);
            descriptionSpring.putConstraint("North", updateValue, 90, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", updateValue, 0, "South", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) updateValue);
            descriptionSpring.putConstraint("West", gameVersionLabel, 30, "East", updateValue);
            descriptionSpring.putConstraint("East", gameVersionLabel, 30 + gameVersionLabel.getPreferredSize().width, "East", updateValue);
            descriptionSpring.putConstraint("North", gameVersionLabel, 90, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", gameVersionLabel, 0, "South", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) gameVersionLabel);
            descriptionSpring.putConstraint("West", this.gameVersion, 5, "East", gameVersionLabel);
            descriptionSpring.putConstraint("East", this.gameVersion, this.gameVersion.getPreferredSize().width + 5, "East", gameVersionLabel);
            descriptionSpring.putConstraint("North", this.gameVersion, 90, "North", descriptionEntityPanel);
            descriptionSpring.putConstraint("South", this.gameVersion, 0, "South", descriptionEntityPanel);
            descriptionEntityPanel.add((Component) this.gameVersion);
            ExtendedPanel pictureCategories = new ExtendedPanel();
            int count = 0;
            for (CategoryDTO c : entity.getCategories()) {
                try {
                    Icon icon = ImageCache.getNativeIcon("category/" + c.getName() + ".png");
                    count++;
                    final JLabel label = new JLabel(icon);
                    label.setHorizontalAlignment(0);
                    label.setAlignmentY(0.5f);
                    pictureCategories.add((Component) label);
                    final ModpackCategoryPopupMenu popupMenu = new ModpackCategoryPopupMenu(c, label);
                    label.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.DescriptionGamePanel.1
                        public void mouseEntered(MouseEvent e) {
                            popupMenu.show(label, e.getX() + 15, e.getY() + 15);
                        }

                        public void mouseExited(MouseEvent e) {
                            popupMenu.setVisible(false);
                        }
                    });
                } catch (Throwable th) {
                    U.log("problem with category " + c.getName());
                }
            }
            pictureCategories.setLayout(new GridLayout(1, count, 0, 10));
            this.descriptionLayout.putConstraint("West", pictureCategories, -200, "East", this);
            this.descriptionLayout.putConstraint("East", pictureCategories, (-25) - ((5 - count) * 35), "East", this);
            this.descriptionLayout.putConstraint("North", pictureCategories, 24, "North", this);
            this.descriptionLayout.putConstraint("South", pictureCategories, 50, "North", this);
            add(pictureCategories);
            this.descriptionLayout.putConstraint("West", this.statusStarButton, -38, "East", this);
            this.descriptionLayout.putConstraint("East", this.statusStarButton, -25, "East", this);
            this.descriptionLayout.putConstraint("North", this.statusStarButton, -35, "South", this);
            this.descriptionLayout.putConstraint("South", this.statusStarButton, -22, "South", this);
            add(this.statusStarButton);
        }

        private String getStringDownloadingCount(Long i) {
            String res;
            if (i.longValue() < 1000) {
                return i.toString();
            }
            if (i.longValue() < PackingOptions.SEGMENT_LIMIT) {
                res = (i.longValue() / 1000) + " " + Localizable.get("modpack.thousand");
            } else {
                res = (i.longValue() / PackingOptions.SEGMENT_LIMIT) + " " + Localizable.get("modpack.million");
            }
            if ("en".equals(TLauncher.getInstance().getConfiguration().getLocale().getLanguage())) {
                res = res.replace(" ", CoreConstants.EMPTY_STRING);
            }
            return res;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$BaseSubtypeModel.class */
    public abstract class BaseSubtypeModel<T extends BaseModelElement> extends GameEntityTableModel {
        protected List<T> list;
        protected final SimpleDateFormat format;

        public abstract GameEntityDTO getRowObject(int i);

        private BaseSubtypeModel() {
            super();
            this.list = new ArrayList();
            this.format = new SimpleDateFormat("dd/MM/YYYY", Localizable.get().getSelected());
        }

        public int find(GameEntityDTO entity) {
            for (int i = 0; i < this.list.size(); i++) {
                if (entity.getId().equals(this.list.get(i).getEntity().getId())) {
                    return i;
                }
            }
            return -1;
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
            int index = find(e);
            if (index != -1) {
                this.list.get(index).getModpackActButton().reset();
                fireTableCellUpdated(index, 5);
            }
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void installEntity(GameEntityDTO e, GameType type) {
            int index = find(e);
            if (index != -1) {
                this.list.get(index).getModpackActButton().setTypeButton(ModpackActButton.REMOVE);
                fireTableCellUpdated(index, 5);
            }
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void removeEntity(GameEntityDTO e) {
            int index = find(e);
            if (index != -1) {
                this.list.get(index).getModpackActButton().setTypeButton(ModpackActButton.INSTALL);
                fireTableCellUpdated(index, 5);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$BaseModelElement.class */
    public static class BaseModelElement {
        private ModpackActButton modpackActButton;
        private GameEntityDTO entity;

        public void setModpackActButton(ModpackActButton modpackActButton) {
            this.modpackActButton = modpackActButton;
        }

        public void setEntity(GameEntityDTO entity) {
            this.entity = entity;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof BaseModelElement) {
                BaseModelElement other = (BaseModelElement) o;
                if (other.canEqual(this)) {
                    Object this$modpackActButton = getModpackActButton();
                    Object other$modpackActButton = other.getModpackActButton();
                    if (this$modpackActButton == null) {
                        if (other$modpackActButton != null) {
                            return false;
                        }
                    } else if (!this$modpackActButton.equals(other$modpackActButton)) {
                        return false;
                    }
                    Object this$entity = getEntity();
                    Object other$entity = other.getEntity();
                    return this$entity == null ? other$entity == null : this$entity.equals(other$entity);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof BaseModelElement;
        }

        public int hashCode() {
            Object $modpackActButton = getModpackActButton();
            int result = (1 * 59) + ($modpackActButton == null ? 43 : $modpackActButton.hashCode());
            Object $entity = getEntity();
            return (result * 59) + ($entity == null ? 43 : $entity.hashCode());
        }

        public String toString() {
            return "CompleteSubEntityScene.BaseModelElement(modpackActButton=" + getModpackActButton() + ", entity=" + getEntity() + ")";
        }

        public BaseModelElement(ModpackActButton modpackActButton, GameEntityDTO entity) {
            this.modpackActButton = modpackActButton;
            this.entity = entity;
        }

        public ModpackActButton getModpackActButton() {
            return this.modpackActButton;
        }

        public GameEntityDTO getEntity() {
            return this.entity;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$VersionModelElement.class */
    public class VersionModelElement extends BaseModelElement {
        private VersionDTO version;

        public void setVersion(VersionDTO version) {
            this.version = version;
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseModelElement
        public String toString() {
            return "CompleteSubEntityScene.VersionModelElement(version=" + getVersion() + ")";
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseModelElement
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof VersionModelElement) {
                VersionModelElement other = (VersionModelElement) o;
                if (other.canEqual(this) && super.equals(o)) {
                    Object this$version = getVersion();
                    Object other$version = other.getVersion();
                    return this$version == null ? other$version == null : this$version.equals(other$version);
                }
                return false;
            }
            return false;
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseModelElement
        protected boolean canEqual(Object other) {
            return other instanceof VersionModelElement;
        }

        @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseModelElement
        public int hashCode() {
            int result = super.hashCode();
            Object $version = getVersion();
            return (result * 59) + ($version == null ? 43 : $version.hashCode());
        }

        public VersionDTO getVersion() {
            return this.version;
        }

        public VersionModelElement(ModpackActButton modpackActButton, GameEntityDTO entity, VersionDTO version) {
            super(modpackActButton, entity);
            this.version = version;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$GameEntityTableModel.class */
    protected abstract class GameEntityTableModel extends AbstractTableModel implements GameEntityListener {
        /* JADX INFO: Access modifiers changed from: protected */
        public GameEntityTableModel() {
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

        public void processingStarted(GameEntityDTO e, VersionDTO version) {
        }

        public void installEntity(GameEntityDTO e, GameType type) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void installEntity(CompleteVersion e) {
        }

        public void removeEntity(GameEntityDTO e) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void removeCompleteVersion(CompleteVersion e) {
        }

        public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void populateStatus(GameEntityDTO status, GameType type, boolean state) {
        }

        @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
        public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$ModpackTable.class */
    protected class ModpackTable extends JTable {
        protected void init() {
            setRowHeight(58);
            getColumnModel().setColumnSelectionAllowed(false);
            setShowVerticalLines(false);
            setCellSelectionEnabled(false);
            setGridColor(ColorUtil.COLOR_244);
            JTableHeader header = getTableHeader();
            header.setPreferredSize(new Dimension(header.getPreferredSize().width - 20, 48));
            header.setDefaultRenderer(new TableCellRenderer() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.ModpackTable.1
                final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    DefaultTableCellRenderer comp = this.cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    comp.setBorder(BorderFactory.createEmptyBorder());
                    comp.setBackground(new Color(63, 186, 255));
                    comp.setHorizontalAlignment(0);
                    SwingUtil.changeFontFamily(comp, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
                    return comp;
                }
            });
            ModpackTableRenderer centerRenderer = new ModpackTableRenderer();
            for (int i = 0; i < getModel().getColumnCount() - 1; i++) {
                getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            getColumnModel().getColumn(1).setPreferredWidth(250);
            getTableHeader().setReorderingAllowed(false);
            setDefaultEditor(BaseModelElement.class, new JTableButtonRenderer());
            setDefaultRenderer(BaseModelElement.class, new JTableButtonRenderer());
        }

        public ModpackTable(AbstractTableModel model) {
            super(model);
            init();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$ModpackTableRenderer.class */
    public class ModpackTableRenderer extends DefaultTableCellRenderer {
        protected ModpackTableRenderer() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            DefaultTableCellRenderer cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cell.setHorizontalAlignment(0);
            if (column == 1) {
                cell.setHorizontalAlignment(2);
            }
            SwingUtil.changeFontFamily(cell, FontTL.ROBOTO_REGULAR, 12, ColorUtil.COLOR_25);
            cell.setFocusable(false);
            if (hasFocus) {
                setBorder(BorderFactory.createEmptyBorder());
            }
            return cell;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$FullGameEntity.class */
    public class FullGameEntity extends GameEntityPanel {
        private final Color UP_BACKGROUND;
        private final UpInstallButton installButton;
        private final GameEntityDTO entity;
        private final GameType type;
        private final DescriptionGamePanel viewEntity;
        private final Injector injector;
        private final GroupPanel centerButtons;
        private final JPanel centerView;
        private JTable table;
        private Integer nextPageIndex;
        private boolean nextPage;
        private boolean processingRequest;
        private HtmlTextPane descriptionFull;
        private JScrollPane jScrollPane;

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

        public GroupPanel getCenterButtons() {
            return this.centerButtons;
        }

        public void clearContent() {
            U.log("clean");
            this.descriptionFull.setText(CoreConstants.EMPTY_STRING);
        }

        public JPanel getCenterView() {
            return this.centerView;
        }

        private FullGameEntity(final GameEntityDTO entity, BackPanel backPanel, final GameType type) {
            this.UP_BACKGROUND = new Color(60, 170, 232);
            this.nextPageIndex = 0;
            this.nextPage = true;
            this.entity = entity;
            this.type = type;
            this.injector = TLauncher.getInjector();
            SpringLayout spring = new SpringLayout();
            setLayout(spring);
            Component updaterFullButton = new UpdaterFullButton(this.UP_BACKGROUND, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.complete.site.button", "official-site.png");
            updaterFullButton.setIconTextGap(15);
            Component updaterFullButton2 = new UpdaterFullButton(this.UP_BACKGROUND, ColorUtil.BLUE_MODPACK_BUTTON_UP, "tlmods.open.link", "official-site.png");
            updaterFullButton2.setIconTextGap(15);
            if (entity.getLinkProject() == null) {
                updaterFullButton.setVisible(false);
            }
            if (Objects.isNull(entity.getTlmodsLinkProject())) {
                updaterFullButton2.setVisible(false);
            }
            ButtonGroup group = new ButtonGroup();
            AbstractButton gameRadioButton = new GameRadioButton("modpack.complete.review.button");
            AbstractButton gameRadioButton2 = new GameRadioButton("modpack.complete.picture.button");
            AbstractButton gameRadioButton3 = new GameRadioButton(Localizable.get("modpack.complete.discussion.button") + String.format(" (%s)", Long.valueOf(entity.getTotalComments())));
            gameRadioButton.setSelected(true);
            gameRadioButton.setActionCommand("REVIEW");
            gameRadioButton2.setActionCommand("PICTURES");
            gameRadioButton3.setActionCommand("DISCUSSION");
            group.add(gameRadioButton);
            group.add(gameRadioButton2);
            group.add(gameRadioButton3);
            AbstractButton gameRadioButton4 = new GameRadioButton("modpack.complete.versions.button");
            gameRadioButton4.setActionCommand("VERSIONS");
            group.add(gameRadioButton4);
            final Color backgroundOldButtonColor = new Color(213, 213, 213);
            final UpdaterButton oldButton = new UpdaterButton(backgroundOldButtonColor, "modpack.complete.old.button");
            oldButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.1
                public void mouseEntered(MouseEvent e) {
                    oldButton.setBackground(new Color(160, 160, 160));
                }

                public void mouseExited(MouseEvent e) {
                    oldButton.setBackground(backgroundOldButtonColor);
                }
            });
            oldButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.2
                public void actionPerformed(ActionEvent e) {
                    GameEntityDTO gameEntityDTO = entity;
                    GameType gameType = type;
                    CompletableFuture.runAsync(()
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0016: INVOKE  
                          (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x000e: INVOKE  (r0v2 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                          (wrap: java.lang.Runnable : 0x0009: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                          (r4v0 'this' org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity$2 A[D('this' org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity$2), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                          (r1v1 'gameEntityDTO' org.tlauncher.modpack.domain.client.GameEntityDTO A[DONT_INLINE])
                          (r2v1 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                        
                         handle type: INVOKE_DIRECT
                         lambda: java.lang.Runnable.run():void
                         call insn: ?: INVOKE  
                          (r0 I:org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity$2)
                          (r1 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                          (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
                         type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.2.lambda$actionPerformed$1(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType):void)
                         type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                          (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0011: INVOKE_CUSTOM (r1v2 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                         handle type: INVOKE_STATIC
                         lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                         call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.2.lambda$actionPerformed$3(java.lang.Throwable):java.lang.Void)
                         type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.2.actionPerformed(java.awt.event.ActionEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$FullGameEntity$2.class
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
                        r1 = r4
                        org.tlauncher.modpack.domain.client.GameEntityDTO r1 = r6
                        r2 = r4
                        org.tlauncher.modpack.domain.client.share.GameType r2 = r7
                        void r0 = () -> { // java.lang.Runnable.run():void
                            r0.lambda$actionPerformed$1(r1, r2);
                        }
                        java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                        void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                            return lambda$actionPerformed$3(v0);
                        }
                        java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.AnonymousClass2.actionPerformed(java.awt.event.ActionEvent):void");
                }
            });
            final JLabel originalEnDescription = new JLabel(ImageCache.getIcon("modpack-original-transation.png"));
            originalEnDescription.setPreferredSize(new Dimension(66, 52));
            originalEnDescription.setBorder(BorderFactory.createEmptyBorder());
            if (TLauncher.getInstance().getConfiguration().getLocale().getLanguage().equals("en")) {
                originalEnDescription.setVisible(false);
            }
            this.installButton = new UpInstallButton(entity, type, CompleteSubEntityScene.this.getMainPane().modpackScene.localmodpacks);
            this.installButton.setBorder(BorderFactory.createEmptyBorder(0, 19, 0, 0));
            this.installButton.setIconTextGap(18);
            JPanel reviewPanel = new ExtendedPanel();
            reviewPanel.setLayout(new FlowLayout(0, 0, 0));
            reviewPanel.setOpaque(true);
            reviewPanel.setBackground(ColorUtil.COLOR_246);
            JPanel versionsPanel = new ExtendedPanel();
            versionsPanel.setLayout(new BorderLayout());
            this.viewEntity = new CompleteDescriptionGamePanel(entity, type);
            final VersionModel model = new VersionModel();
            backPanel.addBackListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.3
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        CompleteSubEntityScene.this.manager.removeGameListener(type, model);
                    }
                }
            });
            CompleteSubEntityScene.this.manager.addGameListener(type, model);
            this.table = new ModpackTable(model);
            this.table.setBackground(Color.WHITE);
            JScrollPane c = ModpackScene.createScrollWrapper(this.table);
            versionsPanel.add(c, "Center");
            SpringLayout upSpring = new SpringLayout();
            ExtendedPanel upButtons = new ExtendedPanel((LayoutManager) upSpring);
            upButtons.setOpaque(true);
            upButtons.setBackground(this.UP_BACKGROUND);
            JPanel centerButtonsWrapper = new JPanel(new BorderLayout(0, 0));
            this.centerButtons = new GroupPanel(242);
            this.centerButtons.setLayout(new GridBagLayout());
            this.centerButtons.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            this.centerButtons.addInGroup(gameRadioButton, 0);
            this.centerButtons.addInGroup(gameRadioButton2, 1);
            this.centerButtons.addInGroup(gameRadioButton4, 2);
            if (!GameType.MODPACK.equals(type)) {
                this.centerButtons.addInGroup(gameRadioButton3, 3);
                this.centerButtons.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 320));
            } else {
                this.centerButtons.addInGroup(gameRadioButton3, 7);
            }
            this.centerView = new JPanel(new CardLayout(0, 0));
            this.centerView.setBackground(Color.WHITE);
            this.centerView.setOpaque(true);
            SwingUtil.configHorizontalSpingLayout(upSpring, backPanel, upButtons, 66);
            upSpring.putConstraint("West", backPanel, 0, "West", upButtons);
            upSpring.putConstraint("East", backPanel, 66, "West", upButtons);
            upButtons.add((Component) backPanel);
            SwingUtil.configHorizontalSpingLayout(upSpring, this.installButton, backPanel, 168);
            upButtons.add((Component) this.installButton);
            SwingUtil.configHorizontalSpingLayout(upSpring, updaterFullButton2, this.installButton, 0);
            upSpring.putConstraint("West", updaterFullButton2, 312, "East", this.installButton);
            upSpring.putConstraint("East", updaterFullButton2, 562, "East", this.installButton);
            upButtons.add(updaterFullButton2);
            SwingUtil.configHorizontalSpingLayout(upSpring, updaterFullButton, this.installButton, 0);
            upSpring.putConstraint("West", updaterFullButton, 563, "East", this.installButton);
            upSpring.putConstraint("East", updaterFullButton, 762, "East", this.installButton);
            upButtons.add(updaterFullButton);
            this.descriptionFull = new HtmlTextPane("text/html", CoreConstants.EMPTY_STRING);
            this.descriptionFull.setText(entity.getDescription());
            this.descriptionFull.setOpaque(true);
            this.descriptionFull.setBackground(ColorUtil.COLOR_246);
            this.jScrollPane = new JScrollPane(this.descriptionFull, 20, 31);
            this.jScrollPane.getVerticalScrollBar().setUI(new ModpackScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.4
                public Dimension getPreferredSize(JComponent c2) {
                    return new Dimension(13, super.getPreferredSize(c2).height);
                }
            });
            this.jScrollPane.setBorder(BorderFactory.createEmptyBorder());
            SwingUtilities.invokeLater(() -> {
                this.jScrollPane.getVerticalScrollBar().setValue(0);
            });
            JPanel panelDescription = new ExtendedPanel((LayoutManager) new FlowLayout(0, 0, 0));
            panelDescription.setPreferredSize(new Dimension(ModpackScene.SIZE.width, 318));
            this.jScrollPane.setPreferredSize(new Dimension(ModpackScene.SIZE.width - (20 * 2), 318 - 40));
            panelDescription.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelDescription.add(this.jScrollPane);
            reviewPanel.add(panelDescription);
            SwingUtil.changeFontFamily(this.installButton, FontTL.ROBOTO_BOLD, 14, Color.WHITE);
            SwingUtil.changeFontFamily(updaterFullButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
            SwingUtil.changeFontFamily(updaterFullButton2, FontTL.ROBOTO_REGULAR, 14, Color.YELLOW);
            SwingUtil.changeFontFamily(gameRadioButton, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(gameRadioButton4, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(gameRadioButton2, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(gameRadioButton3, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
            SwingUtil.changeFontFamily(oldButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
            PicturePanel picturePanel = new PicturePanel(entity, type);
            this.centerView.setBackground(Color.WHITE);
            this.centerView.add(reviewPanel, "REVIEW");
            this.centerView.add(picturePanel, "PICTURES");
            this.centerView.add(versionsPanel, "VERSIONS");
            DiscussionPanel dp = (DiscussionPanel) this.injector.getInstance(DiscussionPanel.class);
            dp.setDto(entity);
            dp.setType(type);
            dp.setCommentCreationForm(CompleteSubEntityScene.this.commentCreationForm);
            this.centerView.add(dp.getScrollPane(), "DISCUSSION");
            spring.putConstraint("North", upButtons, 0, "North", this);
            spring.putConstraint("West", upButtons, 0, "West", this);
            spring.putConstraint("South", upButtons, 56, "North", this);
            spring.putConstraint("East", upButtons, 0, "East", this);
            add((Component) upButtons);
            spring.putConstraint("North", this.viewEntity, 0, "South", upButtons);
            spring.putConstraint("West", this.viewEntity, 0, "West", this);
            spring.putConstraint("South", this.viewEntity, 159, "South", upButtons);
            spring.putConstraint("East", this.viewEntity, 0, "East", this);
            add((Component) this.viewEntity);
            centerButtonsWrapper.add(this.centerButtons, "Center");
            JPanel rightPart = new JPanel(new BorderLayout());
            rightPart.add(originalEnDescription, "East");
            rightPart.add(oldButton, "West");
            oldButton.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            centerButtonsWrapper.add(rightPart, "East");
            spring.putConstraint("North", centerButtonsWrapper, 0, "South", this.viewEntity);
            spring.putConstraint("West", centerButtonsWrapper, 0, "West", this);
            spring.putConstraint("South", centerButtonsWrapper, 52, "South", this.viewEntity);
            spring.putConstraint("East", centerButtonsWrapper, 0, "East", this);
            add((Component) centerButtonsWrapper);
            spring.putConstraint("North", this.centerView, 0, "South", centerButtonsWrapper);
            spring.putConstraint("West", this.centerView, 0, "West", this);
            spring.putConstraint("South", this.centerView, 321, "South", centerButtonsWrapper);
            spring.putConstraint("East", this.centerView, 0, "East", this);
            add((Component) this.centerView);
            ModpackManager modpackManager = (ModpackManager) this.injector.getInstance(ModpackManager.class);
            ActionListener listener = new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.5
                public void actionPerformed(ActionEvent e) {
                    FullGameEntity.this.centerView.getLayout().show(FullGameEntity.this.centerView, e.getActionCommand());
                }
            };
            gameRadioButton.addActionListener(listener);
            gameRadioButton4.addActionListener(listener);
            gameRadioButton2.addActionListener(listener);
            gameRadioButton3.addActionListener(listener);
            updaterFullButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.6
                public void actionPerformed(ActionEvent e) {
                    OS.openLink(entity.getLinkProject());
                }
            });
            updaterFullButton2.addActionListener(ev -> {
                OS.openLink(entity.getTlmodsLinkProject());
            });
            modpackManager.addGameListener(type, this);
            backPanel.addBackListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.7
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        CompleteSubEntityScene.this.manager.removeGameListener(type, FullGameEntity.this);
                    }
                }
            });
            originalEnDescription.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.8
                private boolean active = true;

                public void mouseClicked(MouseEvent mouseEvent) {
                    if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                        int currentValue = FullGameEntity.this.jScrollPane.getVerticalScrollBar().getValue();
                        if (!this.active) {
                            FullGameEntity.this.descriptionFull.setText(entity.getDescription());
                            this.active = true;
                            return;
                        }
                        GameEntityDTO gameEntityDTO = entity;
                        GameType gameType = type;
                        CompletableFuture.runAsync(()
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0033: INVOKE  
                              (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x002b: INVOKE  (r0v15 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                              (wrap: java.lang.Runnable : 0x0026: INVOKE_CUSTOM (r0v14 java.lang.Runnable A[REMOVE]) = 
                              (r5v0 'this' org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity$8 A[D('this' org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity$8), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                              (r1v5 'gameEntityDTO' org.tlauncher.modpack.domain.client.GameEntityDTO A[DONT_INLINE])
                              (r2v1 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                              (r0v6 'currentValue' int A[D('currentValue' int), DONT_INLINE])
                            
                             handle type: INVOKE_DIRECT
                             lambda: java.lang.Runnable.run():void
                             call insn: ?: INVOKE  
                              (r0 I:org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity$8)
                              (r1 I:org.tlauncher.modpack.domain.client.GameEntityDTO)
                              (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
                              (r3 I:int)
                             type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.8.lambda$mouseClicked$2(org.tlauncher.modpack.domain.client.GameEntityDTO, org.tlauncher.modpack.domain.client.share.GameType, int):void)
                             type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                              (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x002e: INVOKE_CUSTOM (r1v6 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                             handle type: INVOKE_STATIC
                             lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                             call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.8.lambda$mouseClicked$3(java.lang.Throwable):java.lang.Void)
                             type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.8.mouseClicked(java.awt.event.MouseEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$FullGameEntity$8.class
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
                            	... 27 more
                            */
                        /*
                            this = this;
                            r0 = r6
                            boolean r0 = javax.swing.SwingUtilities.isLeftMouseButton(r0)
                            if (r0 == 0) goto L50
                            r0 = r5
                            org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity r0 = org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.this
                            javax.swing.JScrollPane r0 = org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.access$400(r0)
                            javax.swing.JScrollBar r0 = r0.getVerticalScrollBar()
                            int r0 = r0.getValue()
                            r7 = r0
                            r0 = r5
                            boolean r0 = r0.active
                            if (r0 == 0) goto L3a
                            r0 = r5
                            r1 = r5
                            org.tlauncher.modpack.domain.client.GameEntityDTO r1 = r6
                            r2 = r5
                            org.tlauncher.modpack.domain.client.share.GameType r2 = r7
                            r3 = r7
                            void r0 = () -> { // java.lang.Runnable.run():void
                                r0.lambda$mouseClicked$2(r1, r2, r3);
                            }
                            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                            void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                                return lambda$mouseClicked$3(v0);
                            }
                            java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                            goto L50
                        L3a:
                            r0 = r5
                            org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity r0 = org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.this
                            org.tlauncher.tlauncher.ui.swing.HtmlTextPane r0 = org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.access$500(r0)
                            r1 = r5
                            org.tlauncher.modpack.domain.client.GameEntityDTO r1 = r6
                            java.lang.String r1 = r1.getDescription()
                            r0.setText(r1)
                            r0 = r5
                            r1 = 1
                            r0.active = r1
                        L50:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.AnonymousClass8.mouseClicked(java.awt.event.MouseEvent):void");
                    }

                    public void mouseEntered(MouseEvent mouseEvent) {
                        originalEnDescription.setIcon(ImageCache.getIcon("modpack-original-transation-up.png"));
                    }

                    public void mouseExited(MouseEvent mouseEvent) {
                        originalEnDescription.setIcon(ImageCache.getIcon("modpack-original-transation.png"));
                    }
                });
                versionsPanel.addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.9
                    public void componentShown(ComponentEvent e) {
                        if (FullGameEntity.this.table.getModel().getRowCount() > 0) {
                            return;
                        }
                        FullGameEntity.this.fillVersions();
                    }
                });
                c.getVerticalScrollBar().addAdjustmentListener(new VersionsAdjustmentListener(this.table, this));
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void processingStarted(GameEntityDTO e, VersionDTO version) {
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void installEntity(GameEntityDTO e, GameType type) {
                this.installButton.installEntity(e, type);
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void populateStatus(GameEntityDTO entity, GameType type, boolean state) {
                if (entity.getId().equals(this.entity.getId())) {
                    this.viewEntity.getStatusStarButton().setStatus(state);
                }
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
            public void removeEntity(GameEntityDTO e) {
                this.installButton.removeEntity(e);
            }

            public void fillVersions() {
                CompletableFuture.runAsync(() -> {
                    DesktopUtil.uncheckCall(() -> {
                        if (this.nextPage) {
                            setProcessingRequest(true);
                            CommonPage<VersionDTO> versions = CompleteSubEntityScene.this.manager.getGameEntityVersions(this.type, this.entity.getId(), this.nextPageIndex);
                            this.nextPageIndex = Integer.valueOf(versions.getCurrent().intValue() + 1);
                            this.nextPage = versions.isNext();
                            SwingUtilities.invokeLater(()
                            /*  JADX ERROR: Method code generation error
                                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0045: INVOKE  
                                  (wrap: java.lang.Runnable : 0x0040: INVOKE_CUSTOM (r0v11 java.lang.Runnable A[REMOVE]) = 
                                  (r5v0 'this' org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity A[D('this' org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                  (r0v7 'versions' org.tlauncher.modpack.domain.client.site.CommonPage<org.tlauncher.modpack.domain.client.version.VersionDTO> A[D('versions' org.tlauncher.modpack.domain.client.site.CommonPage<org.tlauncher.modpack.domain.client.version.VersionDTO>), DONT_INLINE])
                                
                                 handle type: INVOKE_DIRECT
                                 lambda: java.lang.Runnable.run():void
                                 call insn: ?: INVOKE  
                                  (r0 I:org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene$FullGameEntity)
                                  (r1 I:org.tlauncher.modpack.domain.client.site.CommonPage)
                                 type: DIRECT call: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.lambda$null$2(org.tlauncher.modpack.domain.client.site.CommonPage):void)
                                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.lambda$null$3():java.lang.Object, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$FullGameEntity.class
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
                                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                                	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
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
                                	... 67 more
                                */
                            /*
                                this = this;
                                r0 = r5
                                boolean r0 = r0.nextPage
                                if (r0 == 0) goto L4d
                                r0 = r5
                                r1 = 1
                                r0.setProcessingRequest(r1)
                                r0 = r5
                                org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene r0 = org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.this
                                org.tlauncher.tlauncher.managers.ModpackManager r0 = r0.manager
                                r1 = r5
                                org.tlauncher.modpack.domain.client.share.GameType r1 = r1.type
                                r2 = r5
                                org.tlauncher.modpack.domain.client.GameEntityDTO r2 = r2.entity
                                java.lang.Long r2 = r2.getId()
                                r3 = r5
                                java.lang.Integer r3 = r3.nextPageIndex
                                org.tlauncher.modpack.domain.client.site.CommonPage r0 = r0.getGameEntityVersions(r1, r2, r3)
                                r6 = r0
                                r0 = r5
                                r1 = r6
                                java.lang.Integer r1 = r1.getCurrent()
                                int r1 = r1.intValue()
                                r2 = 1
                                int r1 = r1 + r2
                                java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
                                r0.nextPageIndex = r1
                                r0 = r5
                                r1 = r6
                                boolean r1 = r1.isNext()
                                r0.nextPage = r1
                                r0 = r5
                                r1 = r6
                                java.lang.Object r0 = () -> { // java.lang.Runnable.run():void
                                    r0.lambda$null$2(r1);
                                }
                                javax.swing.SwingUtilities.invokeLater(r0)
                                r0 = r5
                                r1 = 0
                                r0.setProcessingRequest(r1)
                            L4d:
                                r0 = 0
                                return r0
                            */
                            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.FullGameEntity.lambda$null$3():java.lang.Object");
                        });
                    }).exceptionally(t -> {
                        setProcessingRequest(false);
                        U.log(t);
                        return null;
                    });
                }

                /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$FullGameEntity$VersionModel.class */
                class VersionModel extends BaseSubtypeModel<VersionModelElement> {
                    VersionModel() {
                        super();
                    }

                    public void addElements(List<? extends VersionDTO> list) {
                        ModpackComboBox modpackComboBox = TLauncher.getInstance().getFrame().mp.modpackScene.localmodpacks;
                        BaseModpackFilter<VersionDTO> bmd = BaseModpackFilter.getBaseModpackStandardFilters(FullGameEntity.this.entity, FullGameEntity.this.type, modpackComboBox);
                        for (VersionDTO v : list) {
                            ModpackTableVersionButton button = new ModpackTableVersionButton(FullGameEntity.this.entity, FullGameEntity.this.type, modpackComboBox, v, bmd);
                            this.list.add(new VersionModelElement(button, FullGameEntity.this.entity, v));
                        }
                    }

                    public int getRowCount() {
                        return this.list.size();
                    }

                    public int getColumnCount() {
                        return 6;
                    }

                    public Object getValueAt(int rowIndex, int columnIndex) {
                        VersionDTO v = ((VersionModelElement) this.list.get(rowIndex)).getVersion();
                        switch (columnIndex) {
                            case 0:
                                return this.format.format(new Date(v.getUpdateDate().longValue()));
                            case 1:
                                return v.getName();
                            case 2:
                                if (Objects.isNull(v.getMinecraftVersionTypes()) || v.getMinecraftVersionTypes().isEmpty()) {
                                    return Localizable.get("modpack.version.any");
                                }
                                return v.getMinecraftVersionTypes().stream().map(vv -> {
                                    return Localizable.get("modpack.version." + vv.getName());
                                }).collect(Collectors.joining(", "));
                            case 3:
                                return v.getType();
                            case 4:
                                if (v.getGameVersionsDTO().isEmpty()) {
                                    return Localizable.get("modpack.version.any");
                                }
                                return v.getGameVersionsDTO().stream().map((v0) -> {
                                    return v0.getName();
                                }).collect(Collectors.joining(", "));
                            case 5:
                                return ((VersionModelElement) this.list.get(rowIndex)).getModpackActButton();
                            default:
                                return null;
                        }
                    }

                    public Class<?> getColumnClass(int columnIndex) {
                        if (columnIndex == 5) {
                            return BaseModelElement.class;
                        }
                        return super.getColumnClass(columnIndex);
                    }

                    public String getColumnName(int column) {
                        switch (column) {
                            case 0:
                                String line = Localizable.get("version.manager.editor.field.time");
                                return line.substring(0, line.length() - 1);
                            case 1:
                                return Localizable.get("version.release");
                            case 2:
                                String line2 = Localizable.get("version.manager.editor.field.type");
                                return line2.substring(0, line2.length() - 1);
                            case 3:
                                return Localizable.get("modpack.table.version.maturiry");
                            case 4:
                                return Localizable.get("modpack.table.pack.element.version");
                            case 5:
                                return Localizable.get("modpack.table.pack.element.operation");
                            default:
                                return CoreConstants.EMPTY_STRING;
                        }
                    }

                    @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseSubtypeModel, org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
                    public void installEntity(GameEntityDTO e, GameType type) {
                        int index = findByVersion(e, e.getVersion());
                        if (index != -1) {
                            ((VersionModelElement) this.list.get(index)).getModpackActButton().setTypeButton(ModpackActButton.REMOVE);
                            fireTableCellUpdated(index, 5);
                        }
                    }

                    @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseSubtypeModel, org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
                    public void removeEntity(GameEntityDTO e) {
                        int index = findByVersion(e, e.getVersion());
                        if (index != -1) {
                            ((VersionModelElement) this.list.get(index)).getModpackActButton().setTypeButton(ModpackActButton.INSTALL);
                            fireTableCellUpdated(index, 5);
                        }
                    }

                    @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
                    public void processingStarted(GameEntityDTO e, VersionDTO version) {
                        if (Objects.nonNull(version) && e.getId().equals(FullGameEntity.this.entity.getId())) {
                            for (int i = 0; i < this.list.size(); i++) {
                                if (((VersionModelElement) this.list.get(i)).getVersion().getId().equals(version.getId())) {
                                    ((VersionModelElement) this.list.get(i)).getModpackActButton().setTypeButton(ModpackActButton.PROCESSING);
                                    fireTableCellUpdated(i, 5);
                                    return;
                                }
                            }
                        }
                    }

                    @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseSubtypeModel, org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.GameEntityTableModel, org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
                    public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
                        int index = findByVersion(e, v);
                        if (index != -1) {
                            ((VersionModelElement) this.list.get(index)).getModpackActButton().reset();
                            fireTableCellUpdated(index, 5);
                        }
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return columnIndex == 5;
                    }

                    public int findByVersion(GameEntityDTO e, VersionDTO v) {
                        if (e.getId().equals(FullGameEntity.this.entity.getId())) {
                            for (int i = 0; i < this.list.size(); i++) {
                                VersionModelElement element = (VersionModelElement) this.list.get(i);
                                if (element.getVersion().getId().equals(v.getId())) {
                                    return i;
                                }
                            }
                            return -1;
                        }
                        return -1;
                    }

                    @Override // org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene.BaseSubtypeModel
                    public GameEntityDTO getRowObject(int rowIndex) {
                        return null;
                    }
                }

                /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/CompleteSubEntityScene$FullGameEntity$CompleteDescriptionGamePanel.class */
                class CompleteDescriptionGamePanel extends DescriptionGamePanel {
                    public static final int SHADOW_PANEL = 223;

                    public CompleteDescriptionGamePanel(GameEntityDTO entity, GameType type) {
                        super(entity, type);
                    }

                    protected void paintComponent(Graphics g0) {
                        super.paintComponent(g0);
                        Rectangle rec = getVisibleRect();
                        int i = 223;
                        Graphics2D g2 = (Graphics2D) g0;
                        for (int y = rec.y; y < rec.height + rec.y; y++) {
                            g2.setColor(new Color(i, i, i));
                            if (i != 255) {
                                i++;
                            }
                            g2.drawLine(rec.x, y, rec.x + rec.width, y);
                        }
                    }
                }
            }

            @Override // org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener
            public void updateFavoriteValue() {
                if (Objects.nonNull(this.fullGameEntity)) {
                    this.fullGameEntity.viewEntity.statusStarButton.updateStatus();
                }
            }
        }
