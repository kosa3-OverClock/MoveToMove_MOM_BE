package com.kosa.kosafinalprojbackend.domains.kanban.project.model.dto;

import com.kosa.kosafinalprojbackend.domains.kanban.card.domain.dto.CardMemberDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectCardDetailDto {

  private ProjectInCardDto projectInCardDto;
  private List<CardMemberDto> cardMemberList;
}
