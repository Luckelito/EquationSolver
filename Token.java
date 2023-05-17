public class Token
{
    public static enum TokenType
    {
        MULTIPLICATION,
        DIVISION,
        ADDITION,
        SUBSTRACTION,
        LEFT_PARENTHESES,
        RIGHT_PARENTHESES,
        NUMBER,
        EQUALS
    } 

    private final float VALUE;
    
    private final TokenType TYPE;

    public Token(TokenType type)
    {
        if (type == TokenType.NUMBER)
        {
            throw new IllegalArgumentException("Please use other constructor for numbers!");
        }

        TYPE = type;
        VALUE = 0;
    }

    public Token(float value)
    {
        TYPE = TokenType.NUMBER;
        VALUE = value;
    }

    public float Value()
    {
        return VALUE;
    }

    public TokenType Type()
    {
        return TYPE;
    }

    @Override
    public String toString()
    {
        switch(TYPE)
        {
            case NUMBER:
                if (VALUE == (int) VALUE)
                {
                    return Integer.toString((int) VALUE);
                }
                return Float.toString(VALUE);
            case ADDITION:
                return "+";
            case SUBSTRACTION:
                return "-";
            case MULTIPLICATION:
                return "*";
            case DIVISION:
                return "/";
            case LEFT_PARENTHESES:
                return "(";
            case RIGHT_PARENTHESES:
                return ")";
            case EQUALS:
                return "=";
            default:
                throw new UnsupportedOperationException("Can't convert this token to a string: " + TYPE);
        }
    }
} 