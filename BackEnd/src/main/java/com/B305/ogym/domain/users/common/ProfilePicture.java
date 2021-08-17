package com.B305.ogym.domain.users.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProfilePicture {

    @Column(name = "profile_picture_addr")
    private String pictureAddr; // S3 주소
}
