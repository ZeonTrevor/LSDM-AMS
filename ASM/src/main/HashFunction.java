package main;

import java.io.UnsupportedEncodingException;

public class HashFunction {

	private int a;
	private int b;
	private int n;
	
	public HashFunction(int a, int b, int n)
	{
		this.a = a;
		this.b = b;
		this.n = n;
	}
	
	public int hash(String s) throws UnsupportedEncodingException
	{
		byte[] bytes = s.getBytes("UTF-8");
		int hash = 0;
		for(byte val : bytes)
			hash += (val*a + b);
		//hash = s.hashCode()*a + b;
		return hash % n;
	}
}
