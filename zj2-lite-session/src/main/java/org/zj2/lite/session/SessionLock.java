package org.zj2.lite.session;

/**
 *
 * <br>CreateDate 三月 27,2022
 * @author peijie.ye
 */
public interface SessionLock {
    SessionLock EMPTY_LOCK = new SessionLock() {
        @Override
        public boolean lock() {
            return true;
        }

        @Override
        public void unlock() {
            //NOTHING
        }
    };

    boolean lock() throws Throwable;//NOSONAR

    void unlock();
}
