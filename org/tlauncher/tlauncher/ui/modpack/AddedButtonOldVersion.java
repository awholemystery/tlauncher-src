package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CompletableFuture;
import javax.swing.JFrame;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/AddedButtonOldVersion.class */
public class AddedButtonOldVersion extends AddedModpackStuffFrame {
    protected final UpdaterButton oldAdditionButton;
    protected final ModpackManager manager;
    private static final long serialVersionUID = 5115926326682859514L;
    private String dateTimeFormat;

    public AddedButtonOldVersion(JFrame parent, String title, String message1, final Long id, final GameType type) {
        super(parent, title, message1);
        this.dateTimeFormat = "dd.MM.YYYY HH:MM:ss";
        final Color backgroundOldButtonColor = new Color(213, 213, 213);
        this.oldAdditionButton = new UpdaterButton(backgroundOldButtonColor, "explorer.button.update");
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        this.spring.putConstraint("South", this.oldAdditionButton, 150, "North", this.message);
        this.spring.putConstraint("West", this.oldAdditionButton, 250, "West", this.panel);
        this.panel.add(this.oldAdditionButton);
        this.oldAdditionButton.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion.1
            public void mouseEntered(MouseEvent e) {
                AddedButtonOldVersion.this.oldAdditionButton.setBackground(new Color(160, 160, 160));
            }

            public void mouseExited(MouseEvent e) {
                AddedButtonOldVersion.this.oldAdditionButton.setBackground(backgroundOldButtonColor);
            }
        });
        this.oldAdditionButton.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion.2
            public void actionPerformed(ActionEvent e) {
                Long l = id;
                GameType gameType = type;
                CompletableFuture.runAsync(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0016: INVOKE  
                      (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x000e: INVOKE  (r0v2 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                      (wrap: java.lang.Runnable : 0x0009: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                      (r4v0 'this' org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion$2 A[D('this' org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion$2), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r1v1 'l' java.lang.Long A[DONT_INLINE])
                      (r2v1 'gameType' org.tlauncher.modpack.domain.client.share.GameType A[DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  
                      (r0 I:org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion$2)
                      (r1 I:java.lang.Long)
                      (r2 I:org.tlauncher.modpack.domain.client.share.GameType)
                     type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion.2.lambda$actionPerformed$1(java.lang.Long, org.tlauncher.modpack.domain.client.share.GameType):void)
                     type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                      (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0011: INVOKE_CUSTOM (r1v2 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                     handle type: INVOKE_STATIC
                     lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                     call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion.2.lambda$actionPerformed$3(java.lang.Throwable):java.lang.Void)
                     type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion.2.actionPerformed(java.awt.event.ActionEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/AddedButtonOldVersion$2.class
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
                    Caused by: java.lang.IndexOutOfBoundsException: Index 2 out of bounds for length 2
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
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                    	... 15 more
                    */
                /*
                    this = this;
                    r0 = r4
                    r1 = r4
                    java.lang.Long r1 = r5
                    r2 = r4
                    org.tlauncher.modpack.domain.client.share.GameType r2 = r6
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$actionPerformed$1(r1, r2);
                    }
                    java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                    void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                        return lambda$actionPerformed$3(v0);
                    }
                    java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion.AnonymousClass2.actionPerformed(java.awt.event.ActionEvent):void");
            }
        });
    }
}
