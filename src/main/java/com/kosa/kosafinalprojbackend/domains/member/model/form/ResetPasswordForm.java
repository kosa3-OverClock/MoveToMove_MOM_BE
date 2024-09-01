package com.kosa.kosafinalprojbackend.domains.member.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordForm {

    private String email;
    private String code;
    private String newPassword;
}
