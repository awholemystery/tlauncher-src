package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.block.BlockableLayeredPane;
import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedLayeredPane.class */
public abstract class ExtendedLayeredPane extends BlockableLayeredPane implements ResizeableComponent {
    private static final long serialVersionUID = -1;
    private Integer LAYER_COUNT;
    protected final JComponent parent;

    /* JADX INFO: Access modifiers changed from: protected */
    public ExtendedLayeredPane() {
        this.LAYER_COUNT = 0;
        this.parent = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExtendedLayeredPane(JComponent parent) {
        this.LAYER_COUNT = 0;
        this.parent = parent;
        if (parent == null) {
            return;
        }
        parent.addComponentListener(new ComponentListener() { // from class: org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane.1
            public void componentResized(ComponentEvent e) {
                ExtendedLayeredPane.this.onResize();
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
                ExtendedLayeredPane.this.onResize();
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    public Component add(Component comp) {
        Integer num = this.LAYER_COUNT;
        this.LAYER_COUNT = Integer.valueOf(this.LAYER_COUNT.intValue() + 1);
        super.add(comp, num);
        return comp;
    }

    public void add(Component... components) {
        if (components == null) {
            throw new NullPointerException();
        }
        for (Component comp : components) {
            add(comp);
        }
    }

    public void onResize() {
        ResizeableComponent[] components;
        if (this.parent == null) {
            return;
        }
        setSize(this.parent.getWidth(), this.parent.getHeight());
        for (ResizeableComponent resizeableComponent : getComponents()) {
            if (resizeableComponent instanceof ResizeableComponent) {
                resizeableComponent.onResize();
            }
        }
    }
}
