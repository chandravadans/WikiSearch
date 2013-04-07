import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class TitleIndexHandler extends DefaultHandler 
{
	File x;
	RandomAccessFile raf;
	File y;
	FileWriter outw;
	BufferedWriter out;
	String content;
	boolean id,title,flag;
	String docid;
	StringBuilder idbuffer=new StringBuilder();
	StringBuilder titbuffer=new StringBuilder();

	@Override
	public void startDocument() throws SAXException 
	{
		y=new File("TitleIndex.txt");
		outw = null;
		try 
		{
			outw = new FileWriter(y);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("IO Exception occured!");
			e.printStackTrace();
		}
		out=new BufferedWriter(outw);

	}

	@Override
	public void endDocument() throws SAXException 
	{
		try {

			out.close();
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes)	throws SAXException
	{
		if(qName=="id")
		{
			id=true;
		}
		if(qName=="title")
		{
			title=true;
			flag=true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
			{

		if(qName=="id")
		{
			id=false;
			flag=false;
		}
		else if(qName=="title")
		{
			title=false;

		}
		else if(qName=="page")
		{
			try {
				if(titbuffer.toString()!="0")
					out.write(docid+"~"+titbuffer.toString()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			titbuffer.delete(0, titbuffer.length());
			//docid=0;
			/*try {
				//out.write(docid+" "+titbuffer.toString()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
		if(qName.equalsIgnoreCase("file"))
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
			}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException 
			{
		if(id==true)
		{
			id=false;
			if(flag==true)
			{
				//System.out.println(new String(ch,start,length));
				docid=(Integer.toHexString(Integer.parseInt(new String(ch,start,length))));
				flag=false;
			}
		}
		if(title==true)
		{
			
			titbuffer.append(ch,start,length);
			title=false;
			
		}
			}
}
