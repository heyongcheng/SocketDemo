package org.example.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import java.util.ArrayList;
import java.util.List;
import org.example.protobuf.SubscribeReqProto.SubscribeReq;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/18 15:31
 */
public class TestSubscribeProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body)
        throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq
            .newBuilder();

        builder.setSubReqID(1);
        builder.setUserName("Lilinfeng");
        builder.setProductName("Netty Book");
        List<String> address = new ArrayList<>();
        address.add("NanJing YuHuaTai");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        builder.addAllAddress(address);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReq subscribeReq = createSubscribeReq();
        System.out.println("Before encode: " + subscribeReq.toString());
        SubscribeReq req = decode(encode(subscribeReq));
        System.out.println("After decode: " + req.toString());
        System.out.println("Assert equal : --> " + req.equals(subscribeReq));
    }

}
