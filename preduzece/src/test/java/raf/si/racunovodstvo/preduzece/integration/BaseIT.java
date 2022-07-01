package raf.si.racunovodstvo.preduzece.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import raf.si.racunovodstvo.preduzece.integration.containers.EurekaContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.GatewayContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.KnjizenjeContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.MySQLMasterContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.MySQLSlaveContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.RedisContainer;
import raf.si.racunovodstvo.preduzece.integration.containers.UserContainer;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

@Testcontainers
class BaseIT {

    //@Container
    protected static final EurekaContainer eurekaContainer = new EurekaContainer(NetworkHolder.network(), 8761);
    //@Container
    protected static final KnjizenjeContainer knjizenjeContainer = new KnjizenjeContainer(NetworkHolder.network(), 8085);
    //@Container
    protected static final UserContainer userContainer = new UserContainer(NetworkHolder.network(), 8086);
    //@Container
    protected static final GatewayContainer gatewayContainer = new GatewayContainer(NetworkHolder.network(), 8100);
    //@Container
    protected static final RedisContainer redisContainer = new RedisContainer(NetworkHolder.network(), 6379);
    //@Container
    protected static final MySQLMasterContainer mySQLMasterContainer = new MySQLMasterContainer(NetworkHolder.network(), 3306);
    //@Container
    protected static final MySQLSlaveContainer mySQLSlaveContainer = new MySQLSlaveContainer(NetworkHolder.network(), 3306, "mysql_slave");
    //@Container
    protected static final MySQLSlaveContainer mySQLSlaveContainer1 =
        new MySQLSlaveContainer(NetworkHolder.network(), 3306, "mysql_slave_1");

    static {
        redisContainer.withReuse(true);
        mySQLMasterContainer.withReuse(true);
        mySQLSlaveContainer.withReuse(true);
        mySQLSlaveContainer1.withReuse(true);
        eurekaContainer.withReuse(true);
        gatewayContainer.withReuse(true);
        knjizenjeContainer.withReuse(true);
        userContainer.withReuse(true);

        redisContainer.start();
        mySQLMasterContainer.start();
        mySQLSlaveContainer.start();
        mySQLSlaveContainer1.start();
        eurekaContainer.start();
        gatewayContainer.start();
        userContainer.start();
        knjizenjeContainer.start();

        HostPortWaitStrategy redisWait = new HostPortWaitStrategy();
        redisWait.waitUntilReady(redisContainer);
        mySQLMasterContainer.waitingFor(redisWait);

        HostPortWaitStrategy mysqlMasterWait = new HostPortWaitStrategy();
        mysqlMasterWait.waitUntilReady(mySQLMasterContainer);
        mySQLSlaveContainer1.waitingFor(mysqlMasterWait);
        mySQLSlaveContainer.waitingFor(mysqlMasterWait);
        eurekaContainer.waitingFor(mysqlMasterWait);

        HostPortWaitStrategy eurekaWait = new HostPortWaitStrategy();
        eurekaWait.waitUntilReady(eurekaContainer);
        gatewayContainer.waitingFor(eurekaWait);

        HostPortWaitStrategy gatewayWait = new HostPortWaitStrategy();
        gatewayWait.waitUntilReady(gatewayContainer);
        userContainer.waitingFor(gatewayWait);
        knjizenjeContainer.waitingFor(gatewayWait);
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        String eurekaHost = "http://" + eurekaContainer.getHost() + ":" + eurekaContainer.getMappedPort(8761) + "/eureka";
        String mysqlHost =
            "jdbc:mysql://" + mySQLMasterContainer.getHost() + ":" + mySQLMasterContainer.getMappedPort(3306) + "/si?serverTimezone=UTC";
        String knjizenjeExternalHost = "http://" + knjizenjeContainer.getHost() + ":" + knjizenjeContainer.getMappedPort(8085);

        String knjizenjeHost = knjizenjeContainer.getContainerInfo().getConfig().getHostName();
        registry.add("spring.datasource.url", () -> mysqlHost);
        registry.add("eureka.client.service-url.defaultZone", () -> eurekaHost);
        registry.add("spring.datasource.password", () -> "test");
        registry.add("service.knjizenje.url", () -> knjizenjeHost);
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("spring.cloud.loadbalancer.ribbon.enabled", () -> "false");
        registry.add("spring.cloud.discovery.client.simple.instances." + knjizenjeHost + "[0].uri", () -> knjizenjeExternalHost);
    }
}
