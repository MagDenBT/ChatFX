package UserList;

import java.io.Serializable;

/**
 * Этот класс задумывается, как универсальная оболочка для любого типа сообщения: будь то текст, аудио, видео, логин/пароль
 * и т.п. Тип сообщения определяется по перечислениям в MsgType. Понимаю, что серверу будет сложнее, сначало определить тип сообщения
 * затем распаковать, чем просто получить сообщение нужного типа, но я пока не научился/было лень авторизовывать множество сокетов
 * для одного пользователя
 */

public class Message implements Serializable {

    private MsgType type;
    private User user;
    private String textMsg;
    private boolean signIn;
    private String signUp;

    public Message(String textMsg) {
        type = MsgType.isTextMsg;
        this.textMsg = textMsg;
    }

    public Message(User user, MsgType type ) {
        this.type = type;
        this.user = user;
    }

    public MsgType getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public boolean isSignIn() {
        return signIn;
    }

    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSignUp() {
        return signUp;
    }
}
