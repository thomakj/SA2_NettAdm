
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteSnmp {


	public String snmpGet(String oid){
		/*
		 * Creates an snmpGet-message and returns it.
		 * 
		 * @param oid	is the value wanted from the snmpGet-request.
		 * @return 		String
		 */
		String cmdTemp = "snmpget -v 2c -c ttm4128 129.241.209.30 ";
		String cmdFinal = cmdTemp + oid + ".0";
		return executeCommand(cmdFinal);
	}

	private String executeCommand(String cmd){
		/*
		 * Executes the snmp-request and returns the response of the executed command.
		 * 
		 * @param cmd	the command that should be executed.
		 * @return		String
		 */
		BufferedReader stdInput = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);

			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return stdInput.readLine(); 
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return "Error";
	}

}
