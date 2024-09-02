package com.kosa.kosafinalprojbackend.domains.kanban.comment.controller;

import com.kosa.kosafinalprojbackend.domains.kanban.comment.domian.form.CommentForm;
import com.kosa.kosafinalprojbackend.domains.kanban.comment.service.CommentService;
import com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode;
import com.kosa.kosafinalprojbackend.global.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.kosa.kosafinalprojbackend.global.error.errorCode.ResponseCode.KANBAN_CARD_MODIFY_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

  private final CommentService commentService;
  
  // 코멘트 내용 수정
  @PatchMapping("/{comment-id}")
  public ResponseEntity<ResponseCode> updateComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable("comment-id") Long commentId,
      @RequestBody CommentForm commentForm) {

    commentService.updateComment(customUserDetails.getId(), commentId, commentForm);

    return ResponseEntity.status(OK).body(KANBAN_CARD_MODIFY_SUCCESS);
  }
}
