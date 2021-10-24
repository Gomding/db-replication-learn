package com.example.dbreplicationlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.datasource.hikari.slave")
public class Slaves {

    List<Slave> slaveList = new ArrayList<>();

    public List<Slave> getSlaveList() {
        return slaveList;
    }

    public static class Slave {
        private String name;
        private String username;
        private String password;
        private String jdbcUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        @Override
        public String toString() {
            return "Slave{" +
                    "name='" + name + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", jdbcUrl='" + jdbcUrl + '\'' +
                    '}';
        }
    }
}
