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

        String expression = "int a =45;";
        SimpleLexer lexer  = new SimpleLexer();

        SimpleTokenReader reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        expression = "inta age = 45;";
        reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        expression = "in age = 45;";
        reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        expression = "age >= 45;";
        reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        expression = "age > 45;";
        reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        expression = "int  a == 4;";
        reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        expression = "2 + 3 * 4 + (45/5)";
        reader = lexer.processExpression(expression);
        lexer.printReader(reader);


    }

    public  void printReader(SimpleTokenReader reader) {
        System.out.println();
        for (Token token : reader.tokens) {
            System.out.println(token.toString());
        }
    }

    public  SimpleTokenReader processExpression(String expression) throws IOException {
        List<Token> tokens = new ArrayList<>();
        CharArrayReader charArrayReader = new CharArrayReader(expression.toCharArray());
        Token token = new Token();
        DfaState state = DfaState.Initial;
        char ch;
        int temp;
        while ((temp = charArrayReader.read()) != -1) {
            ch = (char)temp;
            switch (state){
                case Initial:
                    state = initDfaState(ch);
                    if (state!=DfaState.Initial){
                        token.setText(token.getText()+ch);
                    }
                    break;
                case Id:
                    if (isAlpha(ch) || isDigit(ch)){
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.Identifier);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case Id_i:
                    if (ch == 'n'){
                        state = DfaState.Id_in;
                        token.setText(token.getText()+ch);
                    }else if (isAlpha(ch) || isDigit(ch)){
                        state = DfaState.Id;
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.Identifier);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case Id_in:
                    if (ch == 't'){
                        state = DfaState.Id_int;
                        token.setText(token.getText()+ch);
                    }else if (isAlpha(ch) || isDigit(ch)){
                        state = DfaState.Id;
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.Identifier);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case Id_int:
                    if (isAlpha(ch) || isDigit(ch)){
                        state = DfaState.Id;
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.Int);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case IntLiteral:
                    if (isDigit(ch)){
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.IntLiteral);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case GT:
                    if (ch == '='){
                        state = DfaState.GE;
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.GT);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case Assignment:
                    if (ch == '='){
                        state = DfaState.EQ;
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.Assignment);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
                    }
                    break;
                case LT:
                    if (ch == '='){
                        state = DfaState.LE;
                        token.setText(token.getText()+ch);
                    }else{
                        token.setTokenType(TokenType.LT);
                        tokens.add(token);
                        token = new Token();
                        state = initDfaState(ch);
                        if (state!=DfaState.Initial){
                            token.setText(token.getText()+ch);
                        }
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
                    token.setTokenType(signalTransDfaStatesToTokenTypeMap.get(state));
                    tokens.add(token);
                    token = new Token();
                    state = initDfaState(ch);
                    if (state!=DfaState.Initial){
                        token.setText(token.getText()+ch);
                    }
                    break;
                default:
                    break;
            }

        }


        token.setTokenType(signalTransDfaStatesToTokenTypeMap.get(state));
        tokens.add(token);
        return new SimpleTokenReader(tokens);
    }

    private DfaState initDfaState(char ch) {
        String sch = String.valueOf(ch);

        if (isAlpha(ch)){
            if (ch == 'i'){
                return DfaState.Id_i;
            }else{
                return DfaState.Id;
            }
        }else if(isDigit(ch)){
            return DfaState.IntLiteral;
        }

        DfaState state = (DfaState)signalMatchDfaStateMap.get(sch);

        if (state==null){
            return DfaState.Initial;
        };


        return state;

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
        put(")", DfaState.RightParen);
    }};

    public static HashMap<DfaState,TokenType> signalTransDfaStatesToTokenTypeMap = new HashMap<DfaState,TokenType>(){{
        put(DfaState.GT,TokenType.GT);
        put(DfaState.GE,TokenType.GE);
        put(DfaState.LT,TokenType.LT);
        put(DfaState.LE,TokenType.LE);
        put(DfaState.EQ,TokenType.EQ);
        put(DfaState.Assignment,TokenType.Assignment);
        put(DfaState.Semicolon,TokenType.Semicolon);
        put(DfaState.Plus,TokenType.Plus);
        put(DfaState.Minus,TokenType.Minus);
        put(DfaState.Star,TokenType.Star);
        put(DfaState.Slash,TokenType.Slash);
        put(DfaState.LeftParen,TokenType.LeftParen);
        put(DfaState.RightParen,TokenType.RightParen);
    }};


}
