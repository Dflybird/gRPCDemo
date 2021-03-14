package firstgrpc;

import firstproto.AgentID;
import firstproto.FirstGRPCGrpc;
import firstproto.Location;
import firstproto.Point;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author Gq
 * @Date 2021/3/14 21:01
 * @Version 1.0
 **/
public class FirstServer {

    private final int port;
    private final Server server;

    public FirstServer(int port) {
        this.port = port;
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
        server = serverBuilder.addService(new Service()).build();
    }

    public static void main(String[] args) throws Exception {
        FirstServer firstServer = new FirstServer(10086);
        firstServer.start();
        for (int i = 0; i < 30; i++) {
            Thread.sleep(1000);
            System.out.println(i + "s passed");
        }
//        firstServer.stop();
    }

    public void start() throws IOException {
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirstServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("server shutdown");
            }
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static class Service extends FirstGRPCGrpc.FirstGRPCImplBase {

        private HashMap<String, Float> map;

        public Service() {
            map = new HashMap<>();
            map.put("0", 0.0f);
            map.put("1", 0.1f);
            map.put("2", 0.2f);
            map.put("3", 0.3f);
        }

        @Override
        public void getLocation(AgentID request, StreamObserver<Location> responseObserver){
            String id = request.getName();
            float x = map.get(id);
            Point point = Point.newBuilder().setX(x).setY(0).build();
            Location location = Location.newBuilder().setName(id).setPos(point).build();
            responseObserver.onNext(location);
            responseObserver.onCompleted();
        }
    }
}
