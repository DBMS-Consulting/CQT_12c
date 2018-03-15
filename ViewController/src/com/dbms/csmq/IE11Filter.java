package com.dbms.csmq;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class IE11Filter implements Filter {
    public IE11Filter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
            //String userAgentStr = ((HttpServletRequest)servletRequest).getHeader("user-agent");
          // user-agent for the IE 11 request:
          // Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0)...
//          if (userAgentStr != null &&  userAgentStr.contains("Trident")) {  
//            ServletRequest fakeIE10 = new FakeIE10Request((HttpServletRequest)servletRequest);
//            filterChain.doFilter(fakeIE10, servletResponse);  
//          } else {  
            filterChain.doFilter(servletRequest, servletResponse);  
          //}  
    }

    @Override
    public void destroy() {
    }
    private class FakeIE10Request extends HttpServletRequestWrapper {  

      public FakeIE10Request(HttpServletRequest request) {
        super(request);
      }
      public String getHeader(String name) {
        HttpServletRequest request = (HttpServletRequest)getRequest();
        if ("user-agent".equalsIgnoreCase(name) && request.getHeader("user-agent").contains("Trident")) {
          return "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)";
        }
        return request.getHeader(name);
      }
    }  
}
