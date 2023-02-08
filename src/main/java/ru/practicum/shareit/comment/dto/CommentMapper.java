package ru.practicum.shareit.comment.dto;

import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto, User author, Item item) {
        Comment comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(commentDto.getCreated());
        comment.setText(commentDto.getText());

        return comment;
    }
}
