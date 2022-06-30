package raf.si.racunovodstvo.preduzece.integration.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import raf.si.racunovodstvo.preduzece.integration.containers.RedisContainer;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

public class RedisExtension implements BeforeAllCallback, AfterAllCallback {

    private final RedisContainer redisContainer = new RedisContainer(NetworkHolder.network(), 6379);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        redisContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        redisContainer.start();
    }
}
