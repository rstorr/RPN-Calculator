package week09;

import java.util.*;


/**
 * A calculator which processes each line of input from System.in as
 * an expression in Reverse Polish Notation and outputs the stack using
 * System.out. The calculator has the functionality to process binary
 * caluclations, repeated binary calculations, duplication, copying,
 * rotation and parentheses where the commands inside the
 * parentheses are repeated according to the top of the stack.
 *
 * @author Reuben Storr, Blake Carter
 */

public class RPNApp{

    /** Stack used to store numbers for processing. */
    private static List<Long> stack;
    /** Arraylist of tokens to process. */
    private static List<String> tokens;
    /** States if theres an error in the program. */
    private static boolean noErrors = true;
    
    /**
     * Main method creates scanner object then reads in input
     * whilst there is a new line of input using the nextLine
     * method. The input is split and stored
     * as an arrayList for processing. The sort method is then
     * called on this list. If there are no errors after processing
     * the stack is printed to System.out.
     * @param args command line arguments 
     */
    public static void main (String[] args){

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            String input = sc.nextLine();
            tokens = new LinkedList<String>(Arrays.asList(input.split(" "))); 
            stack = new ArrayList<Long>();
            sort(tokens);
            if (noErrors){
                System.out.println(stack.toString());
            }
            noErrors = true;
        }
        sc.close();
    }

    /**
     * Sorts the tokens into numbers that will be stored in the
     * stack, parentheses which are stored in the tokensInBrackets
     * then processed separately and operations which are then passed
     * to the sortOperation method.
     *
     * @param arr arrayList to be sorted.
     */
    public static void sort(List<String> arr){
        for(int i = 0; i < arr.size(); i++){
            if(noErrors){
                String token = arr.get(i);
                if (token.equals("(")){
                    long repeat;

                    try{
                        repeat = stack.get(stack.size() - 1);
                        stack.remove(stack.size() - 1);
                    }catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Error: too few operands");
                        noErrors = false;
                        return;
                    }
                    if (!arr.contains(")")){
                        System.out.println("Error: unmatched parentheses");
                        noErrors = false;
                        return;
                    }

                    int ios = arr.indexOf("("); //index of start
                    int ioe = arr.lastIndexOf(")"); //index of end
                    List<String> inBrackets = new
                        ArrayList<>(arr.subList(ios + 1, ioe));
                    int j = 1;
                    while(j <= repeat){
                        sort(inBrackets);
                        j++;
                    }
                    /*Skips through tokens inside parentheses as they have
                    already been processed
                    */
                    i += (inBrackets.size() + 1);
                    
                }else if (token.equals(")")){
                    System.out.println("Error: unmatched parentheses");
                    noErrors = false;
                }else {
                    try{
                        int num = Integer.parseInt(token);
                        stack.add((long) num); 
                    }catch (NumberFormatException e) {
                        sortOperation(token);
                    }
                }   
            }
        }
    }

    /**
     * Sorts the token into binary operation, recursive operation,
     * special operation or outputs a bad token error if the token
     * is none of these. An If statement checks that the stack is
     * greater than zero.
     *
     * @param op token to sort.
     */
    public static void sortOperation(String op){
        if (!(stack.size() > 0)){
            System.out.println("Error: too few operands");
            noErrors = false;
            return;
        }
        
        switch(op){
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                if (stack.size() > 1){
                    basicOperation(op);
                }else{
                    System.out.println("Error: too few operands");
                    noErrors = false;
                    return;
                }
                break;
            case "+!":
            case "-!":
            case "*!":
            case "/!":
            case "%!":
                recursiveOperation(op);
                break;
            case "d":
            case "o":
            case "c":
            case "r":
                specialOperation(op);
                break;
            default:
                System.out.println("Error: bad token '" + op + "'");
                noErrors = false;
                break;
        }
        
    }

    /**
     * Processes binary operations using switch method then excutes the
     * operation on the top and second from the top elements in the
     * stack. The top and second from the top elements in the
     * stack are replaced by the result. ArithmeticExceptions are caught
     * using try and catch.
     *
     *@param op binary operation to be processed.
     */
    public static void basicOperation(String op){
        long first = stack.get(stack.size() - 1);
        long second = stack.get(stack.size() - 2);

        stack.remove(stack.size() - 1);
        stack.remove(stack.size() - 1);
        switch(op){
            case "+":
                stack.add(second + first);
                break;
            case "*":
                stack.add(second * first);
                break;      
            case "-":
                stack.add(second - first);
                break;
            case "/":
                try{
                    stack.add(second / first);
                }catch (ArithmeticException e){
                    System.out.println("Error: division by 0");
                    noErrors = false;
                }
                break;
            case "%":
                try{
                    stack.add(second % first);
                }catch (ArithmeticException e){
                    System.out.println("Error: remainder by 0");
                    noErrors = false;
                }
                break;
        }
    }
    
    /**
     * Processes recursive operations using switch method, excutes the binary
     * operation once then repeatedly calls binaryOperation until there is
     * only one element left in the stack.
     *
     * @param op recursive operation to be processed.
     */
    public static void recursiveOperation(String op){
        long first = stack.get(stack.size() - 1);
        long second = stack.get(stack.size() - 2);
       
        switch(op){
            case "+!":
                stack.remove(stack.size() - 1);
                stack.remove(stack.size() - 1);
                stack.add(second + first);
                while (stack.size() > 1){
                    basicOperation("+");
                }
                break;
            case "*!":
                stack.remove(stack.size() - 1);
                stack.remove(stack.size() - 1);
                stack.add(second * first);
                while (stack.size() > 1){
                    basicOperation("*");
                }
                break;
            case "-!":
                stack.remove(stack.size() - 1);
                stack.remove(stack.size() - 1);
                stack.add(second - first);
                while (stack.size() > 1){
                    basicOperation("-");
                }
                break;
            case "/!":
                stack.remove(stack.size() - 1);
                stack.remove(stack.size() - 1);
                stack.add(second / first);
                while (stack.size() > 1){
                    basicOperation("/");
                }
                break;
            case "%!":
                stack.remove(stack.size() - 1);
                stack.remove(stack.size() - 1);
                stack.add(second % first);
                while (stack.size() > 1){
                    basicOperation("%");
                }
                break;
        }
    }

    /**
     * Processes special operations using switch method then calls the
     * necessary method. If there is only one element in the stack
     * and the operation is to rotate then the too few operands error
     * will be output.
     *
     * @param op recursive operation to be processed.
     */
    public static void specialOperation(String op){
        switch(op){
            case "d":
                duplicate();
                break;
            case "o":
                output();
                break;
            case "c":
                copy();
                break;
            case "r":
                if(stack.size() > 1){
                    rotate();
                }else{
                    System.out.println("Error: too few operands");
                    noErrors = false;
                }
                break;
        }

    }

    /**
     * Duplicates the top element of the stack then adds it to
     * the stack.
     */
    public static void duplicate (){
        long top = stack.get(stack.size() - 1);
        stack.add(top);
    }
    
    /**
     * Outputs the top element from the stack.
     */
    public static void output (){
        int topPos = stack.size() - 1;
        System.out.print(stack.get(topPos) + " ");
    }
    
    /**
     * Gets the top two element from the stack and replaces them
     * with top element copies of the second from top element.
     * If the top element is less than zero than a negative
     * copy error is output.
     */
    public static void copy (){
        int topPos = (stack.size() - 1);
        int belowTopPos = (stack.size() - 2);
        long top = stack.get(topPos);
        long belowTop = stack.get(belowTopPos);
        stack.remove(stack.size() - 1);
        stack.remove(stack.size() - 1);
        if (top > -1){
            for(int i = 1; i <= top; i++){
                stack.add(belowTop);
            }
        }else{
            System.out.println("Error: negative copy");
            noErrors = false;
        }
    }
    
    /**
     * Stores the top element in 'top' then removes it. The method then
     * moves the new top element down 'top' positions in the stack.
     * If there are too few items in the stack or If 'top' is less than
     * zero it will trigger an error.
     */
    public static void rotate (){
        long top = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        if (top > -1 && stack.size() >= top ){
            long moved = stack.get(stack.size() - 1);
            int pos = (stack.size() - (int)top);
            stack.add(pos,moved);
            stack.remove(stack.size() - 1);
        }else{
            if(top < 0){
                System.out.println("Error: negative roll");
            }else{
                System.out.println("Error: too few operands");
            }
            noErrors = false;
        }
    }
}
        
