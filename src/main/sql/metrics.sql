CREATE TABLE Metrics(
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    sys_up_time varchar(100) NOT NULL,
    if_out_errors varchar(100) NOT NULL,
    if_in_discards varchar(100) NOT NULL,
    if_oper_status varchar(100) NOT NULL,
    if_last_change varchar(100) NOT NULL
);

SELECT * FROM Metrics

