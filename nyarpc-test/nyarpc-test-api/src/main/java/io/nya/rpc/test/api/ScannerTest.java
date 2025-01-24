package io.nya.rpc.test.api;

import io.nya.rpc.common.scanner.ClassScanner;
import io.nya.rpc.common.scanner.reference.RpcReferenceScanner;
import io.nya.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ScannerTest {

    @Test
    public void testScannerClassNameList() throws Exception{
        List<String> classNameList = ClassScanner.getClassNameList("io.nya.rpc.test.provider");
        classNameList.forEach(System.out::println);
    }
    /*
    @Test
    public void testScannerRpcService() throws Exception{
        Map<String, Object> map = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService("io.nya.rpc.test.api");
        for(Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey());
        }
    }
    */
    @Test
    public void testScannerRpcReference() throws Exception {
        Map<String, Object> map = RpcReferenceScanner.doScannerWithRpcReferenceFilter("io.nya.rpc.test.scanner");
    }
}
