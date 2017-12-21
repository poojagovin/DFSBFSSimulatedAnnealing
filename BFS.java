import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

class state
{
	int row, column;
	state prev;
	int liz=0;
	int spots=0;

	state() {}


	state(int row, int col, state prev, int liz, int spots)
	{
		this.row = row;
		this.column = col;
		this.prev = prev;
		this.liz = liz;
		this.spots = spots;
	}

	boolean hasConflict(state s,int rw, int col, int[][] a, int n)
	{
		while(s!=null)
		{
			a[rw][col]=1;
			a[s.row][s.column]=1;
			boolean hasNoTree=true;

			if(rw==s.row) 
			{
				int i= col<s.column? col : s.column;
				int max = col>s.column? col : s.column;
				while(i<max)
				{
					if(a[rw][i]==2)  hasNoTree=false;
					i++;
				}
				if(hasNoTree) 
				{
					a[rw][col]=0;
					a[s.row][s.column]=0;
					return true;
				}

			}

			if(col==s.column) 
			{
				int i= rw<s.row? rw : s.row;
				int max = rw>s.row? rw : s.row;

				while(i<max)
				{
					if(a[i][col]==2)  hasNoTree=false;
					i++;
				}
				if(hasNoTree) {
					a[rw][col]=0;
					a[s.row][s.column]=0;
					return true;
				}
			}

			if(rw>s.row)
			{
				int i=s.row, j=s.column;

				while(j<col && i<n)
				{
					if(a[i][j]==2) hasNoTree = false;
					i++; j++;
				}
				if(i==rw)
				{
					if(hasNoTree) {
						a[rw][col]=0;
						a[s.row][s.column]=0;
						return true;
					}
				}
				i=s.row; j=s.column;
				while(j>col && i<n)
				{
					if(a[i][j]==2) hasNoTree = false;
					i++;j--;

				}
				if(i==rw)
				{
					if(hasNoTree) {
						a[rw][col]=0;
						a[s.row][s.column]=0;
						return true;
					}
				}

			}
			else
			{
				int i=rw, j=col;
				while(j<s.column && i<n)
				{
					if(a[i][j]==2) hasNoTree = false;
					i++; j++;
				}
				if(i==s.row)
				{
					if(hasNoTree) {
						a[rw][col]=0;
						a[s.row][s.column]=0;
						return true;
					}
				}
				i=rw; j=col;
				while(j>s.column && i<n)
				{
					if(a[i][j]==2) hasNoTree = false;
					i++;j--;
				}
				if(i==s.row)
				{
					if(hasNoTree) {
						a[rw][col]=0;
						a[s.row][s.column]=0;
						return true;
					}
				}

			}
			a[rw][col]=0;
			a[s.row][s.column]=0;
			s=s.prev;
		}

		return false;
	}

	public int[][]BFS (int a[][], int n, int liz)
	{
		long startTime   = System.currentTimeMillis();
		int emptySpots = n*n;
		Queue<state> BFSQueue = new LinkedList<state>();

		for(short c=0;c<n;c++)
			for(short d=0;d<n;d++)
				if(a[c][d]==2) emptySpots--;
		if(liz>emptySpots) return null;

		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				if(a[i][j]==0)
				{
					BFSQueue.add(new state(i,j,null,liz-1,emptySpots-1));
				}
			}
		}
	

		while(true)
		{
			long endTime   = System.currentTimeMillis();
			if(( endTime - startTime) > 240000) return null;
			state s = null;
			try
			{
				s= BFSQueue.remove();
			}
			catch(Exception e) {return null;}
			
			if(s.liz==0)
			{
				while(s!=null)
				{
					a[s.row][s.column]=1;
				}
				return a;
			}

			for(int i=0;i<n;i++)
			{
				for(int j=0;j<n;j++)
				{
					if(a[i][j]==0)
					{
						if(i==s.row && j==s.column) continue;
						boolean conflict = hasConflict(s,i,j,a,n);
						if(conflict) continue;
						if(s.liz==1 && s.spots>0)
						{
							a[i][j] =1;
							state nav = new state(i,j,s,0,s.spots-1);
							while(nav!=null)
							{
								a[nav.row][nav.column]=1;
								nav=nav.prev;
							}
							return a;
						}
						state nav = new state(i,j,s,s.liz-1,s.spots-1);
						if(nav.liz>nav.spots) continue;
						BFSQueue.add(nav);
					}
				}
			}
		}


	}


	public int[][]DFS (int a[][], int n, int liz)
	{
		long startTime   = System.currentTimeMillis();
		int emptySpots = n*n;
		Stack<state> DFSStack = new Stack<state>();

		for(short c=0;c<n;c++)
			for(short d=0;d<n;d++)
				if(a[c][d]==2) emptySpots--;
		if(liz>emptySpots) return null;

		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				if(a[i][j]==0)
				{
					DFSStack.push(new state(i,j,null,liz-1,emptySpots-1));
				}
			}
		}
		liz--; emptySpots--;

		while(true)
		{
			long endTime   = System.currentTimeMillis();
			if(( endTime - startTime) > 288000) return null;
			state s = null;
			try
			{
				s= DFSStack.pop();
			}
			catch(Exception e) {return null;}

			for(int i=0;i<n;i++)
			{
				for(int j=0;j<n;j++)
				{
					if(a[i][j]==0)
					{
						if(i==s.row && j==s.column) continue;
						boolean conflict = hasConflict(s,i,j,a,n);
						if(conflict) continue;
						if(s.liz==1 && s.spots>0)
						{
							a[i][j] =1;
							state nav = new state(i,j,s,0,s.spots-1);
							while(nav!=null)
							{
								a[nav.row][nav.column]=1;
								nav=nav.prev;
							}
							return a;
						}
						state nav = new state(i,j,s,s.liz-1,s.spots-1);
						if(nav.liz>nav.spots) continue;
						DFSStack.push(nav);
					}
				}
			}
		}


	}

	public int getConflicts (int[][] a, int n)
	{
		int count=0; boolean flag=false;

		for(int i=0; i<n;i++)
		{
			for(int j=0; j<n;j++)
			{
				flag=false;

				if(a[i][j]==1)
				{
					int k=i-1;
					while(k>=0 && !flag)
					{
						if(a[k][j]==1) {flag=true; break;}
						if(a[k][j]==2) break;
						k--;
					}

					k= (i+1);
					while(k<n && !flag)
					{
						if(a[k][j]==1) {flag=true; break;}
						if(a[k][j]==2) break;
						k++;
					}

					k=j-1;
					while(k>=0 && !flag)
					{
						if(a[i][k]==1) {flag=true; break;}
						if(a[i][k]==2) break;
						k--;
					}

					k= (j+1);
					while(k<n && !flag)
					{
						if(a[i][k]==1) {flag=true; break;}
						if(a[i][k]==2) break;
						k++;
					}

					k=i-1;
					int l=j-1;
					while(!flag && k>=0 && l>=0)
					{
						if(a[k][l]==1) {flag=true; break;}
						else if(a[k][l]==2) break;
						k--;l--;


					}
					k=i-1;l=j+1;
					while(!flag && k>=0 && l<n)
					{
						if(a[k][l]==1) {flag=true; break;}
						else if(a[k][l]==2) break;
						k--;l++;
					}
					k=i+1;l=j+1;
					while(!flag && k<n && l<n)
					{
						if(a[k][l]==1) {flag=true; break;}
						else if(a[k][l]==2) break;
						k++;l++;

					}
					k=i+1;l=j-1;
					while(!flag && l>=0 && k<n)
					{
						if(a[k][l]==1) {flag=true; break;}
						else if(a[k][l]==2) break;
						k++;l--;
					}

					if(flag) count++;

				}
			}
		}
		return count;
	}

	public int[][] SA(int[][] a, int n, int liz)
	{
		int i=(int) (n-1),j=(int) (n-1);
		int spots = (int) (n*n);
		int newConflicts;


		for(i=(int) (n-1); i>=0;i--)
			for(j= (int) (n-1);j>=0;j--) 
			{
				if(a[i][j]==2) spots--;
			}

		if(liz>spots) return null;

		while((liz)!=0)
		{
			for(j=0;j<n;j++)
			{
				Random ran = new Random();
				i = ran.nextInt(n);
				if(liz!=0 && a[i][j]==0)
				{
					a[i][j] = 1;
					liz--;
				}
			}

		}

		double temperature = 1000000;
		double temp = temperature;
		double delta;
		double probability;
		int posi=0, posj=0;

		long startTime = System.currentTimeMillis();
		long endTime;
		int currentConflicts;

		int iteration=1;
		while(temp>0)
		{
			iteration++;

			currentConflicts = getConflicts ( a, n);
			if(currentConflicts==0 )
			{
				return a;
			}
			int[][] newState = new int[n][n];

			for(i=(int) (n-1); i>=0;i--)
			{ 
				for(j= (int) (n-1);j>=0;j--) 

				{
					newState[i][j] = a[i][j];

				}
			}

			Random randY = new Random();
			posi = randY.nextInt(n);
			randY = new Random();
			posj = randY.nextInt(n);

			while(newState[posi][posj]!=1)
			{
				if(posj<n-1) posj++;
				else if(posi<n-1) posi++;
				else { 
					randY = new Random();
					posi = randY.nextInt(n);
					randY = new Random();
					posj = randY.nextInt(n);
				}
			}
			
			endTime = System.currentTimeMillis()-startTime;
			if( endTime>288000)
			{
				return null;
			}



			randY = new Random();
			int randomi = randY.nextInt(n);
			randY = new Random();
			int randomj = randY.nextInt(n);
			while(a[randomi][randomj]!=0)
			{
				randY = new Random();
				randomi = randY.nextInt(n);
				randY = new Random();
				randomj = randY.nextInt(n);
			}

			newState[randomi][randomj]=1;
			newState[posi][posj]=0;

			newConflicts = getConflicts ( newState, n);
			delta =  currentConflicts - newConflicts;
			probability = Math.exp(delta/temp);
			double random = Math.random();


			if(delta>0)
			{

				a[randomi][randomj]=1;
				a[posi][posj]=0;
			}
			else if(random <= probability)
			{
				a[randomi][randomj]=1;
				a[posi][posj]=0;
			}

			temp =  (1/ (100 * Math.log(1+iteration)));

		}
		return null;

	}
}





public class BFS {


	public static void main(String[] args) {

		String fileName = "input.txt";
		String line = null;
		String searchMethod = null;
		int[][] a = null;
		int n=0, liz=0;
		state s = new state();

		File f = new File("output.txt");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			PrintWriter pw = new PrintWriter(fos);



			try {
				FileReader fileReader = new FileReader(fileName);

				BufferedReader bufferedReader = new BufferedReader(fileReader);

				line = bufferedReader.readLine();
				searchMethod = line;

				line = bufferedReader.readLine();
				n = Integer.parseInt(line);

				line = bufferedReader.readLine();
				liz = Integer.parseInt(line);

				a = new int[n][n];

				for(short i =0;i<n;i++)
				{
					line = bufferedReader.readLine();
					char[] arr = line.toCharArray();
					for(short j=0;j<n;j++)
					{
						if(arr[j]=='2') a[i][j]=2;
						else a[i][j]=0;
					}

				}

				bufferedReader.close();         
			}
			catch(Exception ex) {
				pw.write("FAIL")   ;            
			}

			if(searchMethod.equalsIgnoreCase("BFS"))
				a = s.BFS(a,n,liz);
			else if(searchMethod.equalsIgnoreCase("DFS"))
				a = s.DFS(a,n,liz);
			else if(searchMethod.equalsIgnoreCase("SA")) 
				a = s.SA(a,n,liz);
			else 
				a = s.DFS(a,n,liz);




			if(a==null) { pw.write("FAIL");}

			else
			{
				pw.write("OK\n");
				for(int i=0;i<n;i++) 
				{
					for(int j=0;j<n;j++)
					{
						if(a[i][j]==9) pw.write("0");
						else 
						{
							String str = String.valueOf(a[i][j]);
							pw.write(str); 
						}
					}
					pw.write("\n");
				}

			}

			pw.flush();

			fos.close();


			pw.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
