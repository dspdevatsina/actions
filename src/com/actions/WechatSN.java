package com.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.QRcode;



/**
 *@Auther scott
 *to read SN from source path and sort by license, 
 *it will find "space " and the lost SN
 *then write the first finding SN and the lost SN into aim path + file(result)
 *if isprintrepeating == true , it will print out the repeated SN in command window
 *the source file would not include empty line in the whole file.
 *and the structure would be in the same format.
 *Add Hex Format ,SN start with 50 ,and for dec format ,SN start with 30
 *To deal the task ,modify the parameters of 
 *start 
 *end
 *path
 *sourfile
 *despfile
 *isprintrepeating
 *isprintlost
 *isgenqrcode
*/
public class WechatSN {

	public static final String start = "3020040002150451";
	public static final String end = "3020040002200600";
	public static final String path = "H:\\";
	public static final String sourfile = "20180517-lost27000-zb-3503_3020040002150451_3020040002200600_50150pcs.txt";
	public static final String despfile = "result.txt";
	public static final boolean isprintrepeating = true;     //switch   
	public static final boolean isprintlost      = true;    //switch 
	public static final boolean isgenqrcode      = false;    //generate the QR-code picture or not
	public static void main(String[] args)
		{
			int size = distence(start,end);//Total num between end and start
			String array[] = new String[size];
			dealSN(array, path+sourfile , size , start);
			//printarray(array);
			//System.out.println(array[0].h);
			writeResult(array,path+despfile,false);
			qrcodegen( array);
		}
	/*qrcodegen works for generating QR-code from SN
	 * String[] array is the SN
	 * under the function need to fill path and 
	 * the signal isqenqrcode need to be true
	*/
	public static void qrcodegen(String[] array)
	{
		if(isgenqrcode == true)
		{
			for(int i = 0 ; i<array.length;i++)
			QRcode.QRcodeGen(array[i],path+"aaa\\",explainSN(array[i]));
		}
		
	}
		public static void printarray(String[] array)
		{
			for(int i=0;i<array.length;i++)
			{
				System.out.println(array[i]);
			}
		}
		public static int comparesize(int filesize , int size)
		{
			if(filesize > size)
				return filesize;
			else
				return size;
		}
		public static void dealSN(String[] array , String path , int size ,String start)
		{
			int num_filelines = getFileLines(path);//Total num in the SN file.
			int arraysize = comparesize(size,num_filelines);
			String buffarray[] = new String[arraysize];
			String readflag ;
			readflag = readfiletoarray(path,buffarray);
			for(int i = 0 ; i<arraysize;i++)
			{
				if(buffarray[i] != null)
				{
					int caldistence = distence(start,explainSN(buffarray[i]))-1;
					if(array[caldistence] == null)
						array[caldistence] = buffarray[i];
					else
						if(isprintrepeating == true)
							System.out.println("repeating " + buffarray[i]);
				}
			}
			for(int i =0;i<array.length;i++)
			{
				if(array[i] == null && isprintlost == true)
				{
					System.out.println("Space "+synthetic(start,i));
				}
			}
			return;
		}
		public static String explainSN(String SN)
		{
			String str1 = SN.substring(60, 76);
			//System.out.println(str1);
			return str1;
		}
		public static String readfiletoarray(String path ,String[] array)
		{
			String result="success";
			try 
			{
				BufferedReader filerd = new BufferedReader(new FileReader(path));
				for(int i=0;i<array.length;i++)
				{
					array[i] = filerd.readLine();
				}		
			}catch (IOException ex) {System.out.println(ex.toString());;}
			return result;
		}
		/*Find split number in the SN
		 * 
		 */
		public static int findsplitnum(String start)
		{
			if(isHexFormat(start))
				return 10;
			else
				return 8;
		}
		public static int totallenthlicense()
		{
			return 16;
		}
		/*
		 * is hex format
		 * start with 50 is hex 
		 * start with 30 is dec
		 * */
		public static boolean isHexFormat(String str)
		{
			if(str.startsWith("50"))
			{
				return true;
			}
			else
				return false;
		}
		/*
		 * String number to int
		 * Maybe Hex String to int
		 * mbhexstrtoint(String , boolean)
		 * */
		public static int mbhexstrtoint(String str,boolean ishex)
		{
			int a=0;
			if(ishex)
			{	
				try {
					a = Integer.valueOf(str,16);
				} catch(NumberFormatException e)
				{e.printStackTrace();}
			}
			else
			{
				try {
					a = Integer.parseInt(str);
				} catch(NumberFormatException e)
				{e.printStackTrace();}
			}
			return a;
		}
		/*
		 * to correct the format 
		 * add zero if lost in int to string
		 */
		public static String correctformat(String license)
		{
			int splitnum = findsplitnum(license);
			String A = license.substring(0,splitnum);
			int lostnum = totallenthlicense() - license.length();
			for(int i =0;i<lostnum;i++)
			{
				A += '0';
			}
			return A + license.substring(splitnum);
		}
		/*String synthetic(String start , int mid)
		 * calculate the sum of start and mid
		 */
		public static String synthetic(String start , int mid)
		{
			int splitnum = findsplitnum(start);
			String A = start.substring(splitnum);
			int a =mbhexstrtoint(A,isHexFormat(start));
			a = a + mid;
			if(a>0)
			{
				if(isHexFormat(start))
				{
					return correctformat(start.substring(0, splitnum)+ Integer.toHexString(a).toUpperCase());
				}
				else
					return correctformat(start.substring(0, splitnum)+ String.valueOf(a));
			}
			else 
				return "error";
		}
		/* int distence(String ,int) -- no more use
		 * int distance(String, String)
		 * calculate the total num between start and end
		 * return positive means a valuable size
		 * return -1 means error occurs.
		 * start and end will be split by int splitnum.
		 */

		public static int distence(String start,String end)
		{
			int splitnum = findsplitnum(start);
			String A = start.substring(splitnum);
			String B = end.substring(splitnum);
			int a =mbhexstrtoint(A,isHexFormat(start));
			int b =mbhexstrtoint(B,isHexFormat(start));

			int result = b - a + 1;
			if(result < 0)
				result = -1;
			return result;
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
		public static void writeResult(String[] array,String path,boolean writetype) 
		{
			String str = "";
			String destpath = path;
			try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(destpath,writetype));
			for(int i = 0;i<array.length;i++)
			{
				if(array[i]==null) 
				{
					wr.write("space "+synthetic(start,i));
					wr.newLine();
					//System.out.println(synthetic(start,i));
					continue;
				}
				wr.write(array[i]);
				wr.newLine();
			}
			
			wr.flush();
			wr.close();
			}catch (IOException ex) {System.out.println(ex.toString());;}		
		}
}
/*
public static int distence(String start, int end)
{
	int splitnum = findsplitnum(start);
	String A = start.substring(splitnum);
	int a =0;
	int b = end;
	try {
		a = Integer.parseInt(A);
	} catch(NumberFormatException e)
	{e.printStackTrace();}
	int result = b - a + 1;
	if(result < 0)
		result = -1;
	return result;
}
*/
