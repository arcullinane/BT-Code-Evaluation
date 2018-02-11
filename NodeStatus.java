/**
 * Class to create node status update objects 
 * @author andrew cullinane
 */

public class NodeStatus {

	//field variables
	private long received;
	private long generated;
	private String nodeName;
	private String status;
	private String reporting;

	/**
	 * Constructor for 4 items given in update (self-reporting)
	 * @param received time received at monitoring station given as a long
	 * @param generated time generated at node given as a long
	 * @param nodeName name of the node given as a String
	 * @param status node status given as a String
	 */
	public NodeStatus(long received, long generated, String nodeName, String status) {
		this.received = received;
		this.generated = generated;
		this.nodeName = nodeName;
		this.status = status;
	}

	/**
	 * Constructor for 5 items given in update (reporting on other node)
	 * @param received time received at monitoring station given as a long
	 * @param generated time generated at node given as a long
	 * @param nodeName name of the node given as a String
	 * @param status node status given as a String
	 * @param reporting status of reported node given as a String
	 */
	public NodeStatus(long received, long generated, String nodeName, String status, String reporting) {
		this.received = received;
		this.generated = generated;
		this.nodeName = nodeName;
		this.status = status;
		this.reporting = reporting;
	}

	/**
	 * Getter for received
	 * @return time received at monitoring station as a long
	 */
	public long getReceived() {
		return received;
	}

	/**
	 * Getter for generated
	 * @return time generated at node as a long
	 */
	public long getGenerated() {
		return generated;
	}

	/**
	 * Getter for nodeName
	 * @return name of the node as a String
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * Getter for status
	 * @return node status as a String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Getter for reporting
	 * @return status of reported node as a String
	 */
	public String getReporting() {
		return reporting;
	}

	/**
	 * toString method for NodeStatus class
	 * @return a string of all field variables
	 */
	public String toString() {
		return "[" + received + " " + generated + " " + nodeName + " " + status + " " + reporting + "]";
	}
	
}
