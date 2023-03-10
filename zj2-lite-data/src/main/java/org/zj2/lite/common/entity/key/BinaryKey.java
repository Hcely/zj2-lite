package org.zj2.lite.common.entity.key;

import java.io.Serializable;
import java.util.Arrays;

/**
 * ByteKey
 *
 * @author peijie.ye
 * @date 2023/2/7 12:07
 */
public class BinaryKey implements Serializable, Comparable<BinaryKey> {
    private static final long serialVersionUID = -8982811006868337951L;
    private int writeOff;
    private final byte[] keys;
    private transient int hashcode;

    public BinaryKey(int size) {
        this.keys = new byte[size];
        this.hashcode = 0;
    }

    public BinaryKey write(int b) {
        if(writeOff < keys.length) {
            keys[writeOff] = (byte)b;
            ++writeOff;
        }
        return this;
    }

    public BinaryKey append(int v) {
        write(0xFF & (v >>> 24));
        write(0xFF & (v >>> 16));
        write(0xFF & (v >>> 8));
        write(0xFF & v);
        return this;
    }

    public BinaryKey append(long v) {
        write((int)(0xFF & (v >>> 56)));
        write((int)(0xFF & (v >>> 48)));
        write((int)(0xFF & (v >>> 40)));
        write((int)(0xFF & (v >>> 32)));
        write((int)(0xFF & (v >>> 24)));
        write((int)(0xFF & (v >>> 16)));
        write((int)(0xFF & (v >>> 8)));
        write((int)(0xFF & v));
        return this;
    }

    public BinaryKey flush() {
        writeOff = keys.length;
        hashcode = Arrays.hashCode(keys);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BinaryKey byteKey = (BinaryKey)o;
        return Arrays.equals(keys, byteKey.keys);
    }

    @Override
    public int hashCode() {
        if(hashcode != 0) { return hashcode; }
        if(writeOff == keys.length) {
            hashcode = Arrays.hashCode(keys);
            return hashcode;
        } else {
            return Arrays.hashCode(keys);
        }
    }

    @Override
    public int compareTo(BinaryKey o) {
        return Arrays.compare(keys, o.keys);
    }
}
