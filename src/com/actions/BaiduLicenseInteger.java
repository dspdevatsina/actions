package com.actions;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.*;

/**get All Files in a dir, exclude sub-directory*/


public class BaiduLicenseInteger {
	public static void main(String[] args)
	{
		compfiles();//SN对比函数，需要修改compfiles下的几个文件路径,从COMP文件中，获取Source文件中的完整SN字符串，并写入result。
		//getFileList(String path , int deepth);//合并百度License，需要修改函数调用参数，给出path即可，只会调用一级子目录下的文件。
		//String path = "H:\\bdprofile";
		//getFileList( path , 0);//获取根目录文件列表
	}
	
	public static void writeResult1(String str , String destpath,String filename,boolean writetype) 
	{		
		try {
		BufferedWriter wr = new BufferedWriter(new FileWriter(destpath,writetype));
		wr.write(str);
		wr.newLine();
		wr.flush();
		wr.close();
		}catch (IOException ex) {System.out.println(ex.toString());;}

				
	}
	
	public static int getFileLines(String dir)
	{
		int i=0;
		boolean judge= false;
		String str ="";
		try 
		{
			BufferedReader filerd = new BufferedReader(new FileReader(dir));
			while(true)
			{
				str = filerd.readLine();
				if(str == null)
					break;
				i++;
			}		
		}catch (IOException ex) {System.out.println(ex.toString());;}
		return i;
	}
	
	public static void compfiles()
	{
		String comppath = "H:\\comp.txt";
		String sourcepath = "H:\\source.txt";
		String resultpath = "H:\\result.txt";
		String compstr = "";
		String sourstr = "";
		String resultstr = "";
		boolean writetype = false;
		int i=0;
		int j=0;
		int missingnum = 0;
		int compline= getFileLines(comppath);
		int sourline= getFileLines(sourcepath);
		try {
		BufferedReader comprd = new BufferedReader(new FileReader(comppath));
		for(i=0;i<compline;i++)
		{
			compstr = comprd.readLine();
			BufferedReader sourcerd = new BufferedReader(new FileReader(sourcepath));
			for(j=0;j<sourline;j++)
			{
				sourstr = sourcerd.readLine();
				if(sourstr.indexOf(compstr) != -1)
				{
					System.out.println(sourstr);
					
					writeResult1(sourstr,resultpath,"1",writetype);
					if(writetype == false) {writetype = true;}
					break;
				}
			}
			if(j == sourline)
			{
				System.out.println("missing"+compstr);
				writeResult1("missing"+compstr,resultpath,"1",writetype);
				if(writetype == false) {writetype = true;}
				missingnum++;
			}
		}
		}catch (IOException ex) {System.out.println(ex.toString());;}
		System.out.println("find "+ comppath +" from " +"sourcepath");
		System.out.println("Total num = " + compline);
		System.out.println("source line = " + sourline);
		System.out.println("Total Missing :" +missingnum);
	}
	
	public static void writeResult(String path,String filename,boolean writetype) 
	{
		String str = "";
		String destpath = "F:\\workspace\\testfiles_result.txt";
		str += readbdfile(path,filename);
		try {
		BufferedWriter wr = new BufferedWriter(new FileWriter(destpath,writetype));
		wr.write(str);
		wr.newLine();
		wr.flush();
		wr.close();
		}catch (IOException ex) {System.out.println(ex.toString());;}		
	}
	/*
	 * modify readfile to readbdfile to specific method only for baidu license
	 */
	public static String readbdfile(String path,String filename)
	{
		int[] readchar = new int[2048] ;
		String readstr = "" ;
		int i = 0;
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new FileReader(path));
			readstr = rd.readLine();
		}
		catch (IOException ex) {
			System.out.println("cannot open file: " + path + filename);
		}
		return readstr;
	}

	public static int getFileList(String path , int deepth) {
		File files = new File(path);
		File[] array = files.listFiles();
		boolean isnewfiletowrite = false;
		for(int i = 0 ; i < array.length ; i++)
		{
			if(array[i].isFile() == true)
			{
				writeResult(array[i].getPath(),array[i].getName(),isnewfiletowrite);
			}
			if(isnewfiletowrite==false)
				isnewfiletowrite = true;
		}
		return 0;
	}
	//sort if for sort an array that includes 2 functions:
	//quicksort(int[],int,int)
	//place(int[],int,int) means to split the array and place the smaller before pivot ,the bigger after pivot.
	//and return the index of pivot.
	public static void sort(int[] array)
	{
		quicksort(array,0,array.length);
	}
	public static void quicksort(int[] array , int p1 , int p2)
	{
		if(p1 >= p2 - 1) {return ;}
		int boundry = place(array,p1,p2);
		quicksort(array,p1,boundry);
		quicksort(array,boundry+1,p2);
	}
	
	public static int place(int[] array , int p1 , int p2)
	{
		int pivot = array[p1];
		int lh = p1 + 1;
		int rh = p2 - 1;
		while(true)
		{
			while(pivot <= array[rh] && rh > lh ) { rh-- ; }
			while(pivot > array[lh] && lh < rh)  { lh++ ; }
			if(rh == lh) break;
			int temp = array[rh];
			array[rh] = array[lh];
			array[lh] = temp;
		}
		if(array[lh] >= pivot) return p1;
		array[p1] = array[lh];
		array[lh] = pivot;
		return lh;
	}

}
