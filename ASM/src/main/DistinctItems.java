package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Counting distinct items in a twitter stream using ASM
 * @author trevor
 */
public class DistinctItems {

	private int n; //length of stream
	private int numHashGroups;
	private int numHashFuncsInGroup;
	private HashFunction[][] hashFunctions;
	private int[][] maxR;
	private int logn;
	
	/**
	 * Initializing the object with required number of hash groups, number of 
	 * hash functions in a group and length of stream
	 * @param numHashGroups
	 * @param numHashFuncsInGroup
	 * @param n
	 */
	public DistinctItems(int numHashGroups, int numHashFuncsInGroup, int n)
	{
		this.n = n;
		this.logn = (int) Math.ceil(Math.log(n)/Math.log(2));
		this.numHashGroups = numHashGroups;
		this.numHashFuncsInGroup = numHashFuncsInGroup;
		this.hashFunctions = new HashFunction[numHashGroups][numHashFuncsInGroup];
		this.maxR = new int[numHashGroups][numHashFuncsInGroup];
		initializeHashFunctions();
	}
	
	/**
	 * Initializing all the hash functions 
	 */
	public void initializeHashFunctions()
	{
		HashMap<Integer, Set<Integer>> uniquePairs = new HashMap<Integer, Set<Integer>>();
		for(int i=0; i<numHashGroups; i++)
			for(int j=0;j<numHashFuncsInGroup; j++)
			{
				hashFunctions[i][j] = getHashFunction(uniquePairs);
			}
	}
	
	/**
	 * Returns a unique linear hash function with random parameters a and b
	 * @param pairs
	 * @return
	 */
	public HashFunction getHashFunction(HashMap<Integer, Set<Integer>> pairs)
	{
		int a=0,b=0;
		while(a%2 == 0)
			a = (int)(n * Math.random());
		
		while(b%2 == 0 || contains(a,b, pairs))
			b = (int)(n * Math.random());
		
		Set<Integer> values = pairs.get(a);
		if(values == null)
		{
			values = new HashSet<Integer>();
			pairs.put(a, values);
		}
		values.add(b);
		
		return new HashFunction(a, b, n);
	}
	
	public boolean contains(int a, int b, HashMap<Integer, Set<Integer>> pairs)
	{
		return pairs.get(a) != null && pairs.get(a).contains(b);
	}
	
	/**
	 * Returns the first zero bit encountered in the binary representation of the 
	 * hash value
	 * @param hashValue
	 * @return
	 */
	public int getFirstZeroBit(int hashValue)
	{
		//StringBuilder binary = new StringBuilder();
		int pos = 0;

		for (int i = logn - 1; i >= 0; i--)
	     {	
			int mask = 1 << i;
	        //binary.append((hashValue & mask) != 0 ? 1 : 0);
	        if((hashValue & mask) != 0)
	        	pos = i;
	     }
		//System.out.print(binary + " " + pos);
		return pos;
	}
	
	/**
	 * Processes each hashtag by computing the hash values for each of the hash functions
	 * and storing the maximum zero bit value encountered for each hash function
	 * @param hashtags
	 * @throws UnsupportedEncodingException
	 */
	public void processHashTags(List<String> hashtags) throws UnsupportedEncodingException
	{
		for(String hashtag: hashtags)
		{
			for(int i=0; i < numHashGroups; i++)
				for(int j=0; j < numHashFuncsInGroup; j++)
				{
					int hashValue = hashFunctions[i][j].hash(hashtag);
					//System.out.print(hashtag + " " + hashValue + " ");
					int posFirstZeroBit = getFirstZeroBit(hashValue);
					if(posFirstZeroBit > maxR[i][j])
						maxR[i][j] = posFirstZeroBit;
					//System.out.println();
				}
		}
	}
	
	/**
	 * Returns the estimate of the distinct unique hashtags by taking average of maximum R
	 * in each hash group, followed by the median of the average Rs. Then the estimate is
	 * 2^r, where r is the median of the averageRs.
	 */
	public int countUniqueHashTags()
	{
		List<Double> averageR = new ArrayList<Double>();
		for(int i=0; i < numHashGroups; i++)
		{
			int sum = 0;
			for(int j=0; j < numHashFuncsInGroup; j++)
				sum += maxR[i][j];

			averageR.add(sum * 1.0/numHashFuncsInGroup);
		}
		
		Collections.sort(averageR);
		double r = 0;
		int mid = averageR.size()/2 - 1;
		if(averageR.size() % 2 == 0)
			r = (averageR.get(mid) + averageR.get(mid + 1))/2;
		else
			r = averageR.get(mid + 1);
		return (int) Math.pow(2, r);
	}

	/**
	 * Returns the estimate of the distinct unique hashtags by taking median of estimate(2^R)
	 * in each hash group, followed by the average of the median estimates of each hash group. 
	 * 
	 */
	public int countUniqueHashTags2()
	{
		List<Double> medianR = new ArrayList<Double>();
		for(int i=0; i < numHashGroups; i++)
		{
			List<Double> medianHashGroup = new ArrayList<Double>();
			for(int j=0; j < numHashFuncsInGroup; j++)
				medianHashGroup.add(Math.pow(2, maxR[i][j]));

			Collections.sort(medianHashGroup);
			double median = 0D;
			int mid = medianHashGroup.size()/2 - 1;
			if(medianHashGroup.size() % 2 == 0)
				median = (medianHashGroup.get(mid) + medianHashGroup.get(mid + 1))/2;
			else
				median = medianHashGroup.get(mid + 1);
			medianR.add(median);
		}
		
		double sum = 0;
		for(double val: medianR)
			sum += val;
		sum = sum/medianR.size();
		return (int) sum;
	}
	
	/**
	 * Takes as input: the path to the twitter stream file (args[0]),
	 * n (args[1]) length of stream,
	 * numHashGroups (args[2]) number of hash groups to consider,
	 * numHashFunctionsInGroup (args[3]) number of hash functions in a group.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();

		String inputPath = args[0];
		int n = Integer.parseInt(args[1]);
		int numHashGroups = Integer.parseInt(args[2]);
		int numHashFunctionsInGroup = Integer.parseInt(args[3]);
		
		DistinctItems d = new DistinctItems(numHashGroups, numHashFunctionsInGroup, n);
		
		int count = 0;
		int totalHashtags = 0;
		
		InputStream gzipStream = new GZIPInputStream(new FileInputStream(inputPath));
		BufferedReader br = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

		//HashSet<String> uniqueHashtags = new HashSet<String>();
		String line = null;
		while((line = br.readLine()) != null)
		{
			Tweet tweet = new Tweet(line);
			totalHashtags += tweet.getHashtags().size();
			//uniqueHashtags.addAll(tweet.getHashtags());
			d.processHashTags(tweet.getHashtags());
			if(++count == n)
				break;
		}
		System.out.println("Tweets Read: " + count);
		System.out.println("Total hash tags: " + totalHashtags);
		System.out.println("Estimated distinct hashtags:" + d.countUniqueHashTags());
		//System.out.println("Actual distinct hashtags:" + uniqueHashtags.size());
		System.out.println("Execution time:" + (System.currentTimeMillis() - start)/1000 + " secs");
		br.close();
	}

}
