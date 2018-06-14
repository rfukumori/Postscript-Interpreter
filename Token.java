//@Riku Fukumori

// A class for managing postscript tokens.
import structure5.*;
import java.util.Iterator;

/**
 * A class that implements Tokens that might be read from a stream
 * of postscript commands.  There are, basically, four types of tokens:
 * <ul>
 * <li> Numbers - like 3, 1.4, etc.
 * <li> Booleans - true or false,
 * <li> Symbols - pi, myProcedure, quit, etc.
 * <li> Procedures - lists of other tokens to be interpreted at a later time.
 * </ul>
 *
 * Example usage:
 * <p>
 * To read in the tokens of a postscript file, without interpretation,
 * we might do the following:
 *<pre>
 *  public static void {@link Reader#main(String[] args) main(String[] args)}
 *  {
 *      int i=0;
 *      {@link Reader} r = new {@link Reader#Reader() Reader()};
 *      {@link Token} t;
 *      while ({@link Reader#hasNext() r.hasNext()})
 *      {
 *          t = {@link Reader#next() r.next()};
 *          if (t.{@link #isSymbol isSymbol()} && // only if symbol:
 *              t.{@link #getSymbol getSymbol}().{@link #equals(Object) equals("quit")}) break;
 *          // process token
 *          System.out.println(i+": "+t);
 *          i++;
 *      }
 *  }
 *</pre>
 * @author duane a. bailey
 */
public class Token
{
    /**
     * The kind of token this is.
     */
    private int kind;		// type of token
    /**
     * Token is a number.
     */
    static public final int NumberKind = 1;
    /**
     * Token is a boolean.
     */
    static public final int BooleanKind = 2;
    /**
     * Token is a symbol.
     */
    static public final int SymbolKind = 3;
    /**
     * Token is a procedure.
     */
    static public final int ProcedureKind = 4;

    /**
     * Associated numeric value of token, if isNumber().
     */
    private double number;	// a double token value
    /**
     * Associated boolean value of token, if isBoolean().
     */
    private boolean bool;	// a boolean token value
    /**
     * Associated string value of token, if isSymbol().
     */
    private String symbol;	// name of a symbol
    /**
     * Associated list of token, if isProcedure().
     */
    private List<Token> procedure;	// a list of tokens for procedures

    /**
     * Construct a numeric token.
     *
     * @param value the numeric value of the token
     */
    public Token(double value)
    {
	kind = NumberKind;
	this.number = value;
    }

    /**
     * Construct a boolean token
     * @param bool the boolean value of the token
     */
    public Token(boolean bool)
    {
	kind = BooleanKind;
	this.bool = bool;
    }

    /**
     * Construct a symbol token
     *
     * @param symbol the string representing the token
     */
    public Token(String symbol)
    // post: constructs a symbol token with value symbol
    {
	kind = SymbolKind;
	this.symbol = symbol;
    }

    /**
     * Construct a procedure.
     *
     * @param proc the list of tokens that make up the procedure.
     */
    public Token(List<Token> proc)
    {
	kind = ProcedureKind;
	this.procedure = proc;
    }


    /**
     * Return the kind of token.  Great for use in switch statements.
     * @return integer representing the kind of the token, usually
     * Token.number, Token.symbol, etc.
     */
    public int kind()
    {
	return this.kind;
    }

    /**
     * Returns true if and only if this token is a number.
     * @return true iff token is a number.
     */
    public boolean isNumber()
    {
	return kind == NumberKind;
    }

    /**
     * Returns true if and only if this token is a boolean.
     * @return true iff token is a boolean.
     */
    public boolean isBoolean()
    {
	return kind == BooleanKind;
    }

    /**
     * Returns true if and only if this token is a symbol.
     * @return true iff token is a symbol.
     */
    public boolean isSymbol()
    {
	return kind == SymbolKind;
    }

    /**
     * Returns true if and only if this token is a procedure.
     * @return true iff token is a procedure.
     */
    public boolean isProcedure()
    {
	return kind == ProcedureKind;
    }

    /**
     * Fetch numeric value of token, provided it's a number.
     * @return token's numeric value.
     */
    public double getNumber()
    {
	Assert.pre(isNumber(),"Is a number.");
	return number;
    }

    /**
     * Fetch boolean value of token, provided it's a boolean.
     * @return token's boolean value.
     */
    public boolean getBoolean()
    {
	Assert.pre(isBoolean(),"Is a boolean.");
	return bool;
    }

    /**
     * Fetch string value of token, provided it's a symbol.
     * @return token's string value.
     */
    public String getSymbol()
    {
	Assert.pre(isSymbol(),"Is a string.");
	return symbol;
    }

    /**
     * Fetch the list of tokens associated with a procedure token.
     * @return a List of associated token values.
     */
    public List<Token> getProcedure()
    {
	Assert.pre(isProcedure(),"Is a procedure.");
	return procedure;
    }

    /**
     * Returns true if this token has the same value as the other.
     * (Does not work correctly for procedures, but sufficient.)
     * @param other another token
     * @return true if and only if this token is equivalent to other
     */
    public boolean equals(Object other)
    {
	Token that = (Token)other;
	boolean result = false;
	// if types are different, the tokens are different
	if (this.kind != that.kind) return false;
	switch (this.kind)
	{
	  case NumberKind:
	    result = this.number == that.number;
	    break;
	  case BooleanKind:
	    result = this.bool == that.bool;
	    break;
	  case SymbolKind:
	    result = this.symbol.equals(that.symbol);
	    break;
	  case ProcedureKind:
	    result = this.procedure == that.procedure;
	    break;
	}
	return result;
    }

    /**
     * Generates string representation of a token.
     * @return a string representation of the token.
     */
    public String toString()
    {
	String result = "<unknown>";
	switch (kind)
	{
	  case NumberKind:
	    result = ""+number;
	    break;
	  case BooleanKind:
	    result = ""+bool;
	    break;
	  case SymbolKind:
	    result = symbol;
	    break;
	  case ProcedureKind:
	    result = "{ ";
	    // the iterator allows us to visit elements nondestructively
	    Iterator<Token> i = procedure.iterator();
	    while (i.hasNext())
	    {
		Token t = i.next();
	        result = result + " " + t;
	    }
	    result = result + " }";
	    break;
	}
	return result;
    }
}
