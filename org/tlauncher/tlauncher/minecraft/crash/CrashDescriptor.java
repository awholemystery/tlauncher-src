package org.tlauncher.tlauncher.minecraft.crash;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.tlauncher.tlauncher.minecraft.crash.CrashSignatureContainer;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/crash/CrashDescriptor.class */
public class CrashDescriptor {
    private static CrashSignatureContainer container;
    public static final int goodExitCode = 0;
    private static final String loggerPrefix = "[Crash]";
    private final MinecraftLauncher launcher;

    public CrashDescriptor(MinecraftLauncher launcher) {
        if (launcher == null) {
            throw new NullPointerException();
        }
        this.launcher = launcher;
    }

    public Crash scan() {
        int exitCode = this.launcher.getExitCode();
        if (exitCode == 0 && System.currentTimeMillis() > this.launcher.getStartupTime() + 60000) {
            return null;
        }
        Crash crash = new Crash();
        if (container.getVariables().isEmpty()) {
            return crash;
        }
        Pattern filePattern = container.getPattern("crash");
        String version = this.launcher.getVersionName();
        Scanner scanner = new Scanner(U.readFileLog());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (filePattern.matcher(line).matches()) {
                Matcher fileMatcher = filePattern.matcher(line);
                if (fileMatcher.matches() && fileMatcher.groupCount() == 1) {
                    crash.setFile(fileMatcher.group(1));
                    log("Found crash report file:", crash.getFile());
                }
            } else {
                for (CrashSignatureContainer.CrashSignature signature : container.getSignatures()) {
                    if (!signature.hasVersion() || signature.getVersion().matcher(version).matches()) {
                        if (signature.getExitCode() == 0 || signature.getExitCode() == exitCode) {
                            if (!signature.hasPattern() || signature.getPattern().matcher(line).matches()) {
                                if (signature.isFake()) {
                                    log("Minecraft closed with an illegal exit code not due to error. Scanning has been cancelled");
                                    log("Fake signature:", signature.getName());
                                    scanner.close();
                                    return null;
                                } else if (!crash.hasSignature(signature)) {
                                    log("Signature \"" + signature.getName() + "\" matches!");
                                    crash.addSignature(signature);
                                }
                            }
                        }
                    }
                }
                continue;
            }
        }
        scanner.close();
        if (crash.isRecognized() && exitCode == 0 && !crash.getSignatures().stream().filter(e -> {
            return e.getName().equals("Bad video drivers");
        }).findAny().isPresent()) {
            return null;
        }
        if (crash.isRecognized()) {
            log("Crash has been recognized!");
        } else if (exitCode == 0) {
            return null;
        }
        return crash;
    }

    void log(Object... w) {
        this.launcher.log(loggerPrefix, w);
        U.log(loggerPrefix, w);
    }

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CrashSignatureContainer.class, new CrashSignatureContainer.CrashSignatureContainerDeserializer());
        Gson gson = builder.create();
        try {
            container = (CrashSignatureContainer) gson.fromJson(FileUtil.getResource(TLauncher.class.getResource("/signatures.json")), (Class<Object>) CrashSignatureContainer.class);
        } catch (Exception e) {
            U.log("Cannot parse crash signatures!", e);
            container = new CrashSignatureContainer();
        }
    }
}
