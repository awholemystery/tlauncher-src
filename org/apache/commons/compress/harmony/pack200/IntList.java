package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.util.Arrays;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/IntList.class */
public class IntList {
    private int[] array;
    private int firstIndex;
    private int lastIndex;
    private int modCount;

    public IntList() {
        this(10);
    }

    public IntList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
        this.lastIndex = 0;
        this.firstIndex = 0;
        this.array = new int[capacity];
    }

    public boolean add(int object) {
        if (this.lastIndex == this.array.length) {
            growAtEnd(1);
        }
        int[] iArr = this.array;
        int i = this.lastIndex;
        this.lastIndex = i + 1;
        iArr[i] = object;
        this.modCount++;
        return true;
    }

    public void add(int location, int object) {
        int size = this.lastIndex - this.firstIndex;
        if (0 < location && location < size) {
            if (this.firstIndex == 0 && this.lastIndex == this.array.length) {
                growForInsert(location, 1);
            } else if ((location < size / 2 && this.firstIndex > 0) || this.lastIndex == this.array.length) {
                int[] iArr = this.array;
                int i = this.firstIndex;
                int[] iArr2 = this.array;
                int i2 = this.firstIndex - 1;
                this.firstIndex = i2;
                System.arraycopy(iArr, i, iArr2, i2, location);
            } else {
                int index = location + this.firstIndex;
                System.arraycopy(this.array, index, this.array, index + 1, size - location);
                this.lastIndex++;
            }
            this.array[location + this.firstIndex] = object;
        } else if (location == 0) {
            if (this.firstIndex == 0) {
                growAtFront(1);
            }
            int[] iArr3 = this.array;
            int i3 = this.firstIndex - 1;
            this.firstIndex = i3;
            iArr3[i3] = object;
        } else if (location == size) {
            if (this.lastIndex == this.array.length) {
                growAtEnd(1);
            }
            int[] iArr4 = this.array;
            int i4 = this.lastIndex;
            this.lastIndex = i4 + 1;
            iArr4[i4] = object;
        } else {
            throw new IndexOutOfBoundsException();
        }
        this.modCount++;
    }

    public void clear() {
        if (this.firstIndex != this.lastIndex) {
            Arrays.fill(this.array, this.firstIndex, this.lastIndex, -1);
            this.lastIndex = 0;
            this.firstIndex = 0;
            this.modCount++;
        }
    }

    public int get(int location) {
        if (0 <= location && location < this.lastIndex - this.firstIndex) {
            return this.array[this.firstIndex + location];
        }
        throw new IndexOutOfBoundsException(CoreConstants.EMPTY_STRING + location);
    }

    private void growAtEnd(int required) {
        int size = this.lastIndex - this.firstIndex;
        if (this.firstIndex >= required - (this.array.length - this.lastIndex)) {
            int newLast = this.lastIndex - this.firstIndex;
            if (size > 0) {
                System.arraycopy(this.array, this.firstIndex, this.array, 0, size);
            }
            this.firstIndex = 0;
            this.lastIndex = newLast;
            return;
        }
        int increment = size / 2;
        if (required > increment) {
            increment = required;
        }
        if (increment < 12) {
            increment = 12;
        }
        int[] newArray = new int[size + increment];
        if (size > 0) {
            System.arraycopy(this.array, this.firstIndex, newArray, 0, size);
            this.firstIndex = 0;
            this.lastIndex = size;
        }
        this.array = newArray;
    }

    private void growAtFront(int required) {
        int size = this.lastIndex - this.firstIndex;
        if ((this.array.length - this.lastIndex) + this.firstIndex >= required) {
            int newFirst = this.array.length - size;
            if (size > 0) {
                System.arraycopy(this.array, this.firstIndex, this.array, newFirst, size);
            }
            this.firstIndex = newFirst;
            this.lastIndex = this.array.length;
            return;
        }
        int increment = size / 2;
        if (required > increment) {
            increment = required;
        }
        if (increment < 12) {
            increment = 12;
        }
        int[] newArray = new int[size + increment];
        if (size > 0) {
            System.arraycopy(this.array, this.firstIndex, newArray, newArray.length - size, size);
        }
        this.firstIndex = newArray.length - size;
        this.lastIndex = newArray.length;
        this.array = newArray;
    }

    private void growForInsert(int location, int required) {
        int size = this.lastIndex - this.firstIndex;
        int increment = size / 2;
        if (required > increment) {
            increment = required;
        }
        if (increment < 12) {
            increment = 12;
        }
        int[] newArray = new int[size + increment];
        int newFirst = increment - required;
        System.arraycopy(this.array, location + this.firstIndex, newArray, newFirst + location + required, size - location);
        System.arraycopy(this.array, this.firstIndex, newArray, newFirst, location);
        this.firstIndex = newFirst;
        this.lastIndex = size + increment;
        this.array = newArray;
    }

    public void increment(int location) {
        if (0 > location || location >= this.lastIndex - this.firstIndex) {
            throw new IndexOutOfBoundsException(CoreConstants.EMPTY_STRING + location);
        }
        int[] iArr = this.array;
        int i = this.firstIndex + location;
        iArr[i] = iArr[i] + 1;
    }

    public boolean isEmpty() {
        return this.lastIndex == this.firstIndex;
    }

    public int remove(int location) {
        int result;
        int size = this.lastIndex - this.firstIndex;
        if (0 > location || location >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (location == size - 1) {
            int[] iArr = this.array;
            int i = this.lastIndex - 1;
            this.lastIndex = i;
            result = iArr[i];
            this.array[this.lastIndex] = 0;
        } else if (location == 0) {
            result = this.array[this.firstIndex];
            int[] iArr2 = this.array;
            int i2 = this.firstIndex;
            this.firstIndex = i2 + 1;
            iArr2[i2] = 0;
        } else {
            int elementIndex = this.firstIndex + location;
            result = this.array[elementIndex];
            if (location < size / 2) {
                System.arraycopy(this.array, this.firstIndex, this.array, this.firstIndex + 1, location);
                int[] iArr3 = this.array;
                int i3 = this.firstIndex;
                this.firstIndex = i3 + 1;
                iArr3[i3] = 0;
            } else {
                System.arraycopy(this.array, elementIndex + 1, this.array, elementIndex, (size - location) - 1);
                int[] iArr4 = this.array;
                int i4 = this.lastIndex - 1;
                this.lastIndex = i4;
                iArr4[i4] = 0;
            }
        }
        if (this.firstIndex == this.lastIndex) {
            this.lastIndex = 0;
            this.firstIndex = 0;
        }
        this.modCount++;
        return result;
    }

    public int size() {
        return this.lastIndex - this.firstIndex;
    }

    public int[] toArray() {
        int size = this.lastIndex - this.firstIndex;
        int[] result = new int[size];
        System.arraycopy(this.array, this.firstIndex, result, 0, size);
        return result;
    }

    public void addAll(IntList list) {
        growAtEnd(list.size());
        for (int i = 0; i < list.size(); i++) {
            add(list.get(i));
        }
    }
}
