package raf.si.racunovodstvo.knjizenje.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import raf.si.racunovodstvo.knjizenje.integration.containers.EurekaContainer;
import raf.si.racunovodstvo.knjizenje.integration.containers.PreduzeceContainer;
import raf.si.racunovodstvo.knjizenje.integration.containers.MySQLMasterContainer;
import raf.si.racunovodstvo.knjizenje.integration.containers.MySQLSlaveContainer;
import raf.si.racunovodstvo.knjizenje.integration.containers.RedisContainer;
import raf.si.racunovodstvo.knjizenje.integration.containers.UserContainer;
import raf.si.racunovodstvo.knjizenje.integration.network.NetworkHolder;

@Testcontainers
@ActiveProfiles("test")
class BaseIT {

    protected static final EurekaContainer eurekaContainer = new EurekaContainer(NetworkHolder.network(), 8761);
    protected static final PreduzeceContainer preduzeceContainer = new PreduzeceContainer(NetworkHolder.network(), 8087);
    protected static final UserContainer userContainer = new UserContainer(NetworkHolder.network(), 8086);
    protected static final RedisContainer redisContainer = new RedisContainer(NetworkHolder.network(), 6379);
    protected static final MySQLMasterContainer mySQLMasterContainer = new MySQLMasterContainer(NetworkHolder.network(), 3306);
    protected static final MySQLSlaveContainer mySQLSlaveContainer = new MySQLSlaveContainer(NetworkHolder.network(), 3306, "mysql_slave");

    static {
        redisContainer.withReuse(true);
        mySQLMasterContainer.withReuse(true);
        mySQLSlaveContainer.withReuse(true);
        eurekaContainer.withReuse(true);
        preduzeceContainer.withReuse(true);
        userContainer.withReuse(true);

        eurekaContainer.start();
        mySQLMasterContainer.start();
        mySQLSlaveContainer.start();
        redisContainer.start();
        userContainer.start();
        preduzeceContainer.start();

        HostPortWaitStrategy eurekaWait = new HostPortWaitStrategy();
        eurekaWait.waitUntilReady(eurekaContainer);
        mySQLMasterContainer.waitingFor(eurekaWait);

        HostPortWaitStrategy mysqlMasterWait = new HostPortWaitStrategy();
        mysqlMasterWait.waitUntilReady(mySQLMasterContainer);
        mySQLSlaveContainer.waitingFor(mysqlMasterWait);

        HostPortWaitStrategy mysqlSlaveWait = new HostPortWaitStrategy();
        mysqlSlaveWait.waitUntilReady(mySQLSlaveContainer);
        redisContainer.waitingFor(mysqlSlaveWait);

        HostPortWaitStrategy redisWait = new HostPortWaitStrategy();
        redisWait.waitUntilReady(redisContainer);
        userContainer.waitingFor(redisWait);

        HostPortWaitStrategy userWait = new HostPortWaitStrategy();
        userWait.waitUntilReady(userContainer);
        preduzeceContainer.waitingFor(userWait);
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        String eurekaHost = "http://" + eurekaContainer.getHost() + ":" + eurekaContainer.getMappedPort(8761) + "/eureka";
        String mysqlHost =
            "jdbc:mysql://" + mySQLMasterContainer.getHost() + ":" + mySQLMasterContainer.getMappedPort(3306) + "/si?serverTimezone=UTC";
        String preduzeceExternalHost = "http://" + preduzeceContainer.getHost() + ":" + preduzeceContainer.getMappedPort(8087);
        String userExternalHost = "http://" + userContainer.getHost() + ":" + userContainer.getMappedPort(8086);

        String preduzeceHost = preduzeceContainer.getContainerInfo().getConfig().getHostName();
        String userHost = userContainer.getContainerInfo().getConfig().getHostName();
        registry.add("spring.datasource.url", () -> mysqlHost);
        registry.add("eureka.client.service-url.defaultZone", () -> eurekaHost);
        registry.add("spring.datasource.password", () -> "test");
        registry.add("service.preduzece.url", () -> preduzeceHost);
        registry.add("service.user.url", () -> userHost);
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("spring.cloud.loadbalancer.ribbon.enabled", () -> "false");
        registry.add("spring.cloud.discovery.client.simple.instances." + preduzeceHost + "[0].uri", () -> preduzeceExternalHost);
        registry.add("spring.cloud.discovery.client.simple.instances." + userHost + "[0].uri", () -> userExternalHost);
    }
}
