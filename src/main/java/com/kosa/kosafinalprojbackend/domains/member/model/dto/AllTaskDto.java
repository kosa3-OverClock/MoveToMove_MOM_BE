package com.kosa.kosafinalprojbackend.domains.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllTaskDto {

  private Long totalTask;
  private Long endTask;
  private Long remainTask;
}
