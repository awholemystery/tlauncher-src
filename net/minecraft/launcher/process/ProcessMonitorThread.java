package net.minecraft.launcher.process;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/process/ProcessMonitorThread.class */
class ProcessMonitorThread extends Thread {
    private final JavaProcess process;
    private JavaProcessListener listener;

    public ProcessMonitorThread(JavaProcess process, JavaProcessListener listener) {
        this.process = process;
        this.listener = listener;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:19:0x0086
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        /*
            r5 = this;
            r0 = r5
            net.minecraft.launcher.process.JavaProcess r0 = r0.process
            java.lang.Process r0 = r0.getRawProcess()
            r6 = r0
            java.io.InputStreamReader r0 = new java.io.InputStreamReader
            r1 = r0
            r2 = r6
            java.io.InputStream r2 = r2.getInputStream()
            r1.<init>(r2)
            r7 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader
            r1 = r0
            r2 = r7
            r1.<init>(r2)
            r8 = r0
        L1d:
            r0 = r5
            net.minecraft.launcher.process.JavaProcess r0 = r0.process
            boolean r0 = r0.isRunning()
            if (r0 == 0) goto La8
        L27:
            r0 = r8
            java.lang.String r0 = r0.readLine()     // Catch: java.lang.Throwable -> L67
            r1 = r0
            r9 = r1
            if (r0 == 0) goto L4a
            r0 = r5
            net.minecraft.launcher.process.JavaProcessListener r0 = r0.listener     // Catch: java.lang.Throwable -> L67
            if (r0 == 0) goto L27
            r0 = r5
            net.minecraft.launcher.process.JavaProcessListener r0 = r0.listener     // Catch: java.lang.Throwable -> L67
            r1 = r5
            net.minecraft.launcher.process.JavaProcess r1 = r1.process     // Catch: java.lang.Throwable -> L67
            r2 = r9
            r0.onJavaProcessLog(r1, r2)     // Catch: java.lang.Throwable -> L67
            goto L27
        L4a:
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L51
            goto La5
        L51:
            r10 = move-exception
            java.lang.Class<net.minecraft.launcher.process.ProcessMonitorThread> r0 = net.minecraft.launcher.process.ProcessMonitorThread.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.SEVERE
            r2 = 0
            r3 = r10
            r0.log(r1, r2, r3)
            goto La5
        L67:
            r10 = move-exception
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L70
            goto La5
        L70:
            r10 = move-exception
            java.lang.Class<net.minecraft.launcher.process.ProcessMonitorThread> r0 = net.minecraft.launcher.process.ProcessMonitorThread.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.SEVERE
            r2 = 0
            r3 = r10
            r0.log(r1, r2, r3)
            goto La5
        L86:
            r11 = move-exception
            r0 = r8
            r0.close()     // Catch: java.io.IOException -> L8f
            goto La2
        L8f:
            r12 = move-exception
            java.lang.Class<net.minecraft.launcher.process.ProcessMonitorThread> r0 = net.minecraft.launcher.process.ProcessMonitorThread.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.SEVERE
            r2 = 0
            r3 = r12
            r0.log(r1, r2, r3)
        La2:
            r0 = r11
            throw r0
        La5:
            goto L1d
        La8:
            r0 = r5
            net.minecraft.launcher.process.JavaProcessListener r0 = r0.listener
            boolean r0 = java.util.Objects.nonNull(r0)
            if (r0 == 0) goto Lbf
            r0 = r5
            net.minecraft.launcher.process.JavaProcessListener r0 = r0.listener
            r1 = r5
            net.minecraft.launcher.process.JavaProcess r1 = r1.process
            r0.onJavaProcessEnded(r1)
        Lbf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: net.minecraft.launcher.process.ProcessMonitorThread.run():void");
    }
}
