package com.zxw.utils;

import sun.misc.Contended;


/**
 * @author zxw
 * @date 2020/7/21 9:55
 */
@Contended
public class Cell {
    volatile long value;
    private static final sun.misc.Unsafe UNSAFE;
    private static final long valueOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> ak = Cell.class;
            valueOffset = UNSAFE.objectFieldOffset
                    (ak.getDeclaredField("value"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    public Cell(long value) {
        this.value = value;
    }

    final boolean cas(long cmp, long val){
        return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
    }
}
