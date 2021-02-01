package ua.antonfedoruk.sweater.model.dto;

import lombok.Getter;
import lombok.ToString;
import ua.antonfedoruk.sweater.model.Message;
import ua.antonfedoruk.sweater.model.User;
import ua.antonfedoruk.sweater.model.util.MessageHelper;

@Getter
@ToString
public class MessageDto {
    private Long id;
    private String text;
    private String tag;
    private User author;
    private String filename;
    private Long likes;
    private Boolean meLiked;

    public MessageDto(Message message, Long likes, Boolean meLiked) {
        id = message.getId();
        text = message.getText();
        tag = message.getTag();
        author = message.getAuthor();
        filename = message.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }
}
