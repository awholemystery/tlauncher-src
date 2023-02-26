package org.tlauncher.tlauncher.ui.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorGroupHandler.class */
public class EditorGroupHandler {
    private final List<EditorFieldChangeListener> listeners;
    private final int checkedLimit;
    private int changedFlag;
    private int checkedFlag;

    static /* synthetic */ int access$004(EditorGroupHandler x0) {
        int i = x0.changedFlag + 1;
        x0.changedFlag = i;
        return i;
    }

    static /* synthetic */ int access$104(EditorGroupHandler x0) {
        int i = x0.checkedFlag + 1;
        x0.checkedFlag = i;
        return i;
    }

    public EditorGroupHandler(List<? extends EditorHandler> handlers) {
        if (handlers == null) {
            throw new NullPointerException();
        }
        this.checkedLimit = handlers.size();
        EditorFieldListener listener = new EditorFieldListener() { // from class: org.tlauncher.tlauncher.ui.editor.EditorGroupHandler.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldListener
            public void onChange(EditorHandler handler, String oldValue, String newValue) {
                if (newValue == null) {
                    return;
                }
                if (!newValue.equals(oldValue)) {
                    EditorGroupHandler.access$004(EditorGroupHandler.this);
                }
                EditorGroupHandler.access$104(EditorGroupHandler.this);
                if (EditorGroupHandler.this.checkedFlag == EditorGroupHandler.this.checkedLimit) {
                    if (EditorGroupHandler.this.changedFlag > 0) {
                        for (EditorFieldChangeListener listener2 : EditorGroupHandler.this.listeners) {
                            listener2.onChange(null, null);
                        }
                    }
                    EditorGroupHandler.this.checkedFlag = EditorGroupHandler.this.changedFlag = 0;
                }
            }
        };
        for (int i = 0; i < handlers.size(); i++) {
            EditorHandler handler = handlers.get(i);
            if (handler == null) {
                throw new NullPointerException("Handler is NULL at " + i);
            }
            handler.addListener(listener);
        }
        this.listeners = Collections.synchronizedList(new ArrayList());
    }

    public EditorGroupHandler(EditorHandler... handlers) {
        this(Arrays.asList(handlers));
    }

    public boolean addListener(EditorFieldChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return this.listeners.add(listener);
    }

    public boolean removeListener(EditorFieldChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return this.listeners.remove(listener);
    }
}
