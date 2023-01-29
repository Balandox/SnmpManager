package org.suai.snmp;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.suai.snmp.config.SpringConfig;
import org.suai.snmp.dao.MetricsDAO;
import org.suai.snmp.models.SnmpManager;
import org.suai.snmp.tasks.SaveTask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        ScheduledExecutorService executorService = context.getBean("scheduled", ScheduledExecutorService.class);

        executorService.scheduleWithFixedDelay(context.getBean("saveTask", SaveTask.class), 0, 15, TimeUnit.SECONDS);

    }

}
