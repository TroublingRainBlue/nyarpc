package io.nya.rpc.registry.zookeeper.curator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CuratorTest {

    private CuratorFramework client;
    @Before
    public void curatorConnection() throws Exception {
        client = CuratorFrameworkFactory.
                builder().connectString("127.0.0.1:2181").
                sessionTimeoutMs(4000).retryPolicy(new
                        ExponentialBackoffRetry(1000,3)).
                namespace("nyatest").build();
        client.start();
    }

    @Test
    public void curatorCreate() throws Exception {
        // 没有指定数据默认将ip作为数据存储
        String path = client.create().forPath("/app1");
        System.out.println(path);

        // 指定数据，默认持久型
        path = client.create().forPath("/app2", "nya".getBytes(StandardCharsets.UTF_8));
        System.out.println(path);

        // 设置节点类型
        path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app3");
        System.out.println(path);

        // 创建多级节点
        path = client.create().creatingParentsIfNeeded().forPath("/app4/p1");
        System.out.println(path);
    }

    @Test
    public void curatorGet() throws Exception {
        // get 命令
        byte[] data = client.getData().forPath("/app2");
        System.out.println(new String(data));

        // 查询子节点 ls 命令
        List<String> paths = client.getChildren().forPath("/");
        System.out.println(paths);

        // 查询状态信息 ls -s 命令
        Stat stat = new Stat();
        data = client.getData().storingStatIn(stat).forPath("/app1");
        System.out.println(new String(data));
        System.out.println(stat.toString());
    }

    @Test
    public void curatorSet() throws Exception {
        // set 命令
        client.setData().forPath("/", "hello".getBytes(StandardCharsets.UTF_8));

        // 根据版本号修改
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/app1");
        int version = stat.getVersion();
        client.setData().withVersion(version).forPath("/app1", "2025".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void curatorDelete() throws Exception {
        // delete 命令
        client.delete().forPath("/app1");
        // 删除多级节点
        client.delete().deletingChildrenIfNeeded().forPath("/app4");
        // 确保删除成功
        client.delete().guaranteed().forPath("/app2");
        // 删除回调
        client.delete().inBackground(new BackgroundCallback() {

            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("已删除");
            }
        }).forPath("/");
    }

    @After
    public void close() {
        client.close();
    }
}

