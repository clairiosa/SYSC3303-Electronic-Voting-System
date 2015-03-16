/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Packets.java
 *
 * Static functions for dealing with packets, checksums and byte arrays.
 *
 */

package FinalProject.communication;


import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

final class Packets {

    //todo-dave Add these as function parameters, not constants.
    static final int MAX_PACKET_SIZE = 8192;
    static final int MAX_PACKET_SIZE_BYTES = 4;
    static final int CHECKSUM_BYTE_SIZE = 8;


    /**
     * Class never gets instanced.
     */
    private Packets(){}


    /**
     * Creates a DatagramPacket given an object, the destination address and the port.
     *
     * @param obj           The object to place in the packet.
     * @param address       The destination IP address.
     * @param port          The destination port number.
     * @return              DatagramPacket
     * @throws IOException
     */
    static DatagramPacket craftPacket(Object obj, InetAddress address, int port) throws IOException {
        byte[] objectBytes = serialize(obj);
        byte[] packet = addChecksum(objectBytes);
        return new DatagramPacket(packet, packet.length, address, port);
    }


    /**
     * Decodes the DatagramPacket into an object.
     *
     * @param packet            The packet to decode.
     * @return                  The object that was inside the packet.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static Object decodePacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
        byte[] packetData = packet.getData();
        byte[] objectBytes = Packets.removeChecksum(packetData);
        return deserialize(objectBytes, 0, objectBytes.length);
    }


    /**
     * Returns a byte array of the given object.  Exceptions must be handled by
     * the caller.
     *
     * @param   obj         The object to change into a byte array.
     * @return              The byte array.
     * @throws java.io.IOException
     */
    static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }


    /**
     * Returns an object from an array of bytes.  Not intended to be used when the checksum
     * is present, call removeChecksum first.
     *
     * @param   data        The byte array to change back into an object.
     * @return              The object.
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    static Object deserialize(byte[] data, int offset, int length) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, length);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(in));
        return is.readObject();
    }


    /**
     * Adds an 8 byte checksum to the front of the given byte array.
     *
     * @param   data    Byte array to append the checksum in front of.
     * @return          The data with appended checksum.
     */
    static byte[] addChecksum(byte[] data) {
        int length = data.length;
        byte[] packet = new byte[length+CHECKSUM_BYTE_SIZE];
        Checksum checksum = new CRC32();
        long checksumValue;
        byte[] checksumBytes;

        checksum.update(data, 0, length);
        checksumValue = checksum.getValue();
        checksumBytes = ByteBuffer.allocate(8).putLong(checksumValue).array();

        System.arraycopy(checksumBytes, 0, packet, 0, CHECKSUM_BYTE_SIZE);
        System.arraycopy(data, 0, packet, CHECKSUM_BYTE_SIZE, length);

        return packet;
    }


    /**
     * Remove the 8 byte checksum from the front of the byte array.
     *
     * @param   packet  Byte array to remove checksum from.
     * @return          The data with removed checksum.
     */
    static byte[] removeChecksum(byte[] packet) {
        int length = packet.length;
        byte[] data = new byte[length-CHECKSUM_BYTE_SIZE];

        System.arraycopy(packet, CHECKSUM_BYTE_SIZE, data, 0, length-8);

        return data;
    }


    /**
     * Validates the checksum at the front of the byte array.  Intended to be used immediately
     * upon receipt of the packet.
     *
     * @param   packet  Byte array to validate the checksum for.
     * @return          Boolean indicating if the checksum passed or not.
     */
    static boolean validateChecksum(byte[] packet, int length) {
        Checksum checksum = new CRC32();
        byte[] checksumBytes = new byte[CHECKSUM_BYTE_SIZE];

        System.arraycopy(packet, 0, checksumBytes, 0, CHECKSUM_BYTE_SIZE);

        checksum.update(packet, CHECKSUM_BYTE_SIZE, length-CHECKSUM_BYTE_SIZE);
        long calculatedChecksumValue = checksum.getValue();
        long givenChecksumValue = ByteBuffer.wrap(checksumBytes).getLong();

        return (calculatedChecksumValue == givenChecksumValue);
    }



    static long calculateChecksum(byte[] data, int length) {
        Checksum checksum = new CRC32();
        checksum.update(data, 0, length);
        return checksum.getValue();
    }
}
