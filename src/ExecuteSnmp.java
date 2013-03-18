
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteSnmp {


	public String snmpGet(String oid){
		//		System.out.println(snmpOid + " Dette er snmpOid i execute");
		String cmdTemp = "snmpget -v 2c -c ttm4128 129.241.209.30 ";

		String cmdFinal = cmdTemp + oid + ".0";

		return executeCommand(cmdFinal);
	}

	private String executeCommand(String cmd){
		BufferedReader stdInput = null;
		try {

			// Run snmp Windows command
			Process process = Runtime.getRuntime().exec(cmd);

			// Get input streams
			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			// Read command standard output
			//System.out.println("Cmd som blir kjørt: " + cmd);
			//System.out.println("Standard output: ");

			return stdInput.readLine(); 

		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return "Error";
	}

}
