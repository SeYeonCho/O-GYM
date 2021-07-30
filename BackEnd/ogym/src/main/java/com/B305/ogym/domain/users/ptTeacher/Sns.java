package com.B305.ogym.domain.users.ptTeacher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sns {

    @Id
    @GeneratedValue
    @Column(name = "sns_id")
    private Long id;

    private String platform; // 어떤 플랫폼

}
