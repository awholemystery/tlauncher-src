package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import org.apache.commons.compress.java.util.jar.Pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Pack200PackerAdapter.class */
public class Pack200PackerAdapter extends Pack200Adapter implements Pack200.Packer {
    private final PackingOptions options = new PackingOptions();

    @Override // org.apache.commons.compress.java.util.jar.Pack200.Packer
    public void pack(JarFile file, OutputStream out) throws IOException {
        if (file == null || out == null) {
            throw new IllegalArgumentException("Must specify both input and output streams");
        }
        completed(0.0d);
        try {
            new Archive(file, out, this.options).pack();
            completed(1.0d);
        } catch (Pack200Exception e) {
            throw new IOException("Failed to pack Jar:" + e);
        }
    }

    @Override // org.apache.commons.compress.java.util.jar.Pack200.Packer
    public void pack(JarInputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            throw new IllegalArgumentException("Must specify both input and output streams");
        }
        completed(0.0d);
        PackingOptions options = new PackingOptions();
        try {
            new Archive(in, out, options).pack();
            completed(1.0d);
            in.close();
        } catch (Pack200Exception e) {
            throw new IOException("Failed to pack Jar:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.harmony.pack200.Pack200Adapter
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
        if (newValue != null && !newValue.equals(oldValue)) {
            if (propertyName.startsWith(Pack200.Packer.CLASS_ATTRIBUTE_PFX)) {
                String attributeName = propertyName.substring(Pack200.Packer.CLASS_ATTRIBUTE_PFX.length());
                this.options.addClassAttributeAction(attributeName, (String) newValue);
            } else if (propertyName.startsWith(Pack200.Packer.CODE_ATTRIBUTE_PFX)) {
                String attributeName2 = propertyName.substring(Pack200.Packer.CODE_ATTRIBUTE_PFX.length());
                this.options.addCodeAttributeAction(attributeName2, (String) newValue);
            } else if (propertyName.equals(Pack200.Packer.DEFLATE_HINT)) {
                this.options.setDeflateHint((String) newValue);
            } else if (propertyName.equals(Pack200.Packer.EFFORT)) {
                this.options.setEffort(Integer.parseInt((String) newValue));
            } else if (propertyName.startsWith(Pack200.Packer.FIELD_ATTRIBUTE_PFX)) {
                String attributeName3 = propertyName.substring(Pack200.Packer.FIELD_ATTRIBUTE_PFX.length());
                this.options.addFieldAttributeAction(attributeName3, (String) newValue);
            } else if (propertyName.equals(Pack200.Packer.KEEP_FILE_ORDER)) {
                this.options.setKeepFileOrder(Boolean.parseBoolean((String) newValue));
            } else if (propertyName.startsWith(Pack200.Packer.METHOD_ATTRIBUTE_PFX)) {
                String attributeName4 = propertyName.substring(Pack200.Packer.METHOD_ATTRIBUTE_PFX.length());
                this.options.addMethodAttributeAction(attributeName4, (String) newValue);
            } else if (propertyName.equals(Pack200.Packer.MODIFICATION_TIME)) {
                this.options.setModificationTime((String) newValue);
            } else if (propertyName.startsWith(Pack200.Packer.PASS_FILE_PFX)) {
                if (oldValue != null && !oldValue.equals(CoreConstants.EMPTY_STRING)) {
                    this.options.removePassFile((String) oldValue);
                }
                this.options.addPassFile((String) newValue);
            } else if (propertyName.equals(Pack200.Packer.SEGMENT_LIMIT)) {
                this.options.setSegmentLimit(Long.parseLong((String) newValue));
            } else if (propertyName.equals(Pack200.Packer.UNKNOWN_ATTRIBUTE)) {
                this.options.setUnknownAttributeAction((String) newValue);
            }
        }
    }
}
