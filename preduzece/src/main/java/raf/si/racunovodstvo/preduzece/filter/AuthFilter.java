package raf.si.racunovodstvo.preduzece.filter;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import raf.si.racunovodstvo.preduzece.feign.UserFeignClient;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserFeignClient userFeignClient;

    private static final String[] EXCLUDED_URLS = {"/v3/api-docs", "/swagger-ui.html", "/swagger-ui/"};

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            if (!skipFilter(httpServletRequest.getRequestURL().toString())) {
                String authHeader = httpServletRequest.getHeader("Authorization");

                // baca izuzetak ako nije ispravak token
                ResponseEntity<String> response = userFeignClient.validateToken(authHeader);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (FeignException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
        }

    }

    private Boolean skipFilter(String url) {
        return Arrays.stream(EXCLUDED_URLS).sequential().anyMatch(exUrl -> url.contains(exUrl));
    }
}
