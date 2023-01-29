package org.suai.snmp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnmpManager {

    private final String agent = "udp:192.168.99.100/161";
    private final OID[] oidArray =
            {
                    new OID("1.3.6.1.2.1.1.3.0"),
                    new OID("1.3.6.1.2.1.2.2.1.20.1"),
                    new OID("1.3.6.1.2.1.2.2.1.13.1"),
                    new OID("1.3.6.1.2.1.2.2.1.8.1"),
                    new OID("1.3.6.1.2.1.2.2.1.9.1")
            };

    private Snmp snmp;

    public void start() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public ResponseEvent get(OID[] oids) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if(event != null) {
            return event;
        }
        throw new RuntimeException("Time out occured");
    }

    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(agent);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }



}
