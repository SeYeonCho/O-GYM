package com.B305.ogym.domain.authority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    private String userEmail; // key value로 넣고 싶었지만 하이버네이트에서 백틱 처리 안함
                                // Principal(User Email) // 추후 레디스를 생각하니 매핑하지 않는다.
    private String value;

    public RefreshToken updateValue(String token){
        this.value = token;
        return this;
    }

    @Builder
    public RefreshToken(String userEmail, String value) {
        this.userEmail = userEmail;
        this.value = value;
    }


}
