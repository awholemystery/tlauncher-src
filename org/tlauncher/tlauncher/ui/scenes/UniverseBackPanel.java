package org.tlauncher.tlauncher.ui.scenes;

import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.server.BackPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/UniverseBackPanel.class */
public class UniverseBackPanel extends BackPanel {
    public UniverseBackPanel(String titleName) {
        super(titleName, new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.UniverseBackPanel.1
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    TLauncher.getInstance().getFrame().mp.openDefaultScene();
                }
            }
        }, ImageCache.getIcon("back-arrow.png"));
    }

    public UniverseBackPanel(Color color) {
        super(CoreConstants.EMPTY_STRING, new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.UniverseBackPanel.2
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    TLauncher.getInstance().getFrame().mp.openDefaultScene();
                }
            }
        }, ImageCache.getIcon("back-arrow.png"));
        setBackground(color);
    }
}
