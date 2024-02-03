package project.goodreads.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import project.goodreads.dto.CommentDto;
import project.goodreads.dto.CommentWithIdDto;
import project.goodreads.models.Comment;
import project.goodreads.repositories.CommentRepository;
import project.goodreads.services.CommentService;
import project.goodreads.services.SearchService;

@RestController
@RequestMapping("/api/comments")
@Transactional
public class CommentRestController {

    private final CommentService commentService;
    private final SearchService<Comment> searchService;

    public CommentRestController(CommentRepository commentRepository, CommentService commentService) {
        this.commentService = commentService;
        this.searchService = new SearchService<>(commentRepository);
    }

    @GetMapping
    public List<CommentWithIdDto> getAll(@RequestParam(required = false) String search) {

        List<Comment> comments = searchService.getItems(search, Comment.class);
        List<CommentWithIdDto> commentsDtos = comments.stream().map(c -> Comment.toCommentWithIdDto(c)).toList();

        return commentsDtos;
    }

    @GetMapping("/{id}")
    public CommentWithIdDto getOne(@PathVariable Long id) {

        var comment = commentService.getComment(id);

        return Comment.toCommentWithIdDto(comment);
    }

    @PostMapping
    public ResponseEntity<CommentWithIdDto> createComment(@Valid @RequestBody CommentDto commentDto) {

        var comment = commentService.addComment(commentDto.getContent(), commentDto.getBookId(),
                commentDto.getUserId());

        return ResponseEntity.status(201).body(Comment.toCommentWithIdDto(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentWithIdDto> updateComment(@PathVariable Long id,
            @Valid @RequestBody CommentDto commentDto) {

        var comment = commentService.getComment(id);
        commentService.updateComment(comment, commentDto.getContent(), commentDto.getBookId(), commentDto.getUserId());

        return ResponseEntity.ok(Comment.toCommentWithIdDto(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {

        commentService.deleteComment(id);

        return ResponseEntity.noContent().build();
    }
}
