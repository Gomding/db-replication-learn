package com.example.dbreplicationlearn;

import com.example.dbreplicationlearn.config.Slaves;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DbReplicationLearnApplicationTests {

    @Autowired
    Slaves slaves;

    @Test
    void contextLoads() {
        List<Slaves.Slave> slaveList = slaves.getSlaveList();
        slaveList.forEach(System.out::println);
    }

}
