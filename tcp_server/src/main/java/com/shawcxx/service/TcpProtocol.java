package com.shawcxx.service;

import com.shawcxx.unpack.BaseUnpackService;
import com.shawcxx.common.util.MyUnpackUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;

/**
 * @author Chen jl
 * @date 2022/6/11 20:44
 * @description
 **/
@Slf4j
@Service
public class TcpProtocol implements Protocol<String> {
    @Resource
    private BaseUnpackService baseUnpackService;

    /**
     * tcp主接收方法入口
     *
     * @param readBuffer readBuffer
     * @param aioSession aioSession
     * @return 下发内容
     */
    @Override
    public String decode(ByteBuffer readBuffer, AioSession aioSession) {
        String data = MyUnpackUtil.readBuffer2Hex(readBuffer);
        log.info("收到aioSessionId:{},收到数据:{}", aioSession.getSessionID(), data);
        return baseUnpackService.unpack(data);
    }
}
