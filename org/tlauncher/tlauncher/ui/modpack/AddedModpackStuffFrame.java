package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/AddedModpackStuffFrame.class */
public class AddedModpackStuffFrame extends TemlateModpackFrame {
    protected JPanel panel;
    protected SpringLayout spring;
    protected HtmlTextPane message;
    private static final long serialVersionUID = 8694630846172369187L;
    private static final Dimension DEFAULT_SIZE = new Dimension(572, 310);

    public AddedModpackStuffFrame(JFrame parent, String title, String message1) {
        super(parent, title, DEFAULT_SIZE);
        this.spring = new SpringLayout();
        this.panel = new JPanel(this.spring);
        this.panel.setBackground(Color.WHITE);
        addCenter(this.panel);
        this.message = HtmlTextPane.get(String.format("<div><center>%s</center></div>", Localizable.get(message1)));
        this.message.setOpaque(false);
        BorderFactory.createEmptyBorder(0, 21, 0, 0);
        SwingUtil.changeFontFamily(this.message, FontTL.ROBOTO_REGULAR, 15, ColorUtil.COLOR_25);
        this.spring.putConstraint("West", this.message, 29, "West", this.panel);
        this.spring.putConstraint("East", this.message, -27, "East", this.panel);
        this.spring.putConstraint("North", this.message, 40, "North", this.panel);
        this.spring.putConstraint("South", this.message, 140, "North", this.panel);
        this.panel.add(this.message);
    }
}
