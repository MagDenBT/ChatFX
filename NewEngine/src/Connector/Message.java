package Connector;

import UserList.User;

import java.io.Serializable;

public class Message {

    private MsgType type;
    private User user;
    private String textMsg;

    public Message(MsgType type, User user, String textMsg) {
        if(type == MsgType.isAuth)
            this.textMsg = null;
        else if(type == MsgType.isTextMsg)
            this.textMsg = textMsg;

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
}
