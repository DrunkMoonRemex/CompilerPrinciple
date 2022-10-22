package practice;

/**
 *
 */
public class Token {

    private DfaState tokenType;

    private String text;

    public DfaState getTokenType() {
        return tokenType;
    }

    public void setTokenType(DfaState tokenType) {
        this.tokenType = tokenType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void init(){
        this.tokenType = DfaState.Initial;
        this.setText("");
    }

    @Override
    public String toString() {
        return tokenType + "\t" + text ;

    }
}
