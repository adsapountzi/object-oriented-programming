
package hw1;

/**
 * @author Sapountzi Athanasia Despoina
 * AEM 02624
 * date 28/3/2021
 */
public class TreeNode{
	public String data;	//data of node  (an operator || a number)
	public TreeNode left, right;
	public  int ID;
	static int nodeID = 0; 
	// constructor
	public TreeNode(String str){
		data  = str;
		left  = right = null;
                ID = nodeID++; //unique ID of each node, used in ToDotSting.
	}

	public String nodeToDotString(){ //for the dot, recursive method
            String result;
            result = "\t" + this.ID + "[label=\"" + this.data + "\", shape=circle, color=black]\n";
            if((this.left != null) && (this.right != null)) {
                result += "\t" + this.ID + "--" + this.left.ID + "\n";
                result += this.left.nodeToDotString();
                result += "\t" + this.ID + "--" + this.right.ID + "\n";
                result += this.right.nodeToDotString();
            }
            return(result);
	}

	public String nodeToString(){  //for the arithmetic expression, recursive method
            String arithmeticExpression;

            if((this.left == null) && (this.right == null)){ //leaf
                return  this.data ;
            }
            else { //arithmetic expression with operator
                return "(" +  this.left.nodeToString()  + ")" + "(" +  this.right.nodeToString()  + ")" + this.data; 
            }
	}

  
    public double nodeCalculate(){ // calculate the arithmetic expressions, recursively
        if(this.right == null && this.left == null){
                return Double.parseDouble(data); 
        }
        else{
            switch(this.data.charAt(0)){ // check the type of operator and calculate the apropriate arithmetic expression recursively
                case '^':
                        return Math.pow(this.left.nodeCalculate(), this.right.nodeCalculate());
                case '/':
                        return this.left.nodeCalculate() / this.right.nodeCalculate();
                case '*':
                        return this.left.nodeCalculate() * this.right.nodeCalculate();
                case 'x':
                        return this.left.nodeCalculate() * this.right.nodeCalculate();
                case '+':
                        return this.left.nodeCalculate() + this.right.nodeCalculate();
                case '-':
                        return this.left.nodeCalculate() - this.right.nodeCalculate();

                default:
                        System.out.println("Problem in calculate!!!");
                        System.exit(-42);
            }

        } 
        return (0);
    }
}




