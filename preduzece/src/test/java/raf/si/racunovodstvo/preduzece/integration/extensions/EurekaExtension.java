package raf.si.racunovodstvo.preduzece.integration.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import raf.si.racunovodstvo.preduzece.integration.containers.EurekaContainer;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

public class EurekaExtension implements BeforeAllCallback, AfterAllCallback {

    private final EurekaContainer eurekaContainer = new EurekaContainer(NetworkHolder.network(), 8761);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        eurekaContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        eurekaContainer.start();
    }
}
