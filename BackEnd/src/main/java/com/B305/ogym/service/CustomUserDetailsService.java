package com.B305.ogym.service;

import com.B305.ogym.exception.user.UserNotFoundException;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.UserBase;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDetailsService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
//        return userRepository.findOneWithAuthoritiesByEmail(email)
//            .map(userBase -> createUser(email, userBase))
//            .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
        UserBase result;
        result = userRepository.findOneWithAuthoritiesByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일이 존재하지 않습니다."));
        if (result == null) {
            throw new UserNotFoundException("존재하지 않는 사용자입니다.");
        }
        System.out.println("loadUserByUsername");
        return createUser(email, result);
    }

    private org.springframework.security.core.userdetails.User createUser(String username,
        UserBase userBase) {

//        List<GrantedAuthority> grantedAuthorities = userBase.getAuthority().stream()
//            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
//            .collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority
            .add(new SimpleGrantedAuthority(userBase.getAuthority().getAuthorityName()));
        return new org.springframework.security.core.userdetails.User(userBase.getEmail(),
            userBase.getPassword(),
            grantedAuthority);
    }
}
