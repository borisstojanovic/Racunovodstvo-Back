package raf.si.racunovodstvo.nabavka.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

import java.time.Duration;

public class GatewayContainer extends GenericContainer<GatewayContainer> {

    public GatewayContainer(Network network, int port) {
        super("ghcr.io/raf-si-2021/racunovodstvo-gateway:dev");
        withExposedPorts(port);
        withNetwork(network);
        withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName("gateway"));
        withNetworkAliases("gateway");
        waitingFor(new HostPortWaitStrategy());
        withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofMillis(25000)));
        setStartupAttempts(3);

        addEnv("SPRING_PROFILES_ACTIVE", "prod");
        addEnv("SERVER_PORT", port + "");
        addEnv("REGISTRY_HOST", "eureka");
        addEnv("CORS_ORIGINS", "http://localhost:4200");
    }
}
