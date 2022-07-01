package raf.si.racunovodstvo.knjizenje.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

import java.time.Duration;

public abstract class MySQLContainer extends GenericContainer<MySQLContainer> {

    public MySQLContainer(Network network, int port, String hostName) {
        super("docker.io/bitnami/mysql:8.0");
        withExposedPorts(port);
        withNetwork(network);
        withNetworkAliases(hostName);
        withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName(hostName));
        waitingFor(new HostPortWaitStrategy());
        setStartupAttempts(3);
        withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofMillis(15000)));

        addEnv("MYSQL_REPLICATION_USER", "repl_user");
        addEnv("MYSQL_REPLICATION_PASSWORD", "test");
        addEnv("MYSQL_DATABASE", "si");
    }
}
