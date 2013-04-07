import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.TreeMap;



public class SecondLevel {
	
	char FILEDELIM='*';
	char FIELDELIM='-';
	
	public static void makeSecondLevelIndex(String input,String output)
	//public static void main(String args[])
	{
		try
		{
			//String input="TitleIndex.txt";
			File x=new File(input);
			RandomAccessFile raf=new RandomAccessFile(x,"r");
			//String output="SecondTitle.txt";
			File y=new File(output);
			FileWriter outw=new FileWriter(y);
			BufferedWriter out=new BufferedWriter(outw);
			//FileChannel get=raf.getChannel();
			
			String content;
			String toks[];
			long pos=0;
			content=raf.readLine();
			for(int i=0;;i++)
			{
				if(i%100==0)
				{
					toks=content.split("~");
					out.write(toks[0]+"~"+pos+"\n");
				}
				pos=raf.getFilePointer();
				content=raf.readLine();
				if(content==null)
					break;
			}
			out.close();
			raf.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void readSecondLevelIndex()
	{
		String as[];
		String s;
		BufferedReader in = null;

		try {
			MainDriver.secondLevelIndex = new TreeMap<String, Long>();
			in = new BufferedReader(new FileReader("SecondLevel.txt"));

			s = in.readLine();

			while (s != null) {
				as = s.split(" ");
				MainDriver.secondLevelIndex.put(as[0], Long.parseLong(as[1]));

				s = in.readLine();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	public void readSecondLevelTitleIndex()
	{
		String as[];
		String s;
		BufferedReader in = null;

		try {
			MainDriver.secondLevelTitleIndex = new TreeMap<Long, Long>();
			in = new BufferedReader(new FileReader("SecondTitle.txt"));

			s = in.readLine();

			while (s != null) {
				as = s.split("~");
				MainDriver.secondLevelTitleIndex.put(Long.parseLong(as[0],16), Long.parseLong(as[1]));

				s = in.readLine();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	public String fetchPostings(String word)
	{
		long offset1 = 0,offset2 = 0,offset=-999;
		int lo=0,hi=MainDriver.secondLevelWords.size();
		int curr=(lo+hi)/2;
		if(MainDriver.secondLevelWords.contains(word))
		{
			offset=MainDriver.secondLevelOffsets.get(MainDriver.secondLevelWords.indexOf(word));			
		}
		else
		{
			for(int i=0;i<hi;i++)
			{
				if(MainDriver.secondLevelWords.get(i).compareTo(word)>0)
				{
					//System.out.println("hi");
					hi=i;
					lo=i-1;
					break;
				}
			}
			while((hi-lo)!=1)
			{
				if(MainDriver.secondLevelWords.get(curr).compareTo(word)>0)
				{
					//word<curr, switch hi
					hi=curr;
					curr=(lo+hi)/2;
				}
				else
				{
					lo=curr;
					curr=(lo+hi)/2;
				}
				//System.out.println(lo+" "+hi);
			}
		}

		offset1=MainDriver.secondLevelOffsets.get(lo);
		if(hi==MainDriver.secondLevelOffsets.size())
				hi--;
		offset2=MainDriver.secondLevelOffsets.get(hi);
		//System.out.println(offset2-offset1);
		
		/*if(offset!=-999)
		{
			System.out.println("in secondlevel index, offset is "+ offset);
		}
		else
		{
			System.out.println(offset1+" "+offset2);
		}*/
				
		File x=new File("bigindx_1.txt");
		RandomAccessFile raf=null;
		long bytesToRead=offset2-offset1;
		if(bytesToRead>21911365)
			bytesToRead=21911365;
		//System.out.println(bytesToRead);
		byte[] b=new byte[(int) (bytesToRead)+10];
		try 
		{
			
			raf=new RandomAccessFile(x,"r");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		if(offset!=-999)		//Direct hit, just seek and readline
		{
			try {
				raf.seek(offset);
				return raf.readLine();
			} 
			catch (IOException e) 
			{
				
					e.printStackTrace();
			}
			
		}
		else
		{
			try {
					raf.seek(offset1);
					raf.read(b,0,(int)bytesToRead);
					raf.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String s=new String(b);
		if(MainDriver.correct)
		{
			System.out.println("Similar: ");
			MainDriver.suggestions.suggestnew(word);
			System.out.println();
		}
		int pos=s.indexOf(word);
		if(pos==-1)
		{
			System.out.println("Term not found!");
			//MainDriver.suggestions.suggestnew(word);
			return null;
		}
		//System.out.println(pos);
		StringBuilder sb=new StringBuilder();
		int indx=s.lastIndexOf("*");
		for(int i=pos;i<indx;i++)
		{
			sb.append(s.charAt(i));
		}
		return sb.toString();
		}
	
	public String fetchTitle(Long word)
	{
		long offset1 = 0,offset2 = 0,offset=-999;
		int lo=0,hi=MainDriver.secondLevelTitles.size()-3;
		int curr=(lo+hi)/2;
		if(MainDriver.secondLevelTitles.contains(word))
		{
			offset=MainDriver.secondLevelTitleOffsets.get(MainDriver.secondLevelTitles.indexOf(word));			
		}
		else
		{
			while((hi-lo)!=1)
			{
				if(MainDriver.secondLevelTitles.get(curr)>word)
				{
					//word<curr, switch hi
					hi=curr;
					curr=(lo+hi)/2;
				}
				else
				{
					lo=curr;
					curr=(lo+hi)/2;
				}
			}
		}

		offset1=MainDriver.secondLevelTitleOffsets.get(lo);
		offset2=MainDriver.secondLevelTitleOffsets.get(hi);
		/*if(offset!=-999)
		{
			System.out.println("in secondlevel index, offset is "+ offset);
		}
		else
		{
			System.out.println(offset1+" "+offset2);
		}*/
				
		File x=new File("TitleIndex.txt");
		RandomAccessFile raf=null;
		long bytesToRead=offset2-offset1;
	
		byte[] b=new byte[(int) (bytesToRead)+10];
		try 
		{
			
			raf=new RandomAccessFile(x,"r");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		if(offset!=-999)		//Direct hit, just seek and readline
		{
			try {
				raf.seek(offset);
				return raf.readLine();
			} 
			catch (IOException e) 
			{
					e.printStackTrace();
			}
			
		}
		else
		{
			try {
					raf.seek(offset1);
					raf.read(b,0,(int)bytesToRead);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String s=new String(b);
		//System.out.println(s);
		String hexword=Long.toHexString(word);
		
		int pos=s.indexOf(hexword);
		//System.out.println(pos);
		StringBuilder sb=new StringBuilder();
		pos=s.indexOf('~', pos);
		//int len=s.indexOf("[0-9]", pos);
		for(int i=pos+1;s.charAt(i)!='\n';i++)
		{
			sb.append(s.charAt(i));
		}
		return sb.toString();
		}


}






