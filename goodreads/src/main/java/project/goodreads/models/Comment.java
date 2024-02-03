package project.goodreads.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import project.goodreads.dto.CommentWithIdDto;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public static CommentWithIdDto toCommentWithIdDto(Comment comment) {
        var commentDto = new CommentWithIdDto();

        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setBookId(comment.getBook().getId());
        commentDto.setUserId(comment.getUser().getId());

        return commentDto;
    }

}
