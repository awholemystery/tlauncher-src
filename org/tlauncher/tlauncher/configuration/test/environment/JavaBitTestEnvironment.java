package org.tlauncher.tlauncher.configuration.test.environment;

import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/test/environment/JavaBitTestEnvironment.class */
public class JavaBitTestEnvironment implements TestEnvironment {
    private Configuration c;

    public JavaBitTestEnvironment(Configuration c) {
        this.c = c;
    }

    @Override // org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment
    public boolean testEnvironment() {
        String systemType = OS.getSystemInfo("System Type");
        String processorDesc = OS.getSystemInfo("Processor(s)");
        U.debug("system type:" + systemType);
        U.debug("Processor:" + processorDesc);
        if (OS.CURRENT == OS.WINDOWS && systemType != null && processorDesc != null && systemType.toLowerCase().contains("86")) {
            U.log("system type" + systemType);
            U.log("Processor(s)" + processorDesc);
            if (processorDesc.toLowerCase().contains("64")) {
                this.c.set(TestEnvironment.WARMING_MESSAGE, "system.32x.not.proper", true);
                U.log("not proper bit system for this processor " + OS.getSystemInfo("Processor(s)"));
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment
    public void fix() {
        if (this.c.getBoolean("system.bit.message.not.show")) {
            return;
        }
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0016: INVOKE  
              (wrap: java.lang.Runnable : 0x0011: INVOKE_CUSTOM (r0v5 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment A[D('this' org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              ("system.bit.message.not.show")
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment), (r1 I:java.lang.String) type: DIRECT call: org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment.lambda$fix$0(java.lang.String):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment.fix():void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/test/environment/JavaBitTestEnvironment.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
            Caused by: java.lang.ClassCastException: class jadx.core.dex.instructions.args.InsnWrapArg cannot be cast to class jadx.core.dex.instructions.args.RegisterArg (jadx.core.dex.instructions.args.InsnWrapArg and jadx.core.dex.instructions.args.RegisterArg are in unnamed module of loader 'app')
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:958)
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
            	... 19 more
            */
        /*
            this = this;
            java.lang.String r0 = "system.bit.message.not.show"
            r4 = r0
            r0 = r3
            org.tlauncher.tlauncher.configuration.Configuration r0 = r0.c
            r1 = r4
            boolean r0 = r0.getBoolean(r1)
            if (r0 == 0) goto Lf
            return
        Lf:
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$fix$0(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment.fix():void");
    }
}
