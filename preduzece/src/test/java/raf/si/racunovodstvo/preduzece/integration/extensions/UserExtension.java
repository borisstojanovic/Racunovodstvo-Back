package raf.si.racunovodstvo.preduzece.integration.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import raf.si.racunovodstvo.preduzece.integration.containers.UserContainer;
import raf.si.racunovodstvo.preduzece.integration.info.UserInfo;
import raf.si.racunovodstvo.preduzece.integration.network.NetworkHolder;

public class UserExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private final UserContainer userContainer = new UserContainer(NetworkHolder.network(), 8086);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        userContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        userContainer.start();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(UserInfo.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return new UserInfo(userContainer.getFirstMappedPort());
    }
}
