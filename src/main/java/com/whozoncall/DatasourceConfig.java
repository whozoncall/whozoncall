package com.whozoncall;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfig {
   // @Bean
    //public DataSource datasource() {
//        return DataSourceBuilder.create()
//          .driverClassName("com.mysql.cj.jdbc.Driver")
//          .url("jdbc:mysql://whozoncall-db.cnf2rrygztj3.us-east-2.rds.amazonaws.com:3306/whozoncall")
//          .username("admin")
//          .password("W7RmRygMpPUwtuL")
//          .build();	
 //   }
}