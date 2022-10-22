package practice;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 词法分析器
 */
public class SimpleLexer {

    public static void main(String[] args) throws IOException {

        String expression = "int a = 45;";
        SimpleLexer lexer  = new SimpleLexer();

        SimpleTokenReader reader = lexer.processExpression(expression);
        printReader(reader);

        expression = "inta age = 45;";
        reader = lexer.processExpression(expression);
        printReader(reader);

        expression = "in age = 45;";
        reader = lexer.processExpression(expression);
        printReader(reader);

        expression = "age >= 45;";
        reader = lexer.processExpression(expression);
        printReader(reader);

        expression = "age > 45;";
        reader = lexer.processExpression(expression);
        printReader(reader);

        expression = "int  a == 4;";
        reader = lexer.processExpression(expression);
        printReader(reader);

        expression = "2 + 3 * 4 + (45/5)";
        reader = lexer.processExpression(expression);
        printReader(reader);


    }

    private static void printReader(SimpleTokenReader reader) {
        System.out.println();
        for (Token token : reader.tokens) {
            System.out.println(token.toString());
        }
    }

    private  SimpleTokenReader processExpression(String expression) throws IOException {
        List<Token> tokens = new ArrayList<>();
        CharArrayReader charArrayReader = new CharArrayReader(expression.toCharArray());
        Token token = new Token();
        token.init();
        char ch;
        int temp;
        while ((temp = charArrayReader.read()) != -1) {
            ch = (char)temp;
            switch (token.getTokenType()){
                case Initial:
                    token = initToken(ch);
                    break;
                case Id:
                    if (isAlpha(ch) || isDigit(ch)){
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case Id_i:
                    if (ch == 'n'){
                        token.setTokenType(DfaState.Id_in);
                        token.setText(token.getText()+ch);
                    }else if (isAlpha(ch) || isDigit(ch)){
                        token.setTokenType(DfaState.Id);
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case Id_in:
                    if (ch == 't'){
                        token.setTokenType(DfaState.Id_int);
                        token.setText(token.getText()+ch);
                    }else if (isAlpha(ch) || isDigit(ch)){
                        token.setTokenType(DfaState.Id);
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case Id_int:
                    if (isAlpha(ch) || isDigit(ch)){
                        token.setTokenType(DfaState.Id);
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(DfaState.Int);
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case IntLiteral:
                    if (isDigit(ch)){
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case GT:
                    if (ch == '='){
                        token.setTokenType(DfaState.GE);
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case Assignment:
                    if (ch == '='){
                        token.setTokenType(DfaState.EQ);
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case LT:
                    if (ch == '='){
                        token.setTokenType(DfaState.LE);
                        token.setText(token.getText()+ch);
                    }else{
                        tokens.add(token);
                        token = initToken(ch);
                    }
                    break;
                case GE:
                case LE:
                case EQ:
                case Plus:
                case Minus:
                case Star:
                case Slash:
                case Semicolon:
                case LeftParen:
                case RightParen:
                    tokens.add(token);
                    token = initToken(ch);
                    break;
                default:
                    break;
            }

        }

        tokens.add(token);
        return new SimpleTokenReader(tokens);
    }

    private Token initToken(char ch) {
        Token token = new Token();
        token.init();
        String sch = String.valueOf(ch);

        if (isAlpha(ch)){
            if (ch == 'i'){
                token.setTokenType(DfaState.Id_i);
            }else{
                token.setTokenType(DfaState.Id);
            }
            token.setText(sch);
            return token;
        }else if(isDigit(ch)){
            token.setTokenType(DfaState.IntLiteral);
            token.setText(sch);
            return token;
        }

        DfaState state = (DfaState)signalMatchDfaStateMap.get(sch);

        if (state==null){
            return token;
        };

        token.setTokenType(state);
        token.setText(sch);
        return token;

    }



    private boolean isAlpha(char ch) {
        return String.valueOf(ch).matches("[a-zA-Z_]");
    }
    private boolean isDigit(char ch) {
        return String.valueOf(ch).matches("[0-9]");
    }



    public static HashMap<String,Object> signalMatchDfaStateMap = new HashMap<String,Object>(){{
        put("i", DfaState.Id_i);
        put(">", DfaState.GT);
        put("=", DfaState.Assignment);
        put(";", DfaState.Semicolon);
        put("<", DfaState.LT);
        put("+", DfaState.Plus);
        put("-", DfaState.Minus);
        put("*", DfaState.Star);
        put("/", DfaState.Slash);
        put("(", DfaState.LeftParen);
        put(")", DfaState.LeftParen);
    }};


}
