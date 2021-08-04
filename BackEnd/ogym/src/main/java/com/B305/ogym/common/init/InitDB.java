package com.B305.ogym.common.init;


import com.B305.ogym.domain.authority.Authority;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.users.common.Address;
import com.B305.ogym.domain.users.common.Gender;
import com.B305.ogym.domain.users.ptStudent.Monthly;
import com.B305.ogym.domain.users.ptStudent.PTStudent;
import com.B305.ogym.domain.users.ptTeacher.Career;
import com.B305.ogym.domain.users.ptTeacher.Certificate;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class InitDB {

    private final InitService initService;

    @Autowired  // 단일 생성자이기 때문에 Annotation 붙이지 않아도 상관없음
    public InitDB(InitService initService) {
        this.initService = initService;
    }

    @PostConstruct
    public void init() {
        // initService 메서드 수행
        initService.putPTStudent();
        initService.putPTTeacher();
        initService.putPTStudentPTTeacher();
        initService.putAuth();

//        initService.putCertificate();

    }

    @Service
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em; // @RequiredArgsConstructor 어노테이션을 선언함으로서
        // final로 선언된 객체에 대한 생성자를 spring이 잡아서 의존성 주입을 해줌.
        // EMF 선언해서 EM을 따로 뽑아낼 필요가 없다!
        // 학생 더미데이터 추가
        private final PasswordEncoder passwordEncoder;

        public void putAuth() {
            em.persist(new Authority("ROLE_USER"));
            em.persist(new Authority("ROLE_PTTEACHER"));
            em.persist(new Authority("ROLE_PTSTUDENT"));
        }

        // 학생 더미데이터 추가
        public void putPTStudent() {

            Address address = Address.builder()
                .zipCode("34153")
                .street("대전광역시 유성구 동서대로 98-39")
                .detailedAddress("싸피 사무국")
                .build();

            List<PTStudentPTTeacher> teachers = new ArrayList<>();
            List<Monthly> monthly = new ArrayList<>();

            PTStudent ptStudent = PTStudent.builder()
                .password(passwordEncoder.encode("ssafy"))
                .nickname("츄")
                .username("김지우")
                .address(address)
                .email("chuu@ssafy.com")
                .tel("010-2021-0721")
                .gender(Gender.WOMAN)
                .monthly(monthly)
                .ptStudentPTTeachers(teachers)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

            ptStudent.addMonthly(1, 160, 44);
            ptStudent.addMonthly(2, 160, 43);
            ptStudent.addMonthly(3, 160, 46);
            ptStudent.addMonthly(4, 160, 42);
            ptStudent.addMonthly(5, 160, 43);
            ptStudent.addMonthly(12, 161, 40);
//            ptStudent.addMonthly(2, 161, 42);    // ptStudent + month 중복 데이터

            em.persist(ptStudent);

        }

        // 트레이너 선생님 더미데이터 추가
        public void putPTTeacher() {
            Address address = Address.builder()
                .zipCode("06220")
                .street("서울시 강남구 테헤란로 212")
                .detailedAddress("싸피 사무국")
                .build();

            Certificate certificate1 = Certificate.builder()
                .name("NSCA")
                .publisher("NSCA KOREA")
                .date(LocalDate.of(2015, 11, 11))
                .build();
            Certificate certificate2 = Certificate.builder()
                .name("NASM-CPT")
                .publisher("NASM KOREA")
                .date(LocalDate.of(2013, 05, 8))
                .build();
            Certificate certificate3 = Certificate.builder()
                .name("NASM-PES")
                .publisher("NASM KOREA")
                .date(LocalDate.of(2015, 03, 21))
                .build();
            Certificate certificate4 = Certificate.builder()
                .name("NASM-CES")
                .publisher("NASM KOREA")
                .date(LocalDate.of(2021, 01, 30))
                .build();

            Set<PTStudentPTTeacher> students = new LinkedHashSet<>();
//            List<Certificate> certificates = new ArrayList<>();
//
//            certificates.add(certificate1);
//            certificates.add(certificate2);
//            certificates.add(certificate3);
//            certificates.add(certificate4);

            LocalDate startDate = LocalDate.of(2018, 02, 11);
            LocalDate endDate = LocalDate.of(2021, 07, 22);

            Career career = Career.builder()
//                .description("피지컬갤러리")
                .role("원장")
                .company("OGYM")
                .startDate(startDate)
                .endDate(endDate)
                .build();

            List<Career> careers = new ArrayList<>();
            careers.add(career);

//            Sns sns = Sns.builder()
//                .platform("facebook")
//                .url("https://www.instagram.com/physical_gallery_egg/?hl=ko")
//                .build();

            PTTeacher ptTeacher = PTTeacher.builder()
                .password(passwordEncoder.encode("ssafy"))
                .nickname("김계란")
                .username("김성식")
                .address(address)
                .email("eggkim@ssafy.com")
                .tel("010-2021-0105")
                .ptStudentPTTeachers(students)
//                .certificates(certificates)
                .major("특공무술/재활/스트레칭/마사지/통증완화")
                .gender(Gender.MAN)
                .description("좋았어. 거짓은 머리털만큼도 없다! 신뢰와 정직으로 모시겠습니다")
//                .sns(sns)
                .price(500000)
                .starRating(4)
                .careers(careers)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

            ptTeacher.addCertificate(certificate1);
            ptTeacher.addCertificate(certificate2);
            ptTeacher.addCertificate(certificate3);
            ptTeacher.addCertificate(certificate4);

            em.persist(ptTeacher);

        }

        // 예약시간 더미데이터 추가
        public void putPTStudentPTTeacher() {
            int s_key = 1;
            int t_key = 2;
            Long s_pk = Long.valueOf(s_key);
            Long t_pk = Long.valueOf(t_key);
            PTStudent ptStudent = em.find(PTStudent.class, s_pk);
            PTTeacher ptTeacher = em.find(PTTeacher.class, t_pk);
            LocalDateTime reservationDate = LocalDateTime.of(2021, 07, 28, 13, 00);
            PTStudentPTTeacher ptStudentptTeacher = PTStudentPTTeacher.builder()
                .ptTeacher(ptTeacher)
                .ptStudent(ptStudent)
                .reservationDate(reservationDate)
                .build();
            em.persist(ptStudentptTeacher);
        }

//        public void putCertificate(){
//            Certificate certificate = Certificate.builder()
//                .name("NSCA")
//                .build();
//            em.persist(certificate);
//        }

    }
}
