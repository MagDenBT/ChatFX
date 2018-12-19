package New.Connector;

import New.UserList.User;

import java.io.Serializable;

public class Message implements Serializable {

    private MsgType type;
    private User user;
    private String textMsg;
    private boolean signIn;
    private String signUp;

    public Message(MsgType type, User user, String textMsg) {
        if(type != MsgType.isAuth)
            this.textMsg = textMsg;

        this.type = type;
        this.user = user;
        this.signIn = false;

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
