package com.example.albaease.auth.jwt;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;  // 사용자 정보 서비스


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        //http 헤더에서 jwt 추출
        String token = getTokenFromRequest(request);
        logger.info("doFilterInternal  Extracted token: " + token);

        if (token != null && !jwtUtil.isTokenExpired(token)) {
            String userId = jwtUtil.extractUserId(token);;
            System.out.println("doFilterInternal  Extracted userId: " + userId);
            logger.info("doFilterInternal  Extracted userId: " + userId);
            // 사용자 정보 로드 (CustomUserDetailsService 사용)
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserById(Long.valueOf(userId));

            // SecurityContext에 인증된 사용자 설정
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    //요청에서 jwt토큰 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7).trim();  // 공백 제거
            return token;
        }
        return null;
    }
}
