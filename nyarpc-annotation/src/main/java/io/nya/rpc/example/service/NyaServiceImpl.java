package io.nya.rpc.example.service;

import io.nya.rpc.annotation.RpcService;

@RpcService(interfaceClass = NyaService.class, version = "1.0.1", group = "Nya")
public class NyaServiceImpl implements NyaService{
    // ****
}
