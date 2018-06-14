//@ Riku Fukumori

import structure5.*;
import java.util.Iterator;

public class Interpreter{

    //StackList global variable
    public StackList stack;
    public SymbolTable table;

    //constructor which instatiates a StackList and a SymbolTable
    public Interpreter(){
	this.stack = new StackList();
	this.table = new SymbolTable();
    }

    //method which reads in Tokens and calls helper methods to execute on them after finind out which type of Token they are
    public void interpret(Reader read){
	Token t;
	boolean define=false;
	Reader r = read;
	while (r.hasNext()){
	    t = (Token) r.next();
	    if (t.isSymbol() && t.getSymbol().equals("quit"))break;
	    //deals with defined symbols
	    if (t.isSymbol() && t.getSymbol().startsWith("/")){
		String variable = t.getSymbol().substring(1);
		Token token = new Token(variable);
		stack.push(token);
		define = true;
		//deals with procedures and variables
	    }else if (t.isSymbol() && table.contains(t.getSymbol())){
		runProcedure(table.get(t.getSymbol()));
		//all cases of operations
      	    }else if (t.isSymbol()){
		switch(t.getSymbol()){
		case "pstack":
		    this.pstack();
		    break;
	        case "add":
		    this.add();
		    break;
		case "sub":
		    this.sub();
		    break;
		case "mul":
		    this.mul();
		    break;
		case "div":
		    this.div();
		    break;
		case "dup":
		    this.dup();
		    break;
		case "exch":
		    this.exch();
		    break;
		case "eq":
		    this.eq();
		    break;
		case "ne":
		    this.ne();
		    break;
		case "def":
		    this.def();
		    define = false;
		    break;
		case "ptable":
		    this.ptable();
		    break;
		case "pop":
		    this.pop();
		    break;
		}
		//pushes anything not symbols onto stack
	    }else{
		switch(t.kind()){
		case 1:
		    stack.push(t);
		    break;
		case 2:
		    stack.push(t);
		    break;
		case 4:
		    if (define) stack.push(t);
		    else runProcedure(t);
		    break;
		}
	    }

	}
    }

    //method which deals with procedures
    public void runProcedure(Token procedure){
	//deals with if statements
	if(procedure.toString().contains("if")){
	    DoublyLinkedList<Token> temp = new DoublyLinkedList<Token>();
	    //copy procedure
	    for(int i = 0; i < procedure.getProcedure().size(); i++){
		temp.addLast(procedure.getProcedure().get(i));
	    }
	    //remove if statement
	    temp.removeLast();
	    Token execute = temp.getLast();
	    //removes the executing statement
	    temp.removeLast();
	    //make new interpreter with temp
	    interpret(new Reader(temp));
	    Assert.pre(((Token)stack.get()).isBoolean(), "if statements must have boolean");
	    //run procedure if if statement is true
	    if(((Token)stack.get()).getBoolean()){
		stack.remove();
		runProcedure(execute);
	    }else{
		stack.remove();
	    }
	    //deals with normal procedures
	}else{
	    interpret(new Reader(procedure));
	}
    }

    //pstack method
    //pre: stack is not empty
    public void pstack(){
	Iterator it = stack.iterator();
	while (it.hasNext()){
	    System.out.print(it.next()+" ");
	}
     }

    //add method
    //pre: two tokens before "add" must be numbers and stack must be of size>=2
    //precondition for token being a number is satisfied in Token.getNumber()
    public void add(){
	Assert.pre(stack.size()>=2, "Stack correct size");
	Token firstNum = (Token) stack.remove();
	double firstValue = firstNum.getNumber();
	Token secondNum = (Token) stack.remove();
	double secondValue = secondNum.getNumber();
	Token token = new Token(firstValue + secondValue);
	stack.push(token);
    }

    //sub method
    //pre: two tokens before "sub" must be numbers and stack must be size>=2
    //precondition for token being a number is satisfied in Token.getNumber()
    public void sub(){
	Assert.pre(stack.size()>=2, "Stack correct size");
	Token firstNum = (Token) stack.remove();
        double firstValue = firstNum.getNumber();
        Token secondNum = (Token) stack.remove();
        double secondValue = secondNum.getNumber();
        Token token = new Token(secondValue-firstValue);
        stack.push(token);
    }

    //mul method
    //pre: two tokens before "mul" must be numbers and stack must be of size>=2         //precondition for token being a number is satisfied in Token.getNumber()
    public void mul(){
        Assert.pre(stack.size()>=2, "Stack correct size");
        Token firstNum = (Token) stack.remove();
        double firstValue = firstNum.getNumber();
        Token secondNum = (Token) stack.remove();
        double secondValue = secondNum.getNumber();
        Token token = new Token(firstValue * secondValue);
        stack.push(token);
    }


    //div method
    //pre: two tokens before "div" must be numbers and stack must be of size>=2
    //pre: cannot divide by zero
    //precondition for token being a number is satisfied in Token.getNumber()
    public void div(){
        Assert.pre(stack.size()>=2, "Stack correct size");
        Token firstNum = (Token) stack.remove();
        double firstValue = firstNum.getNumber();
	Assert.pre(firstValue!=0, "firstValue nonzero");
        Token secondNum = (Token) stack.remove();
        double secondValue = secondNum.getNumber();
        Token token = new Token(secondValue / firstValue);
        stack.push(token);
    }

    //dup method
    //pre: stack must be of size>=1
    public void dup(){
	Assert.pre(stack.size()>=1, "Stack correct size");
	Token firstValue = (Token) stack.remove();
	stack.push(firstValue);
	stack.push(firstValue);
    }

    //exch method
    //pre: stack must be of size>=2
    public void exch(){
	Assert.pre(stack.size()>=2, "Stack correct size");
	Token firstValue = (Token) stack.remove();
	Token secondValue = (Token) stack.remove();
	stack.push(secondValue);
	stack.push(firstValue);
    }

    //eq method
    //pre: stack must be of size>=2
    public void eq(){
	Assert.pre(stack.size()>=2, "stack correct size");
	Token firstValue = (Token) stack.remove();
        Token secondValue = (Token) stack.remove();
        boolean b = firstValue.equals(secondValue);
	Token token = new Token(b);
	stack.push(token);
    }

    //ne method
    //pre:stack must be of size>=2
    public void ne(){
	Assert.pre(stack.size()>=2, "stack correct size");
        Token firstValue = (Token) stack.remove();
        Token secondValue = (Token) stack.remove();
        boolean b = !firstValue.equals(secondValue);
	Token token = new Token(b);
	stack.push(token);
    }

    //def method
    //pre: size of stack is >=2 and preceding value is a number, boolean, or procedure and the preceding value to that is a string
    public void def(){
	Assert.pre(stack.size()>=2, "stack correct size");
	Token t1 = (Token) stack.remove();
	Assert.pre(t1.isNumber()||t1.isBoolean()||t1.isProcedure(), "token is a number, boolean, or procedure");
        Token t2 = (Token) stack.remove();
	Assert.pre(t2.isSymbol(), "token is a string");
        String variable = t2.getSymbol();
	table.add(variable,t1);
    }

    //ptable method
    //pre: SymbolTable not empty
    public void ptable(){
	System.out.print(table.toString());
    }

    //remove top value from the stack
    //pre: stack is not empty
    public void pop(){
	Assert.pre(stack.size()>=1, "stack is not empty");
	stack.remove();
    }

    public static void main (String[] args){
	Interpreter p = new Interpreter();
	Reader r = new Reader();
	p.interpret(r);
    }

}
