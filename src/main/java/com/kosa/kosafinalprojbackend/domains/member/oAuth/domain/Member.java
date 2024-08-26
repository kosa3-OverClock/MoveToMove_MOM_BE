package com.kosa.kosafinalprojbackend.domains.member.oAuth.domain;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Member {

    private int memberId;
    private String email;
    private String nickName;
    private String password;
    private String profileUrl;
    private ProviderType providerType;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
