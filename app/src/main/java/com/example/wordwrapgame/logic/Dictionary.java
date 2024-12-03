package com.example.wordwrapgame.logic;

import android.util.Log;

import java.io.*;

public class Dictionary {

    static final int NUM_CHARS = 26;

    private static class Node {
        Node[] children = new Node[NUM_CHARS];
        boolean isWord;
    }

    private Node root;
    int numWords;

    public Dictionary() {
        root = new Node();
        numWords = 0;
    }

    // constructor with a file name
    public Dictionary(String fileName)  {
        root = new Node();
        numWords = 0;
        loadDictionaryFile(fileName);
    }

    public Dictionary(InputStream inputStream)   {
        root = new Node();
        numWords = 0;
        loadDictionaryResource(inputStream);
    }


    public void loadDictionaryFile(String filename) {

        // while there are words in the file
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try  {
            String line;
            while( (line = br.readLine()) != null )
            {
                addWord(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDictionaryResource(InputStream inputStream) {
        // load dictionary from a resource file

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                addWord(line);
            }
        } catch (Exception e) {
            Log.wtf("Dictionary",e.getMessage());
        }
    }


        public boolean addWord(String word) {
        Node currNode = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (index > 25 || index < 0) { // invalid character
                return false;
            } else {
                if (currNode.children[index] == null) {
                    currNode.children[index] = new Node();
                }
                currNode = currNode.children[index];
            }
        }
        if (currNode.isWord) {
            return false; // word already exists
        } else {
            currNode.isWord = true;
            numWords++;
            return true;
        }
    }

    public boolean isWord(String word) {
        Node currNode = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (index > 25 || index < 0) { // invalid character
                return false;
            } else {
                if (currNode.children[index] == null) {
                    return false;
                }
                currNode = currNode.children[index];
            }

        }
        return currNode.isWord;
    }

    public boolean isPrefix(String word) {
        Node currNode = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (index > 25 || index < 0) { // invalid character
                return false;
            } else {
                if (currNode.children[index] == null) { // if we run into a null child, the word doesn't exist
                    return false;
                }
                currNode = currNode.children[index];
            }
        }
        // if we make it to the end of the word, it must be a prefix
        return true;
    }

    public int getNumWords() {
        return numWords;
    }

    public void printWords() {
        printHelper(root, "");
    }

    private void printHelper(Node curr, String prefix) {
        if (curr == null)
            return;
        if (curr.isWord)
            System.out.println(prefix);
        for(int i = 0; i < NUM_CHARS; i++) {
            if (curr.children[i] != null) {
                printHelper(curr.children[i], prefix + (char)('a' + i));
            }
        }
    }


} // end class
