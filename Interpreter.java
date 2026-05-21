/* Includes */
import java.io.*;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Interpreter
{
	private Memory data = new Memory(); // our custom data type for variables
	private ArrayList<String> queue = new ArrayList<String>(); // this will store the commands to execute
	private int pointer = 0; // pointer to control execution flow
	
	/* Function to get input from user */
	public static String getIn()
	{
		String response = ""; //string to hold response from user
		/* Try to create bufferedreader */
		try
		{
			/* Create new buffered reader from console input */
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			response = reader.readLine(); // get input
			return response; // give response back
		}catch(Exception e)
		{
			/* If error, print it then quit it */
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	/* Clear command */
	private void clear(String var)
	{
		// if it doesn't already exist,
		if(data.searchindex(var)==-1)
		{
			data.add(var,"0"); // we create it
		}else
		{
			data.modify_data(var,"0"); // otherwise, set it to 0
		}
	}
	
	/*
	* This command increases the value of a variable by 1
	* If the variable doesn't already exist, we simply
	* create it and then increase it by 1.
	*/
	private void increase(String var)
	{
		if(data.searchindex(var)!=-1) // if it exists,
		{
			data.modify_data(var,Integer.toString((Integer.parseInt(data.get(var))+1))); // simply add one to it
		}else
		{
			clear(var); // if it doesn't exist, create the variable, and
			increase(var); // then increase it
		}
	}
	
	/*
	* This command decreases the value of a variable by 1
	* If the variable doesn't already exist, we simply
	* create it.
	* The lowest value we allow variables to take is 0.
	*/
	private void decrease(String var)
	{
		if(data.searchindex(var)!=-1) //exists
		{
			int value = Integer.parseInt(data.get(var));
			if(value != 0)
			{
				value--;
				data.modify_data(var,Integer.toString(value));
			}
		}
		else
		{
			clear(var); //make var
		}
	}

	/* Function to get the end of the while in question */
	private int getEnd(int start)
	{
		int counter = 0;
		Boolean hasEnd = false;
		/* At the first instance of the counter going to zero, return the position of the last end that did it */
		for(int i = start; i < queue.size(); i++)
		{
			if(i==start) counter=1; //initial value
			if(queue.get(i).contains("while")) //contains a 'while'. add 1
			{
				counter++;
			}
			else if(queue.get(i).contains("end")) //contains a 'end'. minus 1
			{
				counter--;
			}
			if(counter==0) //if counter is zero
			{
				hasEnd = true;
				counter = i;
				break;
			}
		}
		if(hasEnd) return counter; //we got it
		else return -1; //else just return -1
	}

	/* Function to handle while loops */
	private void handlewhile(String var, int number)
	{
		int start = pointer+1; //this stores the value we should go back to when we reach 'end'
		pointer++; //add to pointer
		int end = getEnd(pointer); //make sure there is an end
		int index = data.searchindex(var); //make sure the variable exists
		if(index != -1)
		{
			if(end != -1)
			{
				while(Integer.parseInt(data.get(var))!=number)
				{
					for(int i = start; i < end; i++)
					{
						pointer = i;
						execute_command(queue.get(i));
						if(pointer>i) i = pointer;
					}
				}
				if(pointer!=queue.size()-1)
				{
					pointer = end;
				}
				else
				{
					pointer = end+1;
				}
			}
			else
			{
				System.out.println("ERROR: NO 'end' STATEMENT TO MATCH 'while', instruction number: " + pointer);
			}
		}else
		{
			System.out.println("ERROR: THE VARIABLE DID NOT EXIST: " + var + " in while, instruction number: " + pointer);
			System.exit(0);
		}
	}
	
	/* Function to handle if statement */
	private void handleif(String var, int number)
	{
		int index = data.searchindex(var);
		int end = getEnd(pointer);
		int start = pointer+1;
		pointer++;
		if(index != -1)
		{
			if(end != -1)
			{
				if(Integer.parseInt(data.get(var))!=number)
				{
					for(int i = start;i < end; i++)
					{
						pointer = i;
						execute_command(queue.get(i));
						if(pointer>i) i = pointer;
					}
				}
				if(pointer!=queue.size()-1)
				{
					pointer = end;
				}
				else
				{
					pointer = end+1;
				}
			}else
			{
				System.out.println("ERROR: NO 'end' STATEMENT TO MATCH 'if', instruction number: " + pointer);
			}
		}
		else
		{
			System.out.println("ERROR: THE VARIABLE DID NOT EXIST: " + var + "in if, instruction number: " + pointer);
		}
	}
	
	/* 
 	* This is the function which actually handles how to execute
	* each command. We use regex for pattern matching.
	* If the command doesn't match any known pattern,
	* the user is informed and we simply exit.
	*/
	private void execute_command(String command)
	{
		if(Pattern.matches("clear [a-zA-Z0-9_]+",command)) // command to clear a variable
		{
			clear(command.substring(6,command.length()));
		}
		else if(Pattern.matches("incr [a-zA-Z0-9_]+",command)) // command to increase the value of a variable
		{
			increase(command.substring(5,command.length()));
		}
		else if(Pattern.matches("decr [a-zA-Z0-9_]+",command)) // command to decrease the value of a variable
		{
			decrease(command.substring(5,command.length()));
		}
		else if(Pattern.matches("while [a-zA-Z0-9_]+ not [a-zA-Z0-9_]+ do",command)) // while loop
		{
			String var = command.substring(6, command.indexOf(" not"));
			String number = command.substring(command.indexOf(" not")+5,command.indexOf(" do"));
			String value = "";
			try
			{
				Integer.parseInt(number);
				value = number;
			}catch (Exception e)
			{
				if(data.searchindex(number)!=-1)
				{
					value = data.get(number);
				}
				else
				{
					System.out.println("ERROR: " + number + " doesn't exist. Instruction number: " + pointer);
					System.exit(0);
				}
			}
			handlewhile(var,Integer.parseInt(value));
		}
		else if(Pattern.matches("if [a-zA-Z0-9_]+ not [0-9]+ do",command)) // command to handle if statements
		{
			String var = command.substring(3, command.indexOf(" not"));
			String number = command.substring(command.indexOf(" not")+5,command.indexOf(" do"));
			handleif(var,Integer.parseInt(number));
		}
		else if(Pattern.matches("print [a-zA-Z0-9_]+",command)) // command to print a variable to the console
		{
			String var = command.substring(command.indexOf("print ")+6,command.length());
			if(data.searchindex(var)!=-1)
			{
				//exists
				System.out.println(data.get(var));
			}
			else
			{
				//error
				System.out.println("Error! The variable: " + var + " doesn't exist! Instruction number: " + pointer);
				System.exit(0);
			}
		}
		else if(Pattern.matches("print \".*\"",command)) // print the contents of a quoted string
		{
			System.out.println(command.substring(command.indexOf("print ")+7,command.length()-1));
		}
		else if(Pattern.matches("getin [a-zA-Z0-9_]+",command)) // command to get integer values form the user
		{
			String var = command.substring(command.indexOf("getin ")+6,command.length());
			clear(var);
			String input = getIn();
			try
			{
				int value = Integer.parseInt(input);
				data.modify_data(var,Integer.toString(value));
			}catch (Exception e)
			{
				System.out.println("ERROR: COULD NOT CONVERT INPUT TO INTEGER");
				System.exit(0);
			}
		}
		else
		{
			System.out.println("ERROR: UNKNOWN COMMAND: " + command + ", instruction number: " + pointer);
			System.exit(0);
		}
	}
	
	/* 
 	* Function that actually begins running the program.
	* It simply executes each command in the queue,
	* increasing the instruction pointer until we finish all
	* queued instructions.
 	*/
	public void run()
	{
		while(pointer < queue.size())
		{
			execute_command(queue.get(pointer));
			pointer++;
		}
	}
	
	/* 
 	* Parses a single input line containing one or more semicolon-delimited commands.
 	*
 	* Expected format:
 	*   command1; command2; command3;
 	*
 	* Validation is performed first using a regex to ensure:
 	*   - commands are terminated with ';'
 	*   - only supported characters are present
 	*   - optional whitespace between commands is allowed
 	*
 	* Once validated, the line is scanned character-by-character:
 	*   - leading whitespace before each command is ignored
 	*   - characters are accumulated until a ';' delimiter is found
 	*   - completed commands are pushed into the execution queue
 	*
 	* If the line does not end with properly separated commands,
 	* execution is aborted with an error message.
 	*/
	private void process_line(String line)
	{
		/* If surrounded by semi-colons */
		if(Pattern.matches("(( |\t)*[a-zA-Z0-9_ \":]+;( )*)+",line))
		{
			Boolean tmpbool = true;
			String tmpstring = "";
			/* Time to add each part to the queue */
			for(int i = 0; i < line.length(); i++)
			{
				if(line.charAt(i) == ';')
				{
					tmpbool = true;
					queue.add(tmpstring);
					tmpstring = "";
				}
				else if(tmpbool)
				{
					if(line.charAt(i) != ' ' && line.charAt(i)!= '\t')
					{
						tmpbool = false;
						tmpstring = tmpstring + line.charAt(i);
					}
				}
				else
				{
					tmpstring = tmpstring + line.charAt(i);
				}
			}
		}
		else
		{
			System.out.println("ERROR! You are missing a line encloser (;)! Line: '" + line + "'");
			System.exit(0);
		}
	}
	
	/* Function to read in commands from the file into the queue */
	public void ReadFile(String file)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String tmpstring = "";
			// We read line by line, processing as we go along
			while((tmpstring = br.readLine()) != null)
			{
				process_line(tmpstring);
			}
		}catch(Exception e)
		{
			System.out.println("ERROR! CANNOT READ FROM FILE: " + file);
		}
	}

	/* Shows all the variables in memory */
	public void showall()
	{
		data.showall();
	}
}