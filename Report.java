import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class to collate the node status information and generate a report
 * @author andrew cullinane
 */
public class Report {

	//hashMaps for storing the information
	protected static HashMap<String, Long> lastUpdate = new HashMap<String, Long>();
	protected static HashMap<String, String> lastStatus = new HashMap<String, String>();
	protected static HashMap<String, String> lastMessage = new HashMap<String, String>();

	/**
	 * Method to get data from input file
	 * @param file of node status information
	 * @return an ArrayList of NodeStatus objects
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException if file cannot be read
	 * @throws IllegalArgumentException if an incorrect number of arguments are given
	 */
	public static ArrayList<NodeStatus> getData(File file) {
		//Create an ArrayList for the data
		ArrayList<NodeStatus> data = new ArrayList<NodeStatus>();
		// load file
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			// take each line and make a new instance of NodeStatus class
			for (String x = br.readLine(); x != null; x = br.readLine()) {
				String[] words = x.split(" ");
				// self reporting
				if (words.length == 4) {
					data.add(new NodeStatus(Long.parseLong(words[0]), Long.parseLong(words[1]), words[2], words[3]));
				// reporting on another node
				} else if (words.length == 5) {
					data.add(new NodeStatus(Long.parseLong(words[0]), Long.parseLong(words[1]), words[2], words[3],
							words[4]));
				//incorrect number of arguments
				} else {
					throw new IllegalArgumentException();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException e) {
			System.out.println("Could not read file.");
		} catch (IllegalArgumentException e) {
			System.out.println("Not the correct number of arguments given.");
		}
		return data;
	}

	/**
	 * Method to sort the data ArrayList by time generated
	 * @param data ArrayList
	 * @return a sorted ArrayList of data in time generated order
	 */
	public static ArrayList<NodeStatus> sortByGenerated(ArrayList<NodeStatus> data) {
		Collections.sort(data, new Comparator<NodeStatus>() {
			//sort in ascending time generated order
			@Override
			public int compare(NodeStatus o1, NodeStatus o2) {
				return Long.compare(o1.getGenerated(), o2.getGenerated());
			}
		});
		return data;
	}

	/**
	 * Method to update the hashMaps of information. 
	 * Works out for each element if they are dead alive or it is unknown
	 * @param an ArrayList of data sorted in time generated order
	 * Updates hashMaps
	 */
	public static void updateHashMaps(ArrayList<NodeStatus> sorted) {
		// put first entry into hashMaps
		// node reporting on another node
		if (sorted.get(0).getReporting() != null) {
			// update node
			lastUpdate.put(sorted.get(0).getNodeName(), sorted.get(0).getReceived());
			lastStatus.put(sorted.get(0).getNodeName(), "HELLO");
			lastMessage.put(sorted.get(0).getNodeName(), (sorted.get(0).getNodeName() + " " + sorted.get(0).getStatus()
					+ " " + sorted.get(0).getReporting()));
			// update reported
			lastUpdate.put(sorted.get(0).getReporting(), sorted.get(0).getReceived());
			lastStatus.put(sorted.get(0).getReporting(), sorted.get(0).getStatus());
			lastMessage.put(sorted.get(0).getReporting(), (sorted.get(0).getNodeName() + " " + sorted.get(0).getStatus()
					+ " " + sorted.get(0).getReporting()));
		//node is self reporting
		} else {
			// update node
			lastUpdate.put(sorted.get(0).getNodeName(), sorted.get(0).getReceived());
			lastStatus.put(sorted.get(0).getNodeName(), sorted.get(0).getStatus());
			lastMessage.put(sorted.get(0).getNodeName(),
					(sorted.get(0).getNodeName() + " " + sorted.get(0).getStatus()));
		}
		
		// for each subsequent line of data given update hashMaps
		for (int i = 1; i < sorted.size(); i++) {
			// entry is from the same node as previous entry
			if (sorted.get(i - 1).getNodeName().equals(sorted.get(i).getNodeName())) {
				// node reporting on another node
				if (sorted.get(i).getReporting() != null) {
					// update node
					lastUpdate.put(sorted.get(i).getNodeName(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getNodeName(), "HELLO");
					lastMessage.put(sorted.get(i).getNodeName(), (sorted.get(i).getNodeName() + " "
							+ sorted.get(i).getStatus() + " " + sorted.get(i).getReporting()));
					// update reported
					lastUpdate.put(sorted.get(i).getReporting(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getReporting(), sorted.get(i).getStatus());
					lastMessage.put(sorted.get(i).getReporting(), (sorted.get(i).getNodeName() + " "
							+ sorted.get(i).getStatus() + " " + sorted.get(i).getReporting()));
				// node self reporting
				} else {
					// update node
					lastUpdate.put(sorted.get(i).getNodeName(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getNodeName(), sorted.get(i).getStatus());
					lastMessage.put(sorted.get(i).getNodeName(),
							(sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus()));
				}
				
			// entry is from different node and greater than 50ms apart
			} else if (!sorted.get(i - 1).getNodeName().equals(sorted.get(i).getNodeName())
					&& (sorted.get(i).getGenerated() - sorted.get(i - 1).getGenerated() > 50)) {
				// node reporting on another node
				if (sorted.get(i).getReporting() != null) {
					// update node
					lastUpdate.put(sorted.get(i).getNodeName(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getNodeName(), "HELLO");
					lastMessage.put(sorted.get(i).getNodeName(), (sorted.get(i).getNodeName() + " "
							+ sorted.get(i).getStatus() + " " + sorted.get(i).getReporting()));
					// update reported
					lastUpdate.put(sorted.get(i).getReporting(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getReporting(), sorted.get(i).getStatus());
					lastMessage.put(sorted.get(i).getReporting(), (sorted.get(i).getNodeName() + " "
							+ sorted.get(i).getStatus() + " " + sorted.get(i).getReporting()));
				// node self reporting
				} else {
					// update node
					lastUpdate.put(sorted.get(i).getNodeName(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getNodeName(), sorted.get(i).getStatus());
					lastMessage.put(sorted.get(i).getNodeName(),
							(sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus()));
				}
			
			// entry is from different node and less than 50ms apart => UNKNOWN
			} else {
				//node reporting on another node
				if (sorted.get(i).getReporting() != null) {
					// make previous entry unknown
					lastUpdate.put(sorted.get(i - 1).getNodeName(), sorted.get(i - 1).getReceived());
					lastStatus.put(sorted.get(i - 1).getNodeName(), "UNKNOWN");
					lastMessage.put(sorted.get(i - 1).getNodeName(),
							(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus() + " "
									+ sorted.get(i - 1).getReporting()
									+ "\n" + sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus() + " "
									+ sorted.get(i).getReporting()));
					// update node
					lastUpdate.put(sorted.get(i).getNodeName(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i).getNodeName(), "UNKNOWN");
					// previous node was reporting on another node
					if (sorted.get(i - 1).getReporting() != null) {
						lastMessage.put(sorted.get(i).getNodeName(),
								(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus() + " "
										+ sorted.get(i - 1).getReporting()  
										+ "\n" + sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus() 
										+ " " + sorted.get(i).getReporting()));
					//previous node was self reporting
					} else {
						lastMessage.put(sorted.get(i).getNodeName(),
								(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus()
										+ "\n" + sorted.get(i).getNodeName() + " UNKNOWN " + sorted.get(i).getReceived() + " " 
										+ sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus() + " " + sorted.get(i).getReporting()));
					}
					// update reported
					// previous node was reporting on another node
					if (sorted.get(i - 1).getReporting() != null) {
						lastUpdate.put(sorted.get(i - 1).getReporting(), sorted.get(i).getReceived());
						lastStatus.put(sorted.get(i - 1).getReporting(), "UNKNOWN");
						lastMessage.put(sorted.get(i - 1).getReporting(),
								(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus() + " "
										+ sorted.get(i - 1).getReporting() 
										+ "\n" + sorted.get(i).getNodeName() + " UKNOWN " + sorted.get(i).getReceived() 
										+ " " + sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus() + " " 
										+ sorted.get(i).getReporting()));
					//previous node was self reporting
					} else {
						lastUpdate.put(sorted.get(i - 1).getNodeName(), sorted.get(i).getReceived());
						lastStatus.put(sorted.get(i - 1).getNodeName(), "UNKNOWN");
						lastMessage.put(sorted.get(i - 1).getNodeName(),
								(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus() 
										+ "\n" + sorted.get(i-1).getNodeName() + " UNKNOWN " + sorted.get(i).getReceived() +  " " 
										+ sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus() + " "
										+ sorted.get(i).getReporting()));

					}
				//node is self reporting
				} else {
					// update previous node
					lastUpdate.put(sorted.get(i - 1).getNodeName(), sorted.get(i).getReceived());
					lastStatus.put(sorted.get(i - 1).getNodeName(), "UNKNOWN");
					//previous node was reporting another node
					if (sorted.get(i - 1).getReporting() != null) {
						lastMessage.put(sorted.get(i - 1).getNodeName(),
								(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus() + " "
										+ sorted.get(i - 1).getReporting() 
										+ "\n" + sorted.get(i).getNodeName()	+ " UNKNOWN " + sorted.get(i).getReceived() 
										+ " " + sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus()));
					//previous node was self-reporting
					} else {
						lastMessage.put(sorted.get(i).getNodeName(),
								(sorted.get(i - 1).getNodeName() + " " + sorted.get(i - 1).getStatus()
										+ "\n" + sorted.get(i).getNodeName() + " UNKNOWN" + sorted.get(i).getReceived() 
										+ " " + sorted.get(i).getNodeName() + " " + sorted.get(i).getStatus()));
					}
					// update current node
					//previous node was reporting another node
					if (sorted.get(i - 1).getReporting() != null) {
						lastUpdate.put(sorted.get(i - 1).getReporting(), sorted.get(i).getReceived());
						lastStatus.put(sorted.get(i - 1).getReporting(), "UNKNOWN");
						lastMessage.put(sorted.get(i - 1).getReporting(), (sorted.get(i - 1).getNodeName() + " "
								+ sorted.get(i - 1).getStatus() + " " + sorted.get(i - 1).getReporting()));
					//previous node was self-reporting
					} else {
						lastUpdate.put(sorted.get(i - 1).getNodeName(), sorted.get(i - 1).getReceived());
						lastStatus.put(sorted.get(i - 1).getNodeName(), "UNKNOWN");
						lastMessage.put(sorted.get(i - 1).getNodeName(),
								(sorted.get(i-1).getNodeName() + " " + sorted.get(i - 1).getStatus() 
										+ "\n" + sorted.get(i).getNodeName() + " UNKNOWN " + sorted.get(i).getReceived() + " "
										+ sorted.get(i).getStatus() + " " + sorted.get(i).getReporting()));
					}
				}
			}
		}
	}

	/**
	 * Method to sort the lastUpdate hashMap in order of time received at the monitoring station
	 */
	public static void sortLastUpdate() {
		//Make linkedList of lastUpdate data
		List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(lastUpdate.entrySet());
		//sort of values in lastUpdate
		Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		//Make map of sorted linkedList data
		Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
		for (Map.Entry<String, Long> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		//Update lastUpdate hashMap
		lastUpdate = (HashMap<String, Long>) sortedMap;
	}

	/**
	 * Method to generate output collated node status information
	 * Outputs data to console
	 */
	public static void output() {

		// generate output for when node is alive
		for (String name : lastUpdate.keySet()) {
			String nodeName = name;
			String nodeStatus;
			if (lastStatus.get(name).equals("HELLO") || lastStatus.get(name).equals("FOUND")) {
				nodeStatus = "ALIVE";
				long time = lastUpdate.get(name);
				System.out.println(nodeName + " " + nodeStatus + " " + time + " " + lastMessage.get(name));
			}
		}
		// generate output for when node is dead
		for (String name : lastUpdate.keySet()) {
			String nodeName = name;
			String nodeStatus;
			if (lastStatus.get(name).equals("LOST")) {
				nodeStatus = "DEAD";
				long time = lastUpdate.get(name);
				System.out.println(nodeName + " " + nodeStatus + " " + time + " " + lastMessage.get(name));
			}
		}
		// generate output for when node status is unknown
		for (String name : lastUpdate.keySet()) {
			String nodeName = name;
			String nodeStatus;
			if (lastStatus.get(name).equals("UNKNOWN")) {
				nodeStatus = "UNKNOWN";
				long time = lastUpdate.get(name);
				System.out.println(nodeName + " " + nodeStatus + " " + time + " " + lastMessage.get(name));
			}
		}
	}

}
