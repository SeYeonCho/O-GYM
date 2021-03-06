package com.B305.ogym.service;

import com.B305.ogym.common.util.RedisUtil;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.controller.dto.UserDto.CareerDto;
import com.B305.ogym.controller.dto.UserDto.CertificateDto;
import com.B305.ogym.controller.dto.UserDto.ProfileDto;
import com.B305.ogym.controller.dto.UserDto.SnsDto;
import com.B305.ogym.domain.authority.Authority;
import com.B305.ogym.domain.authority.AuthorityRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.ProfilePicture;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.Career;
import com.B305.ogym.domain.users.ptTeacher.Certificate;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.domain.users.ptTeacher.Sns;
import com.B305.ogym.exception.user.AuthorityNotFoundException;
import com.B305.ogym.exception.user.NotValidRequestParamException;
import com.B305.ogym.exception.user.UserDuplicateEmailException;
import com.B305.ogym.exception.user.UserDuplicateNicknameException;
import com.B305.ogym.exception.user.UserNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;
    private final AuthorityRepository authorityRepository;
    private final RedisUtil redisUtil;

    private final String ROLE_PTTEACHER = "ROLE_PTTEACHER";
    private final String ROLE_PTSTUDENT = "ROLE_PTSTUDENT";

    // ???????????? ?????????
    @CacheEvict(value = "teacherList", allEntries = true)
    @Transactional
    public void signup(UserDto.SaveUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserDuplicateEmailException("?????? ???????????? ?????? email?????????.");
        }
        if (userRepository.existsByNickname(userRequest.getNickname())) {
            throw new UserDuplicateNicknameException("?????? ???????????? ?????? nickname?????????.");
        }
        if (ROLE_PTTEACHER.equals(userRequest.getRole())) {
            Authority teacherRole = authorityRepository.findById(ROLE_PTTEACHER).orElseThrow(
                () -> new AuthorityNotFoundException("?????? ????????? ???????????? ????????????.")
            );

            PTTeacher ptTeacher = userRequest.toPTTeacherEntity();
            ptTeacher.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            ptTeacher.setRole(teacherRole);

            userRequest.getCertificates().forEach(ptTeacher::addCertificate);
            userRequest.getCareers().forEach(ptTeacher::addCareer);
            userRequest.getSnsAddrs().forEach(ptTeacher::addSns);
            ptTeacherRepository.save(ptTeacher);
        } else {
            Authority studentRole = authorityRepository.findById(ROLE_PTSTUDENT).orElseThrow(
                () -> new AuthorityNotFoundException("?????? ????????? ???????????? ????????????.")
            );

            PTStudent ptStudent = userRequest.toPTStudentEntity();
            ptStudent.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            ptStudent.setRole(studentRole);
            if (userRequest.getMonthlyHeights().size() != 12
                || userRequest.getMonthlyWeights().size() != 12) {
                throw new NotValidRequestParamException("12????????? health ????????? ???????????? ???????????????.");
            }
            for (int i = 0; i < 12; i++) {
                ptStudent.addMonthly(i + 1, userRequest.getMonthlyHeights().get(i),
                    userRequest.getMonthlyWeights().get(i));
            }
            ptStudentRepository.save(ptStudent);
        }
    }

    // ???????????? ?????????
    @Transactional
    public void deleteUserBase(String userEmail, String accessToken) {
        redisUtil.setBlackList(accessToken, userEmail, 1800);
        //accessToken ?????????????????? ??????
        redisUtil.delete(userEmail);
        UserBase user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("???????????? ???????????? ???????????? ????????????."));
        userRepository.delete(user);
    }

    // ?????? ?????? ?????? ?????????
    @Transactional
    public Map<String, Object> getUserInfo(String userEmail, List<String> req) {
        UserBase user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("???????????? ???????????? ???????????? ????????????."));
        Map<String, Object> map = new HashMap<>();
        if (ROLE_PTTEACHER.equals(user.getAuthority().getAuthorityName())) {
            PTTeacher teacher = ptTeacherRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("???????????? ???????????? ???????????? ????????????."));
            req.stream().forEach(o -> {
                if ("snss".equals(o)) {
                    List<SnsDto> snssDto = new ArrayList<>();
                    List<Sns> snss = teacher.getSnss();
                    snss.stream().forEach(sns -> {
                        snssDto.add(
                            SnsDto.builder()
                                .platform(sns.getPlatform())
                                .url(sns.getUrl())
                                .build()
                        );
                    });
                    map.put(o, snssDto);
                } else if ("careers".equals(o)) {
                    List<CareerDto> careersDto = new ArrayList<>();
                    List<Career> careers = teacher.getCareers();
                    careers.stream().forEach(career -> {
                        careersDto.add(
                            CareerDto.builder()
                                .company(career.getCompany())
                                .startDate(career.getStartDate())
                                .endDate(career.getEndDate())
                                .role(career.getRole())
                                .build());
                    });
                    map.put(o, careersDto);
                } else if ("certificates".equals(o)) {
                    List<CertificateDto> certificatesDto = new ArrayList<>();
                    List<Certificate> certificates = teacher.getCertificates();
                    certificates.stream().forEach(certificate -> {
                            certificatesDto.add(
                                CertificateDto.builder()
                                    .name(certificate.getName())
                                    .date(certificate.getDate())
                                    .publisher(certificate.getPublisher())
                                    .build());
                        }
                    );
                    map.put(o, certificatesDto);
                } else {
                    map.put(o, teacher.getInfo(o));
                }
            });
            return map;
        } else {
            PTStudent student = ptStudentRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("???????????? ???????????? ???????????? ????????????."));
            req.stream().forEach(o -> {
                map.put(o, student.getInfo(o));
            });
            return map;
        }
    }

    // ????????? ?????? ??????
    @Transactional
    public void putProfile(String userEmail, ProfileDto profileDto) {
        UserBase user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException("???????????? ?????? ???????????????."));
        user.setProfilePicture(ProfilePicture.builder().pictureAddr(profileDto.getUrl()).build());
        userRepository.save(user);
    }
}
