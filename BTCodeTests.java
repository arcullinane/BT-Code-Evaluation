import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test cases for NodeStatus, Report, and NodeStatusReport classes
 * 
 * @author andrew cullinane
 */

class BTCodeTests {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test NodeStatus class
	@Test
	public void test1() {
		NodeStatus testWithFour = new NodeStatus(Long.parseLong("1508405807242"), Long.parseLong("1508405807141"),
				"vader", "HELLO");
		NodeStatus testWithFive = new NodeStatus(Long.parseLong("1508405807378"), Long.parseLong("1508405807387"),
				"luke", "LOST", "vader");
		// getReceived
		long expectedGetReceived = Long.parseLong("1508405807242");
		long obtainedGetReceived = testWithFour.getReceived();
		assertEquals(expectedGetReceived, obtainedGetReceived);
		// getGenerated
		long expectedGetGenerated = Long.parseLong("1508405807141");
		long obtainedGetGenerated = testWithFour.getGenerated();
		assertEquals(expectedGetGenerated, obtainedGetGenerated);
		// getNodeName
		String expectedGetNodeName = "vader";
		String obtainedGetNodeName = testWithFour.getNodeName();
		assertEquals(expectedGetNodeName, obtainedGetNodeName);
		// getStatus
		String expectedGetStatus = "HELLO";
		String obtainedGetStatus = testWithFour.getStatus();
		assertEquals(expectedGetStatus, obtainedGetStatus);
		// getReporting
		String expectedGetReporting = "vader";
		String obtainedGetReporting = testWithFive.getReporting();
		assertEquals(expectedGetReporting, obtainedGetReporting);
	}

	// test getData method
	// file with 4 and 5 arguments
	@Test
	public void test2() {
		File file = new File("test2.txt");
		String expected = "[[1508405807340 1508405807350 luke HELLO null], [1508405807378 1508405807387 luke LOST vader]]";
		String obtained = Report.getData(file).toString();
		assertEquals(expected, obtained);
	}

	// File not found
	@Test
	public void test3() {
		File file = new File("noFile.txt");
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		exception.expect(FileNotFoundException.class);
		Report.getData(file);
		assertEquals("File not found.\n", outContent.toString());
	}

	// Incorrect number of arguments
	@Test
	public void test4() {
		File file = new File("test4.txt");
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		exception.expect(IllegalArgumentException.class);
		Report.getData(file);
		assertEquals("Not the correct number of arguments given.\n", outContent.toString());
	}

	// Test sortByGenerated method
	@Test
	public void test5() {
		File file = new File("test.txt");
		ArrayList<NodeStatus> testArrayList = Report.getData(file);
		String expectedSortedList = "[[1508405807242 1508405807141 vader HELLO null], [1508405807340 1508405807350 luke HELLO null], "
				+ "[1508405807378 1508405807387 luke LOST vader], [1508405807512 1508405807400 vader LOST luke], "
				+ "[1508405807467 1508405807479 luke FOUND r2d2], [1508405807468 1508405807480 luke LOST leia], "
				+ "[1508405807560 1508405807504 vader HELLO null]]";
		String obtainedSortedList = Report.sortByGenerated(testArrayList).toString();
		assertEquals(expectedSortedList, obtainedSortedList);
	}

	// Test updateHashMaps method for well separated data
	@Test
	public void test6() {
		File file = new File("test6.txt");
		ArrayList<NodeStatus> testArrayList = Report.getData(file);
		ArrayList<NodeStatus> sortedList = Report.sortByGenerated(testArrayList);
		Report.updateHashMaps(sortedList);
		String expectedLastUpdate = "{luke=1508405807400, leia=1508405807400, vader=1508405807600, r2d2=1508405807300}";
		String obtainedLastUpdate = Report.lastUpdate.toString();
		assertEquals(expectedLastUpdate, obtainedLastUpdate);
		String expectedLastStatus = "{luke=HELLO, leia=LOST, vader=HELLO, r2d2=FOUND}";
		String obtainedLastStatus = Report.lastStatus.toString();
		assertEquals(expectedLastStatus, obtainedLastStatus);
		String expectedLastMessage = "{luke=luke LOST leia, leia=luke LOST leia, vader=vader HELLO, r2d2=luke FOUND r2d2}";
		String obtainedLastMessage = Report.lastMessage.toString();
		assertEquals(expectedLastMessage, obtainedLastMessage);
	}

	// Test updateHashMaps method for information from the same node,
	// different node > 50ms apart, and
	// different node < 50ms apart
	// Also tests self-reporting and reporting on another node situations
	@Test
	public void test7() {
		File file = new File("test7.txt");
		ArrayList<NodeStatus> testArrayList = Report.getData(file);
		ArrayList<NodeStatus> sortedList = Report.sortByGenerated(testArrayList);
		Report.updateHashMaps(sortedList);
		String expectedLastUpdate = "{luke=1508405807300, leia=1508405807449, vader=1508405807300, r2d2=1508405807449}";
		String obtainedLastUpdate = Report.lastUpdate.toString();
		assertEquals(expectedLastUpdate, obtainedLastUpdate);
		String expectedLastStatus = "{luke=LOST, leia=UNKNOWN, vader=HELLO, r2d2=UNKNOWN}";
		String obtainedLastStatus = Report.lastStatus.toString();
		assertEquals(expectedLastStatus, obtainedLastStatus);
		String expectedLastMessage = "{luke=vader LOST luke, leia=r2d2 HELLO\n"
				+ "leia UNKNOWN 1508405807449 leia LOST r2d2, vader=vader LOST luke, r2d2=r2d2 HELLO\n"
				+ "r2d2 UNKNOWN 1508405807449 leia LOST r2d2}";
		String obtainedLastMessage = Report.lastMessage.toString();
		assertEquals(expectedLastMessage, obtainedLastMessage);
	}

	// Test output method
	@Test
	public void test8() {
		File file = new File("test8.txt");
		ArrayList<NodeStatus> testArrayList = Report.getData(file);
		ArrayList<NodeStatus> sortedList = Report.sortByGenerated(testArrayList);
		Report.updateHashMaps(sortedList);
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		Report.output();
		String expectedOutput = "vader ALIVE 1508405807100 vader LOST luke\n"
				+ "luke DEAD 1508405807100 vader LOST luke\n" + "leia UNKNOWN 1508405807201 r2d2 HELLO\n"
				+ "leia UNKNOWN 1508405807201 leia FOUND r2d2\n" + "r2d2 UNKNOWN 1508405807201 r2d2 HELLO\n"
				+ "r2d2 UNKNOWN 1508405807201 leia FOUND r2d2\n";
		assertEquals(expectedOutput, outContent.toString());
	}

	// Test main method
	@Test
	public void test9() {
		String[] input = { "test.txt" };
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		NodeStatusReport.main(input);
		String expectedOutput = "r2d2 ALIVE 1508405807467 luke FOUND r2d2\n"
				+ "luke UNKNOWN 1508405807560 luke LOST leia\n" + "vader UNKNOWN 1508405807560 vader HELLO\n"
				+ "leia UNKNOWN 1508405807560 luke LOST leia\n" + "vader UNKNOWN 1508405807512 luke LOST vader\n"
				+ "vader UKNOWN 1508405807512 vader LOST luke\n";
		assertEquals(expectedOutput, outContent.toString());
	}

}
