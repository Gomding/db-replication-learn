package com.example.dbreplicationlearn;

import com.example.dbreplicationlearn.config.Slaves;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SlvaesTest {

    @Autowired
    Slaves slaves;

    @Test
    void checkBean() {
        List<Slaves.Slave> slaveList = slaves.getSlaveList();
        slaveList.forEach(System.out::println);
        assertThat(slaveList).hasSize(2);
        assertThat(slaveList).extracting("name").contains("slave1", "slave2");
    }
}
