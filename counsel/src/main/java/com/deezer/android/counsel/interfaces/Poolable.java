package com.deezer.android.counsel.interfaces;

/**
 * Poolable implementations are automatically using an Object Pool
 *
 * @author Xavier Gouchet
 */
public interface Poolable {

    /**
     * Releases this instance in the pool
     */
    void releaseInstance();
}
