package com.B305.ogym.domain.users.ptStudent;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface PTStudentRepositoryCustom {
    Map<String,Object> getInfo(Long studentId, List<String> req);
}
