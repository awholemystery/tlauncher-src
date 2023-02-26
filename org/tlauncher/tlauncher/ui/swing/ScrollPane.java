package org.tlauncher.tlauncher.ui.swing;

import java.awt.Component;
import javax.swing.JScrollPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ScrollPane.class */
public class ScrollPane extends JScrollPane {
    private static final boolean DEFAULT_BORDER = false;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ScrollPane$ScrollBarPolicy.class */
    public enum ScrollBarPolicy {
        ALWAYS,
        AS_NEEDED,
        NEVER
    }

    public ScrollPane(Component view, ScrollBarPolicy vertical, ScrollBarPolicy horizontal, boolean border) {
        super(view);
        setOpaque(false);
        getViewport().setOpaque(false);
        if (!border) {
            setBorder(null);
        }
        setVBPolicy(vertical);
        setHBPolicy(horizontal);
    }

    public ScrollPane(Component view, ScrollBarPolicy vertical, ScrollBarPolicy horizontal) {
        this(view, vertical, horizontal, false);
    }

    public ScrollPane(Component view, ScrollBarPolicy generalPolicy, boolean border) {
        this(view, generalPolicy, generalPolicy, border);
    }

    public ScrollPane(Component view, ScrollBarPolicy generalPolicy) {
        this(view, generalPolicy, generalPolicy);
    }

    public ScrollPane(Component view, boolean border) {
        this(view, ScrollBarPolicy.AS_NEEDED, border);
    }

    public ScrollPane(Component view) {
        this(view, ScrollBarPolicy.AS_NEEDED);
    }

    public void setVerticalScrollBarPolicy(ScrollBarPolicy policy) {
        int i_policy;
        switch (policy) {
            case ALWAYS:
                i_policy = 22;
                break;
            case AS_NEEDED:
                i_policy = 20;
                break;
            case NEVER:
                i_policy = 21;
                break;
            default:
                throw new IllegalArgumentException();
        }
        super.setVerticalScrollBarPolicy(i_policy);
    }

    public void setHorizontalScrollBarPolicy(ScrollBarPolicy policy) {
        int i_policy;
        switch (policy) {
            case ALWAYS:
                i_policy = 32;
                break;
            case AS_NEEDED:
                i_policy = 30;
                break;
            case NEVER:
                i_policy = 31;
                break;
            default:
                throw new IllegalArgumentException();
        }
        super.setHorizontalScrollBarPolicy(i_policy);
    }

    public void setVBPolicy(ScrollBarPolicy policy) {
        setVerticalScrollBarPolicy(policy);
    }

    public void setHBPolicy(ScrollBarPolicy policy) {
        setHorizontalScrollBarPolicy(policy);
    }
}
