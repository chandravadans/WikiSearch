//Possible.java

import java.io.*;
import java.util.*;

class TrieNode
{
	char letter;
	TrieNode[] links;
	boolean fullWord;

	TrieNode(char letter, boolean fullWord)
	{
		this.letter = letter;
		links = new TrieNode[26];
		this.fullWord = fullWord;
	}
}
public class Possible
{
	static TrieNode tree=null;
	
	static TrieNode createTree()
	{
		return(new TrieNode(' ', false));
	}

	static void insertWord(TrieNode root, String word)
	{
		//System.out.println(word);
		word=word.toLowerCase();
		String[] words=word.split(" ");
		word=words[0];
		
		int offset = 97;
		int l = word.length();
		char[] letters = word.toCharArray();
		TrieNode curNode = root;

		for (int i = 0; i < l; i++)
		{
			if (curNode.links[letters[i]-offset] == null)
				curNode.links[letters[i]-offset] = new TrieNode(letters[i], i == l-1 ? true : false);
			curNode = curNode.links[letters[i]-offset];
		}
	}

	static boolean find(TrieNode root, String word)
	{
		char[] letters = word.toCharArray();
		int l = letters.length;
		int offset = 97;
		TrieNode curNode = root;

		int i;
		for (i = 0; i < l; i++)
		{
			if (curNode == null)
				return false;
			curNode = curNode.links[letters[i]-offset];
		}

		if (i == l && curNode == null)
			return false;

		if (curNode != null && !curNode.fullWord)
			return false;

		return true;
	}

	static void printTree(TrieNode root, int level, char[] branch, BufferedWriter out) throws IOException
	{
		if (root == null)
			return;

		for (int i = 0; i < root.links.length; i++)
		{
			branch[level] = root.letter;
			printTree(root.links[i], level+1, branch, out);
		}

		if (root.fullWord)
		{
			for (int j = 1; j <= level; j++)
				out.write(branch[j]);
			out.write("\n");
		}

	}

	public static void possible(TrieNode tree, String word) {
		Set<String> result = new HashSet<String>();

		//Remove a character
		for(int i=0; i < word.length(); ++i)
			result.add(word.substring(0, i) + word.substring(i+1));

		//Swap two consecutive characters
		for(int i=0; i < word.length()-1; ++i)
			result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));

		//Replace a character with other
		for(int i=0; i < word.length(); ++i)
			for(char c='a'; c <= 'z'; ++c)
			result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));

					//Add a new character
					for(int i=0; i <= word.length(); ++i)
						for(char c='a'; c <= 'z'; ++c)
						result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));

								ArrayList<String> res = new ArrayList<String>(result);
								int j = 0;
								for(int i=0; i < result.size(); i++)
									if (find(tree, res.get(i)))
									{
										if(j == 0)
											//System.out.print("Do you mean : ");
										System.out.print(res.get(i)+" ,");
										j++;
									}
	}
	public static void setup() throws IOException
	{
		tree = createTree();

		long build1 = System.currentTimeMillis();
		File f = new File("words.txt");
		FileInputStream fread = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fread));

		String ele;

		while ((ele = br.readLine()) != null)
			insertWord(tree, ele);
		long build2 = System.currentTimeMillis();
		System.out.println("Time to build the trie is:" + ((build2-build1)/1000)+" sec ");
	}
	
	public void suggestnew( String searchWord)
	{

		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		
		/*if (find(tree, searchWord))
		{
			System.out.println("The word was found");
		}
		else*/
		{
			long find1 = System.currentTimeMillis();
			possible(tree, searchWord);
			long find2 = System.currentTimeMillis();
			//System.out.println("\nTime to find possibilities is:" + (find2-find1) + " ms ");
			return;
			/*System.out.println("Do you want to add this word to dictionary [y/n]: "); 

			try {
				if(br1.readLine().equals("y")) 
				{
					long add = System.currentTimeMillis();
					insertWord(tree, searchWord);
					long add1 = System.currentTimeMillis();
					try{
						
						File f1 = new File("Dictionary.txt");
						FileWriter fwrite = new FileWriter(f1,true);
						BufferedWriter out = new BufferedWriter(fwrite);
						char[] branch = new char[50];
						printTree(tree, 0, branch, out);
						out.close();
					}catch (Exception e){
						System.err.println("Error: " + e.getMessage());
					}
					long add2 = System.currentTimeMillis();
					System.out.println("Time to add a new word to Trie and file are:" + (add1-add) +" " + (add2-add1));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();*/
			}
		}
	}


