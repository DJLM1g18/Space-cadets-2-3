/* Includes */
import java.io.*;
import java.lang.Boolean;
import java.util.Arrays;

/* Dynamic data structure object */
public class Memory
{
	/* Variables to hold both the data itself and the index of the data */
	private String[] data;
	private String[] index;
	/* Variable to store most recent search */
	private int search;
	
	/* Object constructor */
	public Memory()
	{
		data = new String[0]; //intiailize
		index = new String[0]; //initialize
		search = 0; //initialize
	}
	
	/* Function to add to the data structure */
	/* Uses overloading and recursion. We use the binary search method for efficiency */
	public void add(String indexname,String dataitem)
	{
		/* As long as the entry doesn't already exist */
		if(searchindex(indexname)==-1 && indexname.length()!=0)
		{
			if(index.length!=0) //non zero length
			{
				add(indexname, dataitem, 0, index.length - 1); //start adding process
			}
			else
			{
				add(indexname, dataitem, 0, true); //add at 0th position
			}
		}	
	}
	private void add(String indexname,String dataitem,int lower,int upper)
	{
		/* Found */
		int middle = (lower+upper)/2; //calculate middle value
		if(upper-lower==1)
		{
			if(indexname.compareTo(index[upper])>0)add(indexname,dataitem,upper+1,false);//higher
			else if(indexname.compareTo(index[lower])<0)add(indexname,dataitem,lower,false); //lower
			else add(indexname,dataitem,upper,false);//middle
		}
		/* Do comparison */
		else if(indexname.compareTo(index[middle])<0)
		{
			//lower
			if(middle==lower)add(indexname,dataitem,lower,false);//done
			else add(indexname,dataitem,lower,middle-1);
		}
		else if(indexname.compareTo(index[middle])>0)
		{
			//higher
			if(middle==upper)add(indexname,dataitem,upper+1,false); //done
			else add(indexname,dataitem,middle+1,upper);
		}
	}
	private void add(String indexname,String dataitem,int place,Boolean zero)
	{
		/* Temporary arrays */
		String[] indextmp = new String[index.length+1];
		String[] datatmp = new String[data.length+1];
		//if not zero
		if(!zero)
		{
			/* We begin by making copies of the original arrays */
			indextmp = Arrays.copyOf(index,index.length+1);
			datatmp = Arrays.copyOf(data,data.length+1);
			
			/* If the place is right at the end */
			if(place == index.length)
			{
				indextmp[place] = indexname;
				datatmp[place] = dataitem;
			}
			else
			{
				//somewhere else in the array
				for(int i = index.length; i > place; i--) //move everything up as required
				{
					indextmp[i] = indextmp[i-1];
					datatmp[i] = data[i-1];
				}
				indextmp[place] = indexname;
				datatmp[place] = dataitem;
			}
		}
		else
		{
			//zero
			indextmp[0] = indexname;
			datatmp[0] = dataitem;
		}
		//replace
		data = datatmp;
		index = indextmp;
	}
	/* Function to search (using binary search) */
	/* Again, we use recursion and overloading */
	public int searchindex(String indexname)
	{
		if(index.length!=0)searchindex(indexname,0,index.length-1);//so long as not zero
		else return (search=-1); //not here because array is empty
		return search; //return the most recently searched item
	}
	private void searchindex(String indexname,int lower,int upper)
	{
		/* Found */
		int middle = (lower+upper)/2; //calculate middle value
		if(upper-lower<=1)
		{
			if(index[upper].compareTo(indexname) == 0)search=upper;//higher
			else if(index[lower].compareTo(indexname) == 0)search=lower;//lower
			else search=-1;//not here
		}
		/* Do comparison */
		else if(indexname.compareTo(index[middle])<0)
		{
			//lower
			if(middle==lower)search=-1;//done, not here
			else searchindex(indexname,lower,middle-1);
		}
		else if(indexname.compareTo(index[middle])>0)
		{
			//higher
			if(middle==upper)search=-1; //done
			else searchindex(indexname,middle+1,upper);
		}
		else
		{
			//there first time
			search = middle; //gotten
		}
	}
	/* Function to modify entries data part */
	public void modify_data(String indexname, String newdata)
	{
		int tmpint = searchindex(indexname); //search for index
		if(tmpint != -1) //so long as the entry actually is in there
		{
			data[tmpint] = newdata; //put in the new data
		}
	}
	/* Function to remove entries */
	public void remove(String indexname)
	{
		int tmpint = searchindex(indexname); //get the place of the item
		if(tmpint != -1 && index.length!=0) //if it actually exists and the size isn't already 0
		{ 
			//temporary arrays
			String[] indextmp = new String[index.length-1];
			String[] datatmp = new String[data.length-1];
			
			/* Add all items until place we want to remove */
			for(int i = 0; i < tmpint; i++)
			{
				//copy
				indextmp[i] = index[i];
				datatmp[i] = data[i];
			}
			for(int i = tmpint; i < indextmp.length; i++)
			{
				//continue copying
				indextmp[i] = index[i+1];
				datatmp[i] = data[i+1];
			}
			//replace
			data = datatmp;
			index = indextmp;
		}
	}
	/* Function to get data at that index */
	public String get(String indexname)
	{
		//if exists, return it
		int tmpint = searchindex(indexname);
		if(tmpint!=-1)return data[tmpint];
		else return null;
	}
	/* Get size of arrays */
	public int size()
	{
		return index.length;
	}
	/* Print all data to console */
	public void showall()
	{
		/* Loop */
		for(int i = 0; i < this.size(); i++)
		{
			System.out.println("Variable: " + index[i] + ", data: " + data[i]);
		}
	}
}