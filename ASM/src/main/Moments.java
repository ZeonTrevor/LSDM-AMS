package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class Moments {

	private int n;
	private int numRandomVariables;
	private int moment;
	HashMap<String, Integer> randomVariables;
	List<Integer> randomPositions;
	
	public Moments(int n, int numRandomVariables, int moment)
	{
		this.n = n;
		this.numRandomVariables = numRandomVariables;
		this.moment = moment;
		this.randomVariables = new HashMap<String, Integer>();
		this.randomPositions = new ArrayList<Integer>();
		initializeRandomPositions();
	}
	
	public void initializeRandomPositions()
	{
		int i=0;
		while(i < numRandomVariables)
		{
			double randomPos = n * Math.random();
			randomPositions.add((int) randomPos);
			i++;
		}
		//System.out.println(randomPositions);
	}
	
	public void processHashTags(List<String> hashtags)
	{
		if(randomVariables.size() > 0)
		{
			for(String hashtag: hashtags)
			{
				if(randomVariables.containsKey(hashtag))
					randomVariables.put(hashtag, randomVariables.get(hashtag) + 1);
			}
		}
	}
	
	public long computeMoment()
	{
		double sum = 0;
		for(Entry<String, Integer> entry: randomVariables.entrySet())
		{
			//System.out.println(entry.getKey() + " " + entry.getValue());
			sum += n * (Math.pow(entry.getValue(), moment) - Math.pow(entry.getValue() - 1, moment));
		}
		if(!randomVariables.isEmpty())
			return (long)sum/randomVariables.size();
		else
			return 0;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		long start = System.currentTimeMillis();

		String inputPath = args[0];
		int n = Integer.parseInt(args[1]);
		int numRandomVariables = Integer.parseInt(args[2]);
		int moment = Integer.parseInt(args[3]);
		
		Moments m = new Moments(n, numRandomVariables, moment);
		
		int count = 0;
		int totalHashtags = 0;
		
		InputStream gzipStream = new GZIPInputStream(new FileInputStream(inputPath));
		BufferedReader br = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

		String line = null;
		while((line = br.readLine()) != null)
		{
			++count;
			Tweet tweet = new Tweet(line);
			totalHashtags += tweet.getHashtags().size();
			if(m.getRandomPositions().contains(count))
			{
				//System.out.println(count);
				for(String hashtag: tweet.getHashtags())
				{
					//System.out.println(hashtag);
					m.getRandomVariables().put(hashtag, 1);
				}
			}
			m.processHashTags(tweet.getHashtags());
			if(count == n)
				break;
		}
		System.out.println("Tweets Read: " + count);
		System.out.println("Total hash tags: " + totalHashtags);
		System.out.println("Estimation of " + moment +" moment:" + m.computeMoment());
		System.out.println("Execution time:" + (System.currentTimeMillis() - start)/1000 + " secs");
		br.close();

	}

	public HashMap<String, Integer> getRandomVariables() {
		return randomVariables;
	}

	public List<Integer> getRandomPositions() {
		return randomPositions;
	}
}
