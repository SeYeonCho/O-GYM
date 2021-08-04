package com.B305.ogym.domain.users.ptTeacher;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PTTeacherRepository extends JpaRepository<PTTeacher,Long>, PTTeacherRepositoryCustom {
    PTTeacher findByEmail(String email);
}
