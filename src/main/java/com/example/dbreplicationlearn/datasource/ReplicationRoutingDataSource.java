package com.example.dbreplicationlearn.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationRoutingDataSource.class);

    public static final String DATA_SOURCE_KEY_MASTER = "master";

    public SlaveDataSourceNames slaveDataSourceNames;

    public void setSlaveDataSourceNames(List<String> names) {
        this.slaveDataSourceNames = new SlaveDataSourceNames(names);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            String nextSlaveDataSourceName = slaveDataSourceNames.getNextName();
            LOGGER.info("Using Slave DB Name : {}", nextSlaveDataSourceName);
            return nextSlaveDataSourceName;
        }
        LOGGER.info("Using Master DB");
        return DATA_SOURCE_KEY_MASTER;
    }

    public static class SlaveDataSourceNames {

        private List<String> values;
        private int counter = 0;

        public SlaveDataSourceNames(List<String> values) {
            this.values = values;
        }

        public String getNextName() {
            LOGGER.info("counter : {}", this.counter);
            if (counter == values.size()) {
                counter = 0;
            }
            LOGGER.info("counter : {}", this.counter);
            return values.get(counter++);
        }
    }
}
