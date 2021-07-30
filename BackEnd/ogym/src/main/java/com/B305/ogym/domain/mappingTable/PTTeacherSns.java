package com.B305.ogym.domain.mappingTable;

import com.B305.ogym.domain.users.ptTeacher.PTTeacher;
import com.B305.ogym.domain.users.ptTeacher.Sns;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pt_teacher_sns")
public class PTTeacherSns {
    @Id
    @GeneratedValue
    @Column(name = "pt_teacher_sns_id")
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pt_teacher_id")
    private PTTeacher ptTeacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_id")
    private Sns sns;

}
