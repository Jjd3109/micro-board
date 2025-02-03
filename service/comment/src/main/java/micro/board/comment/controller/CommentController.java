package micro.board.comment.controller;

import java.util.List;

import org.checkerframework.checker.units.qual.C;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import micro.board.comment.service.CommentService;
import micro.board.comment.service.request.CommentCreateRequest;
import micro.board.comment.service.response.CommentPageResponse;
import micro.board.comment.service.response.CommentResponse;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;


	@GetMapping("/v1/comments/{commentId}")
	public CommentResponse read(
		@PathVariable("commentId") Long commentId
	){
		return commentService.read(commentId);
	}

	// @GetMapping("/v1/comments")
	// public CommentResponse readAll(){
	// 	return commentService.readAll();
	// }

	@PostMapping("/v1/comments")
	public CommentResponse create(
		@RequestBody CommentCreateRequest commentCreateRequest
	){
		return commentService.create(commentCreateRequest);
	}

	@DeleteMapping("/v1/comments/{commentId}")
	public void delete(
		@PathVariable("commentId") Long commentId
	){
		commentService.delete(commentId);
	}

	@GetMapping("/v1/comments")
	public CommentPageResponse readAll(
		@RequestParam("articleId") Long articleId,
		@RequestParam("page") Long page,
		@RequestParam("pageSize") Long pageSize
	){
		return commentService.readAll(articleId, page, pageSize);
	}

	@GetMapping("/v1/comments/infinite-scroll")
	public List<CommentResponse> readAll(
		@RequestParam("articleId") Long articleId,
		@RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
		@RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
		@RequestParam("limit") Long limit
	){

		return commentService.readAll(articleId, lastParentCommentId, lastCommentId, limit);
	}

}
