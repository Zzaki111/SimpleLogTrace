# SimpleLogTrace
通过spring aop特性记录服务调用的链路的工具
定义标准Trace日志bean：TraceBean
使用ThreadLocal存储上下文信息，通过唯一的traceId将调用链路串起来

主要用来提供一种记录调用链的思路，特此记录

当前版本的主要缺陷：
使用threadlocal无法记录跨系统的调用
