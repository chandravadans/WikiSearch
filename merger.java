import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class merger {
	
	static String idxpath="bigindx";
	static int filenum;

	
	public static void mani(String[] args) 
	{
		long parsers=System.currentTimeMillis();
		int npage=3;
		File f=new File("a");
		while(npage!=1)
		{
			filenum=0;
			if(npage%2==0)
			{
				for(int i=1;i<=npage;i=i+2)
				{	filenum++;
				merge(i,i+1);
				}
				npage=npage/2;
			}
			else
			{
				for(int i=1;i<npage;i=i+2)
				{	filenum++;
				merge(i,i+1);
				}
				f = new File(idxpath+"_"+Integer.toString(npage)+".txt");
				filenum++;
				f.renameTo(new File(idxpath+"_"+Integer.toString(filenum)+".txt"));
				npage=npage/2 + 1;

			}
			
		}
		long parserend=System.currentTimeMillis();
		f.renameTo(new File(idxpath));
		System.out.println((parserend-parsers)/1000+" sec to merge");
		

	}
	public static void merge(int i,int j)
	{
		
		try{
			FileWriter fstream = new FileWriter(idxpath+"_temp.txt");
			BufferedWriter out = new BufferedWriter(fstream);

			File f1=new File(idxpath+"_"+Integer.toString(i)+".txt");
			BufferedReader br1 = new BufferedReader(new FileReader(f1));
			File f2=new File(idxpath+"_"+Integer.toString(j)+".txt");
			BufferedReader br2 = new BufferedReader(new FileReader(f2));
			String s1="";
			String s2="";
			int status=0;
			s1=br1.readLine();
			s2=br2.readLine();
			while(true)
			{
				status=func(s1,s2);
				if(status<0)
				{
					out.write(s1+"\n");
					if((s1=br1.readLine())!=null)
					{
						continue;
					}
					else
					{
						br1.close();
						out.write(s2+"\n");
						while((s2=br2.readLine())!=null)
						{
							out.write(s2+"\n");
						}
						break;

					}

				}
				else if(status>0)
				{
					out.write(s2+"\n");
					if((s2=br2.readLine())!=null)
					{
						continue;
					}
					else
					{
						br2.close();
						out.write(s1+"\n");
						while((s1=br1.readLine())!=null)
						{
							out.write(s1+"\n");
						}
						break;
					}
				}
				else if(status==0)
				{
					out.write(s1+s2.split(":")[1]+"\n");
					if((s1=br1.readLine())!=null)
					{

					}
					else
					{
						br1.close();
						while((s2=br2.readLine())!=null)
						{
							out.write(s2+"\n");
						}
						break;

					}
					if((s2=br2.readLine())!=null)
					{}
					else
					{
						br2.close();
						while((s1=br1.readLine())!=null)
						{
							out.write(s1+"\n");
						}
						break;
					}
				}

			}
			out.close();
			fstream.close();
			f1.delete();
			f2.delete();
			File f = new File(idxpath+"_temp.txt");
			f.renameTo(new File(idxpath+"_"+Integer.toString(filenum)+".txt"));
		}
		catch (Exception e)
		{//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	public static int func(String s1,String s2)
	{
		int len1=s1.length();
		int len2=s2.length();
		String s_1="";
		String s_2="";
		for(int i=0;i<len1;i++)
		{
			if(s1.charAt(i)!=':')
				s_1=s_1+Character.toString(s1.charAt(i));
			else
				break;
		}
		for(int i=0;i<len2;i++)
		{
			if(s2.charAt(i)!=':')
				s_2=s_2+Character.toString(s2.charAt(i));
			else
				break;
		}

		return s_1.compareTo(s_2);
	}



}
