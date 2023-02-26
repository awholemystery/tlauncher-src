package net.minecraft.launcher.process;

import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.tlauncher.util.TlauncherUtil;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/process/JavaProcessLauncher.class */
public class JavaProcessLauncher {
    private final String jvmPath;
    private final List<String> commands = new ArrayList();
    private File directory;
    private ProcessBuilder process;
    private JavaProcessListener listener;

    public JavaProcessLauncher(String jvmPath, String[] commands) {
        this.jvmPath = jvmPath;
        Collections.addAll(this.commands, commands);
    }

    public JavaProcess start() throws IOException {
        List<String> full = getFullCommands();
        return new JavaProcess(full, createProcess().start(), this.listener);
    }

    public ProcessBuilder createProcess() {
        if (this.process == null) {
            this.process = new ProcessBuilder(getFullCommands()).directory(this.directory).redirectErrorStream(true);
        }
        String javaOption = TlauncherUtil.findJavaOptionAndGetName();
        if (Objects.nonNull(javaOption)) {
            this.process.environment().put(javaOption, CoreConstants.EMPTY_STRING);
        }
        return this.process;
    }

    private List<String> getFullCommands() {
        List<String> result = new ArrayList<>(this.commands);
        result.add(0, this.jvmPath);
        return result;
    }

    public String getCommandsAsString() {
        List<String> parts = getFullCommands();
        StringBuilder full = new StringBuilder();
        boolean first = true;
        for (String part : parts) {
            if (first) {
                first = false;
            } else {
                full.append(' ');
            }
            full.append(part);
        }
        return full.toString();
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void addCommand(Object command) {
        this.commands.add(command.toString());
    }

    public void addCommand(Object key, Object value) {
        this.commands.add(key.toString());
        this.commands.add(value.toString());
    }

    public void addCommands(Object[] commands) {
        for (Object c : commands) {
            this.commands.add(c.toString());
        }
    }

    public void addSplitCommands(Object commands) {
        addCommands(commands.toString().split(" "));
    }

    public JavaProcessLauncher directory(File directory) {
        this.directory = directory;
        return this;
    }

    public File getDirectory() {
        return this.directory;
    }

    public String toString() {
        return "JavaProcessLauncher[commands=" + this.commands + ", java=" + this.jvmPath + "]";
    }

    public void setListener(JavaProcessListener listener) {
        this.listener = listener;
    }
}
