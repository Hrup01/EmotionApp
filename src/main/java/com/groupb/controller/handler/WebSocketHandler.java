package com.groupb.controller.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupb.mapper.PrivateMessageMapper;
import com.groupb.pojo.PrivateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * webSocket主处理程序
 */
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    //在线人数
    private static final Map<String, WebSocketSession> ONLINE_USERS = new ConcurrentHashMap<>();
    //json格式的序列化和反序列化(前后端传输)
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PrivateMessageMapper privateMessageMapper;//持久化

    /**
     * 建立连接成功后调用
     * @param session 当前用户的session
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = getUsername(session);//获取用户名
        //检查用户是否在线
        if (username != null && !ONLINE_USERS.containsKey(username)) {
            //将用户添加到在线列表
            ONLINE_USERS.put(username, session);
            log.info("用户{}上线，当前在线人数：{}", username, ONLINE_USERS.size());
            //需要给刚上线的用户推送离线时出现的信息
            sendOfflineMessages(username, session);
        }
    }

    /**
     * 发送信息
     * @param session 发送者的session
     * @param message 发送的信息
     * @throws Exception 异常
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String fromUsername =getUsername(session);//获取发送方的用户名
        String payload = message.getPayload();//获取消息（json）
        log.info("用户{}发送消息{}", fromUsername, payload);
        //序列化为PrivateMessage对象
        PrivateMessage msg = objectMapper.readValue(payload, PrivateMessage.class);
        //设置msg消息
        msg.setFromUserName(fromUsername);
        msg.setSendTime(LocalDateTime.now());
        msg.setIsRead(false);//设置未读
        //发送内容和接收对象不能为空
        if (msg.getToUserName()==null||msg.getContent()==null) {
            log.error("接收者或发送内容为空");
            return;
        }
        privateMessageMapper.insert(msg);//持久化
        sendMessageToUser(msg);
    }

    /**
     * 连接关闭
     * @param session 用户session
     * @param status 状态
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //获取下线用户的用户名
        String username = getUsername(session);
        //将用户从在线列表中移除
        if (username!=null) {
            ONLINE_USERS.remove(username);
            log.info("用户{}下线，当前在线人数：{}", username, ONLINE_USERS.size());
        }
    }

    /**
     * 发送信息
     * @param privateMessage 信息
     */
    private void sendMessageToUser(PrivateMessage privateMessage) {
        //从在线列表中获取接收方的会话
        WebSocketSession toSession = ONLINE_USERS.get(privateMessage.getToUserName());
        try {
            //检查接收方是否在线且会话有效
            if (toSession != null && toSession.isOpen()) {
                //将消息对象序列化为json
                String responseJson = objectMapper.writeValueAsString(privateMessage);
                //发送消息给接收方
                toSession.sendMessage(new TextMessage(responseJson));
                //消息发送成功后，更新数据库中消息的状态为已读
                PrivateMessage updateMsg = new PrivateMessage();
                updateMsg.setId(privateMessage.getId());//设置消息Id
                updateMsg.setIsRead(true); //标记为已读
                privateMessageMapper.updateById(updateMsg);//更新数据库
            }
            //如果接收方不在线，消息保存到数据库，等待其上线后推送
        } catch (IOException e) {
            // 发送消息异常
            log.error("出现异常", e);
            e.printStackTrace();
        }
    }


    /**
     * 离线推送
     * @param username 推送用户
     * @param session 用户session
     */
    private void sendOfflineMessages(String username, WebSocketSession session) {
        // 条件：接收方是当前用户+消息未读+按发送时间升序排列
        LambdaQueryWrapper<PrivateMessage> queryWrapper = new LambdaQueryWrapper<PrivateMessage>()
                .eq(PrivateMessage::getToUserName, username)//接收方用户名匹配
                .eq(PrivateMessage::getIsRead, false)//未读消息
                .orderByAsc(PrivateMessage::getSendTime);//按发送时间升序

        //从数据库查询离线消息
        List<PrivateMessage> offlineMessages = privateMessageMapper.selectList(queryWrapper);

        //如果有离线消息，推送给用户
        if (!offlineMessages.isEmpty()) {
            try {
                //遍历离线消息，逐个推送
                for (PrivateMessage msg : offlineMessages) {
                    String responseJson = objectMapper.writeValueAsString(msg);
                    session.sendMessage(new TextMessage(responseJson));
                }
                //批量将已推送的离线消息标记为已读
                //提取所有离线消息的 ID
                List<Long> ids = offlineMessages.stream()
                        .map(PrivateMessage::getId)
                        .collect(Collectors.toList());
                LambdaUpdateWrapper<PrivateMessage> updateWrapper = new LambdaUpdateWrapper<PrivateMessage>()
                        .in(PrivateMessage::getId, ids) //要求该Id在离线消息Id列表中
                        .set(PrivateMessage::getIsRead, true);//将 is_read字段设为 true

                //更新数据库
                //第一个参数为 null，表示不更新实体中的其他字段，只使用wrapper中的set条件
                privateMessageMapper.update(null, updateWrapper);
                log.info("已为用户{}推送{}条离线消息", username, offlineMessages.size());
            } catch (IOException e) {
                // 推送离线消息异常，打印日志
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取用户名称---需要前端请求路径中有用户名参数 例如 ws://localhost:8080/chat?username=xxx
     * @param session 用户session
     * @return 用户名
     */
    private String getUsername(WebSocketSession session) {
        log.info("解析sessionId为{}的用户名",session.getId());
        //从uri中解析
        String query = session.getUri().getQuery();

        //解析查询参数中的 username
        if (query != null && query.contains("username=")) {
            //分割出用户名
            String s = query.split("=")[1];
            log.info("解析出用户名为{}",s);
            return s;
        }
        //未获取返回null
        log.info("未解析用户名");
        return null;
    }

}
