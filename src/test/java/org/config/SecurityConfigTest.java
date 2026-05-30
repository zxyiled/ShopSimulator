package org.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("SecurityConfig")
class SecurityConfigTest {

    @Nested
    @DisplayName("SpaCsrfTokenRequestHandler")
    class SpaCsrfTokenRequestHandlerTest {

        private final SecurityConfig.SpaCsrfTokenRequestHandler handler =
                new SecurityConfig.SpaCsrfTokenRequestHandler();

        @Test
        @DisplayName("resolveCsrfTokenValue returns raw value when X-XSRF-TOKEN header is present")
        void resolveRawFromHeader() {
            var request = new MockHttpServletRequest();
            request.addHeader("X-XSRF-TOKEN", "raw-token-value");
            CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", "server-token");

            String value = handler.resolveCsrfTokenValue(request, token);

            assertEquals("raw-token-value", value);
        }

        @Test
        @DisplayName("resolveCsrfTokenValue delegates to XorCsrfTokenRequestAttributeHandler when no header")
        void resolveDelegatesWhenNoHeader() {
            var request = new MockHttpServletRequest();
            CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", "server-token");

            assertDoesNotThrow(() -> handler.resolveCsrfTokenValue(request, token));
        }
    }

    @Nested
    @DisplayName("CsrfCookieFilter")
    class CsrfCookieFilterTest {

        private final SecurityConfig.CsrfCookieFilter filter =
                new SecurityConfig.CsrfCookieFilter();

        @Test
        @DisplayName("doFilterInternal calls getToken when CsrfToken attribute exists, then continues chain")
        void loadsTokenAndContinuesChain() throws ServletException, IOException {
            var request = mock(HttpServletRequest.class);
            var response = mock(HttpServletResponse.class);
            var chain = mock(FilterChain.class);
            var token = mock(CsrfToken.class);
            when(request.getAttribute(CsrfToken.class.getName())).thenReturn(token);

            filter.doFilterInternal(request, response, chain);

            verify(token).getToken();
            verify(chain).doFilter(request, response);
        }

        @Test
        @DisplayName("doFilterInternal skips getToken when CsrfToken attribute is null, continues chain")
        void skipsTokenWhenNull() throws ServletException, IOException {
            var request = mock(HttpServletRequest.class);
            var response = mock(HttpServletResponse.class);
            var chain = mock(FilterChain.class);
            when(request.getAttribute(CsrfToken.class.getName())).thenReturn(null);

            filter.doFilterInternal(request, response, chain);

            verify(chain).doFilter(request, response);
        }
    }
}
