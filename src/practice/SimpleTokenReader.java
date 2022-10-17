package practice;

import java.util.ArrayList;
import java.util.List;

public class SimpleTokenReader implements TokenReader {

    List<Token> tokens = new ArrayList<>();
    int pos = 0;

    public SimpleTokenReader(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public Token read() {
        if (pos < tokens.size()){
            Token token = tokens.get(pos);
            pos++;
            return token;
        }

        return null;
    }

    @Override
    public Token peel() {
        if (pos < tokens.size()){
            return tokens.get(pos);
        }
        return null;
    }
}
