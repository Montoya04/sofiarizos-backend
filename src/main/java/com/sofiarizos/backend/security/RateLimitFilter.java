package com.sofiarizos.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitFilter implements Filter {

    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW_MS = 10000;

    private final Map<String, UserRequests> requestMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String ip = req.getRemoteAddr();

        long now = Instant.now().toEpochMilli();
        UserRequests userReq = requestMap.computeIfAbsent(ip, k -> new UserRequests(now));

        synchronized (userReq) {
            if (now - userReq.startTime > TIME_WINDOW_MS) {
                userReq.startTime = now;
                userReq.count = 0;
            }

            userReq.count++;
            if (userReq.count > MAX_REQUESTS) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(429);
                res.getWriter().write("Demasiadas solicitudes. Intenta de nuevo en unos segundos.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private static class UserRequests {
        long startTime;
        int count;

        UserRequests(long startTime) {
            this.startTime = startTime;
            this.count = 0;
        }
    }
}
