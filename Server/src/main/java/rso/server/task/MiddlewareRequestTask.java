package rso.server.task;

import rso.core.model.Message;
import rso.core.net.SocketSender;
import rso.core.taskmanager.Task;
import rso.core.taskmanager.TaskMessage;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by marcin on 05/05/15.
 */
public class MiddlewareRequestTask extends Task{

    public MiddlewareRequestTask() {
        addFilterForConnectionDirection(ConnectionDirection.MiddlewareToInner);
    }

    @Override
    public boolean processMessage(TaskMessage taskMessage) {
        System.out.println("Siema dostaje messega!!! "  + taskMessage.getMessage().getMiddlewareRequest().getNodeId());

        Socket socket = null;
//        try {
//            socket = new Socket("192.168.0.39", 6971);
//            SocketSender s = new SocketSender(socket);


//            Message.MiddlewareHeartbeat.Builder hrt = Message.RSOMessage.newBuilder().getMiddlewareHeartbeatBuilder();
//            hrt.setConnectedClients(69).setServerId(32131).setMessageType(Message.MiddlewareMessageType.Heartbeat);

            Message.MiddlewareResponse.Builder mhb = Message.RSOMessage.newBuilder().getMiddlewareResponseBuilder();
            Message.EntityState.Builder es = Message.RSOMessage.newBuilder().getMiddlewareResponseBuilder().getChangesBuilder();
            es.addStudents(Message.Person.newBuilder().setUuid("2").setName("Jaroslaw").setSurname("Kometa").setTimestamp(89087654345l).build());
                    mhb.setChanges(es.build());

            Message.RSOMessage.Builder snd = Message.RSOMessage.newBuilder().setMiddlewareResponse(mhb.build());
//            s.send(snd.build());
            send(snd.build());

//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        return false;
    }
}
