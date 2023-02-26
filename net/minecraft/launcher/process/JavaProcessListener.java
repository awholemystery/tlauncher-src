package net.minecraft.launcher.process;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/process/JavaProcessListener.class */
public interface JavaProcessListener {
    void onJavaProcessLog(JavaProcess javaProcess, String str);

    void onJavaProcessEnded(JavaProcess javaProcess);

    void onJavaProcessError(JavaProcess javaProcess, Throwable th);
}
