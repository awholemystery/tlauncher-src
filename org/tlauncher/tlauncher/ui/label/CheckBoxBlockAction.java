package org.tlauncher.tlauncher.ui.label;

import ch.qos.logback.core.CoreConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executor;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.ui.swing.OwnImageCheckBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/label/CheckBoxBlockAction.class */
public abstract class CheckBoxBlockAction extends OwnImageCheckBox {
    private static final long serialVersionUID = 1;
    private Object object;
    private Executor executor;

    public abstract void executeRequest();

    public void setObject(Object object) {
        this.object = object;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public CheckBoxBlockAction(String selectedIcon, String diselectedIcon) {
        super(CoreConstants.EMPTY_STRING, selectedIcon, diselectedIcon);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction.1
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
                      (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                      (r3v0 'this' org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction$1 A[D('this' org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction$1), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r4v0 'e' java.awt.event.MouseEvent A[D('e' java.awt.event.MouseEvent), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction$1), (r1 I:java.awt.event.MouseEvent) type: DIRECT call: org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction.1.lambda$mousePressed$4(java.awt.event.MouseEvent):void)
                     type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction.1.mousePressed(java.awt.event.MouseEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/label/CheckBoxBlockAction$1.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
                    	at java.base/java.util.ArrayList.forEach(Unknown Source)
                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
                    	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
                    Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
                    	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
                    	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
                    	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
                    	at java.base/java.util.Objects.checkIndex(Unknown Source)
                    	at java.base/java.util.ArrayList.get(Unknown Source)
                    	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
                    	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                    	... 15 more
                    */
                /*
                    this = this;
                    r0 = r3
                    r1 = r4
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$mousePressed$4(r1);
                    }
                    javax.swing.SwingUtilities.invokeLater(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction.AnonymousClass1.mousePressed(java.awt.event.MouseEvent):void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeWithBlock() {
        synchronized (this.object) {
            executeRequest();
        }
    }
}
