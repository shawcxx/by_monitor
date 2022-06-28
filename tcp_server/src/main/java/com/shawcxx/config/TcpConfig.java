package com.shawcxx.config;

import com.shawcxx.service.TcpProtocol;
import com.shawcxx.service.TcpSender;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.smartboot.socket.transport.AioQuickServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author dj
 * @date 2021-12-20 14:17
 * @description
 **/
@Slf4j
@Configuration
public class TcpConfig {

    @Resource
    private TcpConfigBO tcpConfigBO;

    @Resource
    private TcpProtocol tcpProtocol;

    @Resource
    private TcpSender tcpSender;

    @Bean
    public AioQuickServer start() throws IOException {
        val port = tcpConfigBO.getPort();
        AioQuickServer server = new AioQuickServer(port, tcpProtocol, tcpSender);
        server.setReadBufferSize(1024 * 1024);
        server.start();
        log.info("tcp服务启动成功,端口:{}", port);
        return server;
    }

}
