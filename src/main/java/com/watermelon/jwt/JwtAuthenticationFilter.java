package com.watermelon.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.watermelon.controller.exception.JwtValidationException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserDetailsService userDetailsService;

	 /**
     * Lấy chuỗi JWT từ header của request.
     *
     * @param request HttpServletRequest
     * @return Chuỗi JWT
     */
	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		System.out.println("token: "+bearerToken);
		// kiem ta thong tin header co chua thong tin jwt

		if (StringUtils.hasText(bearerToken)
//				&& bearerToken.startsWith("Bearer ")
				) {
//			return bearerToken.substring(7);
			return bearerToken;
		}
		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    try {
	        String jwt = getJwtFromRequest(request);
	        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
	            // Lay username tu JWT
	            String username = jwtTokenProvider.getUserNameFromJwt(jwt);
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            if (userDetails != null) {
	                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                if (authentication != null) {
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	            }
	        }
	    }
	    catch (Exception e) {
	        // Xử lý lỗi xác thực JWT
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
	        return;
	    }
	    filterChain.doFilter(request, response);
	}


}
