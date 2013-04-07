import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.*;



public class PIndexHandler extends DefaultHandler 
{
	
	private class Tuple 
	{
		private int fileno;
		private int times;
		private double tf;
		public int info;
		public int text;
		public int outl;
		public int titl;
		public int catg;

		public Tuple()
		{
			titl=0;
			catg=0;
			info=0;
			text=0;
			outl=0;
			times=0;
		}

		public Tuple(int fileno, int times,int cat) 
		{
			this.fileno = fileno;
			this.times = times;
			tf=0;
			switch(cat)
			{
			case 0:inctitl();break;
			case 1:inccateg();break;
			case 2:incinfo();break;
			case 3:incoutl();break;
			case 4:inctext();break;
			}
		}
		public int getFileNo()
		{
			return this.fileno;
		}
		public void incTimes()
		{
			this.times++;
		}
		public void inctitl()
		{
			this.titl++;
		}
		public void inccateg()
		{
			this.catg++;
		}
		public void incinfo()
		{
			this.info++;
		}
		public void inctext()
		{
			this.text++;
		}
		public void incoutl()
		{
			this.outl++;
		}

	}
	int filenum;
	public static int npage=1,ntitle,ntext;
	int pages=0;
	int numtoks=0;
	int docid=1;
	int LIM=1000;
	long startime;
	HashMap<String, Tuple> index = new HashMap<String, Tuple>();
	TreeMap<String,String> globalHashTab=new TreeMap<String,String>();

	//Paice stemmer=new Paice("stemrules.txt");

	public static String stops[]={"a","able","about","above","abst","accordance","according","accordingly","across","act","actually","added","adj","affected","affecting","affects","after","afterwards","again","against","ah","all","almost","alone","along","already","also","although","always","am","among","amongst","an","and","announce","another","any","anybody","anyhow","anymore","anyone","anything","anyway","anyways","anywhere","apparently","approximately","are","aren","arent","arise","around","as","aside","ask","asking","at","auth","available","away","awfully","b","back","be","became","because","become","becomes","becoming","been","before","beforehand","begin","beginning","beginnings","begins","behind","being","believe","below","beside","besides","between","beyond","biol","both","brief","briefly","but","by","c","ca","came","can","cannot","can't","cause","causes","certain","certainly","co","com","come","comes","contain","containing","contains","could","couldnt","d","date","did","didn't","different","do","does","doesn't","doing","done","don't","down","downwards","due","during","e","each","ed","edu","effect","eg","eight","eighty","either","else","elsewhere","end","ending","enough","especially","et","et-al","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","except","f","far","few","ff","fifth","first","five","fix","followed","following","follows","for","former","formerly","forth","found","four","from","further","furthermore","g","gave","get","gets","getting","give","given","gives","giving","go","goes","gone","got","gotten","h","had","happens","hardly","has","hasn't","have","haven't","having","he","hed","hence","her","here","hereafter","hereby","herein","heres","hereupon","hers","herself","hes","hi","hid","him","himself","his","hither","home","how","howbeit","however","hundred","i","id","ie","if","i'll","im","immediate","immediately","importance","important","in","inc","indeed","index","information","instead","into","invention","inward","is","isn't","it","itd","it'll","its","itself","i've","j","just","k","keep","	keeps","kept","kg","km","know","known","knows","l","largely","last","lately","later","latter","latterly","least","less","lest","let","lets","like","liked","likely","line","little","'ll","look","looking","looks","ltd","m","made","mainly","make","makes","many","may","maybe","me","mean","means","meantime","meanwhile","merely","mg","might","million","miss","ml","more","moreover","most","mostly","mr","mrs","much","mug","must","my","myself","n","na","name","namely","nay","nd","near","nearly","necessarily","necessary","need","needs","neither","never","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","nor","normally","nos","not","noted","nothing","now","nowhere","o","obtain","obtained","obviously","of","off","often","oh","ok","okay","old","omitted","on","once","one","ones","only","onto","or","ord","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","owing","own","p","page","pages","part","particular","particularly","past","per","perhaps","placed","please","plus","poorly","possible","possibly","potentially","pp","predominantly","present","previously","primarily","probably","promptly","proud","provides","put","q","que","quickly","quite","qv","r","ran","rather","rd","re","readily","really","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results","right","run","s","said","same","saw","say","saying","says","sec","section","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sent","seven","several","shall","she","shed","she'll","shes","should","shouldn't","show","showed","shown","showns","shows","significant","significantly","similar","similarly","since","six","slightly","so","some","somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically","specified","specify","specifying","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure","t","take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that'll","thats","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","thered","therefore","therein","there'll","thereof","therere","theres","thereto","thereupon","there've","these","they","theyd","they'll","theyre","they've","think","this","those","thou","though","thoughh","thousand","throug","through","throughout","thru","thus","til","tip","to","together","too","took","toward","towards","tried","tries","truly","try","trying","ts","twice","two","u","un","under","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups","us","use","used","useful","usefully","usefulness","uses","using","usually","v","value","various","'ve","very","via","viz","vol","vols","vs","w","want","wants","was","wasn't","way","we","wed","welcome","we'll","went","were","weren't","we've","what","whatever","what'll","whats","when","whence","whenever","where","whereafter","whereas","whereby","wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","whod","whoever","whole","who'll","whom","whomever","whos","whose","why","widely","willing","wish","with","within","without","won't","words","world","would","wouldn't","www","x","y","yes","yet","you","youd","you'll","your","youre","yours","yourself","yourselves","you've","z","zero","infobox","aa","aaa","aaaa","aaaaa"};
	public static Set<String> stopWords=new HashSet<String>(Arrays.asList(stops));
	boolean flag=false;
	Pattern pat=Pattern.compile("\\{\\{.*(i|I)nfobox(.*?)\\}\\}|\\[\\[([^:]*?)\\]\\]?|\\[\\[Category:(.*?)\\]\\]?");;
	Matcher mat;

	String extras="[,\\.\\s]+";
	Pattern splitter=Pattern.compile(extras);

	StringBuilder idbuffer=new StringBuilder();
	StringBuilder categbuffer=new StringBuilder();
	StringBuilder titbuffer=new StringBuilder();
	StringBuilder textbuffer=new StringBuilder();
	StringBuilder outlbuffer=new StringBuilder();
	StringBuilder infobuffer=new StringBuilder();

	String idstr=new String();
	String categstr=new String();
	String titstr=new String();
	String textstr=new String();
	String outlstr=new String();
	String infostr=new String();

	String idtoks[];
	String categtoks[];
	String tittoks[];
	String texttoks[];
	String outltoks[];
	String infotoks[];

	StringBuilder p=new StringBuilder();
	StringBuilder temp;


	boolean page,title,id,text,categ,outl;			//Which tag is it?
	String idxpath;
	@Override
	public void startDocument() throws SAXException 
	{
		index.clear();
		idxpath=PIndexDriver.idxfile;
		System.out.println(idxpath);
	}

	@Override
	public void endDocument() throws SAXException 
	{
	}


	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) 
			throws SAXException 
			{
		if(qName=="page")
		{
			
			index.clear();
			numtoks=0;
			pages++;
			page=true;

		}
		if(qName=="id")
		{
			id=true;
		}
		if(qName=="text")
		{
			text=true;
		}
		if(qName=="title")
		{	title=true;
		flag=true;
		}
			}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException 
			{
		if(qName=="page")
		{
			
			numtoks=0;
			page=false;
			flag=false;
			{
				if(pages>LIM)
				{
					dump();
					pages=0;
				}
			}
		}
		else if(qName=="id")
		{
			id=false;
			flag=false;
		}
		else if(qName=="text")
		{
			if(text==true)
			{
				text=false;
				textstr=textbuffer.toString().trim();
				textstr =textstr.replaceAll("[^\\p{ASCII}]","");
				textstr =textstr.replaceAll("<!--.*?-->","");
				textstr =textstr.replaceAll("((<ref .*>.*?</ref>))","");

				mat=pat.matcher(textstr);

				while(mat.find())
				{
					if(mat.group(2) !=null)
					{
						infobuffer=infobuffer.append( mat.group(2).toString().trim()+" ");
					}
					if(mat.group(3) !=null)
						outlbuffer=outlbuffer.append( mat.group(3).toString().trim()+" ");
					if(mat.group(4) !=null)
						categbuffer=categbuffer.append(mat.group(4).toString().trim()+" ");
				}


				textstr=textbuffer.toString().replaceAll("\\{\\{.*Infobox(.*?)\\}\\}","");
				textstr=textstr.replaceAll("\\[\\[.*?\\]\\]","");

				//Splitup into tokens
				tittoks=splitter.split(titbuffer.toString());
				categtoks=splitter.split(categbuffer.toString());
				infotoks=splitter.split(infobuffer.toString());
				outltoks=splitter.split(outlbuffer.toString());
				texttoks=splitter.split(textstr);

				updateStats(tittoks,0);
				updateStats(categtoks,1);
				updateStats(infotoks,2);
				updateStats(outltoks,3);
				updateStats(texttoks,4);

				insertIntoGlobalHash();
			}
			textbuffer.setLength(0);
			titbuffer.setLength(0);
			idbuffer.setLength(0);
			categbuffer.setLength(0);
			outlbuffer.setLength(0);
			infobuffer.setLength(0);


		}
		else if(qName=="title")
		{
			title=false;

		}
		if(qName.equalsIgnoreCase("file"))
		{


			try
			{
				dump();
				npage=npage-1;
				long parsers=System.currentTimeMillis();
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
			catch (Exception e) 
			{
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
				idbuffer.delete(0, idbuffer.length());
				idbuffer.append(ch,start,length);
				docid=Integer.parseInt(idbuffer.toString().trim());
				flag=false;
			}
		}
		else if(title==true)
		{
			titbuffer.append(ch,start,length);
			titstr=titbuffer.toString().trim();
			title=false;
		}
		else if(text==true)
		{
			textbuffer.append(ch,start,length);

		}
			}

	public void merge(int i,int j)
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
	public int func(String s1,String s2)
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


	void insertIntoGlobalHash()
	{
		p.setLength(0);
		String tmpstr=new String();

		Iterator<String> iterator=index.keySet().iterator();
		while(iterator.hasNext())
		{
			p.setLength(0);
			String key=iterator.next().toString();
			Tuple postings=index.get(key);
			p.append(postings.fileno+"-"+postings.times+"-");
			if(postings.titl>0)
				p.append("T"+postings.titl);
			if(postings.catg>2)
				p.append("C"+postings.catg);
			if(postings.info>2)
				p.append("I"+postings.info);
			if(postings.outl>2)
				p.append("L"+postings.outl);
			if(postings.text>2)
				p.append("X"+postings.text);
			p.append("*");

			tmpstr=globalHashTab.get(key);
			if(tmpstr==null)
			{
				StringBuilder x=new StringBuilder(key+":");
				x.append(p);
				globalHashTab.put(key, x.toString());
			}
			else
			{
				temp=new StringBuilder(tmpstr);
				temp.append(p.toString());
				globalHashTab.remove(key);
				globalHashTab.put(key, temp.toString());

			}
		}
	}

	void updateStats(String tokens[],int cat)
	{
		numtoks=tokens.length;
		for(int i=0;i<numtoks;i++)
		{
			tokens[i]=tokens[i].toLowerCase();
			if(tokens[i].length()>=3)
			{
			if(tokens[i]!="")
			{
				if(stopWords.contains(tokens[i])==false)
				{
					if(tokens[i].length()>3&&tokens[i].length()<15 && (tokens[i].matches("^[A-Z a-z]+")))
					{

						Stemmer stemmer=new Stemmer();
						stemmer.add(tokens[i].toCharArray(),tokens[i].length());
						stemmer.stem();
						tokens[i]=stemmer.toString();

						Tuple idx = index.get(tokens[i]);
						if (idx == null) 
						{
							idx = new Tuple(docid, 1,cat);
							idx.fileno=docid;
							index.put(tokens[i], idx);
						}
						else
						{
							idx.incTimes();
							idx.fileno=docid;
							switch(cat)
							{
							case 0:index.get(tokens[i]).inctitl();;break;
							case 1:index.get(tokens[i]).inccateg();break;
							case 2:index.get(tokens[i]).incinfo();break;
							case 3:index.get(tokens[i]).incoutl();break;
							case 4:index.get(tokens[i]).inctext();break;
							}


						}
					}

				}
			}
		}
		}
	}

	void dump()
	{
		try{

			String fname=idxpath+"_"+npage+".txt";

			FileOutputStream myfile;

			myfile=new FileOutputStream(""+fname);

			Iterator<String> iterator=globalHashTab.keySet().iterator();
			while(iterator.hasNext())
			{
				String key=iterator.next().toString();
				String val=globalHashTab.get(key)+"\n";
				byte[] buffer=val.getBytes();
				myfile.write(buffer);
			}
			npage++;
			myfile.close();
			globalHashTab.clear();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
}
