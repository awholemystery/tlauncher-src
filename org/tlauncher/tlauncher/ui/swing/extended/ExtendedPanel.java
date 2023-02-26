package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedPanel.class */
public class ExtendedPanel extends JPanel {
    private static final long serialVersionUID = 3562102753301440454L;
    private final List<MouseListener> mouseListeners;
    private Insets insets;
    private float opacity;
    private AlphaComposite aComp;

    public ExtendedPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        this.opacity = 1.0f;
        this.mouseListeners = new ArrayList();
        setOpaque(false);
    }

    public ExtendedPanel(LayoutManager layout) {
        this(layout, true);
    }

    public ExtendedPanel(boolean isDoubleBuffered) {
        this(new FlowLayout(), isDoubleBuffered);
    }

    public ExtendedPanel() {
        this(true);
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("opacity must be in [0;1]");
        }
        this.opacity = f;
        this.aComp = AlphaComposite.getInstance(3, f);
        repaint();
    }

    public Insets getInsets() {
        return this.insets == null ? super.getInsets() : this.insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public Component add(Component comp) {
        super.add(comp);
        return processListener(comp);
    }

    public Component add(Component comp, int index) {
        super.add(comp, index);
        return processListener(comp);
    }

    private Component processListener(Component comp) {
        if (comp == null) {
            return null;
        }
        MouseListener[] compareListeners = comp.getMouseListeners();
        for (MouseListener listener : this.mouseListeners) {
            MouseListener add = listener;
            int length = compareListeners.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                MouseListener compareListener = compareListeners[i];
                if (!listener.equals(compareListener)) {
                    i++;
                } else {
                    add = null;
                    break;
                }
            }
            if (add != null) {
                comp.addMouseListener(add);
            }
        }
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

    public void add(Component component0, Component component1) {
        add(component0, component1);
    }

    public synchronized void addMouseListener(MouseListener listener) {
        Component[] components;
        if (listener == null) {
            return;
        }
        this.mouseListeners.add(listener);
        for (Component comp : getComponents()) {
            comp.addMouseListener(listener);
        }
    }

    public synchronized void addMouseListenerOriginally(MouseListener listener) {
        super.addMouseListener(listener);
    }

    public synchronized void removeMouseListener(MouseListener listener) {
        Component[] components;
        if (listener == null) {
            return;
        }
        this.mouseListeners.remove(listener);
        for (Component comp : getComponents()) {
            comp.removeMouseListener(listener);
        }
    }

    protected synchronized void removeMouseListenerOriginally(MouseListener listener) {
        super.removeMouseListener(listener);
    }

    public boolean contains(Component comp) {
        Component[] components;
        if (comp == null) {
            return false;
        }
        for (Component c : getComponents()) {
            if (comp.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public Insets setInsets(int top, int left, int bottom, int right) {
        Insets insets = new Insets(top, left, bottom, right);
        setInsets(insets);
        return insets;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintComponent(Graphics g0) {
        if (this.opacity == 1.0f) {
            super.paintComponent(g0);
            return;
        }
        Graphics2D g = (Graphics2D) g0;
        g.setComposite(this.aComp);
        super.paintComponent(g0);
    }
}
