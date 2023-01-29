package org.suai.snmp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Metrics")
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sys_up_time")
    private String sysUpTime;

    @Column(name = "if_out_errors")
    private String ifOutErrors;

    @Column(name = "if_in_discards")
    private String ifInDiscards;

    @Column(name = "if_oper_status")
    private String ifOperStatus;

    @Column(name = "if_last_change")
    private String ifLastChange;

}
