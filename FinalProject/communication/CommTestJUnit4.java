//
// Just for fun, wanted to see how it worked.
//

package FinalProject.communication;

import org.junit.*;
import static org.junit.Assert.*;

import java.net.InetAddress;

public class CommTestJUnit4 {

    private Comm comm1;
    private Comm comm2;
    private Comm comm3;

    @BeforeClass
    public static void oneTimeSetUp() {
    }

    @AfterClass
    public static void oneTimeTearDown() {
    }

    @Before
    public void setUp() throws Exception {
        comm1 = new Comm(2000);
        comm2 = new Comm(2011);
        comm3 = new Comm(2111);
    }

    @After
    public void tearDown() throws Exception {
        comm1.shutdown();
        comm2.shutdown();
        comm3.shutdown();
    }

    @Test
    public void testConnectToParent() throws Exception {
        int test;
        test = comm2.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        assertEquals(0, test);
        test = comm3.connectToParent(InetAddress.getByName("127.0.0.1"), 2011);
        assertEquals(0, test);
        test = comm1.connectToParent(InetAddress.getByName("127.0.0.1"), 1000);
        assertEquals(1000, test);
    }

    @Test
    public void testSendMessageClient() throws Exception {
        comm2.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        comm3.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        String sentObject = "test";

        comm1.sendMessageClient(sentObject);

        Object receivedObject = comm2.getMessageBlocking();
        Object receivedObject2 = comm3.getMessageBlocking();
        String receivedString = (String) receivedObject;
        String receivedString2 = (String) receivedObject2;
        assertEquals(receivedObject, receivedObject2);
        assertEquals(sentObject, receivedString);
        assertEquals(sentObject, receivedString2);

        int testFailure;
        testFailure = comm2.sendMessageClient(sentObject);
        assertEquals(1002, testFailure);
    }

    @Test
    public void testSendMessageParent() throws Exception {
        comm2.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        String sentObject = "test";

        comm2.sendMessageParent(sentObject);

        Object receivedObject = comm1.getMessageBlocking();
        String receivedString = (String) receivedObject;
        assertEquals(sentObject, receivedString);
    }

    @Test
    public void testSendMessageBroadcast() throws Exception {
        comm2.connectToParent(InetAddress.getByName("127.0.0.1"), 2000);
        comm3.connectToParent(InetAddress.getByName("127.0.0.1"), 2011);
        String sentObject = "test";

        comm2.sendMessageBroadcast(sentObject);

        Object receivedObject = comm1.getMessageBlocking();
        Object receivedObject2 = comm3.getMessageBlocking();
        String receivedString = (String) receivedObject;
        String receivedString2 = (String) receivedObject2;
        assertEquals(receivedObject, receivedObject2);
        assertEquals(sentObject, receivedString);
        assertEquals(sentObject, receivedString2);
    }

    @Test
    public void testSendMessageReply() throws Exception {

    }

    @Test
    public void testGetMessageNonBlocking() throws Exception {

    }

    @Test
    public void testGetMessageBlocking() throws Exception {

    }

    @Test
    public void testGetMessageBlocking1() throws Exception {

    }
}