package com.liferay.sample.m9a28.security.filter.impl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import java.io.*;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

@Component(
        property = {
                "service.ranking:Integer=1",
                "after-filter=Absolute Redirects Filter",
                "servlet-context-name=",
                "servlet-filter-name=Admin IP Filter",
                "url-pattern=/group/guest/~/control_panel/*",
                "url-pattern=/group/control_panel/*",
        },
        service = Filter.class
)
public class IPFilterImpl implements Filter {

    private static final String ALLOWED_IP_FILE = "allowed-ips.txt";
    private final Set<String> allowedIPs = new HashSet<>();


    @Override
    public void init(FilterConfig filterConfig) {

        _log.info("IP Filter init");

        try {
            loadAllowedIPs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        _log.info("IP DoFilter invoked");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIP = httpRequest.getRemoteAddr();
        System.out.println("Client IP: " + clientIP);

        // Allow only specific IPs
        if (!allowedIPs.contains(clientIP)) {
            httpResponse.sendRedirect("/web/guest/404");
//            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Access Denied");
            return;
        }

        chain.doFilter(request, response); // Allow the request
    }

    @Override
    public void destroy() {
        _log.info("IP Filter Destroy invoked");
    }

    private void loadAllowedIPs() throws IOException {

        allowedIPs.clear();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ALLOWED_IP_FILE);
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    allowedIPs.add(line);
//                    System.out.println("Allowed IP: " + line);
                }
                System.out.println("Allowed IPs loaded: " + allowedIPs);
            } catch (IOException e) {
                System.err.println("Error reading allowed IPs file: " + e.getMessage());
            }
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
            IPFilterImpl.class);
}