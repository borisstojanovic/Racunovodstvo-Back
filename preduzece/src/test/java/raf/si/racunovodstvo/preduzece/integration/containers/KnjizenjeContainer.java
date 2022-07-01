package raf.si.racunovodstvo.preduzece.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class KnjizenjeContainer extends GenericContainer<KnjizenjeContainer> {

    public KnjizenjeContainer(Network network, int port) {
        super("ghcr.io/raf-si-2021/racunovodstvo-knjizenje:dev");
        withExposedPorts(port);
        withNetwork(network);
        withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName("knjizenje"));
        withNetworkAliases("knjizenje");
        waitingFor(new HostPortWaitStrategy());

        addEnv("SERVER_PORT", port + "");
        addEnv("SPRING_PROFILES_ACTIVE", "prod");
        addEnv("EUREKA_INSTANCE_PREFERIPADDRESS", "true");
        addEnv("MYSQL_MASTER_HOST", "jdbc:mysql://mysql_db:3306/si?serverTimezone=UTC");
        addEnv("MYSQL_SLAVE0_HOST", "jdbc:mysql://mysql_slave:3306/si?serverTimezone=UTC");
        addEnv("MYSQL_SLAVE1_HOST", "jdbc:mysql://mysql_slave_1:3306/si?serverTimezone=UTC");
        addEnv("MYSQL_MASTER_ROOT_PASSWORD", "test");
        addEnv("MYSQL_SLAVE_ROOT_PASSWORD", "test");
        addEnv("REDIS_HOST", "redis");
        addEnv("REDIS_PORT", "6379");
        addEnv("REGISTRY_HOST", "eureka");
    }
}
