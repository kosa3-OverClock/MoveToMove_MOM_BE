package com.kosa.kosafinalprojbackend.domains.kanban.comment.service;

import com.kosa.kosafinalprojbackend.domains.kanban.comment.domian.form.CommentForm;
import com.kosa.kosafinalprojbackend.global.error.exception.CustomBaseException;
import com.kosa.kosafinalprojbackend.mybatis.mappers.comment.CommentMapper;
import com.kosa.kosafinalprojbackend.mybatis.mappers.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_COMMENT;
import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.NOT_FOUND_ID;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final MemberMapper memberMapper;
  private final CommentMapper commentMapper;

  // 코멘트 내용 수정
  @Transactional
  public void updateComment(Long memberId, Long commentId, CommentForm commentForm) {

    // 유저 아이디 확인
    if (!memberMapper.existsByMemberId(memberId)) {
      throw new CustomBaseException(NOT_FOUND_ID);
    }

    // 코멘트 확인
    if (!commentMapper.existsByCommentId(commentId)) {
      throw new CustomBaseException(NOT_FOUND_COMMENT);
    }

    commentMapper.updateComment(commentId, commentForm);
  }
}
