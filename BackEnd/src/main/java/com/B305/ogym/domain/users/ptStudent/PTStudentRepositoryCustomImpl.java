package com.B305.ogym.domain.users.ptStudent;

import static com.B305.ogym.domain.mappingTable.QPTStudentPTTeacher.pTStudentPTTeacher;
import static com.B305.ogym.domain.users.ptStudent.QMonthly.monthly;
import static com.B305.ogym.domain.users.ptStudent.QPTStudent.pTStudent;

import com.B305.ogym.controller.dto.UserDto.CareerDto;
import com.B305.ogym.controller.dto.UserDto.CertificateDto;
import com.B305.ogym.domain.mappingTable.QPTStudentPTTeacher;
import com.B305.ogym.domain.users.ptTeacher.QPTTeacher;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class PTStudentRepositoryCustomImpl implements PTStudentRepositoryCustom{

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
        check.put("gender", pTStudent.gender);
        check.put("tel", pTStudent.tel);
        check.put("address", pTStudent.address);
        check.put("role", pTStudent.authority);
    }

    @Override
    public Map<String, Object> getInfo(Long studentId, List<String> req) { // "username" , "id"

        Tuple result = queryFactory
            .select(pTStudent.id, pTStudent.email, pTStudent.username, pTStudent.nickname,
                pTStudent.gender, pTStudent.tel, pTStudent.address, pTStudent.authority)
            .from(pTStudent)
            .join(monthly)
            .where(pTStudent.id.eq(studentId))
            .fetchOne(); // pTStudent의 정보
        Map<String, Object> map = new HashMap<>();

        List<Tuple> monthlyList = queryFactory.select(monthly.height,monthly.height)
            .from(monthly)
            .where(monthly.ptStudent.id.eq(studentId))
            .orderBy(monthly.month.asc())
            .fetch();

        req.stream().forEach(o -> { // 리펙토링 필요
            if ("heights".equals(o)) {
//                map.put(o,result.get(pTStudent.monthly).stream()
//                    .map(t -> t.getHeight()).collect(Collectors.toList()));
                map.put(o,monthlyList.stream().map(t -> t.get(monthly.height)).collect(Collectors.toList()));
            } else if("weights".equals(o)){
//                map.put(o,result.get(pTStudent.monthly).stream()
//                    .map(t -> t.getWeight()).collect(Collectors.toList()));
                map.put(o,monthlyList.stream().map(t -> t.get(monthly.weight)).collect(Collectors.toList()));
            }
            else {
                map.put(o, result.get(check.get(o)));
            }
        });
        // 엔티티 받아오고
        // find ->
        return map;
    }
}
