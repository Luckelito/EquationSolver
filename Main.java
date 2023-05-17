import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the equation solver! Please enter your equation!");
        while (true)
        {
            String input = in.nextLine();
            List<Token> tokens = Tokenizer.tokenize(input); 
            CalcTreeMaker.CalcTree tree = null;
            try
            {
                tree = CalcTreeMaker.createCalcTree(tokens);
            }
            catch (SyntaxException e)
            {
                System.out.println(e);
                System.out.println("Please try to enter input again!");
                continue;
            }

            System.out.println("What do you want to do with the equation? Choose one of the following options:");
            System.out.println("1 - print equation tree");
            System.out.println("2 - solve");
            System.out.println("3 - solve step by step");
            System.out.println("4 - solve step by step in tree form");
            System.out.println("5 - cancel");
            options:
            while (true)
            {
                input = in.nextLine();
                switch(input.strip())
                {
                    case "1":
                        System.out.println(tree.toTreeString());
                        break;
                    case "2":
                        while(tree.simplyfy()) {}
                        System.out.println(tree);
                        break options;
                    case "3":
                        System.out.println(tree);
                        while(tree.simplyfy())
                        {
                            System.out.println(tree);
                        }
                        break options;
                    case "4":
                        System.out.println(tree.toTreeString());
                        while(tree.simplyfy())
                        {
                            System.out.println(tree.toTreeString());
                        }
                        break options;
                    case "5":
                        break options;
                    default:
                        System.out.println("Not a valid option!");
                }

            }

            System.out.println("Thanks for using the equation solver!");
            System.out.println("If you want to use it again, please enter another equation.");

        }
    }    
}
