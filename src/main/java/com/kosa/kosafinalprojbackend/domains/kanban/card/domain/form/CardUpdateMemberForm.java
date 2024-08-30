package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardUpdateMemberForm {

  private List<Long> memberIds;
}
