
package hw1;

/**
 * @author Sapountzi Athanasia Despoina
 * AEM 02624
 * date 28/3/2021
 */
public class Tree {
	public TreeNode root; 
	public static final int HIGH = 3 , MEDIUM = 2, LOW = 1;

	public Tree(String[] Input){//constructor
		root = treeCreate(0, (Input.length- 1), Input);
	}

	public TreeNode treeCreate( int left, int right, String[] Input){
            TreeNode n = null;
            if(left < right){
                int innerPar = 0; // # '('
                int outerPar = 0; // # ')'
                int i = 0; // counter
                int currPriority = 4;
                int priority = 4;
                int lowestOperatorPos = -1;

                for(i = left; i < right; i++){
                    if(Input[i].charAt(0) == '('){
                            innerPar++;
                    }
                    else if(Input[i].charAt(0) == ')') {
                            outerPar++;
                    }
                    else if(innerPar == outerPar){ //If it is true then it is outside of parenthesis
                        if(Input[i].charAt(0) == '^' || Input[i].charAt(0) == '/' || Input[i].charAt(0) == '*' || Input[i].charAt(0) == '+' || Input[i].charAt(0) == '-') {// check if it's an operator
                            if(Input[i].charAt(0) == '^'){ // find out the priority of that operator
                                currPriority = HIGH;
                            }
                            else if(Input[i].charAt(0) == '/' || Input[i].charAt(0) == '*' || Input[i].charAt(0) == 'x') {
                                currPriority = MEDIUM;
                            }
                            else if(Input[i].charAt(0) == '+' || Input[i].charAt(0) == '-'){
                                currPriority = LOW;
                            }

                            if(currPriority <= priority){
                                priority = currPriority;
                                lowestOperatorPos = i; // locate  the position of the operator with the lowest priority
                            }
                        }
                    }
                }

                if(lowestOperatorPos >= 0) { //create a node for the operator and break the arithmetic expression in two parts 
                    n =  new TreeNode(String.valueOf(Input[lowestOperatorPos].charAt(0)));
                    //System.out.println(Input[lowestOperatorPos].charAt(0));
                   // System.out.println("par " + Input[left]);
                    n.left = treeCreate( left, lowestOperatorPos - 1, Input);
                    n.right = treeCreate( lowestOperatorPos + 1, right,  Input);
                }
                else if(lowestOperatorPos < 0){ //no operator found, take out a pair of parenthesis
                    //System.out.println("par " + Input[left]);
                    n = treeCreate( left + 1, right - 1, Input);
                }
            }
            else if(left == right){//expression is only one number
                //System.out.println(Input[left]);
                    n = new TreeNode(Input[left]); //put it as leaf?

            }
            return(n);
	}  

	public String treeToDotString(){
		return(root.nodeToDotString());
	}


	public String treeToString(){ 
		return(root.nodeToString());
	}

	public double treeCalculate(){
		return(root.nodeCalculate());
	}
}
