package org.tlauncher.tlauncher.ui.loc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/RoundUpdaterButton.class */
public class RoundUpdaterButton extends UpdaterButton {
    public static Color TEXT_COLOR;
    int ARC_SIZE;
    int i;

    public RoundUpdaterButton(Color colorText, final Color background, final Color mouseUnder, String value) {
        super(background, value);
        this.ARC_SIZE = 10;
        this.i = 0;
        setOpaque(false);
        TEXT_COLOR = colorText;
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.loc.RoundUpdaterButton.1
            public void mouseEntered(MouseEvent e) {
                RoundUpdaterButton.this.setBackground(mouseUnder);
            }

            public void mouseExited(MouseEvent e) {
                RoundUpdaterButton.this.setBackground(background);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.loc.UpdaterButton
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), this.ARC_SIZE, this.ARC_SIZE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g0.setColor(TEXT_COLOR);
        paintText(g0, this, getVisibleRect(), getText());
    }
}
