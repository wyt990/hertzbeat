package com.usthe.collector.dispatch.export;

import com.usthe.common.entity.message.CollectRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 采集数据消息发送
 * @author tomsun28
 * @date 2021/11/3 15:22
 */
@Component
@Slf4j
public class MetricsDataExporter implements DisposableBean {

    private final LinkedBlockingQueue<CollectRep.MetricsData> metricsDataToAlertQueue;
    private final LinkedBlockingQueue<CollectRep.MetricsData> metricsDataToWarehouseInfluxQueue;
    private final LinkedBlockingQueue<CollectRep.MetricsData> metricsDataToWarehouseRedisQueue;

    public MetricsDataExporter() {
        metricsDataToAlertQueue = new LinkedBlockingQueue<>();
        metricsDataToWarehouseInfluxQueue = new LinkedBlockingQueue<>();
        metricsDataToWarehouseRedisQueue = new LinkedBlockingQueue<>();
    }

    public CollectRep.MetricsData pollAlertMetricsData() throws InterruptedException {
        return metricsDataToAlertQueue.poll(2, TimeUnit.SECONDS);
    }

    public CollectRep.MetricsData pollWarehouseInfluxMetricsData() throws InterruptedException {
        return metricsDataToAlertQueue.poll(2, TimeUnit.SECONDS);
    }

    public CollectRep.MetricsData pollWarehouseRedisMetricsData() throws InterruptedException {
        return metricsDataToWarehouseRedisQueue.poll(2, TimeUnit.SECONDS);
    }

    /**
     * 发送消息
     * @param metricsData 指标组采集数据
     */
    public void send(CollectRep.MetricsData metricsData) {
        metricsDataToAlertQueue.offer(metricsData);
        metricsDataToWarehouseInfluxQueue.offer(metricsData);
        metricsDataToWarehouseRedisQueue.offer(metricsData);
    }

    @Override
    public void destroy() throws Exception {
        metricsDataToAlertQueue.clear();
    }
}