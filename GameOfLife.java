//Type exit once your input is complete.

import java.io.*;
public class GameOfLife {
	String[] strIndex;
	int[][] index;
	int size;
	boolean block[][];
	
	GameOfLife()
	{
		size=0;
	}

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s,str="";
        GameOfLife obj= new GameOfLife();
        while((s = br.readLine()) != null)
        {
        	s=s.trim();
        	if(s.equalsIgnoreCase("exit"))
        		break;
        	str=str+s+",";
        }
        obj.store_input(str);
        obj.create_universe();
        obj.initial_universe();
        obj.next_tick();
       // obj.print_next_tick();
	}

	//Calculates the block size to be displayed, based on user input
	public void find_block_size(int j)
	{
		int max=0;
		for(int i=0;i<j;i++)
		{
			if( index[i][0]>index[i][1])
				max=index[i][0];
			else
				max=index[i][1];
			if(max>size)
				size=max;
		}
	}
	
	//stores the indexes of live cells in index 2D array
	public void store_input(String str)
	{
		strIndex=str.split(",");
		initialize_index(strIndex.length);
		int j=0;
		for( int i=0;i<strIndex.length;i=i+2)
		{
			index[j][0]=Integer.parseInt(strIndex[i].trim());
			index[j][1]=Integer.parseInt(strIndex[i+1].trim());
			j++;
		}
		find_block_size(j);
	}
	public void initialize_index(int n)
	{
		index=new int[n/2][2];
	}
	
	//Create block and initialize it with false
	public void create_universe()
	{
		block=new boolean[size+2][size+2];
		//System.out.println("size="+size);
		for(int i=0;i<=size;i++)
		{
			for(int j=0;j<=size;j++)
			{
				block[i][j]=false;
			}
		}
	}
	
	//Initialize universe for first tick
	public void initial_universe()
	{
		for(int i=0;i<index.length;i++)
		{
			block[index[i][0]][index[i][1]]=true;
		}
	}
	
	//find the next tick
	public void next_tick()
	{
		for(int i=0;i<=size;i++)
		{
			for(int j=0;j<=size;j++)
			{
				count_live_dead(i,j);
			}
		}
	}
	public void count_live_dead(int i, int j)
	{
		int live=0,dead=0;
		if(i>0){
			if(block[i-1][j])
				live+=1;
			else
				dead+=1;
			
			if(block[i-1][j+1])
				live+=1;
			else
				dead+=1;
		}
		if(j>0){
			if(block[i][j-1])
				live+=1;
			else
				dead+=1;
				
			if(block[i+1][j-1])
				live+=1;
			else
				dead+=1;
		}
		if(i>0 && j>0)
		{
			if(block[i-1][j-1])
				live+=1;
			else
				dead+=1;
		}
		if(block[i+1][j])
			live+=1;
		else
			dead+=1;
		
		if(block[i][j+1])
			live+=1;
		else
			dead+=1;
		
		if(block[i+1][j+1])
			live+=1;
		else
			dead+=1;
		
		check_status(live,dead,i,j);
	}
	
	
	//decide the next tick's cell status based on no. of live and dead cells.
	public void check_status(int live, int dead, int i, int j)
	{
		if(block[i][j])
		{
			if(live<2)
			{
				//block[i][j]=false;
				return;
			}
			if(live>3)
			{
				//block[i][j]=false;
				return;
			}
			System.out.println(i+","+j);
		}
		else
		{
			if(live==3)
			{
				//block[i][j]=true;
				System.out.println(i+","+j);
				return;
			}
		}
		return;
	}
	
	/*public void print_next_tick()
	{
		System.out.println("Final");
		print_block();
		for(int i=0;i<=size;i++)
		{
			for(int j=0;j<=size;j++)
			{
				if(block[i][j])
					System.out.println(i+","+j);
			}
		}
	} */

}
