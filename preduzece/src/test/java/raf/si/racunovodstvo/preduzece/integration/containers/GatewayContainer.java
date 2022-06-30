package raf.si.racunovodstvo.preduzece.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class GatewayContainer extends GenericContainer<GatewayContainer> {

    public GatewayContainer(Network network, int port) {
        super("ghcr.io/raf-si-2021/racunovodstvo-gateway:dev");
        withExposedPorts(port);
        withNetwork(network);
        //withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName("gateway"));
        withNetworkAliases("gateway");
        waitingFor(new HostPortWaitStrategy());

        addEnv("SERVER_PORT", port + "");
        addEnv("REGISTRY_HOST", "http://eureka:8761/eureka");
        addEnv("CORS_ORIGINS", "http://localhost:4200");
    }
}
