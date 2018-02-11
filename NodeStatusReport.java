import java.io.File;
import java.util.ArrayList;

/**
 * Class to run the node status report programme
 * @author andrew cullinane
 */
public class NodeStatusReport {

	/**
	 * Main method for running the node status report programme from the command line
	 * @param args- file name of input node status information as .txt file
	 * @return to console collated information about the input node status information
	 */
	public static void main(String[] args) {
		// load the file
		File file = new File(args[0]);
		// create an ArrayList of the data
		ArrayList<NodeStatus> data = Report.getData(file);
		// sort the data by time it was generated
		ArrayList<NodeStatus> sorted = Report.sortByGenerated(data);
		// update the information
		Report.updateHashMaps(sorted);
		// Sort lastUpdate hashMap
		Report.sortLastUpdate();
		// Generate console output
		Report.output();
	}
}
