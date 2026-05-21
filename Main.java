/* Includes */
import java.io.*;

public class Main
{
	/* Main function */
	public static void main(String[] args)
	{
		Interpreter interpreter = new Interpreter();
		if(args.length==0)
		{
			System.out.println("YOU MUST SPECIFY A FILE!");
		}
		else
		{
			/* Read the file into the interpreter, run the commands and then show all data at the end */
			interpreter.ReadFile(args[0]);
			interpreter.run();
			interpreter.showall();
		}
	}
}