//package com.benit.helpworx.common.Filter;
//
//import com.benit.helpworx.common.jwt.JwtTokenUtil;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpUtils;
//import java.io.IOException;
//
//@Deprecated
//@NoArgsConstructor
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//
//    private void run(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws Exception{
//        String accessToken = request.getHeader("accessToken");
//        String usrId=null;
//
//        String jwt = jwtTokenUtil.resolveToken(request);
//
//        usrId=jwtTokenUtil.extractUsrIdAccessToken(usrId);
//
//        if (SecurityContextHolder.getContext().getAuthentication() == null) {
//
//        }
//    }
//    @Override
//    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        //preflight 처리
//        if (request.getMethod().equals("OPTIONS")) {
//            filterChain.doFilter(request, response);
//        }
//
//        try {
//            this.run(request,response,filterChain);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        filterChain.doFilter(request, response);
//
//    }
//}
