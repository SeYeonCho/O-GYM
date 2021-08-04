package com.B305.ogym.domain.users.ptStudent;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyRepository extends JpaRepository<Monthly,Integer> {
    @EntityGraph(attributePaths = "ptStudent")
    Monthly findAllByPtStudent(PTStudent ptStudent);

    Monthly findMonthlyByPtStudentAndMonth(PTStudent ptStudent, int month);
}
