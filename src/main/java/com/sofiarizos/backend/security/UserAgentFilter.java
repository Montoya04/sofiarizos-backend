package com.sofiarizos.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAgentFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String userAgent = req.getHeader("User-Agent");

        if (userAgent == null || userAgent.isEmpty()) {
            ((HttpServletResponse) response).sendError(400, "User-Agent requerido");
            return;
        }

        chain.doFilter(request, response);
    }
}
