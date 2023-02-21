package org.zj2.lite.util.stream;

import org.apache.commons.lang3.RandomUtils;
import org.zj2.lite.util.AbstractDump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * SortDataStream
 *
 * @author peijie.ye
 * @date 2023/2/20 12:47
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SortCombineDataStream<T extends Comparable> extends AbstractDump<SortCombineDataStream.Node>
        implements DataStream<T> {

    public SortCombineDataStream() {
    }

    public SortCombineDataStream(Collection<DataStream<T>> streams) {
        super(streams.size());
        for (DataStream<T> stream : streams) {
            addStream(stream);
        }
    }

    public SortCombineDataStream(DataStream<T>... streams) {
        super(streams.length);
        for (DataStream<T> stream : streams) {
            addStream(stream);
        }
    }

    public void addStream(DataStream<T> stream) {
        if (contains(stream)) { return; }
        T header = stream.next();
        if (header != null) { addEnum(new Node<>(stream, header)); }
    }

    @Override
    public T next() {
        Node<T> node = headerEnum();
        if (node == null) { return null; }
        T header = node.header;
        if (node.nextData()) {
            updateHeader();
        } else {
            pollEnum();
        }
        return header;
    }

    protected static class Node<T extends Comparable> implements Comparable<Node> {
        protected final DataStream<T> stream;
        protected T header;

        protected Node(DataStream<T> stream, T header) {
            this.stream = stream;
            this.header = header;
        }

        public boolean nextData() {
            header = stream.next();
            return header != null;
        }

        public boolean lte(Node<T> n) {
            if (n == null) { return false; }
            T nh = n.header;
            if (nh == null) { return false; }
            T h = header;
            if (h == null) { return true; }
            return h != nh && h.compareTo(nh) < 1;
        }

        @Override
        public int compareTo(Node n) {//NOSONAR
            if (n == null) { return -1; }
            T nh = (T) n.header;
            if (nh == null) { return -1; }
            T h = header;
            if (h == null) { return 1; }
            return h == nh ? 0 : h.compareTo(nh);
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {//NOSONAR
            return o == this || stream == o;
        }
    }
}
