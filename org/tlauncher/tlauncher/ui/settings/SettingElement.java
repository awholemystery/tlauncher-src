package org.tlauncher.tlauncher.ui.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/SettingElement.class */
public class SettingElement extends ExtendedPanel {
    public static int FIRST_PART = 162;
    public static int SECOND_PART = 420;
    public static final Font LABEL_FONT = new JLabel().getFont().deriveFont(1, 12.0f);

    public SettingElement(String name, JComponent panel, int height) {
        setLayout(new BoxLayout(this, 0));
        Component localizableLabel = new LocalizableLabel(name);
        localizableLabel.setHorizontalAlignment(2);
        localizableLabel.setVerticalAlignment(0);
        localizableLabel.setVerticalAlignment(0);
        localizableLabel.setVerticalTextPosition(0);
        localizableLabel.setFont(LABEL_FONT);
        ExtendedPanel p = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        p.setPreferredSize(new Dimension(FIRST_PART, height));
        p.add(localizableLabel);
        panel.setPreferredSize(new Dimension(SECOND_PART, height));
        add((Component) p);
        add((Component) panel);
    }

    public SettingElement(String name, JComponent panel, int height, int labelUpGap, int labelVerticalPosition) {
        setLayout(new BoxLayout(this, 0));
        Component localizableLabel = new LocalizableLabel(name);
        localizableLabel.setHorizontalAlignment(2);
        localizableLabel.setVerticalAlignment(labelVerticalPosition);
        localizableLabel.setVerticalTextPosition(0);
        localizableLabel.setFont(LABEL_FONT);
        ExtendedPanel p = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        p.setInsets(labelUpGap, 0, 0, 0);
        p.setPreferredSize(new Dimension(FIRST_PART, height));
        p.add(localizableLabel);
        panel.setPreferredSize(new Dimension(SECOND_PART, height));
        add((Component) p);
        add((Component) panel);
    }

    public SettingElement(String name, JComponent panel, int height, int labelUpGap) {
        this(name, panel, height, labelUpGap, 0);
    }

    public SettingElement(String name, JComponent elem, int height, int labelUpGap, JComponent thirdElement) {
        setLayout(new FlowLayout(0, 0, 0));
        Component localizableLabel = new LocalizableLabel(name);
        localizableLabel.setHorizontalAlignment(2);
        localizableLabel.setVerticalAlignment(0);
        localizableLabel.setVerticalAlignment(0);
        localizableLabel.setVerticalTextPosition(0);
        localizableLabel.setFont(LABEL_FONT);
        ExtendedPanel p = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        p.setInsets(labelUpGap, 0, 0, 0);
        p.setPreferredSize(new Dimension(FIRST_PART, height));
        p.add(localizableLabel);
        add((Component) p);
        add((Component) elem);
        elem.setPreferredSize(new Dimension(elem.getPreferredSize().width, height));
        add((Component) thirdElement);
    }
}
