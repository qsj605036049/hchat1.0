package com.itheima.hchat;

import com.itheima.hchat.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.management.MXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * hchat启动类
 * @author qinshiji
 * @data 2019/7/19 10:33
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itheima.hchat.mapper")
public class Application {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(Application.class);
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("本机地址为:"+localHost.getHostAddress());
    }
    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker(0,0);
    }
}
