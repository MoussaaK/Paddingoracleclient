package org.dador.paddingOracleClient;


import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Main Class for Padding OracleClient
 */
public class OraclePaddingClient {
    static final String ENCRYPTED_MESSAGE = "5ca00ff4c878d61e1edbf1700618fb287c21578c0580965dad57f70636ea402fa0017c4acc82717730565174e2e3f713d3921bab07cba15f3197b87976525ce4";
    static final int BLOCK_SIZE = 16;

    /**
     * Fonction takes a number and creates a block of x00 values, padded according to PKCS#7
     * example : n=3 result is 00 00 .. 00 03 03 03
     * @param n : number of bytes of padding
     * @return byte[BLOCK_SIZE] filled with 0 and padding values
     */
    protected byte[] getPaddingArray(int n) {
        byte[] result = new byte[BLOCK_SIZE];

        /**
         * TODO : Your CODE HERE
         */
        return result;
    }

    /**
     * Function that create a modified ciphertext bloc for trying a guess
     * Note that the "ciphertext" correspond to the IV part for the Block Cipher
     * @param ciphertext : original ciphertext bloc
     * @param decoded    : decrypted part of the plain text (for next bloc)
     * @param position   : position of the byte to guess
     * @param guess      : the guess for this query
     * @return a byte array with c0...c(i-1)||ci+i+g||cj+mj+i||...||cn+mn+i
     */
    protected byte[] buildGuessForPosition(byte[] ciphertext, byte[] decoded, int position, byte guess) {
        byte[] result = new byte[BLOCK_SIZE];
        
        result = ciphertext;
        result[result.length - 1] = guess;
        
        return result;
    }

    /**
     * Fonction that splits a message into constituent blocs of BLOCK_SIZE
     *
     * @param message
     * @return an array of blocs
     * @throws IllegalArgumentException
     */
    protected byte[][] splitMessageIntoBlocks(byte[] message) throws IllegalArgumentException {
        if (message.length % BLOCK_SIZE != 0) {
            throw new IllegalArgumentException("Message length is not a multiple of bloc size");
        }
        /**
         * TODO : YOUR CODE HERE
         */
        byte[][] splittedArray = new byte[message.length/BLOCK_SIZE][BLOCK_SIZE];
        for (int i = 0; i < message.length/BLOCK_SIZE; i++) {
        	for (int j = 0; j < BLOCK_SIZE; j++) {
        		splittedArray[i][j] = message[i*BLOCK_SIZE + j];
        	}
        }
        return splittedArray;
    }

    /**
     * Function that takes the 2 last blocks of the message
     * and returns the length of the padding.
     * @param poq : a PaddingOracleQuery object
     * @param previousbloc : next to last block of the ciphertext
     * @param lastbloc : last bloc of the ciphertext
     * @return an integer corresponding to padding length
     * @throws IOException
     * @throws URISyntaxException
     */
    public int getPaddingLengthForLastBlock(PaddingOracleQuery poq, byte[] previousbloc, byte[] lastbloc) throws IOException, URISyntaxException {
        /**
         * TODO : Your Code HERE
         */
        // should not arrive here !
        return 0;
    }

    /**
     * Main function that takes 2 consecutive blocks of the ciphertext
     * and returns the decryption of the 2nd message block
     *
     * @param poq : a PaddingOracleQuery object to query server
     * @param iv : the "iv" part of the 2 blocks query
     * @param ciphertext : the block that will be decrypted
     * @param padding : set to 0 if not the last block. Set to paddinglength if last block
     * @return a decrypted byte array
     * @throws IOException
     * @throws URISyntaxException
     */
    public byte[] runDecryptionForBlock(PaddingOracleQuery poq, byte[] iv, byte[] ciphertext, int padding) throws IOException, URISyntaxException {
        byte[] decoded = new byte[BLOCK_SIZE];
        if (padding > 0) {
            decoded = getPaddingArray(padding);
        }
        /**
         * TODO : YOUR CODE HERE
         */
        
        String firstBlockString = HexConverters.toHexFromByteArray(iv);
    	String secondBlockString = HexConverters.toHexFromByteArray(ciphertext);
    	String hexFromByteArray;
		
        
    	byte[] firstBlockByte = HexConverters.toByteArrayFromHex(firstBlockString);
    	byte lastByte = firstBlockByte[firstBlockByte.length - 1];
    	
    	int position = 0;
		byte guess = 0;
		boolean query;
		lastByte = firstBlockByte[firstBlockByte.length - 1];
    	do {
    		
    		firstBlockByte = buildGuessForPosition(firstBlockByte, null, position , guess);
    		firstBlockString = HexConverters.toHexFromByteArray(firstBlockByte);
    		hexFromByteArray = firstBlockString + secondBlockString;
    		query = poq.query(hexFromByteArray);
    		guess++;
    	} while(!query);
    	guess--;
    	
    	decoded[firstBlockByte.length - 1] = (byte) (lastByte^0x01^guess);
    	System.out.println("Value Guessed : " + guess);
    	System.out.println("Server responded : " + query);
    	
        return decoded;
    }

    public static void main(String[] args) {
        OraclePaddingClient opc = new OraclePaddingClient();
        PaddingOracleQuery opq = new PaddingOracleQuery();
        try {
        	byte[][] blocks = opc.splitMessageIntoBlocks(HexConverters.toByteArrayFromHex(ENCRYPTED_MESSAGE));
        	
        	byte[] b = opc.runDecryptionForBlock(opq, blocks[0], blocks[1], 0);
        	String hexFromByteArray = HexConverters.toHexFromByteArray(b);
			opq.query(hexFromByteArray);
		
			System.out.println(HexConverters.toHexFromByteArray(b));
			
        	
        	
        	
        } catch (Exception e) {
            System.out.print("Exception caught. Server down ?");
            e.printStackTrace();
        }
        try {
            /**
             * TODO : YOUR CODE HERE
             */
        } catch (Exception e) {
            System.out.print("Exception caught. Server down ?");
            e.printStackTrace();
        }
    }

}

