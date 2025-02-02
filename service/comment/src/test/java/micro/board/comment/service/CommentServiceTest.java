package micro.board.comment.service;

import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLOutput;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import micro.board.comment.entity.Comment;
import micro.board.comment.repository.CommentRepository;
import micro.board.comment.service.request.CommentCreateRequest;
import micro.board.comment.service.response.CommentResponse;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	CommentService commentService;

	@Mock
	CommentRepository commentRepository;

	@Test
	@DisplayName("댓글 생성 테스트")
	void create() {
		// Given
		String content = "댓글 내용";
		Long articleId = 143626643791118368L;
		Long parentCommentId = null;
		Long writerId = 1L;


		CommentCreateRequest request = new CommentCreateRequest(content, articleId, parentCommentId, writerId);
		Comment mockComment = Comment.create(999L, content, articleId, parentCommentId, writerId); // 가짜 Comment 생성

		// Mock이 save() 호출될 때 mockComment 반환하도록 설정
		when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

		// When
		CommentResponse response = commentService.create(request);

		// Then
		System.out.println("response 값 : " + response);
		assertNotNull(response);
		assertEquals(content, response.getContent());
		assertEquals(articleId, response.getArticleId());
		assertEquals(writerId, response.getWriterId());
	}


}