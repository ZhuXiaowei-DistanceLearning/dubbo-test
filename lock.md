# 锁设计

# 1\. 前言

之前有了解过ReentrantLock可重入锁的源码执行流程，不过这都是代码的执行过程，并没有从锁的设计角度上去考虑技术的架构，最近在学redis的分布式锁，其中令我比较感触的就是锁的设计理念大部分都差不多，仔细想想大部分的设计的核心都差不多，所以决定从锁的设计重新出发再次深入了解。

## 2.过程

设计一把锁需要考虑哪些因素呢？

1. 锁的原子性：必须保证持有锁的服务在使用期间不被其他服务打扰
2. 锁的释放：占有锁的服务可能会出现服务中断等因素，所以为了避免造成死锁，需要对锁及时释放
3. 锁中断：业务执行时间过长，锁自动释放导致其他线程占用锁时，当前线程对锁进行释放
4. 队列节点丢失：在公平锁队列中，其他节点可能服务故障，所以其他节点要及时替代其队列地位

锁优化

1. 提高并发能力，采用CAS分段锁

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

# 附录

# 1.CLH队列

即在其节点的前身中保存一些有关线程的控制信息。 每个节点中的“状态”字段跟踪线程是否应阻塞。 前继节点释放时会发出信号。 否则，队列的每个节点都充当一个特定通知样式的监视器，其中包含一个等待线程。 虽然状态字段不控制是否授予线程锁等。 线程可能会尝试获取它是否在队列中的第一位。 但是先行并不能保证成功。 它只赋予了竞争权。 因此，当前发布的竞争者线程可能需要重新等待。

```java
	 “ prev”（在原始CLH锁中不使用）主要用于处理取消。 如果取消某个节点，则其后继节点（通常）会重新链接到未取消的前任节点。 有关自旋锁情况下类似机制的说明。
      我们还使用“next”来实现阻止机制。 每个节点的线程ID都保留在其自己的节点中，因此前任通过遍历下一个链接以确定它是哪个线程，来通知下一个节点唤醒。 确定后继者必须避免与新排队的节点竞争以设置其前任节点的“ next”字段。 如有必要，可以通过在节点的后继者为空时原子更新的“tail”向后进行检查来解决此问题。（或者换句话说，next-links是一种优化，因此我们通常不需要向后扫描。）
	 * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
```

```java
static final class Node {
    /** 指示节点正在共享模式下等待的标记 */
    static final Node SHARED = new Node();
    /** 指示节点正在以独占模式等待的标记 */
    static final Node EXCLUSIVE = null;

    /** 取消 */
    static final int CANCELLED =  1;
    /** 指示后续线程需要释放 */
    static final int SIGNAL    = -1;
    /** 指示线程正在等待条件 */
    static final int CONDITION = -2;
    /**
     * 指示下一个acquireShared应该无条件传播
     */
    static final int PROPAGATE = -3;

    /**
     * Status field, taking on only the values:
     *   SIGNAL:     The successor of this node is (or will soon be)
     *               blocked (via park), so the current node must
     *               unpark its successor when it releases or
     *               cancels. To avoid races, acquire methods must
     *               first indicate they need a signal,
     *               then retry the atomic acquire, and then,
     *               on failure, block.
     *   CANCELLED:  This node is cancelled due to timeout or interrupt.
     *               Nodes never leave this state. In particular,
     *               a thread with cancelled node never again blocks.
     *   CONDITION:  This node is currently on a condition queue.
     *               It will not be used as a sync queue node
     *               until transferred, at which time the status
     *               will be set to 0. (Use of this value here has
     *               nothing to do with the other uses of the
     *               field, but simplifies mechanics.)
     *   PROPAGATE:  A releaseShared should be propagated to other
     *               nodes. This is set (for head node only) in
     *               doReleaseShared to ensure propagation
     *               continues, even if other operations have
     *               since intervened.
     *   0:          None of the above
     *
     * The values are arranged numerically to simplify use.
     * Non-negative values mean that a node doesn't need to
     * signal. So, most code doesn't need to check for particular
     * values, just for sign.
     *
     * The field is initialized to 0 for normal sync nodes, and
     * CONDITION for condition nodes.  It is modified using CAS
     * (or when possible, unconditional volatile writes).
     */
    volatile int waitStatus;

    /**
     * 链接到当前节点/线程所依赖的先前节点，以检查waitStatus。 
     * 在入队期间分配，并且仅在出队时将其清空（出于GC的考虑）。 
     * 同样，在前继节点cancel后，我们会短路，同时找到一个未cancel的前继节点，这将始终存在，因为根节点从未被取消：只有成功获取后，结点才变为根。 取消的线程永远不会成功获取，并且线程只会取消自身，不会取消任何其他节点。
     */
    volatile Node prev;

    /**
     * 链接到后继节点，当前节点/线程在释放时将其解散。 
     * 在排队时分配，在绕过取消的前任时进行调整，在出队时清零（出于GC的考虑）。
     * enq操作直到附加后才分配前任的下一个字段，因此看到空的下一个字段不一定表示节点在队列末尾。
     * 但是，如果下一个字段为空，则我们可以从尾部扫描上一个以进行再次检查。 被取消节点的下一个字段设置为指向节点本身而不是null，以使isOnSyncQueue的工作更轻松。
     */
    volatile Node next;

    /**
     * The thread that enqueued this node.  Initialized on
     * construction and nulled out after use.
     */
    volatile Thread thread;

    /**
     * Link to next node waiting on condition, or the special
     * value SHARED.  Because condition queues are accessed only
     * when holding in exclusive mode, we just need a simple
     * linked queue to hold nodes while they are waiting on
     * conditions. They are then transferred to the queue to
     * re-acquire. And because conditions can only be exclusive,
     * we save a field by using special value to indicate shared
     * mode.
     */
    Node nextWaiter;

    /**
     * Returns true if node is waiting in shared mode.
     */
    final boolean isShared() {
        return nextWaiter == SHARED;
    }

    /**
     * Returns previous node, or throws NullPointerException if null.
     * Use when predecessor cannot be null.  The null check could
     * be elided, but is present to help the VM.
     *
     * @return the predecessor of this node
     */
    final Node predecessor() throws NullPointerException {
        Node p = prev;
        if (p == null)
            throw new NullPointerException();
        else
            return p;
    }

    Node() {    // Used to establish initial head or SHARED marker
    }

    Node(Thread thread, Node mode) {     // Used by addWaiter
        this.nextWaiter = mode;
        this.thread = thread;
    }

    Node(Thread thread, int waitStatus) { // Used by Condition
        this.waitStatus = waitStatus;
        this.thread = thread;
    }
}
```