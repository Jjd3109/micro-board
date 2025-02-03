package micro.board.comment.service;

import static java.util.function.Predicate.*;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import micro.board.comment.entity.Comment;
import micro.board.comment.repository.CommentRepository;
import micro.board.comment.service.request.CommentCreateRequest;
import micro.board.comment.service.response.CommentPageResponse;
import micro.board.comment.service.response.CommentResponse;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final Snowflake snowflake = new Snowflake();

	@Transactional
	public CommentResponse create(CommentCreateRequest commentCreateRequest){
		Comment parent = findParent(commentCreateRequest);
		
		Comment save = commentRepository.save(
			Comment.create(
				snowflake.nextId(),
				commentCreateRequest.getContent(),
				commentCreateRequest.getArticleId(),
				parent == null ? null : parent.getParentCommentId(),
				commentCreateRequest.getWriterId()
			)
		);
		
		return CommentResponse.from(save);
	}

	private Comment findParent(CommentCreateRequest commentCreateRequest) {

		Long parentCommentId = commentCreateRequest.getParentCommentId();

		if(parentCommentId == null){
			return null;
		}

		return commentRepository.findById(parentCommentId)
			.filter(not(Comment::getDeleted))
			.filter(Comment :: isRoot)
			.orElseThrow();
	}

	public CommentResponse read(Long commentId){
		return CommentResponse.from(commentRepository.findById(commentId).orElseThrow());
	}

	@Transactional
	public void delete(Long commentId) {
		commentRepository.findById(commentId)
			.filter(not(Comment::getDeleted))
			.ifPresent(comment -> {
				if (hasChildren(comment)) {
					comment.delete();
				} else {
					delete(comment);
				}
			});
	}

	private boolean hasChildren(Comment comment) {
		return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
	}

	private void delete(Comment comment) {
		commentRepository.delete(comment);
		if (!comment.isRoot()) {
			commentRepository.findById(comment.getParentCommentId())
				.filter(Comment::getDeleted)
				.filter(not(this::hasChildren))
				.ifPresent(this::delete);
		}
	}

	public CommentPageResponse readAll(Long articleId, Long page, Long pageSize){

		return CommentPageResponse.of(
			commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize).stream()
				.map(CommentResponse::from)
				.toList(),
			commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
		);

	}

	public List<CommentResponse> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit){
		List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
			commentRepository.findAllInfiniteScroll(articleId, limit) :
			commentRepository.findAllInfiniteScroll(articleId, lastParentCommentId, lastCommentId, limit);
		return comments.stream()
			.map(CommentResponse::from)
			.toList();
	}


}
