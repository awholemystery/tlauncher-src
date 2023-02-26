package org.tlauncher.tlauncher.ui.progress;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.ui.PreloaderProgressUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/progress/ProgressFrame.class */
public class ProgressFrame extends JFrame {
    private static final int WIDTH = 240;
    private static final int HEIGHT = 99;
    private String version_info;
    private JProgressBar progressBar;
    private Font font = new Font("Verdana", 0, 10);
    public final Color VERSION_BACKGROUND = new Color(40, 134, 187);

    @Inject
    public ProgressFrame(@Assisted("info") String info) {
        getContentPane().setForeground(Color.LIGHT_GRAY);
        setTitle("TLauncher");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        setBackground(Color.LIGHT_GRAY);
        setDefaultCloseOperation(3);
        this.version_info = info;
        SpringLayout springLayout = new SpringLayout();
        getContentPane().setLayout(springLayout);
        getContentPane().setPreferredSize(new Dimension((int) WIDTH, (int) HEIGHT));
        JLabel version = new JLabel(this.version_info);
        version.setHorizontalAlignment(0);
        version.setForeground(Color.WHITE);
        version.setFont(this.font);
        version.setOpaque(true);
        version.setBackground(this.VERSION_BACKGROUND);
        springLayout.putConstraint("North", version, 0, "North", getContentPane());
        springLayout.putConstraint("West", version, -58, "East", getContentPane());
        springLayout.putConstraint("South", version, 16, "North", getContentPane());
        springLayout.putConstraint("East", version, 0, "East", getContentPane());
        getContentPane().add(version);
        JLabel backgroundImage = new JLabel();
        backgroundImage.setIcon(ImageCache.getNativeIcon("tlauncher.png"));
        springLayout.putConstraint("North", backgroundImage, 0, "North", getContentPane());
        springLayout.putConstraint("West", backgroundImage, 0, "West", getContentPane());
        springLayout.putConstraint("South", backgroundImage, 75, "North", getContentPane());
        springLayout.putConstraint("East", backgroundImage, 244, "West", getContentPane());
        getContentPane().add(backgroundImage);
        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(true);
        this.progressBar.setBorder(BorderFactory.createEmptyBorder());
        this.progressBar.setUI(new PreloaderProgressUI(ImageCache.getBufferedImage("bottom-bar.png"), ImageCache.getBufferedImage("up-progress-bar.png")));
        springLayout.putConstraint("North", this.progressBar, 0, "South", backgroundImage);
        springLayout.putConstraint("West", this.progressBar, 0, "West", getContentPane());
        springLayout.putConstraint("South", this.progressBar, 0, "South", getContentPane());
        springLayout.putConstraint("East", this.progressBar, 4, "East", getContentPane());
        getContentPane().add(this.progressBar);
        pack();
        setVisible(true);
    }
}
