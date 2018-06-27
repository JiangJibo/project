package com.bob.intergration.concrete.zookeeper;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bob.integrate.zookeeper.ZkClientFactory;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * AccessControl,存储控制测试
 *
 * @author wb-jjb318191
 * @create 2018-06-08 11:33
 */
public class ZookeeperACLTest {

    private ZkClient zkClient;
    private ACL ipAcl;
    private ACL digestAcl;

    @Before
    public void init() throws NoSuchAlgorithmException {
        zkClient = ZkClientFactory.getZkClient();
        ipAcl = new ACL(Perms.READ | Perms.WRITE, new Id("ip", "127.0.0.1"));
        digestAcl = new ACL(Perms.READ | Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("lanboal:123456")));
    }

    @Test
    public void testCreateWithIpACL() {
        zkClient.create("/ipAcl", null, Arrays.asList(ipAcl), CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateWithDigestACL() {
        zkClient.create("/digestAcl", "Digest测试", Arrays.asList(digestAcl), CreateMode.PERSISTENT);
    }

    @Test
    public void testGetDataWithACL() {
        zkClient.addAuthInfo("ip", "127.0.0.1".getBytes());
        zkClient.writeData("/ipAcl", "alc测试");
        System.out.println(zkClient.readData("/ipAcl").toString());
    }

    @Test
    public void testGetDataWithDigestACL() {
        zkClient.addAuthInfo("digest", "lanboal:123456".getBytes());
        String result = zkClient.readData("/digestAcl");
        System.out.println(result);
    }

    @Test
    public void testGetACL() {
        Map.Entry<List<ACL>, Stat> aclEntry = zkClient.getAcl("/digestAcl");
        List<ACL> acls = aclEntry.getKey();
        Stat stat = aclEntry.getValue();
    }

}
