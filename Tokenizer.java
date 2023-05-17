import java.util.List;
import java.util.LinkedList;
import java.util.regex.*;

public class Tokenizer
{
    public static List<Token> tokenize(String input)
    {
        var tokens = new LinkedList<Token>();

        Matcher matcher = Pattern.compile(
            "[\\*/\\+\\-\\(\\)=]|" +
            "(0|([1-9][0-9]*))" 
        ).matcher(input);

        while (true)
        {
            if (!matcher.find())
            {
                break;
            }

            switch (matcher.group())
            {
                case "*":
                    tokens.add(new Token(Token.TokenType.MULTIPLICATION));
                    break;
                case "/":
                    tokens.add(new Token(Token.TokenType.DIVISION));
                    break;
                case "+":
                    tokens.add(new Token(Token.TokenType.ADDITION));
                    break;
                case "-":
                    tokens.add(new Token(Token.TokenType.SUBSTRACTION));
                    break;
                case "(":
                    tokens.add(new Token(Token.TokenType.LEFT_PARENTHESES));
                    break;
                case ")":
                    tokens.add(new Token(Token.TokenType.RIGHT_PARENTHESES));
                    break;
                case "=":
                    tokens.add(new Token(Token.TokenType.EQUALS));
                    break;
                default:
                    try 
                    {
                        int value = Integer.parseInt(matcher.group());
                        tokens.add(new Token(value));
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Invalid token: [" + matcher.group() + "]");
                        return null;
                    }

            }
        }

        return tokens;
    }


}