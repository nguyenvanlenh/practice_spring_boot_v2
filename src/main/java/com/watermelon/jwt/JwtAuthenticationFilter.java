package com.watermelon.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	/**
	 * Phương thức để lấy chuỗi JWT từ đối tượng HttpServletRequest.
	 *
	 * @param request Đối tượng HttpServletRequest chứa thông tin từ client.
	 * @return Chuỗi JWT nếu tồn tại trong header "Authorization", ngược lại trả về
	 *         null.
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		// Bước 1: Lấy giá trị của header "Authorization" từ request
		String bearerToken = request.getHeader("Authorization");
//	    String token = bearerToken.split(" ")[1].trim();
		// Bước 2: Hiển thị thông tin token trong console (Có thể xóa trong môi trường
		// sản xuất)
		// Bước 3: Kiểm tra xem header có chứa thông tin JWT không
		if (StringUtils.hasText(bearerToken)
	            && bearerToken.startsWith("Bearer ")
		) {
			// Bước 4: Trả về toàn bộ chuỗi token (có thể xử lý thêm nếu cần)
			return bearerToken.substring(7);
		}
		// Bước 5: Trả về null nếu không tìm thấy chuỗi JWT trong header
		return null;
	}

	private boolean validate(String jwt) {
		boolean result = false;
		if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
			result = true;
		}
		return result;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Bước 1: Lấy chuỗi JWT từ request
		String jwt = getJwtFromRequest(request);

		// Bước 2: Kiểm tra xem chuỗi JWT có giá trị và có hợp lệ không
		if (validate(jwt)) {
			// Bước 3: Lấy tên người dùng từ JWT
			String username = jwtTokenProvider.getUserNameFromJwt(jwt);

			// Bước 4: Tải thông tin người dùng từ UserDetailsService
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// Bước 5: Kiểm tra xem UserDetails có giá trị không
			if (userDetails == null) {
				// Trường hợp UserDetails không có giá trị, có thể xử lý hoặc báo lỗi tùy thuộc
				// vào logic của bạn
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: User details not found");
				return;
			}

			// Bước 6: Tạo đối tượng authentication từ UserDetails
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());

			// Bước 7: Thiết lập chi tiết xác thực từ request
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			// Bước 8: Thiết lập đối tượng authentication vào SecurityContextHolder
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

}
