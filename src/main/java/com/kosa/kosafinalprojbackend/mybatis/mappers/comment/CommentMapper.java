package com.kosa.kosafinalprojbackend.mybatis.mappers.comment;

import com.kosa.kosafinalprojbackend.domains.kanban.comment.domian.form.CommentForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper {

  // 코멘트 존재여부
  boolean existsByCommentId(Long commentId);

  // 코멘트 내용 수정
  void updateComment(@Param("commentId") Long commentId,
                     @Param("commentForm") CommentForm commentForm);
}
