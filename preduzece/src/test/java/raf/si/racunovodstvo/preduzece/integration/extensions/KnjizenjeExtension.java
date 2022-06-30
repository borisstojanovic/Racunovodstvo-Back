package raf.si.racunovodstvo.preduzece.integration.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import raf.si.racunovodstvo.preduzece.integration.containers.KnjizenjeContainer;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

public class KnjizenjeExtension implements BeforeAllCallback, AfterAllCallback {

    private final KnjizenjeContainer knjizenjeContainer = new KnjizenjeContainer(NetworkHolder.network(), 8085);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        knjizenjeContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        knjizenjeContainer.start();
    }
}
