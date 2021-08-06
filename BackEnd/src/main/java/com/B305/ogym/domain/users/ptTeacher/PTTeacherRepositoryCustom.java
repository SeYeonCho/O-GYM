package com.B305.ogym.domain.users.ptTeacher;

import com.B305.ogym.controller.dto.HealthDto.MyStudentsHealthListResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface PTTeacherRepositoryCustom {
    MyStudentsHealthListResponse findMyStudentsHealth(Long teacherId);
    Map<String,Object> getInfo(Long teacherId, List<String> req);
}
