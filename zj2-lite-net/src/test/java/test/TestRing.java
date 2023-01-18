package test;

import org.zj2.lite.common.Releasable;
import org.zj2.lite.common.function.BeanIntConsumer;
import org.zj2.lite.net.util.RingArrayStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  TestRing
 *
 * @author peijie.ye
 * @date 2023/1/18 11:37
 */
public class TestRing {
    private static final int COUNT = 1280_0000;
    private static AtomicLong num = new AtomicLong();

    private static void handle(E e) {
        num.addAndGet(e.i);
        e.str = e.str + e.i;
    }

    public static void main(String[] args) throws InterruptedException {
        RingArrayStream<E> stream = new RingArrayStream<>(4, 1 << 20, E::new);
        List<Thread> producers = new ArrayList<>(5);
        List<Thread> threads = new ArrayList<>(15);
        for (int i = 0; i < 4; ++i) {
            Producer producer = new Producer(stream);
            producers.add(producer);
            threads.add(producer);
            threads.add(new Consumer(0, stream));
            threads.add(new Consumer(1, stream));
            threads.add(new Consumer(2, stream));
            threads.add(new Consumer(3, stream));
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

    private static class E implements Releasable {
        int i;
        String str = "";

        @Override
        public void release() {
            str = "";
        }
    }
}
