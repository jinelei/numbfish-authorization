package com.jinelei.iotgenius.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import com.jinelei.iotgenius.common.filter.RepeatHttpServletRequestWrapper;

@SuppressWarnings("unused")
public class RepeatRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            RepeatHttpServletRequestWrapper wrapper = new RepeatHttpServletRequestWrapper(httpRequest);
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
