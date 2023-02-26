package org.apache.log4j.spi;

import java.io.PrintWriter;
import java.util.Vector;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/spi/VectorWriter.class */
class VectorWriter extends PrintWriter {
    private Vector v;

    VectorWriter() {
        super(new NullWriter());
        this.v = new Vector();
    }

    @Override // java.io.PrintWriter
    public void print(Object o) {
        this.v.addElement(String.valueOf(o));
    }

    @Override // java.io.PrintWriter
    public void print(char[] chars) {
        this.v.addElement(new String(chars));
    }

    @Override // java.io.PrintWriter
    public void print(String s) {
        this.v.addElement(s);
    }

    @Override // java.io.PrintWriter
    public void println(Object o) {
        this.v.addElement(String.valueOf(o));
    }

    @Override // java.io.PrintWriter
    public void println(char[] chars) {
        this.v.addElement(new String(chars));
    }

    @Override // java.io.PrintWriter
    public void println(String s) {
        this.v.addElement(s);
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(char[] chars) {
        this.v.addElement(new String(chars));
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(char[] chars, int off, int len) {
        this.v.addElement(new String(chars, off, len));
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(String s, int off, int len) {
        this.v.addElement(s.substring(off, off + len));
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(String s) {
        this.v.addElement(s);
    }

    public String[] toStringArray() {
        int len = this.v.size();
        String[] sa = new String[len];
        for (int i = 0; i < len; i++) {
            sa[i] = (String) this.v.elementAt(i);
        }
        return sa;
    }
}
