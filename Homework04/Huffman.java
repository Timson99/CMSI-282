  //package huffman;

import java.util.Map;
import java.util.*;

/**
 * Huffman instances provide reusable Huffman Encoding Maps for
 * compressing and decompressing text corpi with comparable
 * distributions of characters.
 */
public class Huffman {
    
    // -----------------------------------------------
    // Construction
    // -----------------------------------------------

    private HuffNode trieRoot;
    private Map<Character, String> encodingMap = new HashMap<Character,String>();
    private Map<Character, Integer> frequencyTable;
    
    private String corpus;
    
    
     //Lowest Count Returned first
    private PriorityQueue<HuffNode> frequencyQueue;
     
   
    /**
     * Creates the Huffman Trie and Encoding Map using the character
     * distributions in the given text corpus
     * @param corpus A String representing a message / document corpus
     *        with distributions over characters that are implicitly used
     *        throughout the methods that follow. Note: this corpus ONLY
     *        establishes the Encoding Map; later compressed corpi may
     *        differ.
     */
    Huffman (String corpus) {
        
        this.corpus = corpus;
        createFrequencyTable();
        createFrequencyQueue();
        createHuffmanTrie();
        createEncodingMap(trieRoot, ""); 
        
    }
    
    
    
    private void createFrequencyTable() {
        
        frequencyTable = new HashMap<Character,Integer>();
        
        for(int i = 0; i < corpus.length(); i++) {
            
            char tempChar = corpus.charAt(i);
            if(frequencyTable.containsKey(tempChar)) {
                frequencyTable.put(tempChar, frequencyTable.get(tempChar) + 1);
            }
            else {
                frequencyTable.put(tempChar,1);
            }
        }   
    }
    
    private void createFrequencyQueue() {
        
        frequencyQueue = new PriorityQueue<HuffNode>();
        
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            Character character = entry.getKey();
            Integer count = entry.getValue();
            
            frequencyQueue.add( new HuffNode( character , count ) );
        }
    }
    
    private void createHuffmanTrie() {
        
        while(frequencyQueue.size() > 1) {
            HuffNode leftChild = frequencyQueue.remove();
            HuffNode rightChild = frequencyQueue.remove();
            HuffNode parent = new HuffNode('\0', leftChild.count + rightChild.count);
            parent.left = leftChild;
            parent.right = rightChild;
            frequencyQueue.add(parent);
        }
        trieRoot = frequencyQueue.remove();
    }
    
    private void createEncodingMap(HuffNode localRoot, String path) {
        
        if( localRoot.isLeaf() ) {
            encodingMap.put(localRoot.character, path);
            return;
        }
        else {
            createEncodingMap(localRoot.left,  path + "0");
            createEncodingMap(localRoot.right, path + "1");
        }
    }
   

    // -----------------------------------------------
    // Compression
    // -----------------------------------------------
    
    /**
     * Compresses the given String message / text corpus into its Huffman coded
     * bitstring, as represented by an array of bytes. Uses the encodingMap
     * field generated during construction for this purpose.
     * @param message String representing the corpus to compress.
     * @return {@code byte[]} representing the compressed corpus with the
     *         Huffman coded bytecode. Formatted as 3 components: (1) the
     *         first byte contains the number of characters in the message,
     *         (2) the bitstring containing the message itself, (3) possible
     *         0-padding on the final byte.
     */
    public byte[] compress (String message) {
        
        String content = "";
        for(int i = 0; i < message.length(); i++) 
            content += encodingMap.get(message.charAt(i));
        
        int byteArrSize = (int)(content.length() / 8) + 2;     
        byte[] binaryCompression = new byte[byteArrSize];
        
        byte characterCount = (byte)message.length();
        binaryCompression[0] = characterCount;
        
        while(content.length() % 8 != 0)
            content += "0";
        
        for(int i = 1; i < byteArrSize; i++) {
            String byteStr = content.substring( (i-1) * 8, i*8 );
            binaryCompression[i] = (byte)Integer.parseInt(byteStr,2); 
        }
            
        return binaryCompression;
    }
    
    
    // -----------------------------------------------
    // Decompression
    // -----------------------------------------------
    
    /**
     * Decompresses the given compressed array of bytes into their original,
     * String representation. Uses the trieRoot field (the Huffman Trie) that
     * generated the compressed message during decoding.
     * @param compressedMsg {@code byte[]} representing the compressed corpus with the
     *        Huffman coded bytecode. Formatted as 3 components: (1) the
     *        first byte contains the number of characters in the message,
     *        (2) the bitstring containing the message itself, (3) possible
     *        0-padding on the final byte.
     * @return Decompressed String representation of the compressed bytecode message.
     */
    public String decompress (byte[] compressedMsg) {
        
        int arrSize = compressedMsg.length;
        int msgSize = (int)compressedMsg[0];
        String compressedBitStr = "";
        String decompressedMsg = "";
          
        for(int i = 1; i < arrSize; i++) {
            compressedBitStr += String.format("%8s", Integer.toBinaryString(compressedMsg[i] & 0xFF)).replace(' ', '0');
        }
        
        int counter = 0;
        HuffNode localRoot = trieRoot;
        while(decompressedMsg.length() < msgSize || counter == compressedBitStr.length() ) {
           
            char pathChar = compressedBitStr.charAt(counter);
            
            if(pathChar == '1') {
                localRoot = localRoot.right;
            }
            else if(pathChar == '0') {
                localRoot = localRoot.left;
            }
            else {
                break;
            }
            
            if(localRoot.isLeaf()) {
                
                decompressedMsg += localRoot.character;
                localRoot = trieRoot;
            }
            
        
            counter++;
        }
        return decompressedMsg;
    }
    
    
    // -----------------------------------------------
    // Huffman Trie
    // -----------------------------------------------
    
    /**
     * Huffman Trie Node class used in construction of the Huffman Trie.
     * Each node is a binary (having at most a left and right child), contains
     * a character field that it represents (in the case of a leaf, otherwise
     * the null character \0), and a count field that holds the number of times
     * the node's character (or those in its subtrees) appear in the corpus.
     */
    private static class HuffNode implements Comparable<HuffNode> {
        
        public HuffNode left, right;
        public char character;
        public int count;
        
        HuffNode (char character, int count) {
            this.count = count;
            this.character = character;
        }
        
        public boolean isLeaf () {
            return left == null && right == null;
        }
        
        public int compareTo (HuffNode other) {
            return this.count - other.count;
        }
        
    }

}
