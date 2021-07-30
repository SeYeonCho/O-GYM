package com.B305.ogym.domain.users.ptStudent;

import com.B305.ogym.domain.mappingTable.PTStudentMonthly;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.users.common.Address;
import com.B305.ogym.domain.users.common.Gender;
import com.B305.ogym.domain.users.common.UserBase;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Student")
@Table(name = "pt_student")
@PrimaryKeyJoinColumn(name = "pt_student_id")
public class PTStudent extends UserBase {

//    @Builder
//    public PTStudent(Long id, String password, Address address, String nickname,
//        String tel, Gender gender, String email){
////        super(id, password, address, nickname, tel, gender, email);
//    }

    @Builder.Default
    @OneToMany(mappedBy = "ptStudent", cascade = CascadeType.ALL)
    private List<PTStudentMonthly> ptStudentMonthly = new ArrayList<>(); // 월 별 체중 , 키

    @Builder.Default
    @OneToMany(mappedBy = "ptStudent")
    private List<PTStudentPTTeacher> ptStudentPTTeachers = new ArrayList<>(); // 예약 정보

    public static PTStudent createPTStudent(
        String email, String password, String username, String nickname, Gender gender, String tel,
        Address address

    ) {
        return PTStudent.builder()
            .email(email)
            .password(password)
            .username(username)
            .nickname(nickname)
            .gender(gender)
            .tel(tel)
            .address(address)
            .build();
    }

}
