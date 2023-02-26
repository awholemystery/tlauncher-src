package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.harmony.pack200.BHSDCodec;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPDouble;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFloat;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPInteger;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPInterfaceMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPLong;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPNameAndType;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.NewAttribute;
import org.apache.commons.io.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands.class */
public class NewAttributeBands extends BandSet {
    private final AttributeLayout attributeLayout;
    private int backwardsCallCount;
    protected List<AttributeLayoutElement> attributeLayoutElements;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$AttributeLayoutElement.class */
    public interface AttributeLayoutElement {
        void readBands(InputStream inputStream, int i) throws IOException, Pack200Exception;

        void addToAttribute(int i, NewAttribute newAttribute);
    }

    public NewAttributeBands(Segment segment, AttributeLayout attributeLayout) throws IOException {
        super(segment);
        this.attributeLayout = attributeLayout;
        parseLayout();
        attributeLayout.setBackwardsCallCount(this.backwardsCallCount);
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void read(InputStream in) throws IOException, Pack200Exception {
    }

    public List<Attribute> parseAttributes(InputStream in, int occurrenceCount) throws IOException, Pack200Exception {
        for (AttributeLayoutElement element : this.attributeLayoutElements) {
            element.readBands(in, occurrenceCount);
        }
        List<Attribute> attributes = new ArrayList<>(occurrenceCount);
        for (int i = 0; i < occurrenceCount; i++) {
            attributes.add(getOneAttribute(i, this.attributeLayoutElements));
        }
        return attributes;
    }

    private Attribute getOneAttribute(int index, List<AttributeLayoutElement> elements) {
        NewAttribute attribute = new NewAttribute(this.segment.getCpBands().cpUTF8Value(this.attributeLayout.getName()), this.attributeLayout.getIndex());
        for (AttributeLayoutElement element : elements) {
            element.addToAttribute(index, attribute);
        }
        return attribute;
    }

    private void parseLayout() throws IOException {
        if (this.attributeLayoutElements == null) {
            this.attributeLayoutElements = new ArrayList();
            StringReader stream = new StringReader(this.attributeLayout.getLayout());
            while (true) {
                AttributeLayoutElement e = readNextAttributeElement(stream);
                if (e != null) {
                    this.attributeLayoutElements.add(e);
                } else {
                    resolveCalls();
                    return;
                }
            }
        }
    }

    private void resolveCalls() {
        int backwardsCalls = 0;
        for (int i = 0; i < this.attributeLayoutElements.size(); i++) {
            AttributeLayoutElement element = this.attributeLayoutElements.get(i);
            if (element instanceof Callable) {
                Callable callable = (Callable) element;
                if (i == 0) {
                    callable.setFirstCallable(true);
                }
                for (LayoutElement layoutElement : callable.body) {
                    backwardsCalls += resolveCallsForElement(i, callable, layoutElement);
                }
            }
        }
        this.backwardsCallCount = backwardsCalls;
    }

    private int resolveCallsForElement(int i, Callable currentCallable, LayoutElement layoutElement) {
        int backwardsCalls = 0;
        if (layoutElement instanceof Call) {
            Call call = (Call) layoutElement;
            int index = call.callableIndex;
            if (index == 0) {
                backwardsCalls = 0 + 1;
                call.setCallable(currentCallable);
            } else if (index > 0) {
                int k = i + 1;
                while (true) {
                    if (k >= this.attributeLayoutElements.size()) {
                        break;
                    }
                    AttributeLayoutElement el = this.attributeLayoutElements.get(k);
                    if (el instanceof Callable) {
                        index--;
                        if (index == 0) {
                            call.setCallable((Callable) el);
                            break;
                        }
                    }
                    k++;
                }
            } else {
                backwardsCalls = 0 + 1;
                int k2 = i - 1;
                while (true) {
                    if (k2 < 0) {
                        break;
                    }
                    AttributeLayoutElement el2 = this.attributeLayoutElements.get(k2);
                    if (el2 instanceof Callable) {
                        index++;
                        if (index == 0) {
                            call.setCallable((Callable) el2);
                            break;
                        }
                    }
                    k2--;
                }
            }
        } else if (layoutElement instanceof Replication) {
            List<LayoutElement> children = ((Replication) layoutElement).layoutElements;
            for (LayoutElement child : children) {
                backwardsCalls += resolveCallsForElement(i, currentCallable, child);
            }
        }
        return backwardsCalls;
    }

    private AttributeLayoutElement readNextAttributeElement(StringReader stream) throws IOException {
        stream.mark(1);
        int next = stream.read();
        if (next == -1) {
            return null;
        }
        if (next == 91) {
            return new Callable(readBody(getStreamUpToMatchingBracket(stream)));
        }
        stream.reset();
        return readNextLayoutElement(stream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LayoutElement readNextLayoutElement(StringReader stream) throws IOException {
        int nextChar = stream.read();
        if (nextChar == -1) {
            return null;
        }
        switch (nextChar) {
            case 40:
                int number = readNumber(stream).intValue();
                stream.read();
                return new Call(number);
            case CoreConstants.RIGHT_PARENTHESIS_CHAR /* 41 */:
            case 42:
            case 43:
            case CoreConstants.COMMA_CHAR /* 44 */:
            case CoreConstants.DASH_CHAR /* 45 */:
            case 46:
            case IOUtils.DIR_SEPARATOR_UNIX /* 47 */:
            case 48:
            case TarConstants.LF_LINK /* 49 */:
            case 50:
            case TarConstants.LF_CHR /* 51 */:
            case TarConstants.LF_BLK /* 52 */:
            case TarConstants.LF_DIR /* 53 */:
            case TarConstants.LF_FIFO /* 54 */:
            case TarConstants.LF_CONTIG /* 55 */:
            case 56:
            case 57:
            case CoreConstants.COLON_CHAR /* 58 */:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 67:
            case 68:
            case 69:
            case 71:
            case 74:
            case 76:
            case TarConstants.LF_MULTIVOLUME /* 77 */:
            case 81:
            case 85:
            default:
                return null;
            case 66:
            case 72:
            case 73:
            case 86:
                return new Integral(new String(new char[]{(char) nextChar}));
            case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
            case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
                return new Integral(new String(new char[]{(char) nextChar, (char) stream.read()}));
            case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
            case 82:
                StringBuilder string = new StringBuilder(CoreConstants.EMPTY_STRING).append((char) nextChar).append((char) stream.read());
                char nxt = (char) stream.read();
                string.append(nxt);
                if (nxt == 'N') {
                    string.append((char) stream.read());
                }
                return new Reference(string.toString());
            case 78:
                char uint_type = (char) stream.read();
                stream.read();
                String str = readUpToMatchingBracket(stream);
                return new Replication(CoreConstants.EMPTY_STRING + uint_type, str);
            case 79:
                stream.mark(1);
                if (stream.read() != 83) {
                    stream.reset();
                    return new Integral("O" + ((char) stream.read()));
                }
                return new Integral("OS" + ((char) stream.read()));
            case 80:
                stream.mark(1);
                if (stream.read() != 79) {
                    stream.reset();
                    return new Integral("P" + ((char) stream.read()));
                }
                return new Integral("PO" + ((char) stream.read()));
            case 84:
                String int_type = CoreConstants.EMPTY_STRING + ((char) stream.read());
                if (int_type.equals("S")) {
                    int_type = int_type + ((char) stream.read());
                }
                List<UnionCase> unionCases = new ArrayList<>();
                while (true) {
                    UnionCase c = readNextUnionCase(stream);
                    if (c != null) {
                        unionCases.add(c);
                    } else {
                        stream.read();
                        stream.read();
                        stream.read();
                        List<LayoutElement> body = null;
                        stream.mark(1);
                        char next = (char) stream.read();
                        if (next != ']') {
                            stream.reset();
                            body = readBody(getStreamUpToMatchingBracket(stream));
                        }
                        return new Union(int_type, unionCases, body);
                    }
                }
        }
    }

    private UnionCase readNextUnionCase(StringReader stream) throws IOException {
        Integer nextTag;
        stream.mark(2);
        stream.read();
        int next = stream.read();
        char ch2 = (char) next;
        if (ch2 == ')' || next == -1) {
            stream.reset();
            return null;
        }
        stream.reset();
        stream.read();
        List<Integer> tags = new ArrayList<>();
        do {
            nextTag = readNumber(stream);
            if (nextTag != null) {
                tags.add(nextTag);
                stream.read();
            }
        } while (nextTag != null);
        stream.read();
        stream.mark(1);
        char ch3 = (char) stream.read();
        if (ch3 == ']') {
            return new UnionCase(tags);
        }
        stream.reset();
        return new UnionCase(tags, readBody(getStreamUpToMatchingBracket(stream)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$LayoutElement.class */
    public abstract class LayoutElement implements AttributeLayoutElement {
        private LayoutElement() {
        }

        protected int getLength(char uint_type) {
            int length = 0;
            switch (uint_type) {
                case 'B':
                    length = 1;
                    break;
                case 'H':
                    length = 2;
                    break;
                case 'I':
                    length = 4;
                    break;
                case 'V':
                    length = 0;
                    break;
            }
            return length;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$Integral.class */
    public class Integral extends LayoutElement {
        private final String tag;
        private int[] band;

        public Integral(String tag) {
            super();
            this.tag = tag;
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
            this.band = NewAttributeBands.this.decodeBandInt(NewAttributeBands.this.attributeLayout.getName() + "_" + this.tag, in, NewAttributeBands.this.getCodec(this.tag), count);
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int n, NewAttribute attribute) {
            int value = this.band[n];
            if (this.tag.equals("B") || this.tag.equals("FB")) {
                attribute.addInteger(1, value);
            } else if (this.tag.equals("SB")) {
                attribute.addInteger(1, (byte) value);
            } else if (this.tag.equals("H") || this.tag.equals("FH")) {
                attribute.addInteger(2, value);
            } else if (this.tag.equals("SH")) {
                attribute.addInteger(2, (short) value);
            } else if (this.tag.equals("I") || this.tag.equals("FI")) {
                attribute.addInteger(4, value);
            } else if (this.tag.equals("SI")) {
                attribute.addInteger(4, value);
            } else if (!this.tag.equals("V") && !this.tag.equals("FV") && !this.tag.equals("SV")) {
                if (this.tag.startsWith("PO")) {
                    char uint_type = this.tag.substring(2).toCharArray()[0];
                    attribute.addBCOffset(getLength(uint_type), value);
                } else if (this.tag.startsWith("P")) {
                    char uint_type2 = this.tag.substring(1).toCharArray()[0];
                    attribute.addBCIndex(getLength(uint_type2), value);
                } else if (!this.tag.startsWith("OS")) {
                    if (this.tag.startsWith("O")) {
                        char uint_type3 = this.tag.substring(1).toCharArray()[0];
                        attribute.addBCLength(getLength(uint_type3), value);
                    }
                } else {
                    char uint_type4 = this.tag.substring(2).toCharArray()[0];
                    int length = getLength(uint_type4);
                    if (length == 1) {
                        value = (byte) value;
                    } else if (length == 2) {
                        value = (short) value;
                    } else if (length == 4) {
                        value = value;
                    }
                    attribute.addBCLength(length, value);
                }
            }
        }

        int getValue(int index) {
            return this.band[index];
        }

        public String getTag() {
            return this.tag;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$Replication.class */
    public class Replication extends LayoutElement {
        private final Integral countElement;
        private final List<LayoutElement> layoutElements;

        public Replication(String tag, String contents) throws IOException {
            super();
            this.layoutElements = new ArrayList();
            this.countElement = new Integral(tag);
            StringReader stream = new StringReader(contents);
            while (true) {
                LayoutElement e = NewAttributeBands.this.readNextLayoutElement(stream);
                if (e != null) {
                    this.layoutElements.add(e);
                } else {
                    return;
                }
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
            this.countElement.readBands(in, count);
            int arrayCount = 0;
            for (int i = 0; i < count; i++) {
                arrayCount += this.countElement.getValue(i);
            }
            for (LayoutElement layoutElement : this.layoutElements) {
                layoutElement.readBands(in, arrayCount);
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int index, NewAttribute attribute) {
            this.countElement.addToAttribute(index, attribute);
            int offset = 0;
            for (int i = 0; i < index; i++) {
                offset += this.countElement.getValue(i);
            }
            long numElements = this.countElement.getValue(index);
            for (int i2 = offset; i2 < offset + numElements; i2++) {
                for (LayoutElement layoutElement : this.layoutElements) {
                    layoutElement.addToAttribute(i2, attribute);
                }
            }
        }

        public Integral getCountElement() {
            return this.countElement;
        }

        public List<LayoutElement> getLayoutElements() {
            return this.layoutElements;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$Union.class */
    public class Union extends LayoutElement {
        private final Integral unionTag;
        private final List<UnionCase> unionCases;
        private final List<LayoutElement> defaultCaseBody;
        private int[] caseCounts;
        private int defaultCount;

        public Union(String tag, List<UnionCase> unionCases, List<LayoutElement> body) {
            super();
            this.unionTag = new Integral(tag);
            this.unionCases = unionCases;
            this.defaultCaseBody = body;
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
            this.unionTag.readBands(in, count);
            int[] values = this.unionTag.band;
            this.caseCounts = new int[this.unionCases.size()];
            for (int i = 0; i < this.caseCounts.length; i++) {
                UnionCase unionCase = this.unionCases.get(i);
                for (int value : values) {
                    if (unionCase.hasTag(value)) {
                        int[] iArr = this.caseCounts;
                        int i2 = i;
                        iArr[i2] = iArr[i2] + 1;
                    }
                }
                unionCase.readBands(in, this.caseCounts[i]);
            }
            for (int value2 : values) {
                boolean found = false;
                for (UnionCase unionCase2 : this.unionCases) {
                    if (unionCase2.hasTag(value2)) {
                        found = true;
                    }
                }
                if (!found) {
                    this.defaultCount++;
                }
            }
            if (this.defaultCaseBody != null) {
                for (LayoutElement element : this.defaultCaseBody) {
                    element.readBands(in, this.defaultCount);
                }
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int n, NewAttribute attribute) {
            this.unionTag.addToAttribute(n, attribute);
            int offset = 0;
            int[] tagBand = this.unionTag.band;
            int tag = this.unionTag.getValue(n);
            boolean defaultCase = true;
            for (UnionCase unionCase : this.unionCases) {
                if (unionCase.hasTag(tag)) {
                    defaultCase = false;
                    for (int j = 0; j < n; j++) {
                        if (unionCase.hasTag(tagBand[j])) {
                            offset++;
                        }
                    }
                    unionCase.addToAttribute(offset, attribute);
                }
            }
            if (defaultCase) {
                int defaultOffset = 0;
                for (int j2 = 0; j2 < n; j2++) {
                    boolean found = false;
                    for (UnionCase unionCase2 : this.unionCases) {
                        if (unionCase2.hasTag(tagBand[j2])) {
                            found = true;
                        }
                    }
                    if (!found) {
                        defaultOffset++;
                    }
                }
                if (this.defaultCaseBody != null) {
                    for (LayoutElement element : this.defaultCaseBody) {
                        element.addToAttribute(defaultOffset, attribute);
                    }
                }
            }
        }

        public Integral getUnionTag() {
            return this.unionTag;
        }

        public List<UnionCase> getUnionCases() {
            return this.unionCases;
        }

        public List<LayoutElement> getDefaultCaseBody() {
            return this.defaultCaseBody;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$Call.class */
    public class Call extends LayoutElement {
        private final int callableIndex;
        private Callable callable;

        public Call(int callableIndex) {
            super();
            this.callableIndex = callableIndex;
        }

        public void setCallable(Callable callable) {
            this.callable = callable;
            if (this.callableIndex < 1) {
                callable.setBackwardsCallable();
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) {
            if (this.callableIndex > 0) {
                this.callable.addCount(count);
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int n, NewAttribute attribute) {
            this.callable.addNextToAttribute(attribute);
        }

        public int getCallableIndex() {
            return this.callableIndex;
        }

        public Callable getCallable() {
            return this.callable;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$Reference.class */
    public class Reference extends LayoutElement {
        private final String tag;
        private Object band;
        private final int length;

        public Reference(String tag) {
            super();
            this.tag = tag;
            this.length = getLength(tag.charAt(tag.length() - 1));
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
            if (this.tag.startsWith("KI")) {
                this.band = NewAttributeBands.this.parseCPIntReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("KJ")) {
                this.band = NewAttributeBands.this.parseCPLongReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("KF")) {
                this.band = NewAttributeBands.this.parseCPFloatReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("KD")) {
                this.band = NewAttributeBands.this.parseCPDoubleReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("KS")) {
                this.band = NewAttributeBands.this.parseCPStringReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RC")) {
                this.band = NewAttributeBands.this.parseCPClassReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RS")) {
                this.band = NewAttributeBands.this.parseCPSignatureReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RD")) {
                this.band = NewAttributeBands.this.parseCPDescriptorReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RF")) {
                this.band = NewAttributeBands.this.parseCPFieldRefReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RM")) {
                this.band = NewAttributeBands.this.parseCPMethodRefReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RI")) {
                this.band = NewAttributeBands.this.parseCPInterfaceMethodRefReferences(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            } else if (this.tag.startsWith("RU")) {
                this.band = NewAttributeBands.this.parseCPUTF8References(NewAttributeBands.this.attributeLayout.getName(), in, Codec.UNSIGNED5, count);
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int n, NewAttribute attribute) {
            if (this.tag.startsWith("KI")) {
                attribute.addToBody(this.length, ((CPInteger[]) this.band)[n]);
            } else if (this.tag.startsWith("KJ")) {
                attribute.addToBody(this.length, ((CPLong[]) this.band)[n]);
            } else if (this.tag.startsWith("KF")) {
                attribute.addToBody(this.length, ((CPFloat[]) this.band)[n]);
            } else if (this.tag.startsWith("KD")) {
                attribute.addToBody(this.length, ((CPDouble[]) this.band)[n]);
            } else if (this.tag.startsWith("KS")) {
                attribute.addToBody(this.length, ((CPString[]) this.band)[n]);
            } else if (this.tag.startsWith("RC")) {
                attribute.addToBody(this.length, ((CPClass[]) this.band)[n]);
            } else if (this.tag.startsWith("RS")) {
                attribute.addToBody(this.length, ((CPUTF8[]) this.band)[n]);
            } else if (this.tag.startsWith("RD")) {
                attribute.addToBody(this.length, ((CPNameAndType[]) this.band)[n]);
            } else if (this.tag.startsWith("RF")) {
                attribute.addToBody(this.length, ((CPFieldRef[]) this.band)[n]);
            } else if (this.tag.startsWith("RM")) {
                attribute.addToBody(this.length, ((CPMethodRef[]) this.band)[n]);
            } else if (this.tag.startsWith("RI")) {
                attribute.addToBody(this.length, ((CPInterfaceMethodRef[]) this.band)[n]);
            } else if (this.tag.startsWith("RU")) {
                attribute.addToBody(this.length, ((CPUTF8[]) this.band)[n]);
            }
        }

        public String getTag() {
            return this.tag;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$Callable.class */
    public static class Callable implements AttributeLayoutElement {
        private final List<LayoutElement> body;
        private boolean isBackwardsCallable;
        private boolean isFirstCallable;
        private int count;
        private int index;

        public Callable(List<LayoutElement> body) {
            this.body = body;
        }

        public void addNextToAttribute(NewAttribute attribute) {
            for (LayoutElement element : this.body) {
                element.addToAttribute(this.index, attribute);
            }
            this.index++;
        }

        public void addCount(int count) {
            this.count += count;
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
            int count2;
            if (this.isFirstCallable) {
                count2 = count + this.count;
            } else {
                count2 = this.count;
            }
            for (LayoutElement element : this.body) {
                element.readBands(in, count2);
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int n, NewAttribute attribute) {
            if (this.isFirstCallable) {
                for (LayoutElement element : this.body) {
                    element.addToAttribute(this.index, attribute);
                }
                this.index++;
            }
        }

        public boolean isBackwardsCallable() {
            return this.isBackwardsCallable;
        }

        public void setBackwardsCallable() {
            this.isBackwardsCallable = true;
        }

        public void setFirstCallable(boolean isFirstCallable) {
            this.isFirstCallable = isFirstCallable;
        }

        public List<LayoutElement> getBody() {
            return this.body;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/NewAttributeBands$UnionCase.class */
    public class UnionCase extends LayoutElement {
        private List<LayoutElement> body;
        private final List<Integer> tags;

        public UnionCase(List<Integer> tags) {
            super();
            this.tags = tags;
        }

        public boolean hasTag(int i) {
            return this.tags.contains(Integer.valueOf(i));
        }

        public boolean hasTag(long l) {
            return this.tags.contains(Integer.valueOf((int) l));
        }

        public UnionCase(List<Integer> tags, List<LayoutElement> body) {
            super();
            this.tags = tags;
            this.body = body;
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void readBands(InputStream in, int count) throws IOException, Pack200Exception {
            if (this.body != null) {
                for (LayoutElement element : this.body) {
                    element.readBands(in, count);
                }
            }
        }

        @Override // org.apache.commons.compress.harmony.unpack200.NewAttributeBands.AttributeLayoutElement
        public void addToAttribute(int index, NewAttribute attribute) {
            if (this.body != null) {
                for (LayoutElement element : this.body) {
                    element.addToAttribute(index, attribute);
                }
            }
        }

        public List<LayoutElement> getBody() {
            return this.body == null ? Collections.EMPTY_LIST : this.body;
        }
    }

    private StringReader getStreamUpToMatchingBracket(StringReader stream) throws IOException {
        int read;
        StringBuilder sb = new StringBuilder();
        int foundBracket = -1;
        while (foundBracket != 0 && (read = stream.read()) != -1) {
            char c = (char) read;
            if (c == ']') {
                foundBracket++;
            }
            if (c == '[') {
                foundBracket--;
            }
            if (foundBracket != 0) {
                sb.append(c);
            }
        }
        return new StringReader(sb.toString());
    }

    public BHSDCodec getCodec(String layoutElement) {
        if (layoutElement.indexOf(79) >= 0) {
            return Codec.BRANCH5;
        }
        if (layoutElement.indexOf(80) >= 0) {
            return Codec.BCI5;
        }
        if (layoutElement.indexOf(83) >= 0 && layoutElement.indexOf("KS") < 0 && layoutElement.indexOf("RS") < 0) {
            return Codec.SIGNED5;
        }
        if (layoutElement.indexOf(66) >= 0) {
            return Codec.BYTE1;
        }
        return Codec.UNSIGNED5;
    }

    private String readUpToMatchingBracket(StringReader stream) throws IOException {
        int read;
        StringBuilder sb = new StringBuilder();
        int foundBracket = -1;
        while (foundBracket != 0 && (read = stream.read()) != -1) {
            char c = (char) read;
            if (c == ']') {
                foundBracket++;
            }
            if (c == '[') {
                foundBracket--;
            }
            if (foundBracket != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Integer readNumber(StringReader stream) throws IOException {
        stream.mark(1);
        char first = (char) stream.read();
        boolean negative = first == '-';
        if (!negative) {
            stream.reset();
        }
        stream.mark(100);
        int length = 0;
        while (true) {
            int i = stream.read();
            if (i == -1 || !Character.isDigit((char) i)) {
                break;
            }
            length++;
        }
        stream.reset();
        if (length == 0) {
            return null;
        }
        char[] digits = new char[length];
        int read = stream.read(digits);
        if (read != digits.length) {
            throw new IOException("Error reading from the input stream");
        }
        return Integer.valueOf(Integer.parseInt((negative ? "-" : CoreConstants.EMPTY_STRING) + new String(digits)));
    }

    private List<LayoutElement> readBody(StringReader stream) throws IOException {
        List<LayoutElement> layoutElements = new ArrayList<>();
        while (true) {
            LayoutElement e = readNextLayoutElement(stream);
            if (e != null) {
                layoutElements.add(e);
            } else {
                return layoutElements;
            }
        }
    }

    public int getBackwardsCallCount() {
        return this.backwardsCallCount;
    }

    public void setBackwardsCalls(int[] backwardsCalls) throws IOException {
        int index = 0;
        parseLayout();
        for (AttributeLayoutElement element : this.attributeLayoutElements) {
            if ((element instanceof Callable) && ((Callable) element).isBackwardsCallable()) {
                ((Callable) element).addCount(backwardsCalls[index]);
                index++;
            }
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void unpack() throws IOException, Pack200Exception {
    }
}
