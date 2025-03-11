package com.jinelei.iotgenius.auth.client.configuration.filter;

import com.jinelei.iotgenius.auth.client.configuration.component.ReReadableHttpServletRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class ReReadableRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            ReReadableHttpServletRequestWrapper wrapper = new ReReadableHttpServletRequestWrapper(httpRequest);
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
