package com.watermelon.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.watermelon.controller.exception.JwtValidationException;
import com.watermelon.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	
	private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    // Use a secure key for HS512
    private static final Key JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    @Value("${wl.jwt.expiration}")
    private int JWT_EXPIRATION;

    /**
     * Tạo chuỗi JWT từ thông tin người dùng.
     *
     * @param userDetails Thông tin người dùng
     * @return Chuỗi JWT
     */
    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date dateExpiration = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(dateExpiration)
                .signWith(JWT_SECRET, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Lấy tên người dùng từ chuỗi JWT.
     *
     * @param token Chuỗi JWT
     * @return Tên người dùng
     */
    public String getUserNameFromJwt(String token) {
//        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Kiểm tra tính hợp lệ của chuỗi JWT.
     *
     * @param authToken Chuỗi JWT cần kiểm tra
     * @return True nếu hợp lệ, ngược lại là False
     * @throws JwtValidationException Ném ngoại lệ nếu có lỗi trong quá trình kiểm tra
     */
    public boolean validateToken(String authToken) {

		try {
			Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			// Xử lý ngoại lệ khi xác thực chữ ký của chuỗi JWT thất bại
			log.error("Invalid JWT signature");
			log.trace("Invalid JWT signature trace", e);
		} catch (MalformedJwtException e) {
			// Xử lý ngoại lệ khi chuỗi JWT không đúng định dạng
			log.error("Malformed JWT token");
			log.trace("Malformed JWT token trace", e);
		} catch (ExpiredJwtException e) {
			// Xử lý ngoại lệ khi chuỗi JWT đã hết hạn
			log.error("Expired JWT token");
			log.trace("Expired JWT token trace", e);
		} catch (UnsupportedJwtException e) {
			// Xử lý ngoại lệ khi chuỗi JWT không được hỗ trợ
			log.error("Unsupported JWT token");
			log.trace("Unsupported JWT token trace", e);
		} catch (IllegalArgumentException e) {
			// Xử lý ngoại lệ khi đối số không hợp lệ
			log.error("JWT token claims string is empty.");
			log.trace("JWT token claims string is empty trace", e);
		}

		return false;
	}

}
