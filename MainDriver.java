import java.awt.List;
import java.awt.TextField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;


public class MainDriver {
	public static TreeMap<String, Long> secondLevelIndex;
	public static TreeMap<Long,Long> secondLevelTitleIndex;
	public static Vector<String>secondLevelWords;
	public static Vector<Long>secondLevelOffsets;
	public static Vector<Long>secondLevelTitles;
	public static Vector<Long>secondLevelTitleOffsets;
	static SecondLevel x=new SecondLevel();

	static boolean Tfield=false;
	static boolean Xfield=false;
	static boolean Lfield=false;
	static boolean Ifield=false;
	static boolean Cfield=false;
	public static boolean bad=false;
	public static boolean correct=false;
	public static Possible suggestions=null;
	//static long endt=0;
	//static long start=0;

	public static void main(String[] args) throws IOException 
	{


		long start=0;
		x.readSecondLevelIndex();
		//System.out.println("Reading Secondary Took "+(System.currentTimeMillis()-start)+"ms");
		secondLevelWords=new Vector<String>(secondLevelIndex.keySet());
		secondLevelOffsets=new Vector<Long>(secondLevelIndex.values());
		//System.out.println(secondLevelOffsets.size());
		x.readSecondLevelTitleIndex();
		//System.out.println("Reading Title Took "+(System.currentTimeMillis()-start)+"ms");
		secondLevelTitles=new Vector<Long>(secondLevelTitleIndex.keySet());
		secondLevelTitleOffsets=new Vector<Long>(secondLevelTitleIndex.values());
		//System.out.println("titlemap size: "+secondLevelTitleIndex.size());
		System.out.println("Do you want suggestions(yes/NO)? (Experimental, might break!)");
		Scanner inp=new Scanner(System.in);
		String opt=inp.next();
		if(opt.compareToIgnoreCase("yes")==0)
		{
			correct=true;
			suggestions=new Possible();
			Possible.setup();
		}

		System.out.println("Welcome to iSearch. Type what you desire, and I'll do my best to fetch it! :)");
		while(true)
		{
			System.out.println("Enter Query");
			bad=false;
			//long endt=System.currentTimeMillis();
			//start=System.currentTimeMillis();
			query();
			//System.out.println((endt-start)+" ms taken");

			allreset();
		}
		/*String post=(x.fetchPostings("thierri"));
		//System.out.println(post);
		String toks[]=post.split("\\*");
		System.out.println(toks.length);
		System.out.println(toks[0]);
		System.out.println(toks[1]);*/
	}
	public static void query()
	{
		long start,endt;
		
		HashMap<Long, Double>DocsNScores=new HashMap<Long,Double>();
		HashMap<String,String>PostingsOfWords=new HashMap<String,String>();
		Scanner in=new Scanner(System.in);
		
		String query=in.nextLine();
		start=System.currentTimeMillis();
		if(query.isEmpty())
			return;
		query=query.toLowerCase();
		boolean ormodel=false;
		if(query.compareToIgnoreCase("exit")==0)
		{
			System.out.println("bye!");
			System.exit(0);
		}

		//System.out.println(query);
		String[]terms=null;
		Double score=new Double(0);
		Long docid=new Long(0);
		int minpostwordidx = 0;
		int minpostings=999999999;
		boolean multiple=false;
		if(!query.contains(":"))
		{
			allset();//Generic query
		}
		else
		{
			//Check if only one field
			/*if(query.indexOf(':')!=query.lastIndexOf(':'))
			{
				//multiple field query
				multiple=true;
			}*/
			//else
			{
				if(query.indexOf("t : ")!=-1)
				{
					Tfield=true;
					//query.replaceAll("t\\ \\:\\ ", "");
					/*System.out.println(query);
					StringBuffer temp=new StringBuffer("");
					for(int pp=query.indexOf("t : ")+2;pp<query.length();pp++)
						temp.append(query.charAt(pp));
					System.out.println(temp.toString());
					query=temp.toString();*/

				}
				if(query.contains("b : "))
				{
					Xfield=true;
					/*StringBuffer temp=new StringBuffer("");
					for(int pp=query.indexOf("b : ")+4;pp<query.length();pp++)
						temp.append(query.charAt(pp));
					System.out.println(temp.toString());
					query=temp.toString();*/

				}
				if(query.contains("c : "))
				{
					Cfield=true;
					/*StringBuffer temp=new StringBuffer("");
					for(int pp=query.indexOf("c : ")+2;pp<query.length();pp++)
						temp.append(query.charAt(pp));
					query=temp.toString();*/

				}
				if(query.contains("i : "))
				{
					Ifield=true;
					/*StringBuffer temp=new StringBuffer("");
					for(int pp=query.indexOf("i : ")+2;pp<query.length();pp++)
						temp.append(query.charAt(pp));
					System.out.println(temp.toString());
					query=temp.toString();*/
				}
				if(query.contains("o : "))
				{
					Lfield=true;
					/*StringBuffer temp=new StringBuffer("");
					for(int pp=query.indexOf("o:")+2;pp<query.length();pp++)
						temp.append(query.charAt(pp));
					System.out.println(temp.toString());
					query=temp.toString();*/

				}

			}
			if(multiple)
			{
				System.out.println("Multiple query");
			}
		}
		terms=query.split(" ");
		ArrayList<String> newterms=new ArrayList<String>();
		//String newterms[]=new String[15];
		for(int i=0;i<terms.length;i++)
		{
			if(terms[i].length()>1)
				newterms.add(terms[i].trim());
		}

		Vector<String> words=new Vector<String>();
		int numterms=newterms.size();
		/*System.out.print("No. of good terms="+numterms);
		for(int i=0;i<newterms.size();i++)			//Adding terms of query to vector
		{
			System.out.println(newterms.get(i));
		}*/

		for(int i=0;i<newterms.size();i++)			//Adding terms of query to vector
		{
			Stemmer stemmer=new Stemmer();
			stemmer.add(newterms.get(i).toCharArray(),newterms.get(i).length());
			stemmer.stem();
			words.add(stemmer.toString());
			//newterms.get(i));
		}
		String presentWord=null;
		int numdocs = 0;
		for(int i=0;i<numterms;i++)			//Populate hash tab of words and indices, find min word indx.
		{
			boolean stopword=false;
			//Remove stopwords
			presentWord=words.elementAt(i);
			//System.out.println("Now: "+presentWord);


			if(PIndexHandler.stopWords.contains(presentWord))
			{
				stopword=true;
			}
			if(!stopword)
			{


				//		System.out.print("Fetching postings for "+presentWord+" : ");
				String postings=x.fetchPostings(presentWord);
				if(postings==null)
				{
					//System.out.println("No results");
					continue;
				}
				PostingsOfWords.put(presentWord, postings);
				String docs[]=postings.split("\\*");
				numdocs=docs.length;
				//System.out.println(numdocs);
				if(numdocs<minpostings)
				{
					minpostings=numdocs;
					minpostwordidx=i;
				}
			}
		}
		//System.out.println("DocsNScores has size "+DocsNScores.size());
		//System.out.println("Documents containing "+ presentWord+" = "+numdocs);
		System.out.println(words.elementAt(minpostwordidx)+" appears in min no of docs: "+minpostings);
		presentWord=words.elementAt(minpostwordidx);

		//Fill the hash DocsNScores with postings of least freq word and their scores.
		String postingslist=PostingsOfWords.get(presentWord);
		//System.out.println("Fetched postings for "+words.get(minpostwordidx));
		//System.out.println(" Got postings as "+postingslist);
		if(minpostings==999999999)
		{
			return;
		}
		if(postingslist.length()<100)
		{
			System.out.println("Or model switch!");
			//return;
			ormodel=true;
		}
		String []docs=postingslist.split("\\*");
		int numpostings=docs.length;
		System.out.println(presentWord+" : "+numpostings);
		//System.out.println(numpostings);
		//System.out.println(docs[0]);


		for(int xx=0;xx<numpostings;xx++)		//docs[xx]->gives posting string for a file.
		{
			if(docs[xx].contains(":"))
			{
				//first posting
				StringBuilder x=new StringBuilder();
				int pos=docs[xx].indexOf(':');
				//System.out.println(pos);
				for(int yy=pos+1;yy<docs[xx].length();yy++)
				{
					x.append(docs[xx].charAt(yy));
				}
				//System.out.println(x.toString());
				String[] toks=(x.toString().split("-"));
				docid=Long.parseLong(toks[0]);				//Checked. docid is good.
				//		System.out.println(toks.length);
				score=getScore(x.toString(),numpostings);
				if(score!=0)
				{	
					//System.out.println(docid+" "+score);
					DocsNScores.put(docid, score);
				}
			}
			else
			{	
				String[] toks=(docs[xx].split("-"));
				//System.out.println(toks.length);
				docid=Long.parseLong(toks[0]);				//Checked. docid is good.
				//System.out.println(docs[xx]);
				score=getScore(docs[xx],numpostings);
				if(score!=0)
				{	
					//System.out.println(docid+" "+score);
					DocsNScores.put(docid, score);
				}
				//double score=getScore()
			}
		}
		//System.out.println("Repo size: "+DocsNScores.size());
		//LinkedHashMap <Long,Double> DocsNScores=new LinkedHashMap<Long,Double>();
		//DocsNScores=(LinkedHashMap<Long, Double>) sortMapByValues(DocsNScores);
		//sorted has all the docids now
		for(int i=0;i<newterms.size();i++)
		{
			if(i==minpostwordidx)
				continue;
			else
			{

				boolean stopword=false;
				//Remove stopwords
				presentWord=words.elementAt(i);
				if(PIndexHandler.stopWords.contains(presentWord))
				{
					stopword=true;
				}
				if(!stopword)
				{
					Stemmer stemmer=new Stemmer();
					stemmer.add(presentWord.toCharArray(),presentWord.length());
					stemmer.stem();
					presentWord=stemmer.toString();

					//System.out.println("Fetching postings for "+presentWord);
					String postings=PostingsOfWords.get(presentWord);
					if(postings==null)
					{
						ormodel=true;
						bad=true;
						break;
					}
					if(!bad)
					{
						docs=postings.split("\\*");
						numpostings=docs.length;

						for(int xx=0;xx<numpostings;xx++)		//docs[xx]->gives posting string for a file.
						{
							if(docs[xx].contains(":"))
							{
								//first posting
								StringBuilder x=new StringBuilder();
								int pos=docs[xx].indexOf(':');
								//System.out.println(pos);
								for(int yy=pos+1;yy<docs[xx].length();yy++)
								{
									x.append(docs[xx].charAt(yy));
								}
								//System.out.println(x.toString());
								String[] toks=(x.toString().split("-"));
								docid=Long.parseLong(toks[0]);				//Checked. docid is good.
								//Imp. chking only in existing hash
								score=getScore(x.toString(),numpostings);
								if(!ormodel)
								{
									if(DocsNScores.containsKey(docid))
									{
										if(score!=0)
										{	
											//System.out.println(docid+" "+score);
											DocsNScores.put(docid,DocsNScores.get(docid)+score);
										}
									}
								}
								else
								{
									if(DocsNScores.containsKey(docid))
									{
										if(score!=0)
										{	
											//System.out.println(docid+" "+score);
											DocsNScores.put(docid,DocsNScores.get(docid)+score);
										}
									}
									else
									{
										if(score!=0)
										{	
											//System.out.println(docid+" "+score);
											DocsNScores.put(docid,score);
										}
									}
								}

							}
							else
							{
								//Not the first posting
								String[] tokens=(docs[xx].split("-"));
								//System.out.println(toks.length);
								docid=Long.parseLong(tokens[0]);				//Checked. docid is good.
								//System.out.println(docs[xx]);
								score=getScore(docs[xx],numpostings);
								if(!ormodel)
								{
									if(DocsNScores.containsKey(docid))
									{
										if(score!=0)
										{	
											//System.out.println(docid+" "+score);
											DocsNScores.put(docid,DocsNScores.get(docid)+score);
										}
									}
								}
								else
								{
									if(DocsNScores.containsKey(docid))
									{
										if(score!=0)
										{	
											//System.out.println(docid+" "+score);
											DocsNScores.put(docid,DocsNScores.get(docid)+score);
										}
									}
									else
									{
										if(score!=0)
										{	
											//System.out.println(docid+" "+score);
											DocsNScores.put(docid,score);
										}
									}
								}
								/*else
								if(score!=0)
								{	
									//System.out.println(docid+" "+score);
									DocsNScores.put(docid,score);
								}*/
								//double score=getScore()
							}
						}
					}
				}
			}
		}

		LinkedHashMap <Long,Double> sorted2=new LinkedHashMap<Long,Double>();
		sorted2=(LinkedHashMap<Long, Double>) sortMapByValues(DocsNScores);
		Vector<Long> docids2=new Vector<Long>((sorted2.keySet()));
		endt=System.currentTimeMillis();
		long intervel=endt-start;
		System.out.println((long)(intervel)+" ms");
		int res=docids2.size();
		//System.out.println(res+" Documents fetched in "+(System.currentTimeMillis()-start)+" ms ");
		for(int i=0;i<20;i++)
		{
			if(i>=res)
				break;
			else
			{
				String tit=x.fetchTitle(docids2.get(i));
				System.out.println((i+1)+" : "+tit);
			}
		}
	}
	public static void allset()
	{
		Tfield=true;
		Xfield=true;
		Lfield=false;
		Ifield=false;
		Cfield=false;
	}
	public static void allreset()
	{
		Tfield=false;
		Xfield=false;
		Lfield=false;
		Ifield=false;
		Cfield=false;
	}
	public static Double getScore(String Postings,Integer NoofDocPost)
	{
		//String[] NoofDocPost=Postings.split("-");
		Double num=new Double(0);
		//for(int i=0;i<NoofDocPost.length;i++)
		{
			String[] Docvalue=Postings.split("-");
			int dval=0;
			if(Tfield)
			{
				if(Docvalue[2].contains("T"))
				{
					int vali=Docvalue[2].indexOf("T");
					dval+=(Integer.parseInt(Docvalue[2].substring(vali+1).split("[TXICL]")[0])*7);
				}
			}
			if(Xfield)
			{
				if(Docvalue[2].contains("X"))
				{
					int vali=Docvalue[2].indexOf("X");
					dval+=(Integer.parseInt(Docvalue[2].substring(vali+1).split("[TXICL]")[0])*5);
				}
			}
			if(Ifield)
			{
				if(Docvalue[2].contains("I"))
				{
					int vali=Docvalue[2].indexOf("I");
					dval+=(Integer.parseInt(Docvalue[2].substring(vali+1).split("[TXICL]")[0])*3);
				}
			}
			if(Lfield)
			{
				if(Docvalue[2].contains("L"))
				{
					int vali=Docvalue[2].indexOf("L");
					dval+=(Integer.parseInt(Docvalue[2].substring(vali+1).split("[TXICL]")[0]));
				}
			}
			if(Cfield)
			{
				if(Docvalue[2].contains("C"))
				{
					int vali=Docvalue[2].indexOf("C");
					dval+=(Integer.parseInt(Docvalue[2].substring(vali+1).split("[TXICL]")[0])*1);
				}
			}
			/*if(dval!=0)
				System.out.println("tf="+dval);*/
			num=new Double((double)dval* Math.log((double)12961995/NoofDocPost));

		}
		return num;
	}






	/**

	 * Sort a map according to values.

	 * @param  < K >  the key of the map.
	 * @param  < V >  the value to sort according to.
	 * @param mapToSort the map to sort.

	 * @return a map sorted on the values.

	 */  

	public static  < K, V extends Comparable < ? super V >  >  Map < K, V > 
	sortMapByValues(final Map  < K, V >  mapToSort)
	{
		ArrayList < Map.Entry < K, V >  >  entries =
				new ArrayList < Map.Entry < K, V >  > (mapToSort.size());  

		entries.addAll(mapToSort.entrySet());

		Collections.sort(entries,
				new Comparator < Map.Entry < K, V >  > ()
				{
			@Override
			public int compare(
					final Map.Entry < K, V >  entry1,
					final Map.Entry < K, V >  entry2)
			{
				return entry2.getValue().compareTo(entry1.getValue());
			}
				});      

		Map < K, V >  sortedMap = new LinkedHashMap < K, V > ();      

		for (Map.Entry < K, V >  entry : entries)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}      

		return sortedMap;
	}
}
