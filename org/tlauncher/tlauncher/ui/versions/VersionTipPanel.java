package org.tlauncher.tlauncher.ui.versions;

import ch.qos.logback.core.CoreConstants;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
import org.tlauncher.tlauncher.ui.swing.extended.HTMLLabel;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionTipPanel.class */
public class VersionTipPanel extends CenterPanel implements LocalizableComponent, ResizeableComponent {
    private final HTMLLabel tip;

    VersionTipPanel(VersionHandler handler) {
        super(CenterPanel.tipTheme, CenterPanel.squareInsets);
        this.tip = new HTMLLabel();
        add((Component) this.tip);
        this.tip.addPropertyChangeListener("html", new PropertyChangeListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionTipPanel.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent evt) {
                Object o = evt.getNewValue();
                if (o == null || !(o instanceof View)) {
                    return;
                }
                View view = (View) o;
                BasicHTML.getHTMLBaseline(view, 500 - VersionTipPanel.this.getHorizontalInsets(), 0);
            }
        });
        updateLocale();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        this.tip.setText(CoreConstants.EMPTY_STRING);
        String text = Localizable.get("version.list.tip");
        if (text == null) {
            return;
        }
        this.tip.setText(text.replace("{Ctrl}", OS.OSX.isCurrent() ? "Command" : "Ctrl"));
        onResize();
    }

    @Override // org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        setSize(500, this.tip.getHeight() + getVerticalInsets());
    }

    private int getVerticalInsets() {
        return getInsets().top + getInsets().bottom;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getHorizontalInsets() {
        return getInsets().left + getInsets().right;
    }
}
