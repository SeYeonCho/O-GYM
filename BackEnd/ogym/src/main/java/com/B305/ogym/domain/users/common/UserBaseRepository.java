package com.B305.ogym.domain.users.common;

import com.B305.ogym.domain.users.ptStudent.PTStudentRepository;
import com.B305.ogym.domain.users.ptTeacher.PTTeacherRepository;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserBaseRepository {

    private final EntityManager em;
    private final PTTeacherRepository ptTeacherRepository;
    private final PTStudentRepository ptStudentRepositoryRepository;


    public UserBase login(long userId, String role){
        if(role == "ROLE_PTTEAHCER"){
            return ptTeacherRepository.findById(userId).get();
        }else if(role == "ROLE_PTSTUDENT"){
            return ptStudentRepositoryRepository.findById(userId).get();
        }else{
            return null;
        }
    }
}
