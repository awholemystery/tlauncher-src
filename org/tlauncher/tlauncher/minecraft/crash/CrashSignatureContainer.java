package org.tlauncher.tlauncher.minecraft.crash;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/crash/CrashSignatureContainer.class */
public class CrashSignatureContainer {
    private static final int universalExitCode = 0;
    private Map<String, String> variables = new LinkedHashMap();
    private List<CrashSignature> signatures = new ArrayList();

    public Map<String, String> getVariables() {
        return this.variables;
    }

    public List<CrashSignature> getSignatures() {
        return this.signatures;
    }

    public String getVariable(String key) {
        return this.variables.get(key);
    }

    public Pattern getPattern(String key) {
        return Pattern.compile(this.variables.get(key));
    }

    public String toString() {
        return getClass().getSimpleName() + "{\nvariables='" + this.variables + "',\nsignatures='" + this.signatures + "'}";
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/crash/CrashSignatureContainer$CrashSignature.class */
    public class CrashSignature {
        private String name;
        private String version;
        private String path;
        private String pattern;
        private int exit;
        private boolean fake;
        private boolean forge;
        private Pattern versionPattern;
        private Pattern linePattern;

        public CrashSignature() {
        }

        public String getName() {
            return this.name;
        }

        public Pattern getVersion() {
            return this.versionPattern;
        }

        public boolean hasVersion() {
            return this.version != null;
        }

        public boolean isFake() {
            return this.fake;
        }

        public Pattern getPattern() {
            return this.linePattern;
        }

        public boolean hasPattern() {
            return this.pattern != null;
        }

        public String getPath() {
            return this.path;
        }

        public int getExitCode() {
            return this.exit;
        }

        public String toString() {
            return getClass().getSimpleName() + "{name='" + this.name + "', version='" + this.version + "', path='" + this.path + "', pattern='" + this.pattern + "', exitCode=" + this.exit + ", forge=" + this.forge + ", versionPattern='" + this.versionPattern + "', linePattern='" + this.linePattern + "'}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/crash/CrashSignatureContainer$CrashSignatureListSimpleDeserializer.class */
    public static class CrashSignatureListSimpleDeserializer {
        private final Gson defaultContext = TLauncher.getGson();
        private Map<String, String> variables;
        private String forgePrefix;

        CrashSignatureListSimpleDeserializer() {
        }

        public void setVariables(Map<String, String> vars) {
            this.variables = vars == null ? new HashMap<>() : vars;
            this.forgePrefix = this.variables.containsKey("forge") ? this.variables.get("forge") : CoreConstants.EMPTY_STRING;
        }

        public List<CrashSignature> deserialize(JsonElement elem) throws JsonParseException {
            List<CrashSignature> signatureList = (List) this.defaultContext.fromJson(elem, new TypeToken<List<CrashSignature>>() { // from class: org.tlauncher.tlauncher.minecraft.crash.CrashSignatureContainer.CrashSignatureListSimpleDeserializer.1
            }.getType());
            for (CrashSignature signature : signatureList) {
                analyzeSignature(signature);
            }
            return signatureList;
        }

        private CrashSignature analyzeSignature(CrashSignature signature) {
            if (signature.name != null && !signature.name.isEmpty()) {
                if (signature.version != null) {
                    String pattern = signature.version;
                    for (Map.Entry<String, String> en : this.variables.entrySet()) {
                        String varName = en.getKey();
                        String varVal = en.getValue();
                        pattern = pattern.replace("${" + varName + "}", varVal);
                    }
                    signature.versionPattern = Pattern.compile(pattern);
                }
                if (signature.pattern != null) {
                    String pattern2 = signature.pattern;
                    for (Map.Entry<String, String> en2 : this.variables.entrySet()) {
                        String varName2 = en2.getKey();
                        String varVal2 = en2.getValue();
                        pattern2 = pattern2.replace("${" + varName2 + "}", varVal2);
                    }
                    if (signature.forge) {
                        pattern2 = this.forgePrefix + pattern2;
                    }
                    signature.linePattern = Pattern.compile(pattern2);
                }
                if (signature.versionPattern == null && signature.linePattern == null && signature.exit == 0) {
                    throw new JsonParseException("Useless signature found: " + signature.name);
                }
                return signature;
            }
            throw new JsonParseException("Invalid name: \"" + signature.name + "\"");
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/crash/CrashSignatureContainer$CrashSignatureContainerDeserializer.class */
    static class CrashSignatureContainerDeserializer implements JsonDeserializer<CrashSignatureContainer> {
        private final Gson defaultContext = TLauncher.getGson();
        private final CrashSignatureListSimpleDeserializer listDeserializer = new CrashSignatureListSimpleDeserializer();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public CrashSignatureContainer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = element.getAsJsonObject();
            Map<String, String> rawVariables = (Map) this.defaultContext.fromJson(object.get("variables"), new TypeToken<Map<String, String>>() { // from class: org.tlauncher.tlauncher.minecraft.crash.CrashSignatureContainer.CrashSignatureContainerDeserializer.1
            }.getType());
            Map<String, String> variables = new LinkedHashMap<>();
            for (Map.Entry<String, String> rawEn : rawVariables.entrySet()) {
                String varName = rawEn.getKey();
                String varVal = rawEn.getValue();
                for (Map.Entry<String, String> en : variables.entrySet()) {
                    String replaceName = en.getKey();
                    String replaceVal = en.getValue();
                    varVal = varVal.replace("${" + replaceName + "}", replaceVal);
                }
                variables.put(varName, varVal);
            }
            this.listDeserializer.setVariables(variables);
            List<CrashSignature> signatures = this.listDeserializer.deserialize(object.get("signatures"));
            CrashSignatureContainer list = new CrashSignatureContainer();
            list.variables = variables;
            list.signatures = signatures;
            return list;
        }
    }
}
