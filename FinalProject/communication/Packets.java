/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	Packets.java
 *
 */

package FinalProject.communication;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

final class Packets {

    //todo-dave Add these as function parameters, not constants.
    static final int MAX_PACKET_SIZE = 511;
    static final int MAX_PACKET_SIZE_BYTES = 4;
    static final int CHECKSUM_BYTE_SIZE = 8;

    private Packets(){}


    /**
     * Creates a packet
     * @param obj
     * @return
     * @throws IOException
     */
    static byte[] craftPacket(Object obj) throws IOException {
        byte[] objectBytes = serialize(obj);
        byte[] objectLength = ByteBuffer.allocate(8).putInt(objectBytes.length).array();

        byte[] packet = new byte[MAX_PACKET_SIZE-CHECKSUM_BYTE_SIZE-MAX_PACKET_SIZE_BYTES];
        System.arraycopy(objectLength, 0, packet, 0, MAX_PACKET_SIZE_BYTES);
        System.arraycopy(objectBytes, 0, packet, MAX_PACKET_SIZE_BYTES, objectLength.length);

        return addChecksum(packet);
    }

    /**
     *
     * @param packet
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static Object decodePacket(byte[] packet) throws IOException, ClassNotFoundException {
        byte[] data = removeChecksum(packet);
        byte[] objectLength = new byte[MAX_PACKET_SIZE_BYTES];
        System.arraycopy(data, 0, objectLength, 0, MAX_PACKET_SIZE_BYTES);
        byte[] objectBytes = new byte[ByteBuffer.wrap(objectLength).getInt()];
        System.arraycopy(data, MAX_PACKET_SIZE_BYTES, objectBytes, 0, objectBytes.length);

        return deserialize(objectBytes);
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
    static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
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
    static boolean validateChecksum(byte[] packet) {
        int length = packet.length;
        Checksum checksum = new CRC32();
        byte[] checksumBytes = new byte[CHECKSUM_BYTE_SIZE];

        System.arraycopy(packet, 0, checksumBytes, 0, CHECKSUM_BYTE_SIZE);

        checksum.update(packet, CHECKSUM_BYTE_SIZE, length-CHECKSUM_BYTE_SIZE);
        long calculatedChecksumValue = checksum.getValue();
        long givenChecksumValue = ByteBuffer.wrap(checksumBytes).getLong();

        return (calculatedChecksumValue == givenChecksumValue);
    }
}
