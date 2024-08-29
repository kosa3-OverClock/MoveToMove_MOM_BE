package com.kosa.kosafinalprojbackend.domains.kanban.column.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KanbanCardForm {

  private Long kanbanCardId;
  private String title;
  private String content;
  private Long priority;
  private Long taskSize;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endAt;

  List<Long> memberList = new ArrayList<>();
}
