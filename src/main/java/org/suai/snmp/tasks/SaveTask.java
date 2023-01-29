package org.suai.snmp.tasks;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suai.snmp.dao.MetricsDAO;
import org.suai.snmp.models.Metrics;
import org.suai.snmp.models.SnmpManager;

import java.io.IOException;

public class SaveTask implements Runnable{

    private final MetricsDAO metricsDAO;
    private final SnmpManager snmpManager;

    public SaveTask(MetricsDAO metricsDAO, SnmpManager snmpManager) {
        this.metricsDAO = metricsDAO;
        this.snmpManager = snmpManager;
    }

    @Override
    public void run() {


        try {
            PDU pdu = this.snmpManager.get(this.snmpManager.getOidArray()).getResponse();

            Metrics metrics = new Metrics();
            metrics.setSysUpTime(pdu.get(0).toValueString());
            metrics.setIfOutErrors(pdu.get(1).toValueString());
            metrics.setIfInDiscards(pdu.get(2).toValueString());
            metrics.setIfOperStatus(pdu.get(3).toValueString());
            metrics.setIfLastChange(pdu.get(4).toValueString());
            this.metricsDAO.save(metrics);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
