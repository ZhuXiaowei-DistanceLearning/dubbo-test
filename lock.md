# 锁设计

# 1\. 前言

之前有了解过ReentrantLock可重入锁的源码执行流程，不过这都是代码的执行过程，并没有从锁的设计角度上去考虑技术的架构，最近在学redis的分布式锁，其中令我比较感触的就是锁的设计理念大部分都差不多，仔细想想大部分的设计的核心都差不多，所以决定从锁的设计重新出发再次深入了解。

## 2.过程

## 2.1 接口Lock

其实和之前的缓存设计差不多，我们定义一个Lock接口来表示我们的锁本身。其中最核心的当然就是lock()和unlock()方法，毕竟加锁和解锁是锁的核心。其次可以定义tryLock()这是为了当多个线程进行锁的竞争时，让当前锁尝试去获取到锁。具体如下

```java
package com.zxw.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author zxw
 * @date 2020/7/2 19:27
 */
public interface Lock {
    /**
     * 加锁
     */
    void Lock();

    /**
     * 解锁
     */
    void unLock();

    /**
     * 一直请求获取锁
     *
     * @return
     */
    boolean tryLock();

    /**
     * 设置等待超时时间
     *
     * @param time     时间
     * @param timeUnit 时间单位
     * @return
     */
    boolean tryLock(long time, TimeUnit timeUnit);
}
```

我们来看看具体调用的时候情况是怎样的，首先是一个服务接口，然后3个服务都同时请求这个接口的方法，由于大家都是同时请求，所以不能保证共享资源的正确访问，所以我们可加锁保证共享资源的正确性。如下，相当一个拦截器。

![image-20200702195402375](D:\code\IDEA CODE\dubbo-test\lock.assets\image-20200702195402375.png)

这里有个不确定性就是如果一个方法一直有服务过来调用，那么可能会导致其中一个服务一直请求不到锁，因为抢不到锁，这就是我们所说的非公平锁。

但是有的时候我们希望服务之间按顺序调用以满足我们的业务需求，那怎么办呢？让它们一个一个按逻辑调用就行了，例如下面。

![image-20200702204025702](D:\code\IDEA CODE\dubbo-test\lock.assets\image-20200702204025702.png)

那么怎么才能让它顺序执行呢？ 我们弄个FIFO队列就好，将加入的节点都放入队列中，就能保证每个服务都能请求。![image-20200702204150022](D:\code\IDEA CODE\dubbo-test\lock.assets\image-20200702204150022.png)

