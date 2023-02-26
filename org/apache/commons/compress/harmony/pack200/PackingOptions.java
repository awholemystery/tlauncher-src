package org.apache.commons.compress.harmony.pack200;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.harmony.pack200.NewAttribute;
import org.apache.commons.compress.java.util.jar.Pack200;
import org.objectweb.asm.Attribute;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/PackingOptions.class */
public class PackingOptions {
    private static final Attribute[] EMPTY_ATTRIBUTE_ARRAY = new Attribute[0];
    public static final long SEGMENT_LIMIT = 1000000;
    public static final String STRIP = "strip";
    public static final String ERROR = "error";
    public static final String PASS = "pass";
    public static final String KEEP = "keep";
    private boolean stripDebug;
    private boolean verbose;
    private String logFile;
    private Attribute[] unknownAttributeTypes;
    private boolean gzip = true;
    private boolean keepFileOrder = true;
    private long segmentLimit = SEGMENT_LIMIT;
    private int effort = 5;
    private String deflateHint = "keep";
    private String modificationTime = "keep";
    private final List<String> passFiles = new ArrayList();
    private String unknownAttributeAction = "pass";
    private final Map<String, String> classAttributeActions = new HashMap();
    private final Map<String, String> fieldAttributeActions = new HashMap();
    private final Map<String, String> methodAttributeActions = new HashMap();
    private final Map<String, String> codeAttributeActions = new HashMap();

    public void addClassAttributeAction(String attributeName, String action) {
        this.classAttributeActions.put(attributeName, action);
    }

    public void addCodeAttributeAction(String attributeName, String action) {
        this.codeAttributeActions.put(attributeName, action);
    }

    public void addFieldAttributeAction(String attributeName, String action) {
        this.fieldAttributeActions.put(attributeName, action);
    }

    public void addMethodAttributeAction(String attributeName, String action) {
        this.methodAttributeActions.put(attributeName, action);
    }

    private void addOrUpdateAttributeActions(List<Attribute> prototypes, Map<String, String> attributeActions, int tag) {
        NewAttribute newAttribute;
        if (attributeActions != null && attributeActions.size() > 0) {
            for (String name : attributeActions.keySet()) {
                String action = attributeActions.get(name);
                boolean prototypeExists = false;
                Iterator<Attribute> it = prototypes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Object prototype = it.next();
                    NewAttribute newAttribute2 = (NewAttribute) prototype;
                    if (newAttribute2.type.equals(name)) {
                        newAttribute2.addContext(tag);
                        prototypeExists = true;
                        break;
                    }
                }
                if (!prototypeExists) {
                    if ("error".equals(action)) {
                        newAttribute = new NewAttribute.ErrorAttribute(name, tag);
                    } else if ("strip".equals(action)) {
                        newAttribute = new NewAttribute.StripAttribute(name, tag);
                    } else if ("pass".equals(action)) {
                        newAttribute = new NewAttribute.PassAttribute(name, tag);
                    } else {
                        newAttribute = new NewAttribute(name, action, tag);
                    }
                    prototypes.add(newAttribute);
                }
            }
        }
    }

    public void addPassFile(String passFileName) {
        String fileSeparator = System.getProperty("file.separator");
        if (fileSeparator.equals("\\")) {
            fileSeparator = fileSeparator + "\\";
        }
        this.passFiles.add(passFileName.replaceAll(fileSeparator, "/"));
    }

    public String getDeflateHint() {
        return this.deflateHint;
    }

    public int getEffort() {
        return this.effort;
    }

    public String getLogFile() {
        return this.logFile;
    }

    public String getModificationTime() {
        return this.modificationTime;
    }

    private String getOrDefault(Map<String, String> map, String type, String defaultValue) {
        return map == null ? defaultValue : map.getOrDefault(type, defaultValue);
    }

    public long getSegmentLimit() {
        return this.segmentLimit;
    }

    public String getUnknownAttributeAction() {
        return this.unknownAttributeAction;
    }

    public Attribute[] getUnknownAttributePrototypes() {
        if (this.unknownAttributeTypes == null) {
            List<Attribute> prototypes = new ArrayList<>();
            addOrUpdateAttributeActions(prototypes, this.classAttributeActions, 0);
            addOrUpdateAttributeActions(prototypes, this.methodAttributeActions, 2);
            addOrUpdateAttributeActions(prototypes, this.fieldAttributeActions, 1);
            addOrUpdateAttributeActions(prototypes, this.codeAttributeActions, 3);
            this.unknownAttributeTypes = (Attribute[]) prototypes.toArray(EMPTY_ATTRIBUTE_ARRAY);
        }
        return this.unknownAttributeTypes;
    }

    public String getUnknownClassAttributeAction(String type) {
        return getOrDefault(this.classAttributeActions, type, this.unknownAttributeAction);
    }

    public String getUnknownCodeAttributeAction(String type) {
        return getOrDefault(this.codeAttributeActions, type, this.unknownAttributeAction);
    }

    public String getUnknownFieldAttributeAction(String type) {
        return getOrDefault(this.fieldAttributeActions, type, this.unknownAttributeAction);
    }

    public String getUnknownMethodAttributeAction(String type) {
        return getOrDefault(this.methodAttributeActions, type, this.unknownAttributeAction);
    }

    public boolean isGzip() {
        return this.gzip;
    }

    public boolean isKeepDeflateHint() {
        return "keep".equals(this.deflateHint);
    }

    public boolean isKeepFileOrder() {
        return this.keepFileOrder;
    }

    public boolean isPassFile(String passFileName) {
        Iterator<String> it = this.passFiles.iterator();
        while (it.hasNext()) {
            String pass = it.next();
            if (passFileName.equals(pass)) {
                return true;
            }
            if (!pass.endsWith(".class")) {
                if (!pass.endsWith("/")) {
                    pass = pass + "/";
                }
                return passFileName.startsWith(pass);
            }
        }
        return false;
    }

    public boolean isStripDebug() {
        return this.stripDebug;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void removePassFile(String passFileName) {
        this.passFiles.remove(passFileName);
    }

    public void setDeflateHint(String deflateHint) {
        if (!"keep".equals(deflateHint) && !"true".equals(deflateHint) && !"false".equals(deflateHint)) {
            throw new IllegalArgumentException("Bad argument: -H " + deflateHint + " ? deflate hint should be either true, false or keep (default)");
        }
        this.deflateHint = deflateHint;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }

    public void setKeepFileOrder(boolean keepFileOrder) {
        this.keepFileOrder = keepFileOrder;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setModificationTime(String modificationTime) {
        if (!"keep".equals(modificationTime) && !Pack200.Packer.LATEST.equals(modificationTime)) {
            throw new IllegalArgumentException("Bad argument: -m " + modificationTime + " ? transmit modtimes should be either latest or keep (default)");
        }
        this.modificationTime = modificationTime;
    }

    public void setQuiet(boolean quiet) {
        this.verbose = !quiet;
    }

    public void setSegmentLimit(long segmentLimit) {
        this.segmentLimit = segmentLimit;
    }

    public void setStripDebug(boolean stripDebug) {
        this.stripDebug = stripDebug;
    }

    public void setUnknownAttributeAction(String unknownAttributeAction) {
        this.unknownAttributeAction = unknownAttributeAction;
        if (!"pass".equals(unknownAttributeAction) && !"error".equals(unknownAttributeAction) && !"strip".equals(unknownAttributeAction)) {
            throw new IllegalArgumentException("Incorrect option for -U, " + unknownAttributeAction);
        }
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
