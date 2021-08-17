package com.B305.ogym.service;

import com.B305.ogym.common.util.RedisUtil;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.domain.authority.Authority;
import com.B305.ogym.domain.authority.AuthorityRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.MonthlyRepository;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.user.NotValidRequestParamException;
import com.B305.ogym.exception.user.UserDuplicateEmailException;
import com.B305.ogym.exception.user.UserDuplicateNicknameException;
import com.B305.ogym.exception.user.UserNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MonthlyRepository monthlyRepository;
    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;
    private final AuthorityRepository authorityRepository;
    private final RedisUtil redisUtil;

    // 회원가입 메서드
    @CacheEvict(value = "teacherList", allEntries = true)
    @Transactional
    public void signup(UserDto.SaveUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserDuplicateEmailException("이미 가입되어 있는 email입니다.");
        }
        if (userRepository.existsByNickname(userRequest.getNickname())) {
            throw new UserDuplicateNicknameException("이미 가입되어 있는 nickname입니다.");
        }
        if ("ROLE_PTTEACHER".equals(userRequest.getRole())) {
            Authority teacherRole = authorityRepository.findById("ROLE_PTTEACHER").get();

            PTTeacher ptTeacher = userRequest.toPTTeacherEntity();
            ptTeacher.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            ptTeacher.setRole(teacherRole);

            userRequest.getCertificates().forEach(ptTeacher::addCertificate);
            userRequest.getCareers().forEach(ptTeacher::addCareer);
            userRequest.getSnsAddrs().forEach(ptTeacher::addSns);
            ptTeacherRepository.save(ptTeacher);
        } else {
            Authority studentRole = authorityRepository.findById("ROLE_PTSTUDENT").get();
            PTStudent ptStudent = userRequest.toPTStudentEntity();
            ptStudent.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            ptStudent.setRole(studentRole);
            if (userRequest.getMonthlyHeights().size() != 12
                || userRequest.getMonthlyWeights().size() != 12) {
                throw new NotValidRequestParamException("12개월의 health 정보를 입력하지 않았습니다.");
            }
            for (int i = 0; i < 12; i++) {
                ptStudent.addMonthly(i + 1, userRequest.getMonthlyHeights().get(i),
                    userRequest.getMonthlyWeights().get(i));
            }
            ptStudentRepository.save(ptStudent);
        }
    }

    // 회원탈퇴 메서드
    @Transactional
    public void deleteUserBase(String userEmail, String accessToken) {
        redisUtil.setBlackList(accessToken, userEmail, 1800);
        //accessToken 블랙리스트에 저장
        redisUtil.delete(userEmail);
        UserBase user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일이 존재하지 않습니다."));
        userRepository.delete(user);
    }

    // 유저 정보 조회 메서드
    @Transactional
    public Map<String, Object> getUserInfo(String userEmail, List<String> req) {
        UserBase user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("해당하는 이메일이 존재하지 않습니다."));
        if ("ROLE_PTTEACHER".equals(user.getAuthority().getAuthorityName())) {
            return ptTeacherRepository.getInfo(userEmail, req);
        } else {
            return ptStudentRepository.getInfo(userEmail, req);
        }
    }

}
