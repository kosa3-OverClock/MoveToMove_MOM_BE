package com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto;

import java.util.List;

import com.kosa.kosafinalprojbackend.domains.kanban.column.model.dto.KanbanColumnInCardDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardDetailDto {

  private KanbanColumnInCardDto kanbanColumnInCard;
  private List<CardMemberDto> cardMemberList;
  private List<CardCommentDto> cardCommentList;
}
