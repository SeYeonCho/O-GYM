package com.B305.ogym.domain.users.ptTeacher;

import com.B305.ogym.controller.dto.HealthDto.MyStudentsHealthListResponse;
import com.B305.ogym.controller.dto.PTDto.SearchDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PTTeacherRepositoryCustom {
    MyStudentsHealthListResponse findMyStudentsHealth(String teacherEmail);
    Map<String,Object> getInfo(Long teacherId, List<String> req);
    Page<PTTeacher> searchAll(SearchDto searchDto, Pageable pageable);
}
