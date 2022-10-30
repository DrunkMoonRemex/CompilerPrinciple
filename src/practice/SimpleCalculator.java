package practice;

public class SimpleCalculator {
    public static void main(String[] args) throws Exception {

        SimpleCalculator calculator = new SimpleCalculator();
        String expression = "int a = 45+(3+2)*4;";
        SimpleLexer lexer  = new SimpleLexer();

        SimpleTokenReader reader = lexer.processExpression(expression);
        lexer.printReader(reader);

        SimpleASTNode node = calculator.intDeclare(reader);
        calculator.dumpAST(node,"");


        String exp = "45+(3+2)*5";
        int num = calculator.calculate(exp);
        System.out.println(num);

        exp = "2+";
        num = calculator.calculate(exp);
        System.out.println(num);

        exp = "2+3+4";
        num = calculator.calculate(exp);
        System.out.println(num);

        exp = "2+3-4*5";
        num = calculator.calculate(exp);
        System.out.println(num);

    }

    private  int calculate(String expression) {
        int res = 0;
        try{
        SimpleLexer lexer = new SimpleLexer();
        SimpleCalculator calculator = new SimpleCalculator();
        SimpleTokenReader reader = lexer.processExpression(expression);
        SimpleASTNode node = calculator.additive(reader);
        calculator.dumpAST(node,"");

         res = getResult(node);
        }catch (Exception e){
            System.out.println(e);
        }
        return res;
    }

    private int getResult(SimpleASTNode node) {

        switch (node.getType()){
            case Additive:
                SimpleASTNode child1 = (SimpleASTNode) node.children.get(0);
                SimpleASTNode child2 = (SimpleASTNode) node.children.get(1);
                int leftResult = getResult(child1);
                int rightResult = getResult(child2);
                if ("+".equals(node.getText())){
                    return leftResult+rightResult;
                }else if ("-".equals(node.getText())){
                    return leftResult-rightResult;
                }
                break;
            case Multiplicative:
                SimpleASTNode child3 = (SimpleASTNode) node.children.get(0);
                SimpleASTNode child4 = (SimpleASTNode) node.children.get(1);
                int leftRes = getResult(child3);
                int rightRes = getResult(child4);
                if ("*".equals(node.getText())){
                    return leftRes*rightRes;
                }else if ("/".equals(node.getText())){
                    return leftRes/rightRes;
                }
                break;
            case IntLiteral:
                return Integer.parseInt(node.getText());
            default:
                break;

        }


        return 0;

    }

    /**
     * 整型变量声明语句，如：
     * int a;
     * int b = 2*3;
     *
     * @return
     * @throws Exception
     */
    private SimpleASTNode intDeclare(TokenReader tokens) throws Exception {
        SimpleASTNode node = new SimpleASTNode();
        Token token = tokens.peek();
        if (token!= null && token.getTokenType() == TokenType.Int){
            tokens.read();
            token = tokens.peek();
            if (token != null && token.getTokenType() == TokenType.Identifier){
                node = new SimpleASTNode(ASTNodeType.Identifier,token.getText());
                tokens.read();
                token = tokens.peek();
                if (token != null && token.getTokenType() == TokenType.Assignment){
                    tokens.read();
                    SimpleASTNode child = additive(tokens);
                    if (child != null){
                        node.addChild(child);
                    }else{
                        throw new Exception("invalid expression");
                    }
                }

                token = tokens.peek();
                if(token != null && token.getTokenType() == TokenType.Semicolon){
                    tokens.read();
                }else{
                    throw new Exception("invalid statement,excepted semicolon");
                }


            }else{
                throw new Exception("error identifier");
            }


        }


        return node;
    }

    private SimpleASTNode additive(TokenReader tokens) throws Exception {
        SimpleASTNode child1 = multiplicative(tokens);
        SimpleASTNode node = child1;

        if (child1 == null){
            return null;
        }

        Token token = tokens.peek();
        if (token != null && (token.getTokenType() == TokenType.Plus || token.getTokenType() == TokenType.Minus)){
            node = new SimpleASTNode(ASTNodeType.Additive,token.getText());
            node.addChild(child1);
            tokens.read();
            SimpleASTNode child2 = additive(tokens);
            if (child2 == null){
                throw new Exception("invalid additive expression,the right part is null");
            }
            node.addChild(child2);
        }

        return node;
    }

    private SimpleASTNode multiplicative(TokenReader tokens) throws Exception {
        SimpleASTNode child1 = primary(tokens);
        SimpleASTNode node = child1;
        if (child1 == null){
            return null;
        }

        Token token = tokens.peek();
        if (token != null && (token.getTokenType() == TokenType.Star || token.getTokenType() == TokenType.Slash)){
            node = new SimpleASTNode(ASTNodeType.Multiplicative,token.getText());
            node.addChild(child1);
            tokens.read();
            SimpleASTNode child2 = multiplicative(tokens);
            if (child2 == null){
                throw new Exception("invalid multiplicative expression,the right part is null");
            }
            node.addChild(child2);
        }

        return node;
    }

    private SimpleASTNode primary(TokenReader tokens) throws Exception {
        SimpleASTNode node = new SimpleASTNode();
        Token token = tokens.peek();

        if (token == null){
            return null;
        }

        if (token.getTokenType() == TokenType.Identifier){
            node = new SimpleASTNode(ASTNodeType.Identifier,token.getText());
            tokens.read();
            return node;
        }

        if (token.getTokenType() == TokenType.IntLiteral){
            node = new SimpleASTNode(ASTNodeType.IntLiteral,token.getText());
            tokens.read();
            return node;
        }

        if (token.getTokenType() == TokenType.LeftParen){
            tokens.read();
            node = additive(tokens);
            if (node == null){
                throw new Exception("error expression,there have nothing on the LeftParen's right");
            }
            if (tokens.peek().getTokenType() == TokenType.RightParen){
                tokens.read();
                return node;
            }else{
                throw new Exception("error expresion ,the last part should be a  rightParen");
            }

        }


        return null;
    }


    private void dumpAST(ASTNode node, String indent) {
        System.out.println();
        System.out.println(indent + node.getType() + " " + node.getText());
        for (ASTNode child : node.getChildren()) {
            dumpAST(child, indent + "\t");
        }
    }
}
