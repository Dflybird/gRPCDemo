package firstgrpc;

import firstproto.AgentID;
import firstproto.FirstGRPCGrpc;
import firstproto.Location;
import firstproto.Point;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @Author Gq
 * @Date 2021/3/14 21:59
 * @Version 1.0
 **/
public class FirstClient {

    private final FirstGRPCGrpc.FirstGRPCBlockingStub blockingStub;
    private final FirstGRPCGrpc.FirstGRPCStub asyncStub;

    public FirstClient(Channel channel) {
        blockingStub = FirstGRPCGrpc.newBlockingStub(channel);
        asyncStub = FirstGRPCGrpc.newStub(channel);
    }

    public static void main(String[] args) {

        String target = "localhost:10086";

        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        FirstClient firstClient = new FirstClient(channel);

        firstClient.getLocation("0");
        firstClient.getLocation("1");
        firstClient.getLocation("2");
    }

    private void getLocation(String id) {
        AgentID agentID = AgentID.newBuilder().setName(id).build();

        Location location = blockingStub.getLocation(agentID);
        Point point = location.getPos();
        System.out.println("id: "+ location.getName() + " | x: " + point.getX());
    }

}
