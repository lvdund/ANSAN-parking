package com.sparking.security;

import com.sparking.repository.BlackListRepo;
import com.sparking.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@AllArgsConstructor
public class JWTFilterUser implements Filter {
    private UserRepo userRepo;
    private JWTService jwtService;
    private BlackListRepo blackListRepo;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("token");
        if (token != null) {
            String phone = jwtService.decode(token);
            if (phone != null && userRepo.findByEmail(phone) != null && blackListRepo.findByToken(token).size() ==0){
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(401);
            }
        }else {
            response.setStatus(401);
        }
    }
}
