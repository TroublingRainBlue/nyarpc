package io.nya.rpc.provider;

import io.nya.rpc.common.scanner.server.RpcServiceScanner;
import io.nya.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcSingleServer extends BaseServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String packageName, String reflectType) {
        super(serverAddress, reflectType);
        try {
            this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(packageName);
        } catch (Exception e) {
            LOGGER.error("Rpc Server init error===>>>{}", e.toString());
        }
    }
}
