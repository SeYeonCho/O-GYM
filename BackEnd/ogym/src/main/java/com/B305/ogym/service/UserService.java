package com.B305.ogym.service;

import com.B305.ogym.common.util.SecurityUtil;
import com.B305.ogym.controller.dto.UserDto;
import com.B305.ogym.controller.dto.UserDto.SaveStudentRequest;
import com.B305.ogym.controller.dto.UserDto.SaveTeacherRequest;
import com.B305.ogym.domain.autority.Authority;
import com.B305.ogym.domain.autority.AuthorityRepository;
import com.B305.ogym.domain.mappingTable.PTStudentMonthly;
import com.B305.ogym.domain.mappingTable.PTStudentMonthlyRepository;
import com.B305.ogym.domain.users.UserRepository;
import com.B305.ogym.domain.users.common.Address;
import com.B305.ogym.domain.users.common.Gender;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptStudent.Monthly;
import com.B305.ogym.domain.users.ptStudent.MonthlyRepository;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import com.B305.ogym.exception.user.UserDuplicateException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    private final PTStudentMonthlyRepository ptStudentMonthlyRepository;
    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepository;
    private final AuthorityRepository authorityRepository;


    @Transactional
    public void signup(SaveStudentRequest StudentRequest) {
        if (userRepository.findOneWithAuthoritiesByEmail(StudentRequest.getEmail())
            != null) {
            throw new UserDuplicateException("이미 가입되어 있는 유저입니다.");
        }

        Authority studentRole = authorityRepository.findById("ROLE_PTSTUDENT").get();
        PTStudent ptStudent = StudentRequest.toEntity();
        ptStudent.setPassword(passwordEncoder.encode(StudentRequest.getPassword()));
        ptStudent.setRole(studentRole);
        ptStudentRepository.save(ptStudent);

        List<Monthly> months = monthlyRepository.findAll(); // 1 ~ 12
        for (int i = 0; i < 12; i++) { // 12개월 다 넣는다.
//            Optional<Monthly> month = monthlyRepository.findById(i + 1); //  미리 리스트로 받아서 사용하자
//            Monthly monthly = month.orElse(new Monthly(i + 1));
            PTStudentMonthly ptStudentMonthly = PTStudentMonthly.createHealth(
                StudentRequest.getMonthlyHeights().get(i),
                StudentRequest.getMonthlyHeights().get(i),
                ptStudent, //  연관관계 편의 메소드
                months.get(i)
            );
            ptStudentMonthlyRepository.save(ptStudentMonthly);
        }

    }

    @Transactional
    public void signup(SaveTeacherRequest teacherRequest) {
        if (userRepository.findOneWithAuthoritiesByEmail(teacherRequest.getEmail())
            != null) {
            throw new UserDuplicateException("이미 가입되어 있는 유저입니다.");
        }

        Authority teacherRole = authorityRepository.findById("ROLE_PTTEACHER").get();

        PTTeacher ptTeacher = teacherRequest.toEntity();
        ptTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
        ptTeacher.setRole(teacherRole);

        teacherRequest.getCertificates().stream().forEach(o -> ptTeacher.addCertificate(o));
        teacherRequest.getCareers().stream().forEach(o -> ptTeacher.addCareer(o));
        ptTeacherRepository.save(ptTeacher);
    }

    @Transactional
    public void signup(UserDto.SaveUserRequest signupReqeust) {
        if (userRepository.findOneWithAuthoritiesByEmail(signupReqeust.getEmail())
            != null) {
            throw new UserDuplicateException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
            .authorityName(signupReqeust.getRole())
            .build();

        Gender gender = Gender.MAN;
        if (signupReqeust.getGender() == 1) {
            gender = Gender.WOMAN;
        }

        Address address = Address.builder()
            .zipCode(signupReqeust.getZipCode())
            .street(signupReqeust.getStreet())
            .detailedAddress(signupReqeust.getDetailedAddress())
            .build();

        if ("ROLE_PTTEACHER".equals(signupReqeust.getRole())) {
            PTTeacher ptTeacher = PTTeacher.builder()
                .email(signupReqeust.getEmail())
                .password(passwordEncoder.encode(signupReqeust.getPassword()))
                .username(signupReqeust.getUsername())
                .nickname(signupReqeust.getNickname())
                .gender(gender)
                .tel(signupReqeust.getTel())
                .address(address)
                .authority(authority)
                .build();

            ptTeacherRepository.save(ptTeacher);

        } else if ("ROLE_PTSTUDENT".equals(signupReqeust.getRole())) {
            PTStudent ptStudent = PTStudent.builder()
                .email(signupReqeust.getEmail())
                .password(passwordEncoder.encode(signupReqeust.getPassword()))
                .username(signupReqeust.getUsername())
                .nickname(signupReqeust.getNickname())
                .gender(gender)
                .tel(signupReqeust.getTel())
                .address(address)
                .authority(authority)
                .build();

            ptStudentRepository.save(ptStudent);
        } else {
            System.out.println("??????? 머함");
        }

    }

    public UserBase getUserWithAuthorities(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    public UserBase getMyUserWithAuthorities() {
        Optional<String> result = SecurityUtil.getCurrentUsername();
        if (result.isEmpty()) {
            return null;
        } else {
            return userRepository.findOneWithAuthoritiesByEmail(result.get());
        }

    }

    @Transactional
    public void deleteUserBase() {
        UserBase userBase = getMyUserWithAuthorities();
        userRepository.delete(userBase);
    }


}
