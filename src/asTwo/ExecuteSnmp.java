package asTwo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class ExecuteSnmp {
	
	private String cmdFinal;
	private String cmdTemp;
	private String snmpOid;

	public ExecuteSnmp(String oid){
		snmpOid = oid;
//		System.out.println(snmpOid + " Dette er snmpOid i execute");
		cmdTemp = "snmpget -v 2c -c ttm4128 129.241.209.30 ";
		
		//Might need to add .0 to cmdFinal...
		cmdFinal = cmdTemp + snmpOid + ".0";
		
//		System.out.println(cmdFinal + " detter er cmd som blir kjørt");
		
		executeCommand();
	}
	
	public void executeCommand(){
		try {
			
			// Run snmp Windows command
			Process process = Runtime.getRuntime().exec(cmdFinal);
			
			// Get input streams
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			// Read command standard output
			String s;
			System.out.println("Cmd som blir kjørt: " + cmdFinal);
			System.out.println("Standard output: ");
			
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				writeToFile("Current time: " + Calendar.getInstance().getTime()  + " " + s + "\n");
				}		
			System.out.println("");
			}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}
	
	public void writeToFile(String data) throws IOException{
    	
    	FileWriter fstream = null;
    	try{
    		fstream = new FileWriter("C:\\Users\\Ole\\Documents\\Skole\\Programvarearkitektur\\snmpData.txt", true);
    		BufferedWriter out = new BufferedWriter(fstream);
    		out.write(data);
    		out.close();
    		}
    	catch (Exception e) {
			// TODO: handle exception
		}
    	finally{
    		fstream.close();
    	}
    
    }


	
//	public static void main(String args[]) throws InterruptedException {
//		
//	}
		
}
