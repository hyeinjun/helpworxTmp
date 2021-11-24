package com.benit.helpworx.common.interceptor;

import com.benit.helpworx.common.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception{

        //preflight 처리
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        System.out.println("what");
        String accessToken=request.getHeader("accessToken");

        if(accessToken!=null&&!accessToken.equals("Bearer")){
            accessToken= jwtTokenUtil.resolveToken(request);
            if(jwtTokenUtil.validateAccessToken(accessToken)){
                System.out.println(request.getHeader("Access-Control-Allow-Origin")+" / success:accessToken");
                return true;
            }
        }

        response.getWriter().write("accessToken doesn't validate");
        return false;
    }

}
