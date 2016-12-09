package exceptions;

public class CantPlayThisCardException extends Exception{
	
	public CantPlayThisCardException(String msg){
		super(msg);
	}
}
