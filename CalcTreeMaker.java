import java.util.List;

public class CalcTreeMaker 
{
    // Trees should only be able to have subtrees of a lower priority (or a subtree with parenthesis)
    private enum OperationPriority
    {
        LOW, // Addition and subtraction
        MIDDLE, // Multiplication and division
        HIGH // Equality
    }

    public static CalcTree createCalcTree(List<Token> tokens)
    {
        return createCalcTree(tokens, 0, tokens.size(), null, OperationPriority.LOW).TREE;
    }

    private static ReturnObject createCalcTree(List<Token> tokens, int startIndex, int endIndex, CalcTree leftChild, OperationPriority operationPriority)
    {
        if (startIndex == endIndex)
        {
            return new ReturnObject(leftChild, startIndex);
        }

        CalcTree currentTree = new CalcTree(tokens.get(startIndex), leftChild);
        startIndex++;

        switch (currentTree.token.Type())
        {
            case NUMBER:
            {
                return createCalcTree(tokens, startIndex, endIndex, currentTree, operationPriority);
            }
    
            case LEFT_PARENTHESES:
            {
                int parenthesesCount = 1;
                for(int i = startIndex; i < endIndex; i++)
                {
                    Token.TokenType type = tokens.get(i).Type();
                    if (type == Token.TokenType.LEFT_PARENTHESES)
                    {
                        parenthesesCount++;
                    }
                    if (type == Token.TokenType.RIGHT_PARENTHESES)
                    {
                        parenthesesCount--;
                    }
                    if (type == Token.TokenType.EQUALS)
                    {
                        throw new SyntaxException("Right parentheses expected but not found left of equals sign!");
                    }

                    if (parenthesesCount == 0)
                    {
                        CalcTree parenthesesTree = createCalcTree(tokens, startIndex, i, null, OperationPriority.LOW).TREE;
                        parenthesesTree.parentheses = true;
                        return createCalcTree(tokens, i + 1, endIndex, parenthesesTree, operationPriority);
                    }
                }
                throw new SyntaxException("Right parentheses expected but not found!");
            }
    
            case RIGHT_PARENTHESES:
            {
                throw new SyntaxException("Right parentheses found before a left parentheses!");
            }
    
            case ADDITION:
            case SUBSTRACTION:
            {
                if (leftChild == null)
                {
                    throw new SyntaxException("Left operand not found for the operator!");
                }

                if (operationPriority == OperationPriority.HIGH)
                {
                    return new ReturnObject(leftChild, startIndex - 1);
                }

                ReturnObject result = createCalcTree(tokens, startIndex, endIndex, null, OperationPriority.MIDDLE);
                currentTree.rightChild = result.TREE;
                return createCalcTree(tokens, result.STARTINDEX, endIndex, currentTree, operationPriority);
            }
            case MULTIPLICATION:
            case DIVISION:
            {
                if (leftChild == null)
                {
                    throw new SyntaxException("Left operand not found for operator!");
                }

                ReturnObject result = createCalcTree(tokens, startIndex, endIndex, null, OperationPriority.HIGH);
                currentTree.rightChild = result.TREE;
                return createCalcTree(tokens, result.STARTINDEX, endIndex, currentTree, operationPriority);
            }
            case EQUALS:
            if (operationPriority != OperationPriority.LOW)
            {
                return new ReturnObject(leftChild, startIndex - 1);
            }

            ReturnObject result = createCalcTree(tokens, startIndex, endIndex, null, OperationPriority.LOW);
            currentTree.rightChild = result.TREE;
            return createCalcTree(tokens, result.STARTINDEX, endIndex, currentTree, operationPriority);
            default:
                throw new UnsupportedOperationException("That token type has not been implemented yet. Please blame Lukas. Token type received: " + currentTree.token.Type());
        }
       
    }

    private static class ReturnObject
    {
        public final CalcTree TREE;
        public final int STARTINDEX;

        public ReturnObject(CalcTree tree, int startIndex)
        {
            TREE = tree;
            STARTINDEX = startIndex;
        }
    }

    public static class CalcTree
    {
        private CalcTree leftChild, rightChild;
        private Token token;
        private boolean parentheses;

        public CalcTree(Token token, CalcTree leftChild)
        {
            this.token = token;
            this.parentheses = false;
            this.leftChild = leftChild;
        }

        public boolean simplyfy()
        {
            if (isNumber())
            {
                return false;
            }

            if (leftChild.isNumber() && rightChild.isNumber())
            {
                float newValue;
                switch(token.Type())
                {
                    case ADDITION:
                        newValue = leftChild.number() + rightChild.number();
                        break;
                    case SUBSTRACTION:
                        newValue = leftChild.number() - rightChild.number();
                        break;
                    case MULTIPLICATION:
                        newValue = leftChild.number() * rightChild.number();
                        break;
                    case DIVISION:
                        newValue = leftChild.number() / rightChild.number();
                        break;
                    case EQUALS:
                        return false;
                    default:
                        throw new UnsupportedOperationException("Token type can't be simplified: " + token.Type());
                }

                token = new Token(newValue);
                return true;
            }

            if (leftChild.simplyfy())
            {
                return true;
            }
            if (rightChild.simplyfy())
            {
                return true;
            }

            return false;
        }

        public boolean isNumber()
        {
            return token.Type() == Token.TokenType.NUMBER;
        }

        public float number()
        {
            if (!isNumber())
            {
                throw new RuntimeException("Tree is not a number! Tree type: " + token.Type());
            }

            return token.Value();
        }

        @Override
        public String toString()
        {
            if (token.Type() == Token.TokenType.NUMBER)
            {
                return token.toString();
            }

            String toPrint = "";
            if (parentheses)
            {
                toPrint += "(";
            }

            toPrint += leftChild + " " + token + " " + rightChild;
            
            if (parentheses)
            {
                toPrint += ")";
            }
            return toPrint;
        }

        public String toTreeString()
        {
            StringBuilder buffer = new StringBuilder("");
            toTreeString(buffer, "", "");
            return buffer.toString();
        }

        public void toTreeString(StringBuilder buffer, String prefix, String childrenPrefix)
        {
            buffer.append(prefix);
            buffer.append(token);
            buffer.append('\n');
            if (isNumber())
            {
                return;
            }
            
            leftChild.toTreeString(buffer, childrenPrefix + "\u251c\u2500\u2500 ", childrenPrefix + "\u2502   ");
            rightChild.toTreeString(buffer, childrenPrefix + "\u2514\u2500\u2500 ", childrenPrefix + "    ");
        }
    }
}
