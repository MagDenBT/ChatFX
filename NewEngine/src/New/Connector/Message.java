package New.Connector;

import New.UserList.User;

public class Message {

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

    public String getSignUp() {
        return signUp;
    }
}
