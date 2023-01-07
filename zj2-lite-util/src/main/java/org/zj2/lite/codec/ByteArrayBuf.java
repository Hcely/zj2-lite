package org.zj2.lite.codec;

/**
 *  ByteArrayBuf
 *
 * @author peijie.ye
 * @date 2022/11/24 16:39
 */
public class ByteArrayBuf {
    private final byte[] buffer;
    private final int capacity;
    private int writePos;

    public ByteArrayBuf(int capacity) {
        this.buffer = new byte[capacity];
        this.capacity = capacity;
        this.writePos = 0;
    }

    /**
     * 写入一个byte
     * @param b
     * @return boolean 成功或失败
     */
    public boolean write(int b) {
        if (writePos < capacity) {
            buffer[writePos++] = (byte) b;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 写入buffer
     * @param
     * @return 写入量
     */
    public int write(byte[] buf) {
        if (buf == null) {
            return 0;
        } else {
            return write(buf, 0, buf.length);
        }
    }

    /**
     * 写入buffer
     * @param
     * @return 写入量
     */
    public int write(byte[] buf, int offset) {
        if (buf == null) {
            return 0;
        } else {
            return write(buf, offset, buf.length - offset);
        }
    }

    /**
     * 写入buffer
     * @param
     * @return 写入量
     */
    public int write(byte[] buf, int offset, int length) {
        if (length < 1) { return 0; }
        int remain = capacity - writePos;
        //noinspection ManualMinMaxCalculation
        int writeLen = length < remain ? length : remain;
        if (writeLen > 0) {
            System.arraycopy(buf, offset, buffer, writePos, writeLen);
            writePos += writeLen;
        }
        return writeLen;
    }

    public byte[] buffer() {
        return buffer;
    }

    /**
     * buffer容量
     * @return
     */
    public int capacity() {
        return capacity;
    }

    /**
     * buffer剩余容量
     * @return
     */
    public int remain() {
        return capacity - writePos;
    }

    /**
     * 写位置
     * @return
     */
    public int writePos() {
        return writePos;
    }

    /**
     * 设置写位置,返回旧的写位置
     * @param pos
     * @return
     */
    public int setWritePos(int pos) {
        final int i = writePos;
        //noinspection ManualMinMaxCalculation
        writePos = pos < 0 ? 0 : (pos < capacity ? pos : capacity);//NOSONAR
        return i;
    }

    /**
     * 加写位置,返回旧的写位置
     * @param len
     * @return
     */
    public int addWritePos(int len) {
        return setWritePos(writePos + len);
    }

    /**
     * 重置buffer写位置
     * @return
     */
    public ByteArrayBuf reset() {
        writePos = 0;
        return this;
    }
}
