package raf.si.racunovodstvo.preduzece.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public abstract class MySQLContainer extends GenericContainer<MySQLContainer> {

    public MySQLContainer(Network network, int port, String hostName) {
        super("docker.io/bitnami/mysql:8.0");
        withExposedPorts(port);
        withNetwork(network);
        withNetworkAliases(hostName);
        withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName(hostName));
        waitingFor(new HostPortWaitStrategy());

        addEnv("MYSQL_REPLICATION_USER", "repl_user");
        addEnv("MYSQL_REPLICATION_PASSWORD", "test");
        addEnv("MYSQL_DATABASE", "si");
    }
}
