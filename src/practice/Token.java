package practice;

/**
 *
 */
public class Token {

    private TokenType tokenType;

    private String text = "";

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return tokenType + "\t" + text ;
    }
}
