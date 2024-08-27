package com.kosa.kosafinalprojbackend.mybatis.mappers.kanbancolumn;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KanbanColumnMapper {

  // 칸반 컬럼 저장
  void insertKanbanColumns(Map<String, Object> params);
}
