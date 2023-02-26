package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.harmony.pack200.AttributeDefinitionBands;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.Label;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands.class */
public class NewAttributeBands extends BandSet {
    protected List<AttributeLayoutElement> attributeLayoutElements;
    private int[] backwardsCallCounts;
    private final CpBands cpBands;
    private final AttributeDefinitionBands.AttributeDefinition def;
    private boolean usedAtLeastOnce;
    private Integral lastPIntegral;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$AttributeLayoutElement.class */
    public interface AttributeLayoutElement {
        void addAttributeToBand(NewAttribute newAttribute, InputStream inputStream);

        void pack(OutputStream outputStream) throws IOException, Pack200Exception;

        void renumberBci(IntList intList, Map<Label, Integer> map);
    }

    public NewAttributeBands(int effort, CpBands cpBands, SegmentHeader header, AttributeDefinitionBands.AttributeDefinition def) throws IOException {
        super(effort, header);
        this.def = def;
        this.cpBands = cpBands;
        parseLayout();
    }

    public void addAttribute(NewAttribute attribute) {
        this.usedAtLeastOnce = true;
        InputStream stream = new ByteArrayInputStream(attribute.getBytes());
        for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
            attributeLayoutElement.addAttributeToBand(attribute, stream);
        }
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
        for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
            attributeLayoutElement.pack(outputStream);
        }
    }

    public String getAttributeName() {
        return this.def.name.getUnderlyingString();
    }

    public int getFlagIndex() {
        return this.def.index;
    }

    public int[] numBackwardsCalls() {
        return this.backwardsCallCounts;
    }

    public boolean isUsedAtLeastOnce() {
        return this.usedAtLeastOnce;
    }

    private void parseLayout() throws IOException {
        String layout = this.def.layout.getUnderlyingString();
        if (this.attributeLayoutElements == null) {
            this.attributeLayoutElements = new ArrayList();
            StringReader reader = new StringReader(layout);
            while (true) {
                AttributeLayoutElement e = readNextAttributeElement(reader);
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
        for (int i = 0; i < this.attributeLayoutElements.size(); i++) {
            AttributeLayoutElement element = this.attributeLayoutElements.get(i);
            if (element instanceof Callable) {
                Callable callable = (Callable) element;
                List<LayoutElement> body = callable.body;
                for (LayoutElement layoutElement : body) {
                    resolveCallsForElement(i, callable, layoutElement);
                }
            }
        }
        int backwardsCallableIndex = 0;
        for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
            if (attributeLayoutElement instanceof Callable) {
                Callable callable2 = (Callable) attributeLayoutElement;
                if (callable2.isBackwardsCallable) {
                    callable2.setBackwardsCallableIndex(backwardsCallableIndex);
                    backwardsCallableIndex++;
                }
            }
        }
        this.backwardsCallCounts = new int[backwardsCallableIndex];
    }

    private void resolveCallsForElement(int i, Callable currentCallable, LayoutElement layoutElement) {
        if (!(layoutElement instanceof Call)) {
            if (!(layoutElement instanceof Replication)) {
                return;
            }
            List<LayoutElement> children = ((Replication) layoutElement).layoutElements;
            for (LayoutElement child : children) {
                resolveCallsForElement(i, currentCallable, child);
            }
            return;
        }
        Call call = (Call) layoutElement;
        int index = call.callableIndex;
        if (index == 0) {
            call.setCallable(currentCallable);
        } else if (index > 0) {
            for (int k = i + 1; k < this.attributeLayoutElements.size(); k++) {
                AttributeLayoutElement el = this.attributeLayoutElements.get(k);
                if (el instanceof Callable) {
                    index--;
                    if (index == 0) {
                        call.setCallable((Callable) el);
                        return;
                    }
                }
            }
        } else {
            for (int k2 = i - 1; k2 >= 0; k2--) {
                AttributeLayoutElement el2 = this.attributeLayoutElements.get(k2);
                if (el2 instanceof Callable) {
                    index++;
                    if (index == 0) {
                        call.setCallable((Callable) el2);
                        return;
                    }
                }
            }
        }
    }

    private AttributeLayoutElement readNextAttributeElement(StringReader reader) throws IOException {
        reader.mark(1);
        int next = reader.read();
        if (next == -1) {
            return null;
        }
        if (next == 91) {
            return new Callable(readBody(getStreamUpToMatchingBracket(reader)));
        }
        reader.reset();
        return readNextLayoutElement(reader);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LayoutElement readNextLayoutElement(StringReader reader) throws IOException {
        int nextChar = reader.read();
        if (nextChar == -1) {
            return null;
        }
        switch (nextChar) {
            case 40:
                int number = readNumber(reader).intValue();
                reader.read();
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
                return new Integral(new String(new char[]{(char) nextChar, (char) reader.read()}));
            case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
            case 82:
                StringBuilder string = new StringBuilder(CoreConstants.EMPTY_STRING).append((char) nextChar).append((char) reader.read());
                char nxt = (char) reader.read();
                string.append(nxt);
                if (nxt == 'N') {
                    string.append((char) reader.read());
                }
                return new Reference(string.toString());
            case 78:
                char uint_type = (char) reader.read();
                reader.read();
                String str = readUpToMatchingBracket(reader);
                return new Replication(CoreConstants.EMPTY_STRING + uint_type, str);
            case 79:
                reader.mark(1);
                if (reader.read() != 83) {
                    reader.reset();
                    return new Integral("O" + ((char) reader.read()), this.lastPIntegral);
                }
                return new Integral("OS" + ((char) reader.read()), this.lastPIntegral);
            case 80:
                reader.mark(1);
                if (reader.read() != 79) {
                    reader.reset();
                    this.lastPIntegral = new Integral("P" + ((char) reader.read()));
                    return this.lastPIntegral;
                }
                this.lastPIntegral = new Integral("PO" + ((char) reader.read()), this.lastPIntegral);
                return this.lastPIntegral;
            case 84:
                String int_type = String.valueOf((char) reader.read());
                if (int_type.equals("S")) {
                    int_type = int_type + ((char) reader.read());
                }
                List<UnionCase> unionCases = new ArrayList<>();
                while (true) {
                    UnionCase c = readNextUnionCase(reader);
                    if (c != null) {
                        unionCases.add(c);
                    } else {
                        reader.read();
                        reader.read();
                        reader.read();
                        List<LayoutElement> body = null;
                        reader.mark(1);
                        char next = (char) reader.read();
                        if (next != ']') {
                            reader.reset();
                            body = readBody(getStreamUpToMatchingBracket(reader));
                        }
                        return new Union(int_type, unionCases, body);
                    }
                }
        }
    }

    private UnionCase readNextUnionCase(StringReader reader) throws IOException {
        Integer nextTag;
        reader.mark(2);
        reader.read();
        int next = reader.read();
        char ch2 = (char) next;
        if (ch2 == ')' || next == -1) {
            reader.reset();
            return null;
        }
        reader.reset();
        reader.read();
        List<Integer> tags = new ArrayList<>();
        do {
            nextTag = readNumber(reader);
            if (nextTag != null) {
                tags.add(nextTag);
                reader.read();
            }
        } while (nextTag != null);
        reader.read();
        reader.mark(1);
        char ch3 = (char) reader.read();
        if (ch3 == ']') {
            return new UnionCase(tags);
        }
        reader.reset();
        return new UnionCase(tags, readBody(getStreamUpToMatchingBracket(reader)));
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$LayoutElement.class */
    public abstract class LayoutElement implements AttributeLayoutElement {
        public LayoutElement() {
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

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$Integral.class */
    public class Integral extends LayoutElement {
        private final String tag;
        private final List band;
        private final BHSDCodec defaultCodec;
        private Integral previousIntegral;
        private int previousPValue;

        public Integral(String tag) {
            super();
            this.band = new ArrayList();
            this.tag = tag;
            this.defaultCodec = NewAttributeBands.this.getCodec(tag);
        }

        public Integral(String tag, Integral previousIntegral) {
            super();
            this.band = new ArrayList();
            this.tag = tag;
            this.defaultCodec = NewAttributeBands.this.getCodec(tag);
            this.previousIntegral = previousIntegral;
        }

        public String getTag() {
            return this.tag;
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            Label label = null;
            int value = 0;
            if (this.tag.equals("B") || this.tag.equals("FB")) {
                value = NewAttributeBands.this.readInteger(1, inputStream) & 255;
            } else if (this.tag.equals("SB")) {
                value = NewAttributeBands.this.readInteger(1, inputStream);
            } else if (this.tag.equals("H") || this.tag.equals("FH")) {
                value = NewAttributeBands.this.readInteger(2, inputStream) & 65535;
            } else if (this.tag.equals("SH")) {
                value = NewAttributeBands.this.readInteger(2, inputStream);
            } else if (this.tag.equals("I") || this.tag.equals("FI")) {
                value = NewAttributeBands.this.readInteger(4, inputStream);
            } else if (this.tag.equals("SI")) {
                value = NewAttributeBands.this.readInteger(4, inputStream);
            } else if (!this.tag.equals("V") && !this.tag.equals("FV") && !this.tag.equals("SV")) {
                if (this.tag.startsWith("PO") || this.tag.startsWith("OS")) {
                    char uint_type = this.tag.substring(2).toCharArray()[0];
                    int length = getLength(uint_type);
                    value = NewAttributeBands.this.readInteger(length, inputStream) + this.previousIntegral.previousPValue;
                    label = attribute.getLabel(value);
                    this.previousPValue = value;
                } else if (this.tag.startsWith("P")) {
                    char uint_type2 = this.tag.substring(1).toCharArray()[0];
                    int length2 = getLength(uint_type2);
                    value = NewAttributeBands.this.readInteger(length2, inputStream);
                    label = attribute.getLabel(value);
                    this.previousPValue = value;
                } else if (this.tag.startsWith("O")) {
                    char uint_type3 = this.tag.substring(1).toCharArray()[0];
                    int length3 = getLength(uint_type3);
                    value = NewAttributeBands.this.readInteger(length3, inputStream) + this.previousIntegral.previousPValue;
                    label = attribute.getLabel(value);
                    this.previousPValue = value;
                }
            }
            if (label == null) {
                label = Integer.valueOf(value);
            }
            this.band.add(label);
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
            PackingUtils.log("Writing new attribute bands...");
            byte[] encodedBand = NewAttributeBands.this.encodeBandInt(this.tag, NewAttributeBands.this.integerListToArray(this.band), this.defaultCodec);
            outputStream.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + this.tag + "[" + this.band.size() + "]");
        }

        public int latestValue() {
            return ((Integer) this.band.get(this.band.size() - 1)).intValue();
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
            if (this.tag.startsWith("O") || this.tag.startsWith("PO")) {
                renumberOffsetBci(this.previousIntegral.band, bciRenumbering, labelsToOffsets);
            } else if (this.tag.startsWith("P")) {
                for (int i = this.band.size() - 1; i >= 0; i--) {
                    Object label = this.band.get(i);
                    if (!(label instanceof Integer)) {
                        if (label instanceof Label) {
                            this.band.remove(i);
                            Integer bytecodeIndex = labelsToOffsets.get(label);
                            this.band.add(i, Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue())));
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        private void renumberOffsetBci(List relative, IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
            for (int i = this.band.size() - 1; i >= 0; i--) {
                Object label = this.band.get(i);
                if (!(label instanceof Integer)) {
                    if (label instanceof Label) {
                        this.band.remove(i);
                        Integer bytecodeIndex = labelsToOffsets.get(label);
                        Integer renumberedOffset = Integer.valueOf(bciRenumbering.get(bytecodeIndex.intValue()) - ((Integer) relative.get(i)).intValue());
                        this.band.add(i, renumberedOffset);
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$Replication.class */
    public class Replication extends LayoutElement {
        private final Integral countElement;
        private final List<LayoutElement> layoutElements;

        public Integral getCountElement() {
            return this.countElement;
        }

        public List<LayoutElement> getLayoutElements() {
            return this.layoutElements;
        }

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

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            this.countElement.addAttributeToBand(attribute, inputStream);
            int count = this.countElement.latestValue();
            for (int i = 0; i < count; i++) {
                for (AttributeLayoutElement layoutElement : this.layoutElements) {
                    layoutElement.addAttributeToBand(attribute, inputStream);
                }
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream out) throws IOException, Pack200Exception {
            this.countElement.pack(out);
            for (AttributeLayoutElement layoutElement : this.layoutElements) {
                layoutElement.pack(out);
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
            for (AttributeLayoutElement layoutElement : this.layoutElements) {
                layoutElement.renumberBci(bciRenumbering, labelsToOffsets);
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$Union.class */
    public class Union extends LayoutElement {
        private final Integral unionTag;
        private final List<UnionCase> unionCases;
        private final List<LayoutElement> defaultCaseBody;

        public Union(String tag, List<UnionCase> unionCases, List<LayoutElement> body) {
            super();
            this.unionTag = new Integral(tag);
            this.unionCases = unionCases;
            this.defaultCaseBody = body;
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            this.unionTag.addAttributeToBand(attribute, inputStream);
            long tag = this.unionTag.latestValue();
            boolean defaultCase = true;
            for (UnionCase unionCase : this.unionCases) {
                if (unionCase.hasTag(tag)) {
                    defaultCase = false;
                    unionCase.addAttributeToBand(attribute, inputStream);
                }
            }
            if (defaultCase) {
                for (LayoutElement layoutElement : this.defaultCaseBody) {
                    layoutElement.addAttributeToBand(attribute, inputStream);
                }
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
            this.unionTag.pack(outputStream);
            for (UnionCase unionCase : this.unionCases) {
                unionCase.pack(outputStream);
            }
            for (LayoutElement element : this.defaultCaseBody) {
                element.pack(outputStream);
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
            for (UnionCase unionCase : this.unionCases) {
                unionCase.renumberBci(bciRenumbering, labelsToOffsets);
            }
            for (LayoutElement element : this.defaultCaseBody) {
                element.renumberBci(bciRenumbering, labelsToOffsets);
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

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$Call.class */
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

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            this.callable.addAttributeToBand(attribute, inputStream);
            if (this.callableIndex < 1) {
                this.callable.addBackwardsCall();
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream outputStream) {
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        }

        public int getCallableIndex() {
            return this.callableIndex;
        }

        public Callable getCallable() {
            return this.callable;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$Reference.class */
    public class Reference extends LayoutElement {
        private final String tag;
        private List<ConstantPoolEntry> band;
        private boolean nullsAllowed;

        public Reference(String tag) {
            super();
            this.nullsAllowed = false;
            this.tag = tag;
            this.nullsAllowed = tag.indexOf(78) != -1;
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            int index = NewAttributeBands.this.readInteger(4, inputStream);
            if (this.tag.startsWith("RC")) {
                this.band.add(NewAttributeBands.this.cpBands.getCPClass(attribute.readClass(index)));
            } else if (this.tag.startsWith("RU")) {
                this.band.add(NewAttributeBands.this.cpBands.getCPUtf8(attribute.readUTF8(index)));
            } else if (this.tag.startsWith("RS")) {
                this.band.add(NewAttributeBands.this.cpBands.getCPSignature(attribute.readUTF8(index)));
            } else {
                this.band.add(NewAttributeBands.this.cpBands.getConstant(attribute.readConst(index)));
            }
        }

        public String getTag() {
            return this.tag;
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
            int[] ints;
            if (this.nullsAllowed) {
                ints = NewAttributeBands.this.cpEntryOrNullListToArray(this.band);
            } else {
                ints = NewAttributeBands.this.cpEntryListToArray(this.band);
            }
            byte[] encodedBand = NewAttributeBands.this.encodeBandInt(this.tag, ints, Codec.UNSIGNED5);
            outputStream.write(encodedBand);
            PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + this.tag + "[" + ints.length + "]");
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$Callable.class */
    public class Callable implements AttributeLayoutElement {
        private final List<LayoutElement> body;
        private boolean isBackwardsCallable;
        private int backwardsCallableIndex;

        public Callable(List<LayoutElement> body) {
            this.body = body;
        }

        public void setBackwardsCallableIndex(int backwardsCallableIndex) {
            this.backwardsCallableIndex = backwardsCallableIndex;
        }

        public void addBackwardsCall() {
            int[] iArr = NewAttributeBands.this.backwardsCallCounts;
            int i = this.backwardsCallableIndex;
            iArr[i] = iArr[i] + 1;
        }

        public boolean isBackwardsCallable() {
            return this.isBackwardsCallable;
        }

        public void setBackwardsCallable() {
            this.isBackwardsCallable = true;
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            for (AttributeLayoutElement element : this.body) {
                element.addAttributeToBand(attribute, inputStream);
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
            for (AttributeLayoutElement element : this.body) {
                element.pack(outputStream);
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
            for (AttributeLayoutElement element : this.body) {
                element.renumberBci(bciRenumbering, labelsToOffsets);
            }
        }

        public List<LayoutElement> getBody() {
            return this.body;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/NewAttributeBands$UnionCase.class */
    public class UnionCase extends LayoutElement {
        private final List<LayoutElement> body;
        private final List<Integer> tags;

        public UnionCase(List<Integer> tags) {
            super();
            this.tags = tags;
            this.body = Collections.EMPTY_LIST;
        }

        public boolean hasTag(long l) {
            return this.tags.contains(Integer.valueOf((int) l));
        }

        public UnionCase(List<Integer> tags, List<LayoutElement> body) {
            super();
            this.tags = tags;
            this.body = body;
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void addAttributeToBand(NewAttribute attribute, InputStream inputStream) {
            for (LayoutElement element : this.body) {
                element.addAttributeToBand(attribute, inputStream);
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
            for (LayoutElement element : this.body) {
                element.pack(outputStream);
            }
        }

        @Override // org.apache.commons.compress.harmony.pack200.NewAttributeBands.AttributeLayoutElement
        public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
            for (LayoutElement element : this.body) {
                element.renumberBci(bciRenumbering, labelsToOffsets);
            }
        }

        public List<LayoutElement> getBody() {
            return this.body;
        }
    }

    private StringReader getStreamUpToMatchingBracket(StringReader reader) throws IOException {
        int read;
        StringBuilder sb = new StringBuilder();
        int foundBracket = -1;
        while (foundBracket != 0 && (read = reader.read()) != -1) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public int readInteger(int i, InputStream inputStream) {
        int result = 0;
        for (int j = 0; j < i; j++) {
            try {
                result = (result << 8) | inputStream.read();
            } catch (IOException e) {
                throw new UncheckedIOException("Error reading unknown attribute", e);
            }
        }
        if (i == 1) {
            result = (byte) result;
        }
        if (i == 2) {
            result = (short) result;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    private String readUpToMatchingBracket(StringReader reader) throws IOException {
        int read;
        StringBuilder sb = new StringBuilder();
        int foundBracket = -1;
        while (foundBracket != 0 && (read = reader.read()) != -1) {
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

    private List<LayoutElement> readBody(StringReader reader) throws IOException {
        List<LayoutElement> layoutElements = new ArrayList<>();
        while (true) {
            LayoutElement e = readNextLayoutElement(reader);
            if (e != null) {
                layoutElements.add(e);
            } else {
                return layoutElements;
            }
        }
    }

    public void renumberBci(IntList bciRenumbering, Map<Label, Integer> labelsToOffsets) {
        for (AttributeLayoutElement attributeLayoutElement : this.attributeLayoutElements) {
            attributeLayoutElement.renumberBci(bciRenumbering, labelsToOffsets);
        }
    }
}
