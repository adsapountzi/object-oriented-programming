package hw1;
import java.util.Scanner;

/**
 * @author Sapountzi Athanasia Despoina
 * AEM 02624
 * date 28/3/2021
 */

public class ArithmeticCalculator {
    public Tree arithmeticTree;
	public static void main(String[] args) {
            ArithmeticCalculator calculator;
            int i;

            java.util.Scanner sc = new java.util.Scanner(System.in);

            System.out.println("Expression: ");

            String expression = sc.nextLine();

            calculator = new ArithmeticCalculator(expression);

            if(sc.hasNextLine()){
                    expression = sc.nextLine();
            }

            for(i = 0; i < expression.length() - 1; i++){
                if(expression.charAt(i) == '-') {
                    if(expression.charAt(i+1) == 'd') {
                        System.out.println("graph DotTree {\n" + calculator.toDotString() +"\n}\n");
                    }
                    else if(expression.charAt(i+1) == 's'){
                        System.out.println("Postfix: " + calculator.toString());
                    }
                    else if(expression.charAt(i+1) == 'c'){
                        System.out.println("Result: " + calculator.calculate());
                    }
                }
            }
        }
    
	public ArithmeticCalculator(String expression) {
		int i = -1, num, j, k,innerPar = 0, outerPar = 0, numberOfExtension = 0, afterNumberPosition = 0;
		String extension, fullExtension, extendedExpression,tempStr = "";
		
                expression = expression.replaceAll("\\s",""); // s for removing whitespaces, tabs.
		//check for extension 
		i = expression.indexOf("\\");
		while(i != -1){
                    if(isOperator(expression.charAt(i+1))){//check if next character is operator
                        //this loop saves the digit(s) of the number in tempStr
                        for(j = i + 2;  ( (j < expression.length()) && !isOperator(expression.charAt(j)) && !(expression.charAt(j) == '(') && !(expression.charAt(j) == ')') ); j++){
                            //tempStr(j - (i + 2)) = expression.charAt(j);                                  
                            if(j == i+2){
                                tempStr = String.valueOf(expression.charAt(j));
                            }
                            else{
                                tempStr = tempStr + expression.charAt(j);
                            }

                        }

                        afterNumberPosition = j ; //position after the right after the last digit of the number

                        if((tempStr.indexOf(".") == -1) && (Integer.parseInt(tempStr) > 1 )){ //check if number is an integer > 1.
                            numberOfExtension = Integer.parseInt(tempStr);
                            if(expression.charAt(i-1) == ')') { // case: ')', thus an arithmetic expression.
                                outerPar++;
                                for(j = i - 2; innerPar != outerPar; j--){ //this loop finds the matching '('
                                    if(expression.charAt(j) == '('){
                                        innerPar++;
                                    }
                                    else if(expression.charAt(j) == ')'){
                                        outerPar++;
                                    }
                                }	
                            }
                            else if(Character.isDigit(expression.charAt(i-1))){// i-1 is just a number, eg 5\/2
                                for(j = i - 1;((j >= 0) && ((Character.isDigit(expression.charAt(j))) || (expression.charAt(j) == '.' ))); j--){} //this loop finds the first digit of that number

                            }

                             extension = expression.substring(j + 1, i); //j+1 because the loops finish when j is one extra position to left, than it is supposed to be.

                            fullExtension = extension;
                            for(k = 1; k < numberOfExtension; k++){
                                    fullExtension = fullExtension + expression.charAt(i+1) + extension;
                            }
                            fullExtension = '('+fullExtension+')';
                            //     System.out.println(afterNumberPosition + " " + j);
                            if(afterNumberPosition < expression.length()){
                            expression = expression.substring(0,j + 1) + fullExtension + expression.substring(afterNumberPosition);
                           //  System.out.println("after");
                            }
                            else{
                                expression = expression.substring(0,j+1) + fullExtension;
                              //  System.out.println("else");
                              //  System.out.println(expression);
                            }
                        }
                        else{
                            System.out.println("[ERROR] Invalid expansion expression");
                            System.exit(-42);
                        }
                    }
                    else{
                        System.out.println("[ERROR] Invalid expansion expression");
                        System.exit(-42);
                    }
                    i = expression.indexOf("\\"); //check for another extension
		} 
                
		// =====>  end of extension <======
		
		// check for number of parenthesis
                innerPar = 0;
                outerPar = 0;

		for(j = 0; j < expression.length(); j++){
                    if(expression.charAt(j) == '('){
                            innerPar++;
                    }
                    if(expression.charAt(j) == ')'){
                            outerPar++;
                    }
                    if( !(expression.charAt(j) == '(') && !(expression.charAt(j) == ')') && !Character.isDigit(expression.charAt(j)) && !isOperator(expression.charAt(j)) && !(expression.charAt(j) == '.')) {
                        System.out.println("[ERROR] Invalid character");
                        System.exit(-42);
                    }	//check if arithmetic expression consists only of permitted characters.

			// check for consecutive operators
                    else if((j+1 < expression.length()) && isOperator(expression.charAt(j)) && isOperator(expression.charAt(j+1))){
                    System.out.println("[ERROR] Two consecutive operands");
                            System.exit(-42);
                    }// check for op ) , ( op
                    else if((j+1 < expression.length()) && (expression.charAt(j) == '(')&& isOperator(expression.charAt(j+1)) ){
                            System.out.println("[ERROR] Operand appears after opening parenthesis");
                            System.exit(-42);
                    }
                    else if((j+1 < expression.length()) && isOperator(expression.charAt(j)) && (expression.charAt(j+1) == ')')){
                            System.out.println("[ERROR] Operand appears before closing parenthesis");
                            System.exit(-42);
                    }
                        //System.out.println(expression.charAt(j) + " " +expression.charAt(j+1));
		} //close loop
                // System.out.println("out of loop");
		
                // check if the number of inner and outer parenthesis makes sense.
		if(innerPar > outerPar){ //bullet 1
			System.out.println("[ERROR] Not closing opened parenthesis");
			System.exit(-42);
		}
		else if(innerPar < outerPar) { // bullet 2
			System.out.println("[ERROR] Closing unopened parenthesis");
			System.exit(-42);
		}


		String[] input = new String[expression.length()]; 
		int lastValuePosition = 0;
		int positionArray = 0;
		int firstDigit = -1; // flag used in order to figure out if we found the first digit of a number, also used to find the position of the digit.
		
		for(j = 0; j < expression.length(); j++){ //create the array of strings
                    if (isOperator(expression.charAt(j)) || expression.charAt(j) == '(' || expression.charAt(j) == ')'){
                        if(firstDigit != -1 && (firstDigit < j-1)){
                            input[positionArray] = expression.substring(firstDigit, j);
                            positionArray++;
                            firstDigit = -1;
                        }
                        else if(firstDigit != -1 && (firstDigit == j-1)){
                            input[positionArray] =  String.valueOf(expression.charAt(j-1));
                            positionArray++;
                            firstDigit = -1;
                        }
                        input[positionArray] =  String.valueOf(expression.charAt(j));
                        positionArray++;
                    }
                    else if( Character.isDigit(expression.charAt(j)) && (firstDigit == -1)) {
                            firstDigit = j; 
                    }
		}
                if(firstDigit != -1){
                    input[positionArray] = expression.substring(firstDigit, j);
                    positionArray++;
                }
                //  for( int l = 0; l<positionArray; l++){
                //     System.out.println(input[l]);
                // }
                String[] temp = new String[positionArray]; //create the array of string with the right ammount of space
                for(int l = 0; l<positionArray; l++){
                    temp[l] = input[l];
                }
		arithmeticTree = new Tree(temp); // create Tree!
            }
        

	public boolean isOperator(char a){ //boolean function used to check if char a is an operator
            return a == '/' || a == '*' || a == '+' || a == '-' || a == '^' ||  a == 'x';
       }

	public String toDotString(){
		return(arithmeticTree.treeToDotString());
	}


	public String toString(){ 
		return(arithmeticTree.treeToString());
	}

	public double calculate(){
		return(arithmeticTree.treeCalculate());
	}

	/*private static String ReadLine() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        String line = sc.nextLine(); 
        return line;
    }*/
}