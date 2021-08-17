package com.B305.ogym.domain.users.ptTeacher;

import com.B305.ogym.controller.dto.PTDto.CareerDto;
import com.B305.ogym.controller.dto.PTDto.CertificateDto;
import com.B305.ogym.controller.dto.PTDto.PTTeacherDto;
import com.B305.ogym.controller.dto.PTDto.SnsDto;
import com.B305.ogym.domain.mappingTable.PTStudentPTTeacher;
import com.B305.ogym.domain.users.common.UserBase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Teacher")
@Table(name = "pt_teacher")
@PrimaryKeyJoinColumn(name = "pt_teacher_id")
public class PTTeacher extends UserBase {

    // 평점
    private float starRating;

    // 전문분야
    private String major;

    // 자격증 리스트
    @Builder.Default
    @OneToMany(mappedBy = "ptTeacher", cascade = CascadeType.ALL)
    private List<Certificate> certificates = new ArrayList<>();

    // 가격
    private int price;

    // 자기소개
    private String description;

    // SNS 링크
    @Builder.Default
    @OneToMany(mappedBy = "ptTeacher", cascade = CascadeType.ALL)
    private List<Sns> snss = new ArrayList<>();

    // 경력 리스트
    @Builder.Default
    @OneToMany(mappedBy = "ptTeacher", cascade = CascadeType.ALL)
    private List<Career> careers = new ArrayList<>();

    // 학생 리스트
    @Builder.Default
    @OneToMany(mappedBy = "ptTeacher", cascade = CascadeType.ALL)
    private Set<PTStudentPTTeacher> ptStudentPTTeachers = new LinkedHashSet<>();

    public void addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        if (certificate.getPtTeacher() != this) {
            certificate.setPtTeacher(this);
        }
    }

    public void addCareer(Career career) {
        this.careers.add(career);
        if (career.getPtTeacher() != this) {
            career.setPtTeacher(this);
        }
    }

    public void addSns(Sns sns) {
        this.snss.add(sns);
        if (sns.getPtTeacher() != this) {
            sns.setPtTeacher(this);
        }
    }

    public PTTeacherDto toPTTeacherDto() {

        List<Certificate> certificates = this.getCertificates();
        List<CertificateDto> certificateDtos = new ArrayList<>();

        for (int i = 0; i < certificates.size(); i++) {
            Certificate certificate = certificates.get(i);
            CertificateDto certificateDto = CertificateDto.builder()
                .name(certificate.getName())
                .date(certificate.getDate())
                .publisher(certificate.getPublisher())
                .build();
            certificateDtos.add(certificateDto);
        }

        List<Career> careers = this.getCareers();
        List<CareerDto> careerDtos = new ArrayList<>();

        for (int i = 0; i < careers.size(); i++) {
            Career career = careers.get(i);
            CareerDto careerDto = CareerDto.builder()
                .company(career.getCompany())
                .startDate(career.getStartDate())
                .endDate(career.getEndDate())
                .role(career.getRole())
                .build();
            careerDtos.add(careerDto);
        }

        Set<PTStudentPTTeacher> ptStudentPTTeachers = this.getPtStudentPTTeachers();
        List<PTStudentPTTeacher> ptStudentPTTeachersList = new ArrayList<>(ptStudentPTTeachers);
        List<LocalDateTime> reservations = new ArrayList<>();

        for (int i = 0; i < ptStudentPTTeachersList.size(); i++) {
            PTStudentPTTeacher ptStudentPTTeacher = ptStudentPTTeachersList.get(i);
            reservations.add(ptStudentPTTeacher.getReservationDate());
        }

        Collections.sort(reservations);

        List<Sns> snsList = this.getSnss();
        List<SnsDto> snsDtos = new ArrayList<>();

        for (int i = 0; i < snsList.size(); i++) {
            Sns sns = snsList.get(i);
            SnsDto snsDto = SnsDto.builder()
                .platform(sns.getPlatform())
                .url(sns.getUrl())
                .build();
            snsDtos.add(snsDto);
        }

        PTTeacherDto ptTeacherDto = PTTeacherDto.builder().username(this.getUsername())
            .gender(this.getGender())
            .nickname(this.getNickname())
            .age(this.getAge())
            .tel(this.getTel())
            .email(this.getEmail())
            .profilePicture(this.getProfilePicture())
            .starRating(this.getStarRating())
            .major(this.getMajor())
            .price(this.getPrice())
            .description(this.getDescription())
            .certificates(certificateDtos)
            .careers(careerDtos)
            .reservations(reservations)
            .snsList(snsDtos)
            .build();

        return ptTeacherDto;

    }

}
