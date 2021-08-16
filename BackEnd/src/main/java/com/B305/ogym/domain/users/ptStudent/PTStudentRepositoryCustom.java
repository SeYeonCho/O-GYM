package com.B305.ogym.domain.users.ptStudent;

import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PTStudentRepositoryCustom {

    Map<String, Object> getInfo(String studentEmail, List<String> req);

    List<PTStudentPTTeacher> getReservationTime(String stduentEamil);

}
