package org.tlauncher.tlauncher.ui.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.editor.EditorField;
import org.tlauncher.tlauncher.ui.editor.EditorIntegerField;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.SettingsScene;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.ui.SettingsSliderUI;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/SettingsMemorySlider.class */
public class SettingsMemorySlider extends BorderPanel implements EditorField {
    public static final Color HINT_BACKGROUND_COLOR = new Color(113, 113, 113);
    private final JSlider slider;
    private final EditorIntegerField inputField;
    private final LocalizableLabel mb;
    private Configuration c;
    private JLabel question;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingsMemorySlider() {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        JPanel base = new ExtendedPanel();
        springLayout.putConstraint("North", base, 50, "North", this);
        springLayout.putConstraint("West", base, 0, "East", this);
        springLayout.putConstraint("South", base, -236, "South", this);
        springLayout.putConstraint("East", base, 0, "East", this);
        add((Component) base);
        this.slider = new JSlider();
        this.slider.setOpaque(false);
        this.slider.setMinimum(512);
        this.slider.setMaximum(OS.Arch.MAX_MEMORY);
        int tick = (OS.Arch.MAX_MEMORY - 512) / 5;
        if (tick == 0) {
            this.slider.setMajorTickSpacing(1);
        } else {
            this.slider.setMajorTickSpacing(tick);
        }
        this.slider.setSnapToTicks(true);
        this.slider.setPaintLabels(true);
        this.slider.setUI(new SettingsSliderUI(this.slider));
        this.slider.setPreferredSize(new Dimension(336, 35));
        this.inputField = new EditorIntegerField();
        this.inputField.setColumns(5);
        this.mb = new LocalizableLabel("settings.java.memory.mb");
        SpringLayout spring = new SpringLayout();
        base.setLayout(spring);
        JLayeredPane sliderPanel = new JLayeredPane();
        spring.putConstraint("North", sliderPanel, 0, "North", base);
        spring.putConstraint("West", sliderPanel, 0, "West", base);
        spring.putConstraint("East", sliderPanel, -70, "East", base);
        spring.putConstraint("South", sliderPanel, 0, "South", base);
        base.add(sliderPanel);
        spring.putConstraint("North", this.inputField, 0, "North", base);
        spring.putConstraint("West", this.inputField, 2, "East", sliderPanel);
        spring.putConstraint("East", this.inputField, 45, "East", sliderPanel);
        base.add(this.inputField);
        spring.putConstraint("North", this.mb, 3, "North", base);
        spring.putConstraint("West", this.mb, 1, "East", this.inputField);
        spring.putConstraint("East", this.mb, 0, "East", base);
        base.add(this.mb);
        sliderPanel.add(this.slider, 0);
        this.slider.setBounds(0, 0, 336, 35);
        this.c = TLauncher.getInstance().getConfiguration();
        this.question = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
        this.question.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.1
            public void mousePressed(MouseEvent e) {
                Alert.showLocMessage(SettingsMemorySlider.this.c.get(TestEnvironment.WARMING_MESSAGE));
            }
        });
        sliderPanel.add(this.question, 1);
        this.question.setBounds(330, 0, 20, 20);
        this.inputField.getDocument().addDocumentListener(new DocumentListener() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.2
            public void insertUpdate(DocumentEvent e) {
                SettingsMemorySlider.this.updateInfo();
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        if (!TLauncher.getInstance().getConfiguration().getBoolean("settings.tip.close")) {
            addHint(base, spring);
        }
        this.slider.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.3
            public void mouseReleased(MouseEvent e) {
                SettingsMemorySlider.this.requestFocusInWindow();
            }
        });
        this.slider.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.4
            public void mouseReleased(MouseEvent e) {
                SettingsMemorySlider.this.onSliderUpdate();
            }
        });
        this.slider.addKeyListener(new KeyAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.5
            public void keyReleased(KeyEvent e) {
                SettingsMemorySlider.this.onSliderUpdate();
            }
        });
    }

    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
    }

    private void addHint(JPanel panel_1, SpringLayout sl_panel_1) {
        final Component component = new JLabel() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.6
            BufferedImage image = ImageCache.getImage("close-cross.png");

            protected void paintComponent(Graphics g) {
                paint(g);
            }

            public void paint(Graphics g0) {
                ((Graphics2D) g0).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Rectangle rec = getVisibleRect();
                g0.setColor(SettingsScene.BACKGROUND);
                g0.fillRect(rec.x, rec.y, rec.width, rec.height);
                g0.setColor(getBackground());
                g0.fillRect(rec.x, rec.y, rec.width - 10, rec.height);
                g0.fillRoundRect(rec.x, rec.y, rec.width, rec.height, 10, 10);
                paintPicture(g0, this, rec);
                ((Graphics2D) g0).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }

            protected void paintPicture(Graphics g, JComponent c, Rectangle rect) {
                Graphics2D g2d = (Graphics2D) g;
                int x = (getWidth() - this.image.getWidth((ImageObserver) null)) / 2;
                int y = (getHeight() - this.image.getHeight((ImageObserver) null)) / 2;
                g2d.drawImage(this.image, x, y, (ImageObserver) null);
            }
        };
        component.setOpaque(true);
        component.setBackground(HINT_BACKGROUND_COLOR);
        component.setPreferredSize(new Dimension(32, 30));
        component.setSize(new Dimension(32, 30));
        component.setPreferredSize(new Dimension(32, 30));
        Component localizableLabel = new LocalizableLabel("settings.warning");
        final ExtendedPanel extendedPanel = new ExtendedPanel(new BorderLayout(10, 0)) { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.7
            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
            public void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(SettingsMemorySlider.HINT_BACKGROUND_COLOR);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
        };
        extendedPanel.setInsets(0, 5, 0, 0);
        sl_panel_1.putConstraint("East", extendedPanel, -20, "East", this.slider);
        sl_panel_1.putConstraint("North", extendedPanel, 9, "South", this.slider);
        sl_panel_1.putConstraint("South", extendedPanel, 40, "South", this.slider);
        localizableLabel.setVerticalTextPosition(0);
        localizableLabel.setHorizontalAlignment(0);
        localizableLabel.setVerticalAlignment(0);
        localizableLabel.setForeground(new Color(212, 212, 212));
        localizableLabel.setFont(localizableLabel.getFont().deriveFont(12.0f));
        extendedPanel.setOpaque(true);
        extendedPanel.add(localizableLabel, "Center");
        extendedPanel.add(component, "East");
        panel_1.add(extendedPanel);
        component.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.settings.SettingsMemorySlider.8
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                ExtendedPanel extendedPanel2 = extendedPanel;
                SwingUtilities.invokeLater(() -> {
                    extendedPanel2.setVisible(false);
                });
                TLauncher.getInstance().getConfiguration().set("settings.tip.close", (Object) true);
            }

            public void mouseEntered(MouseEvent e) {
                component.setBackground(new Color(124, 124, 124));
            }

            public void mouseExited(MouseEvent e) {
                component.setBackground(SettingsMemorySlider.HINT_BACKGROUND_COLOR);
            }
        });
        sl_panel_1.putConstraint("West", extendedPanel, 0, "West", panel_1);
        extendedPanel.setBackground(HINT_BACKGROUND_COLOR);
    }

    public void setBackground(Color color) {
        if (this.inputField != null) {
            this.inputField.setBackground(color);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents(reason, this.slider, this.inputField);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents(reason, this.slider, this.inputField);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        return this.inputField.getValue();
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String value) {
        this.inputField.setValue((Object) value);
        updateInfo();
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        return this.inputField.getIntegerValue() >= 512 && this.inputField.getIntegerValue() <= OS.Arch.MAX_MEMORY;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSliderUpdate() {
        this.inputField.setValue(Integer.valueOf(this.slider.getValue()));
        updateTip();
    }

    private void updateSlider() {
        int intVal = this.inputField.getIntegerValue();
        if (intVal > 1) {
            this.slider.setValue(intVal);
        }
    }

    private void updateTip() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInfo() {
        updateSlider();
        updateTip();
    }

    public void initMemoryQuestion() {
        if (!this.c.isExist(TestEnvironment.WARMING_MESSAGE)) {
            this.question.setVisible(false);
        }
        this.question.repaint();
    }
}
