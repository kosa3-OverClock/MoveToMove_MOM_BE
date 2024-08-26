package com.kosa.kosafinalprojbackend.domains.member.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {

  private String email;
  private String nickName;
  private String password;
  private String profileUrl;
}
