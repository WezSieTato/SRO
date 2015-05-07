package rso.client;

import org.springframework.stereotype.Component;
import rso.core.abstraction.BaseNode;
import rso.core.model.Message;
import rso.core.net.SocketReciver;
import rso.core.net.SocketSender;
import rso.core.taskmanager.TaskMessage;
import rso.middleware.server.ClientProto;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by modzelej on 2015-05-06.
 */
@Component("client")
public class ClientProgram extends BaseNode implements Runnable{

    private Socket socket;
    private SocketReciver reciver;
    private SocketSender sender;

    public void run() {
//        try {
//            socket = new Socket("192.168.0.13", 6972);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if(socket != null){
//            reciver = new SocketReciver(socket);
//            try {
//                sender = new SocketSender(socket);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            Message.MiddlewareMessage.Builder builder1 = Message.MiddlewareMessage.newBuilder();
//            builder1.setSubjectId(69).setNodeId(1);
//            Message.RSOMessage.Builder builder2 = Message.RSOMessage.newBuilder();
//            builder2.setMiddlewareMessage(builder1.build());
//
//            sender.send(builder2.build());
//
//            TaskMessage message = reciver.read();
//
//            System.out.println("Odebralem to: " + message.getMessage().getMiddlewareMessage().getSubjectId());
//
//        }

        Thread t = new Thread(new ClientProto(null));
        t.start();


    }
}
