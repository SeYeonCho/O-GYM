package com.B305.ogym.domain.mappingTable;

import static com.B305.ogym.domain.mappingTable.QPTStudentPTTeacher.pTStudentPTTeacher;
import static com.B305.ogym.domain.users.ptStudent.QPTStudent.pTStudent;
import static com.B305.ogym.domain.users.ptTeacher.QPTTeacher.pTTeacher;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.springframework.util.StringUtils;


public class PTStudentPTTeacherCustomImpl implements PTStudentPTTeacherCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    Map<String, Expression> check = new HashMap<>();

    public PTStudentPTTeacherCustomImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);

    }

    @Override
    public List<String> getNowReservation(String teacherEmail, String studentEmail, LocalDateTime now) {
        LocalDateTime from = now.minusMinutes(10);
        LocalDateTime to = now.plusMinutes(10);
        PTStudentPTTeacher ptStudentPTTeacher = queryFactory.select(pTStudentPTTeacher)
            .from(pTStudentPTTeacher)
            .join(pTStudentPTTeacher.ptTeacher, pTTeacher)
            .join(pTStudentPTTeacher.ptStudent, pTStudent)
            .where(pTStudentPTTeacher.reservationDate.between(from, to),
                eqStudentEmail(studentEmail),
                eqTeacherEmail(teacherEmail))
            .fetchOne();
        List<String> result = new ArrayList<>();
        if(ptStudentPTTeacher == null){
            return null;
        }
        result.add(ptStudentPTTeacher.getPtTeacher().getNickname());
        result.add(ptStudentPTTeacher.getPtStudent().getNickname());
        return result;
    }
    private BooleanExpression eqTeacherEmail(String teacherEmail) {
        if(teacherEmail == null){
           return null;
        }else{
            return pTTeacher.email.eq(teacherEmail);
        }
    }
    private BooleanExpression eqStudentEmail(String studentEmail) {
        if(studentEmail == null){
            return null;
        }else{
            return pTStudent.email.eq(studentEmail);
        }
    }
}
