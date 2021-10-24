package com.example.dbreplicationlearn.datasource;

import org.slf4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    public static final String DATA_SOURCE_KEY_MASTER = "master";
    public static final String DATA_SOURCE_KEY_SLAVE = "slave";

    @Override
    protected Object determineCurrentLookupKey() {
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            System.out.println(DATA_SOURCE_KEY_SLAVE);
            return DATA_SOURCE_KEY_SLAVE;
        }
        System.out.println(DATA_SOURCE_KEY_MASTER);
        return DATA_SOURCE_KEY_MASTER;
    }
}
