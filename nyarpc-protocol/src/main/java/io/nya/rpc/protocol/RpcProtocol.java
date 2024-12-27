package io.nya.rpc.protocol;

import io.nya.rpc.protocol.header.RpcHeader;

public class RpcProtocol<T>  {

    private static final long serialVersionUID = 292789485166173277L;

    private RpcHeader header;
    private T body;

    public void setBody(T body) {
        this.body = body;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public RpcHeader getHeader() {
        return header;
    }

    public T getBody() {
        return body;
    }
}
