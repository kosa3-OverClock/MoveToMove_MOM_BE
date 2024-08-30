package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardUpdateForm {

  private String updateColumn;
  private Object updateData;
}
