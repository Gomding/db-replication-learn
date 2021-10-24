package com.example.dbreplicationlearn.config;

import com.example.dbreplicationlearn.datasource.ReplicationRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dbreplicationlearn.datasource.ReplicationRoutingDataSource.DATA_SOURCE_KEY_MASTER;

/**
 * @EnableAutoConfiguration : 스프링 애플리케이션 컨텍스트의 자동 구성을 활성화하여 필요할 가능성이 있는 빈을 추측하고 구성합니다.
 * 자동 구성 클래스는 일반적으로 클래스 경로와 정의한 빈을 기반으로 적용됩니다.
 * exclude 값 : 특정 자동 구성 클래스를 제외합니다. 제외할 클래스를 정의함.
 * @EnableTransactionManagement : Spring의 <tx:*> XML 네임스페이스에서 찾을 수 있는 지원과 유사한 Spring의 주석 기반 트랜잭션 관리 기능을 활성화합니다.
 * 이것은 @Configuration 클래스에서 기존의 필수 트랜잭션 관리 또는 사후 트랜잭션 관리를 구성하는 데 사용됩니다.
 * @EnableJpaRepositories : JPA 저장소를 활성화하기 위한 주석. 기본적으로 Spring Data 리포지토리에 대해 주석이 달린 구성 클래스의 패키지를 스캔합니다.
 */

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@EnableConfigurationProperties(Slaves.class)
@EnableJpaRepositories(basePackages = {"com.example.dbreplicationlearn"})
public class DatabaseConfig {

    private final Slaves slaves;

    public DatabaseConfig(Slaves slaves) {
        this.slaves = slaves;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    private DataSource createDataSource(Slaves.Slave slave) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(slave.getJdbcUrl())
                .username(slave.getUsername())
                .password(slave.getPassword())
                .build();
    }

    @Bean
    public Map<String, DataSource> slaveDataSources() {
        List<Slaves.Slave> slaveList = slaves.getSlaveList();
        return slaveList.stream()
                .collect(Collectors.toMap(Slaves.Slave::getName, this::createDataSource));
    }

    @Bean
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource master,
                                        @Qualifier("slaveDataSources") Map<String, DataSource> slaveDataSources) {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        HashMap<Object, Object> sources = new HashMap<>();
        sources.put(DATA_SOURCE_KEY_MASTER, master);
        sources.putAll(slaveDataSources);

        routingDataSource.setTargetDataSources(sources);
        routingDataSource.setDefaultTargetDataSource(master);
        List<String> slaveDataSourceNames = new ArrayList<>(slaveDataSources.keySet());
        routingDataSource.setSlaveDataSourceNames(slaveDataSourceNames);

        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
        // LazyConnectionDataSourceProxy : JDBC 커넥션을 실제로 필요한 순간에 가져온다. 그렇지 않은 경우에도 Pool에서 JDBC 커넥션을 가져오는걸 방지할 수 있음
    }
}
