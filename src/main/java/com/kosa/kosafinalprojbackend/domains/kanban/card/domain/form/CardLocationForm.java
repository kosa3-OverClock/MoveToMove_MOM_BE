package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardLocationForm {

  private Long kanbanColumnId;
  private Long cardSeq;
}
