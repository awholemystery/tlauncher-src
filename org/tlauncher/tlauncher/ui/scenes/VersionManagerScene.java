package org.tlauncher.tlauncher.ui.scenes;

import java.awt.Component;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.versions.VersionHandler;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/VersionManagerScene.class */
public class VersionManagerScene extends PseudoScene {
    private static final long serialVersionUID = 758826812081732720L;
    final VersionHandler handler;

    public VersionManagerScene(MainPane main) {
        super(main);
        this.handler = new VersionHandler(this);
        add((Component) this.handler.list);
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        super.onResize();
        this.handler.list.setLocation((getWidth() / 2) - (this.handler.list.getWidth() / 2), (getHeight() / 2) - (this.handler.list.getHeight() / 2));
    }
}
