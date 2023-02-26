package org.tlauncher.tlauncher.ui.modpack;

import by.gdev.http.download.service.FileCacheService;
import by.gdev.util.model.download.Metadata;
import by.gdev.util.model.download.Repo;
import com.google.inject.Injector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.PictureType;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/BigPictureObserver.class */
public class BigPictureObserver extends TemlateModpackFrame {
    private Color backgroundPanel;
    private static final int heightPanel = 586;
    private static final int width = 1050;
    int currentPicture;
    final JLabel picture;
    final JLabel close;
    private GameType type;
    private GameEntityDTO gameEntity;
    private Repo pictures;
    private ModpackManager manager;
    private FileCacheService fileCacheService;

    public BigPictureObserver(JFrame parent, String title, int i, GameType type, GameEntityDTO gameEntity) {
        super(parent, title, new Dimension((int) width, (int) heightPanel), OS.is(OS.LINUX));
        this.backgroundPanel = new Color(188, 188, 188);
        this.picture = new JLabel();
        this.close = new JLabel(ImageCache.getNativeIcon("picture-exit.png"));
        Injector inj = TLauncher.getInjector();
        this.fileCacheService = (FileCacheService) inj.getInstance(FileCacheService.class);
        this.manager = (ModpackManager) inj.getInstance(ModpackManager.class);
        this.type = type;
        this.gameEntity = gameEntity;
        this.currentPicture = i;
        JLayeredPane layeredPane = new JLayeredPane();
        Button previousPicture = new Button("big-picture-previous-arrow.png", "previous-arrow-under.png");
        Button nextPicture = new Button("big-picture-next-arrow.png", "next-arrow-under.png");
        previousPicture.setPreferredSize(new Dimension(46, (int) TarConstants.PREFIXLEN));
        nextPicture.setPreferredSize(new Dimension(46, (int) TarConstants.PREFIXLEN));
        layeredPane.setBackground(this.backgroundPanel);
        this.picture.setBorder(BorderFactory.createLineBorder(Color.white, 5));
        this.close.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.BigPictureObserver.1
            public void mouseEntered(MouseEvent e) {
                BigPictureObserver.this.close.setIcon(ImageCache.getNativeIcon("picture-exit-on.png"));
            }

            public void mouseExited(MouseEvent e) {
                BigPictureObserver.this.close.setIcon(ImageCache.getNativeIcon("picture-exit.png"));
            }

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    BigPictureObserver.this.setVisible(false);
                }
            }
        });
        layeredPane.add(previousPicture, 1);
        layeredPane.add(nextPicture, 1);
        layeredPane.add(this.picture, 1);
        layeredPane.add(this.close, 0);
        layeredPane.setSize(new Dimension((int) width, (int) heightPanel));
        previousPicture.setBounds(10, 266, 19, 33);
        nextPicture.setBounds(1021, 266, 19, 33);
        putOnPanel();
        add(layeredPane);
        nextPicture.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.modpack.BigPictureObserver.2
            public void actionPerformed(ActionEvent e) {
                if (Objects.nonNull(BigPictureObserver.this.pictures) && BigPictureObserver.this.currentPicture < BigPictureObserver.this.pictures.getResources().size() - 1) {
                    BigPictureObserver.this.currentPicture++;
                    BigPictureObserver.this.putOnPanel();
                }
            }
        });
        previousPicture.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.modpack.BigPictureObserver.3
            public void actionPerformed(ActionEvent e) {
                if (BigPictureObserver.this.currentPicture > 0) {
                    BigPictureObserver.this.currentPicture--;
                    BigPictureObserver.this.putOnPanel();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putOnPanel() {
        CompletableFuture.runAsync(() -> {
            try {
                if (Objects.isNull(this.pictures)) {
                    this.pictures = this.manager.getGameEntitiesPictures(this.type, this.gameEntity.getId(), PictureType.MAX);
                }
                if (!this.pictures.getResources().isEmpty()) {
                    Metadata meta = this.pictures.getResources().get(this.currentPicture);
                    this.picture.setIcon(new ImageIcon(Files.readAllBytes(this.fileCacheService.getRawObject(this.pictures.getRepositories(), meta, true))));
                }
            } catch (Exception e) {
                U.log(e);
            }
        }).thenAccept(e -> {
            Dimension pictureSize = this.picture.getPreferredSize();
            this.picture.setBounds((width - pictureSize.width) / 2, (heightPanel - pictureSize.height) / 2, pictureSize.width, pictureSize.height);
            this.close.setBounds((((width - pictureSize.width) / 2) + pictureSize.width) - 21, ((heightPanel - pictureSize.height) / 2) - 21, 42, 42);
        });
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/BigPictureObserver$Button.class */
    private class Button extends ImageUdaterButton {
        public Button(final String s, final String s1) {
            super(s);
            setOpaque(false);
            addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.BigPictureObserver.Button.1
                public void mouseEntered(MouseEvent e) {
                    Button.super.setImage(Button.loadImage(s1));
                }

                public void mouseExited(MouseEvent e) {
                    Button.super.setImage(Button.loadImage(s));
                }
            });
        }

        @Override // org.tlauncher.tlauncher.ui.loc.ImageUdaterButton, org.tlauncher.tlauncher.ui.swing.ImageButton
        public void paintComponent(Graphics g) {
            paintPicture(g, this, getBounds());
        }
    }
}
