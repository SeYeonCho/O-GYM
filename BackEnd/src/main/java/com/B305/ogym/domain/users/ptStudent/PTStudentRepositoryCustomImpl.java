package com.B305.ogym.domain.users.ptStudent;

import static com.B305.ogym.domain.users.ptStudent.QMonthly.monthly;
import static com.B305.ogym.domain.users.ptStudent.QPTStudent.pTStudent;

import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class PTStudentRepositoryCustomImpl implements PTStudentRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    Map<String, Expression> check = new HashMap<>();

    public PTStudentRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);

        check.put("id", pTStudent.id);
        check.put("email", pTStudent.email);
        check.put("username", pTStudent.username);
        check.put("nickname", pTStudent.nickname);
        check.put("age", pTStudent.age);
        check.put("gender", pTStudent.gender);
        check.put("tel", pTStudent.tel);
        check.put("address", pTStudent.address);
        check.put("role", pTStudent.authority);
    }

    // 학생 정보를 조회하는 메서드
    @Override
    public Map<String, Object> getInfo(String studentEmail, List<String> req) { // "username" , "id"

        Tuple result = queryFactory
            .select(pTStudent.id, pTStudent.email, pTStudent.username, pTStudent.nickname,
                pTStudent.age,
                pTStudent.gender, pTStudent.tel, pTStudent.address, pTStudent.authority)
            .from(pTStudent)
            .where(pTStudent.email.eq(studentEmail))
            .fetchOne();
        Map<String, Object> map = new HashMap<>();

        List<Tuple> monthlyList = queryFactory.select(monthly.height, monthly.weight)
            .from(monthly)
            .where(monthly.ptStudent.email.eq(studentEmail))
            .orderBy(monthly.month.asc())
            .fetch();

        req.forEach(o -> {
            if ("heights".equals(o)) {

                map.put(o, monthlyList.stream().map(t -> t.get(monthly.height))
                    .collect(Collectors.toList()));
            } else if ("weights".equals(o)) {

                map.put(o, monthlyList.stream().map(t -> t.get(monthly.weight))
                    .collect(Collectors.toList()));
            } else {
                assert result != null;
                map.put(o, result.get(check.get(o)));
            }
        });

        return map;
    }

    // 자신의 예약 정보를 확인하는 메서드
    @Override
    public List<PTStudentPTTeacher> getReservationTime(String studentEmail) {
        return em.createQuery("select pt"
            + " from PTStudentPTTeacher pt"
            + " join fetch pt.ptTeacher t"
            + " join fetch pt.ptStudent s"
            + " where s.email =: studentEmail"
            + " order by pt.reservationDate", PTStudentPTTeacher.class)
            .setParameter("studentEmail", studentEmail)
            .getResultList();

    }

}
