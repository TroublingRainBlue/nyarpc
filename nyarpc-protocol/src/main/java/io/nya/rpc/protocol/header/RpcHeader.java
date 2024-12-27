package io.nya.rpc.protocol.header;

import java.io.Serializable;

public class RpcHeader implements Serializable {
    private static final long serialVersionUID = 6011436680686290298L;

    // 魔数
    private short magic;

    // 报文类型
    private byte msgType;

    // 状态
    private byte status;

    // 消息ID
    private long requestId;

    // 序列化类型，约定为 16 字节，不足 16 字节则补 0，不得超过 16 字节
    private String serializationType;

    // 消息长度
    private int msgLen;

    // getter、setter

    public byte getMsgType() {
        return msgType;
    }

    public byte getStatus() {
        return status;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public long getRequestId() {
        return requestId;
    }

    public short getMagic() {
        return magic;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
