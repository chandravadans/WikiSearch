import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class TitleIndexDriver {
	public static void MakeTitleIndex(String[] argv) throws IOException,SAXException
	{
		XMLReader p=XMLReaderFactory.createXMLReader();
		p.setContentHandler(new TitleIndexHandler());
		//String idxfile=argv[1];
		String idxfile=new String("TitleIndex.txt");
		//File file = new File(argv[0]);
		File file=new File("sample.xml");
		InputStream inputStream= new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream,"UTF-8");
		BufferedReader bufr=new BufferedReader(reader);

		InputSource is = new InputSource(bufr);
		is.setEncoding("UTF-8");
		long startTime=System.currentTimeMillis();
		p.parse(is);
		
		long endTime=System.currentTimeMillis();
		System.out.println((endTime-startTime)/1000+" sec taken for making title index");
	}
	void getTitle(long docid)
	{
		File xp=new File("hello");
		
	}
}


