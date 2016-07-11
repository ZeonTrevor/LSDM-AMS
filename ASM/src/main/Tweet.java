package main;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tweet {

	String text;
	Date timestamp;
	String language;
	ArrayList<String> userMentions;
	ArrayList<String> hashtags;
	ArrayList<String> urls;
	
	public Tweet(String jsonStr)
	{
		JSONObject tweet = new JSONObject(jsonStr);
		if(tweet.has("text") && !tweet.isNull("text"))
			text = tweet.getString("text");
		if(tweet.has("timestamp") && !tweet.isNull("timestamp"))
			timestamp = new Date(tweet.getLong("timestamp_ms"));
		if(tweet.has("lang") && !tweet.isNull("lang"))
			language = tweet.getString("lang");
		if(tweet.has("entities") && !tweet.isNull("entities"))
		{
			JSONObject entities = tweet.getJSONObject("entities");
			userMentions = setUserMentions(entities.getJSONArray("user_mentions"));
			hashtags = setHashTags(entities.getJSONArray("hashtags"));
			urls = setUrls(entities.getJSONArray("urls"));
		}
		else
			hashtags = new ArrayList<String>();
	}
	
	private ArrayList<String> setUserMentions(JSONArray userMentions)
	{
		ArrayList<String> userNames = new ArrayList<String>();
		for(Object user: userMentions)
		{
			JSONObject userMention = (JSONObject) user;
			if(userMention.has("name") && !userMention.isNull("name"))
				userNames.add(userMention.getString("name"));
		}
		return userNames;
	}
	
	private ArrayList<String> setHashTags(JSONArray hashtagsArr)
	{
		ArrayList<String> hashtags = new ArrayList<String>();
		for(Object hashtagObj: hashtagsArr)
		{
			JSONObject hashtag = (JSONObject) hashtagObj;
			if(hashtag.has("text") && !hashtag.isNull("text"))
				hashtags.add(hashtag.getString("text"));
		}
		return hashtags;
	}
	
	private ArrayList<String> setUrls(JSONArray urlsArr)
	{
		ArrayList<String> urls = new ArrayList<String>();
		for(Object urlObj: urlsArr)
		{
			JSONObject url = (JSONObject) urlObj;
			if(url.has("expanded_url") && !url.isNull("expanded_url"))
				urls.add(url.getString("expanded_url"));
		}
		return urls;
	}

	public String getText() {
		return text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getLanguage() {
		return language;
	}

	public ArrayList<String> getUserMentions() {
		return userMentions;
	}

	public ArrayList<String> getHashtags() {
		return hashtags;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}
}
