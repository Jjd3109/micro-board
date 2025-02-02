package micro.board.comment.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import micro.board.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;


}
