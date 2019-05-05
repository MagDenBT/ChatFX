package UserList;

public enum Sex {
    MAN(1), WOMAN(0);
    private int in;

    Sex(int in) {
        this.in = in;
    }

    public int getIn() {
        return in;
    }

    public static Sex findByKey(int i){
        String test;
        if(i==0) return WOMAN;
        else return MAN;
    }
}

