package com.gjames.socketio.demo;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;

import java.util.*;

public class ChatLauncher {
    private static Map<String, List<UUID>> rooms = new HashMap<String, List<UUID>>();

    public static void main(String[] args) throws InterruptedException {
        Configuration config = new Configuration();
        SocketConfig sockConfig = new SocketConfig();
        sockConfig.setReuseAddress(true);
        config.setSocketConfig(sockConfig);
        config.setOrigin("*");
//        config.setHostname("192.168.1.69");
        config.setHostname("10.206.4.56");
        config.setPort(8080);
        for (int i = 0; i < 1000; i++) {
            rooms.put("room" + i, new ArrayList<UUID>());
        }
        final SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("SEND_BALL_VECTOR_POSITION", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String ballRecVectorPosition, AckRequest ackRequest) throws Exception {
                setEventToClientsInSameRoom(socketIOClient, ballRecVectorPosition, "REC_BALL_VECTOR_POSITION", server);
            }
        });

        server.addEventListener("SEND_VECTOR_POSITION", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String recVectorPosition, AckRequest ackRequest) throws Exception {
                setEventToClientsInSameRoom(socketIOClient, recVectorPosition, "REC_VECTOR_POSITION", server);
            }
        });

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                log("@@@@@@@@@ Client connected! @@@@@@@@@");
                RoomJoin roomJoin = assignEmptyRoom(socketIOClient);
                socketIOClient.sendEvent("JOINED_ROOM", "{roomId:" + roomJoin.roomId
                        + ", playerNumber: " + roomJoin.playerNumber + "}");
            }
        });
        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

    private static void setEventToClientsInSameRoom(SocketIOClient socketIOClient, String data, String eventString, SocketIOServer server) {
        String roomKey = findRoomForUUID(socketIOClient.getSessionId());
        List<UUID> clientIds = rooms.get(roomKey);
        Iterator<SocketIOClient> iterator = server.getAllClients().iterator();
        for (UUID uuid : clientIds) {
            SocketIOClient client = iterator.next();
            if (client.getSessionId().equals(uuid)
                    && !client.getSessionId().equals(socketIOClient.getSessionId())) {
                client.sendEvent(eventString, data);
            }
        }
    }

    private static void log(String s) {
        System.out.println(s);
    }

    private static String findRoomForUUID(UUID sessionId) {
        Set<String> roomKeys = rooms.keySet();
        for (String rk : roomKeys) {
            List<UUID> uuids = rooms.get(rk);
            for (UUID uid : uuids) {
                if (uid.equals(sessionId)) {
                    return rk;
                }
            }
        }
        return null;
    }


    private static RoomJoin assignEmptyRoom(SocketIOClient socketIOClient) {
        Set<String> roomKeys = rooms.keySet();
        for (String rk : roomKeys) {
            if (rooms.get(rk).size() < 2) {
                if (!rooms.get(rk).contains(socketIOClient.getSessionId())) {
                    rooms.get(rk).add(socketIOClient.getSessionId());
                    return new RoomJoin(rk, (rooms.get(rk).size() - 1));
                }
            }
        }
        return null;
    }

    public static class RoomJoin {
        public final String roomId;
        public final int playerNumber;

        public RoomJoin(String roomId, int playerNumber) {
            this.roomId = roomId;
            this.playerNumber = playerNumber;
        }
    }
}