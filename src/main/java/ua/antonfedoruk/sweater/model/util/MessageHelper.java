package ua.antonfedoruk.sweater.model.util;

import ua.antonfedoruk.sweater.model.User;

public abstract class MessageHelper {
    public static  String getAuthorName(User user) {
        return user != null ? user.getUsername() : "<none>";
    }
}
