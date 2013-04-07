import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class PIndexDriver {

public static String idxfile;

	public static void priIndx(String[] argv) throws IOException,SAXException 
	{
		XMLReader p=XMLReaderFactory.createXMLReader();
		p.setContentHandler(new PIndexHandler());
		
		idxfile="smallindex";
		
		File file = new File("file.xml");
		InputStream inputStream= new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream,"UTF-8");

		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		long startTime=System.currentTimeMillis();
		p.parse(is);
		
		long endTime=System.currentTimeMillis();
		System.out.println((endTime-startTime)/1000+" sec taken");


	}

}
