package net.minecraft.launcher.process;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/process/JavaProcess.class */
public class JavaProcess {
    private final List<String> commands;
    private final Process process;

    public JavaProcess(List<String> commands, Process process, JavaProcessListener listener) {
        this.commands = commands;
        this.process = process;
        ProcessMonitorThread monitor = new ProcessMonitorThread(this, listener);
        monitor.start();
    }

    public Process getRawProcess() {
        return this.process;
    }

    public boolean isRunning() {
        try {
            this.process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    public int getExitCode() {
        try {
            return this.process.exitValue();
        } catch (IllegalThreadStateException ex) {
            ex.fillInStackTrace();
            throw ex;
        }
    }

    public String toString() {
        return "JavaProcess[commands=" + this.commands + ", isRunning=" + isRunning() + "]";
    }

    public void stop() {
        this.process.destroy();
    }
}
