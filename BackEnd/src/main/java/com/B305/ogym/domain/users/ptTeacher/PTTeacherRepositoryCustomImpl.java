package com.B305.ogym.domain.users.ptTeacher;


import com.B305.ogym.controller.dto.HealthDto;

import com.B305.ogym.controller.dto.HealthDto.MyStudentsHealthListResponse;
import com.B305.ogym.controller.dto.HealthDto.StudentHealth;
import com.B305.ogym.controller.dto.UserDto.CareerDto;
import com.B305.ogym.controller.dto.UserDto.CertificateDto;
import com.B305.ogym.domain.users.ptStudent.QMonthly;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import static com.B305.ogym.domain.mappingTable.QPTStudentPTTeacher.pTStudentPTTeacher;
import static com.B305.ogym.domain.users.ptTeacher.QPTTeacher.pTTeacher;
import static com.B305.ogym.domain.users.ptStudent.QPTStudent.pTStudent;
import static com.B305.ogym.domain.users.ptStudent.QMonthly.monthly;
import static com.B305.ogym.domain.users.ptTeacher.QCertificate.certificate;
import static com.B305.ogym.domain.users.ptTeacher.QCareer.career;

public class PTTeacherRepositoryCustomImpl implements PTTeacherRepositoryCustom {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;
    Map<String, Expression> check = new HashMap<>();

    public PTTeacherRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);

        check.put("id", pTTeacher.id);
        check.put("email", pTTeacher.email);
        check.put("username", pTTeacher.username);
        check.put("nickname", pTTeacher.nickname);
        check.put("gender", pTTeacher.gender);
        check.put("tel", pTTeacher.tel);
        check.put("address", pTTeacher.address);
        check.put("role", pTTeacher.authority);
        check.put("major", pTTeacher.major);
        check.put("price", pTTeacher.price);
        check.put("description ", pTTeacher.description);
    }

    @Override
    public MyStudentsHealthListResponse findMyStudentsHealth(Long teacherId) {
        List<StudentHealth> result = queryFactory
            .select(Projections.fields(StudentHealth.class,
                pTStudent.username.as("username"),
                pTStudent.nickname.as("nickname"),
                pTStudent.gender.as("gender"),
                pTStudent.profilePicture.pictureAddr.as("profileUrl")

            ))
            .from(pTStudentPTTeacher)
            .join(pTStudentPTTeacher.ptTeacher, pTTeacher)
            .join(pTStudentPTTeacher.ptStudent, pTStudent)
            .where(pTStudentPTTeacher.ptTeacher.id.eq(teacherId))
            .fetch();

        List<String> students = result.stream().map(o -> o.getUsername())
            .collect(Collectors.toList());

        List<Tuple> studentshealth = queryFactory
            .select(monthly.height, monthly.weight, monthly.ptStudent.username)
            .from(monthly)
            .join(monthly.ptStudent, pTStudent)
            .where(monthly.ptStudent.username.in(students))
            .orderBy(monthly.month.asc())
            .fetch();

        result.stream().forEach(o -> {
            studentshealth.stream().forEach(t -> {
                if (t.get(monthly.ptStudent.username).equals(o.getUsername())) {
                    o.addHeight(t.get(monthly.height));
                    o.addWeight(t.get(monthly.weight));
                }
            });
        });

        MyStudentsHealthListResponse myStudentsHealthListResponse = new MyStudentsHealthListResponse();
        myStudentsHealthListResponse.setStudentHealthList(result);
        return myStudentsHealthListResponse;
    }

    @Override
    public Map<String, Object> getInfo(Long teacherId, List<String> req) { // "username" , "id"

        Tuple result = queryFactory
            .select(pTTeacher.id, pTTeacher.email, pTTeacher.username, pTTeacher.nickname,
                pTTeacher.gender, pTTeacher.tel, pTTeacher.address, pTTeacher.authority,
                pTTeacher.major, pTTeacher.price, pTTeacher.description)
            .from(pTTeacher)
            .where(pTTeacher.id.eq(teacherId))
            .fetchOne(); // pTTeahcer의 정보
        Map<String, Object> map = new HashMap<>();

        req.stream().forEach(o -> {
            if ("certificates".equals(o)) {
                List<CertificateDto> certificates = queryFactory
                    .select(Projections.fields(CertificateDto.class,
                        certificate.name.as("name"),
                        certificate.publisher.as("publisher"),
                        certificate.date.as("date")))
                    .from(certificate)
                    .where(certificate.ptTeacher.id.eq(teacherId))
                    .fetch();
                map.put(o, certificates);
            } else if ("careers".equals(o)) {
                List<CareerDto> careers = queryFactory
                    .select(Projections.fields(CareerDto.class,
                        career.company.as("company"),
                        career.startDate.as("startDate"),
                        career.endDate.as("endDate"),
                        career.role.as("role")))
                    .from(career)
                    .where(career.ptTeacher.id.eq(teacherId))
                    .fetch();
                map.put(o, careers);
            } else {
                map.put(o, result.get(check.get(o)));
            }
        });

        return map;
    }
}
