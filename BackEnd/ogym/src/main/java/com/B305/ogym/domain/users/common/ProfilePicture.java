package com.B305.ogym.domain.users.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProfilePicture {
    @Column(name = "profile_picture_addr")
    private String pictureAddr; // S3 주소 작명을 어떻게 해야할지.....
}
