package project.goodreads.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.exceptions.NullException;
import project.goodreads.models.Comment;
import project.goodreads.repositories.CommentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookService bookService;

    public Comment getComment(Long id) {
        if (id == null)
            throw new NullException("Comment id cannot be null");

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        return comment;
    }

    public Comment addComment(String content, Long bookId, Long userId) {
        var comment = new Comment();

        return updateComment(comment, content, bookId, userId);
    }

    public Comment updateComment(Comment comment, String content, Long bookId, Long userId) {
        comment.setContent(content);
        comment.setBook(bookService.getBook(bookId));
        comment.setUser(userService.getUser(userId));

        commentRepository.save(comment);

        return comment;
    }

    public void deleteComment(Long id) {
        if (id == null)
            throw new NullException("Comment id cannot be null");

        commentRepository.deleteById(id);
    }
}
