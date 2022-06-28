package com.shawcxx.service;

import com.shawcxx.common.constant.SysConstant;
import com.shawcxx.common.util.MyUnpackUtil;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;
import org.springframework.stereotype.Service;

/**
 * @author Chen jl
 * @date 2022/6/11 20:45
 * @description
 **/
@Slf4j
@Service
public class TcpSender implements MessageProcessor<String> {
    @Override
    public void process(AioSession aioSession, String msg) {
        String sessionId = aioSession.getSessionID();
        AioSession onlineSession = SysConstant.SESSION_MAP.get(sessionId);
        if (null == onlineSession) {
            return;
        }
        try {
            WriteBuffer writeBuffer = onlineSession.writeBuffer();
            byte[] bytes = MyUnpackUtil.hex2Bytes(msg);
            writeBuffer.write(bytes);
            writeBuffer.flush();
            log.info("发送id:{},发送数据:{}", sessionId, msg);
        } catch (Exception e) {
            log.error("Push消息异常", e);
        }
    }


    /**
     * tcp链接情况
     *
     * @param aioSession       aioSession
     * @param stateMachineEnum StateMachineEnum
     * @param throwable        throwable
     */
    @Override
    public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        String aioSessionId = aioSession.getSessionID();
        switch (stateMachineEnum) {
            case NEW_SESSION:
//                log.info("创建链接客户端:{} ", aioSessionId);
                SysConstant.SESSION_MAP.put(aioSessionId, aioSession);
                break;
            case SESSION_CLOSED:
//                log.info("断开客户端链接: {}", aioSessionId);
                SysConstant.SESSION_MAP.remove(aioSessionId);
                break;
            default:
        }
    }
}
