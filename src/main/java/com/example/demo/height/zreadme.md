## Disruptor 高级操作

事件处理核心 EventProcessor

### 一、串、并行操作

### 二、菱形操作

### 三、多边形操作
强大的地方，可以构造有向拓扑图来执行，例如：Storm底层就是用的Disruptor做的封装，来执行拓扑图。


### 四、多生产者多消费者

* 依赖WorkerPool实现多消费者

* 单消费者使用BatchEventProcessor

* 多消费者使用WorkProcessor
