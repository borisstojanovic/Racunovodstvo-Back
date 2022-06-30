package raf.si.racunovodstvo.preduzece.integration.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import raf.si.racunovodstvo.preduzece.integration.containers.GatewayContainer;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

public class GatewayExtension implements BeforeAllCallback, AfterAllCallback {

    private final GatewayContainer gatewayContainer = new GatewayContainer(NetworkHolder.network(), 8100);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        gatewayContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        gatewayContainer.start();
    }
}
