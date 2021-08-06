package com.B305.ogym.domain.users.ptStudent;

import com.B305.ogym.controller.dto.HealthDto;
import com.B305.ogym.controller.dto.HealthDto.GetMyHealthResponse;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.users.common.Address;
import com.B305.ogym.domain.users.common.Gender;
import com.B305.ogym.domain.users.common.UserBase;
import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import java.time.LocalDateTime;
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
    private List<Monthly> monthly = new ArrayList<>(); // 월 별 체중, 키

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

    // 월별 건강정보 추가
    // month가 1~12 사이의 값이 아닐때에는 exception을 던져야한다.
    public void addMonthly(int month, int height, int weight){
        Monthly monthly = Monthly.builder()
            .month(month)
            .height(height)
            .weight(weight)
            .ptStudent(this)
            .build();
        this.monthly.add(monthly);
    }

    // 내 건강정보 조회
    public HealthDto.GetMyHealthResponse getMyHealthResponse(PTStudent ptStudent){
        List<Monthly> monthly = ptStudent.getMonthly();

        List<Integer> heightList = new ArrayList<>();
        List<Integer> weightList = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            heightList.add(0);
            weightList.add(0);
        }

        for(Monthly data : monthly){
            int month = data.getMonth();
            int height = data.getHeight();
            int weight = data.getWeight();

            heightList.set(month-1, height);
            weightList.set(month-1, weight);
        }

        HealthDto.GetMyHealthResponse getMyHealthResponse =
            GetMyHealthResponse.builder()
                .heightList(heightList)
                .weightList(weightList)
                .build();

        return getMyHealthResponse;
    }

    public void deleteMonthly(int month){

    }


    public PTStudentPTTeacher makeReservation(PTTeacher ptTeacher, PTStudent ptStudent, LocalDateTime time) {
        PTStudentPTTeacher ptStudentPTTeacher = PTStudentPTTeacher.builder()
            .ptTeacher(ptTeacher)
            .ptStudent(ptStudent)
            .reservationDate(time)
            .build();
        return ptStudentPTTeacher;
    }
}
