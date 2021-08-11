package com.B305.ogym.service;

import com.B305.ogym.common.jwt.TokenProvider;
import com.B305.ogym.controller.dto.AuthDto;
import com.B305.ogym.controller.dto.AuthDto.TokenDto;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.domain.authority.RefreshToken;
import com.B305.ogym.domain.authority.RefreshTokenRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.UserBase;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster.Refresh;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto authorize(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password); // 인증 전 객체

        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String authorities = getAuthorities(authentication);
        TokenDto tokenDto = tokenProvider.createToken(authentication.getName(),authorities);

        RefreshToken refreshToken = new RefreshToken(email,tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(String requestAccessToken , String requestRefreshToken){
        // Refresh 토큰 검증
        if (!tokenProvider.validateToken(requestRefreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }
        // Access Token
        Authentication authentication = tokenProvider.getAuthentication(requestAccessToken);

        // Email에 해당하는 refreshToken이 존재하는지 체크 (로그아웃 OR 회원탈퇴? -> 회원탈퇴 수정)
        UserBase principal = (UserBase) authentication.getPrincipal();
//        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName()) // 헷갈리네
        RefreshToken refreshToken = refreshTokenRepository.findById(principal.getEmail()) // 헷갈리네
            .orElseThrow(() -> new RuntimeException("로그아웃 되어있습니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(requestRefreshToken)) {
            throw new RuntimeException("토큰이 일치하지 않습니다.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        TokenDto tokenDto = tokenProvider.createToken(principal.getEmail(),authorities);

        refreshToken.updateValue(tokenDto.getRefreshToken());

        return tokenDto;
    }
    public String getAuthorities( Authentication authentication){
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }
}
