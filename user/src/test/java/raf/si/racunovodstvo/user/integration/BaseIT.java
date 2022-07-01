package raf.si.racunovodstvo.user.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import raf.si.racunovodstvo.user.integration.containers.MySQLMasterContainer;
import raf.si.racunovodstvo.user.integration.containers.MySQLSlaveContainer;
import raf.si.racunovodstvo.user.integration.containers.RedisContainer;
import raf.si.racunovodstvo.user.integration.network.NetworkHolder;

@Testcontainers
class BaseIT {

    protected static final RedisContainer redisContainer = new RedisContainer(NetworkHolder.network(), 6379);
    protected static final MySQLMasterContainer mySQLMasterContainer = new MySQLMasterContainer(NetworkHolder.network(), 3306);

    static {
        redisContainer.withReuse(true);
        mySQLMasterContainer.withReuse(true);

        mySQLMasterContainer.start();
        redisContainer.start();

        HostPortWaitStrategy mysqlMasterWait = new HostPortWaitStrategy();
        mysqlMasterWait.waitUntilReady(mySQLMasterContainer);
        redisContainer.waitingFor(mysqlMasterWait);
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        String mysqlHost =
            "jdbc:mysql://" + mySQLMasterContainer.getHost() + ":" + mySQLMasterContainer.getMappedPort(3306) + "/si?serverTimezone=UTC";

        registry.add("spring.datasource.url", () -> mysqlHost);
        registry.add("spring.datasource.password", () -> "test");
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("spring.cloud.loadbalancer.ribbon.enabled", () -> "false");
    }
}
