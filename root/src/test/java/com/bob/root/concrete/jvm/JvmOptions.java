package com.bob.root.concrete.jvm;

/**
 * jvm调优参数说明
 *
 *
 * -XX:+UseConcMarkSweepGC
 * 使用CMS一般需要使用以下参数来调整期性能：
 * -XX:+CMSIncrementalMode     采用增量式的标记方式，减少标记时应用停顿时间
 * -XX:+CMSParallelRemarkEnabled     启用并行标记
 * -XX:CMSInitiatingOccupancyFraction=70     Old generation消耗比例达到多少时进行回收，通常配置60-80之间
 * -XX:CMSFullGCsBeforeCompaction=1     多少次Full GC 后压缩old generation一次
 * -XX:+UseCMSInitiatingOccupancyOnly
 * -XX:+ScavengeBeforeFullGC             Old generation GC前对young generation GC一次，默认开启。
 * -XX:+CMSScavengeBeforeRemark     CMS remark之前进行一次young generation GC
 *
 * 使用CMS样例：
 * JAVA_OPTS="
 * -DappName=XXX
 * -server
 * -Xms10g -Xmx10g
 * -XX:NewSize=4g -XX:MaxNewSize=4g
 * -XX:PermSize=256m -XX:MaxPermSize=256m
 *
 * -XX:+UseConcMarkSweepGC
 * -XX:+CMSIncrementalMode -XX:+CMSParallelRemarkEnabled
 * -XX:CMSInitiatingOccupancyFraction=70 -XX:CMSFullGCsBeforeCompaction=1
 * -XX:+UseCMSInitiatingOccupancyOnly
 * -XX:+ScavengeBeforeFullGC
 * -XX:+CMSScavengeBeforeRemark
 * -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:/home/XX/gc/XX_gc.log
 * -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M
 *
 * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/XX/dump_OOME.hprof
 *
 * -XX:+DisableExplicitGC
 *
 * "
 *
 * 1. PSYoungGen 代表并行回收 young generation.
 * 2. ParOldGen 代表并行回收 old generation.
 * 3. PSPermGen 代表并行回收 Permanent generation.
 *
 * -XX:+PrintGCDetails
 * -XX:+PrintGCDateStamps
 *
 * @author wb-jjb318191
 * @create 2018-07-25 9:18
 */
public interface JvmOptions {

    String Xms = "-Xms64M";

    String Xmx = "-Xmx128M";

    String UseSerialGC = "-XX:+UseSerialGC";

    String UseParallelGC = "-XX:+UseParallelGC";

    String UseParallelOldGC = "XX:-UseParallelOldGC";

    String UseConcMarkSweepGC = "-XX:+UseConcMarkSweepGC";

}