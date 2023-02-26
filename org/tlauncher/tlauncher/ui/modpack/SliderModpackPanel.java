package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.ui.ModpackSliderUI;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/SliderModpackPanel.class */
public class SliderModpackPanel extends ExtendedPanel {
    private final Color color179 = new Color(179, 179, 179);
    private final Color downBackground = new Color(244, 252, 255);
    private final int downHeight = 40;
    private final Color color233 = new Color(233, 233, 233);
    JSlider slider;

    public SliderModpackPanel(Dimension dimension) {
        setPreferredSize(dimension);
        this.slider = new JSlider();
        this.slider.setOpaque(false);
        this.slider.setUI(new ModpackSliderUI(this.slider));
        this.slider.setMinimum(512);
        this.slider.setMaximum(OS.Arch.MAX_MEMORY);
        this.slider.setMajorTickSpacing((OS.Arch.MAX_MEMORY - 512) / 20);
        this.slider.setSnapToTicks(true);
        this.slider.setPaintLabels(false);
        this.slider.setValue((this.slider.getMaximum() * 2) / 3);
        JLayeredPane layeredPane = new JLayeredPane();
        JPanel downPanel = new JPanel(new FlowLayout(0));
        downPanel.add(Box.createVerticalStrut((int) ModpackScene.WIDTH_SEARCH_PANEL));
        JLabel recomended = new LocalizableLabel("modpack.config.recommended");
        recomended.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, this.color233));
        recomended.setHorizontalAlignment(0);
        recomended.setPreferredSize(new Dimension((int) TarConstants.PREFIXLEN_XSTAR, 40));
        JLabel highLevel = new LocalizableLabel("modpack.config.high.level");
        highLevel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, this.color233));
        highLevel.setHorizontalAlignment(0);
        highLevel.setPreferredSize(new Dimension(171, 20));
        SwingUtil.changeFontFamily(this.slider, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
        SwingUtil.changeFontFamily(highLevel, FontTL.ROBOTO_REGULAR, 12, this.color179);
        SwingUtil.changeFontFamily(recomended, FontTL.ROBOTO_REGULAR, 12, this.color179);
        downPanel.setBackground(this.downBackground);
        layeredPane.setPreferredSize(dimension);
        layeredPane.add(this.slider, new Integer(2));
        layeredPane.add(highLevel, new Integer(1));
        layeredPane.add(recomended, new Integer(1));
        layeredPane.add(downPanel, new Integer(0));
        recomended.setBounds(225, 27, (int) CompleteSubEntityScene.BUTTON_PANEL_SUB_VIEW, 39);
        highLevel.setBounds(356, 27, 171, 39);
        downPanel.setBounds(8 + 3, (dimension.height - 40) - 12, (dimension.width - (8 * 2)) - 3, 40);
        this.slider.setBounds(0, 0, dimension.width, dimension.height - 20);
        add((Component) layeredPane);
    }

    public int getValue() {
        return this.slider.getValue();
    }

    public void setValue(int value) {
        this.slider.setValue(value);
    }
}
