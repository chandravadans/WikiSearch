import java.util.HashMap;
import java.util.Map;
import java.lang.Math;


public class TFIDF 
{
	//Posting List
	boolean Tfield=false;
	boolean Wfield=false;
	boolean Xfield=false;
	boolean Ifield=false;
	boolean Cfield=false;
	Map<String,Double > map ;
	//	String posting="1b829_W1_1;1b857_W1_1;1b8af_W1_1;1b9a7_W1_1;1b9ef_W1_1;1b9f3_W3_3;1b9fe_W1_1;1ba07_W1_1;1ba66_W1_1;1bb96_W1_1;1bed5_W2_2;1befd_W1_1;1bf69_W1_1;1c02f_W3_3;1c0de_W1_1;1c1fe_W2_2;1c320_W1_1;1c3a7_W2_2;1c48c_W1_1;1c48e_W1_1;1c578_W1_1;1c6e8_W1_1;1c718_W1_1;1c7c8_W2_2;1c808_W1_1;1c817_W1_1;1c872_W1_1;1c882_W1_1;1c8d4_W1_1;1c90d_W1_1;1c9c7_W1_1;1cad8_W1_1;1cc50_W1_1;1cd4a_W1_1;1ce2e_W1_1;1ce80_W1_1;1cf7c_W1_1;1d188_W1_1;1d207_W4_4;1d23f_W1_1;1d470_W1_1;1d578_W1_1;1d57a_W1_1;1d586_W3_3;1d8a5_W1_1;1db1c_W2_2;1dc1d_W1_1;1dcd4_W1_1;1dd5d_W1_1;1dd8c_W2_2;1df66_W1_1;1e0d0_W1_1;1e478_W1_1;1e4f4_W1_1;1e5b3_W1_1;1e65a_W1_1;1e696_W1_1;1e713_W1_1;1e72a_W1_1;1e755_W1_1;1e808_W1_1;1e836_W1_1;1e845_W2_2;1e854_W1_1;1e8b3_W1_1;1e8bf_W2_2;1e933_W1_1;1e93a_W2_2;1e93c_W4_4;1e9e4_W1_1;1ea60_W1_1;1ea61_W1_1;1ea68_W2_2;1ea7b_W4_4;1eb66_W1_1;1ed67_W1_1";
	//REMOVE
//	Tfield=Wfield=Xfield=Ifield=Cfield=true;
	public TFIDF()
	{
		map = new HashMap<String,Double >();
	}
	

	public void IndPostings(String Postings)
	{
		String[] NoofDocPost=Postings.split(";");
		for(int i=0;i<NoofDocPost.length;i++)
		{
			String[] Docvalue=NoofDocPost[i].split("_");
			int dval=0;
			if(Tfield)
			{
				if(Docvalue[1].contains("T"))
				{
					int vali=Docvalue[1].indexOf("W");
					dval+=(Integer.parseInt(Docvalue[1].substring(vali+1).split("[TWICX]")[0])*11);
				}
			}
			if(Wfield)
			{
				if(Docvalue[1].contains("W"))
				{
					int vali=Docvalue[1].indexOf("W");
					dval+=(Integer.parseInt(Docvalue[1].substring(vali+1).split("[TWICX]")[0])*5);
				}
			}
			if(Ifield)
			{
				if(Docvalue[1].contains("I"))
				{
					int vali=Docvalue[1].indexOf("I");
					dval+=(Integer.parseInt(Docvalue[1].substring(vali+1).split("[TWICX]")[0])*3);
				}
			}
			if(Xfield)
			{
				if(Docvalue[1].contains("X"))
				{
					int vali=Docvalue[1].indexOf("X");
					dval+=(Integer.parseInt(Docvalue[1].substring(vali+1).split("[TWICX]")[0]));
				}
			}
			if(Cfield)
			{
				if(Docvalue[1].contains("C"))
				{
					int vali=Docvalue[1].indexOf("W");
					dval+=(Integer.parseInt(Docvalue[1].substring(vali+1).split("[TWICX]")[0])*2);
				}
			}
			double num=(double)dval* Math.log((double)12961995/NoofDocPost.length);
			if (map.containsKey(Docvalue[0])) 
			{
				double nu=map.get(Docvalue[0]);
				nu+=num;
				map.put(Docvalue[0], nu);
			}
			else
				map.put(Docvalue[0], num);
		}
	}
}

