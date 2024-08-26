package com.kosa.kosafinalprojbackend.domains.member.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private Long memberId;
    private String email;
    private String nickName;
    private String password;
    private String profileUrl;
    private String social;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
