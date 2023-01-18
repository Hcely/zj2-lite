package test;

import org.zj2.lite.common.Releasable;
import org.zj2.lite.common.function.BeanIntConsumer;
import org.zj2.lite.net.util.RingArrayStream;

import java.util.ArrayList;
import java.util.List;

/**
 *  TestRing
 *
 * @author peijie.ye
 * @date 2023/1/18 11:37
 */
public class TestRing {
    static final int COUNT = 1280_0000;
    static final int STEP_COUNT = 4;
    static long num = 0;

    static void handle(E e) {
        num += e.i;
        e.str = e.str + e.i;
    }

    public static void main(String[] args) throws InterruptedException {
        RingArrayStream<E> stream = new RingArrayStream<>(STEP_COUNT, 1 << 20, E::new);
        List<Thread> producers = new ArrayList<>(5);
        List<Thread> threads = new ArrayList<>(15);
        for (int i = 0; i < 4; ++i) {
            Producer producer = new Producer(stream);
            producers.add(producer);
            threads.add(producer);
            for (int n = 0; n < STEP_COUNT; ++n) { threads.add(new Consumer(n, stream)); }
        }
        for (Thread e : threads) { e.start(); }
        for (Thread e : producers) { e.join(); }
        stream.destroy();
        Thread.sleep(5000);
        System.out.println(num);
    }


    private static class Producer extends Thread implements BeanIntConsumer<E> {
        private final RingArrayStream<E> stream;

        private Producer(RingArrayStream<E> stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            System.out.println("start");
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < COUNT; ++i) {
                stream.add(i, this);
            }
            System.out.println(System.currentTimeMillis() - startTime);
        }

        @Override
        public void accept(E e, int value) {
            e.i = value;
            e.str = "AAAA_AAAA_AAAA";
        }
    }

    private static class Consumer extends Thread {
        private final int step;
        private final RingArrayStream<E> stream;

        private Consumer(int step, RingArrayStream<E> stream) {
            this.step = step;
            this.stream = stream;
        }

        @Override
        public void run() {
            stream.consume(step, TestRing::handle);
        }
    }

    static class E implements Releasable {
        int i;
        String str = "";

        @Override
        public void release() {
            str = "";
        }
    }
}
