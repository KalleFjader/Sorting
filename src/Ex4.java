import java.io.*;
import java.util.Scanner;
import java.lang.System;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.IOException;
import java.io.FileNotFoundException;

public final class Ex4 {
    private static final int MAXSIZE = 1000000;  // Max nr of words
    private static final int CUTOFF = 20;       // Cut-off for recursive quicksort

    public static String[] insertionSort (String[] w) {
        String temp="";
        for(int i=0;i<w.length;i++){
            for(int j=i+1;j<w.length;j++){
                if(w[i].compareToIgnoreCase(w[j])>0){
                    temp = w[i];
                    w[i]=w[j];
                    w[j]=temp;
                }
            }
        }
        return w;
    }

    public static String[] mergeSort(String[] a, int n) {
        if (n < 2) {
            return a;
        }
        int mid = n / 2;
        String[] l = new String[mid];
        String[] r = new String[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = a[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(a, l, r, mid, n - mid);
        return a;
    }

    public static void merge(String[] a, String[] l, String[] r, int left, int right) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i].compareToIgnoreCase(r[j]) <= 0) {
                a[k++] = l[i++];
            }
            else {
                a[k++] = r[j++];
            }
        }
        while (i < left) {
            a[k++] = l[i++];
        }
        while (j < right) {
            a[k++] = r[j++];
        }
    }


    public static String[] quickSort(String[] w, int begin, int end) {
        if (w.length < CUTOFF) {
            insertionSort(w);
        }
        if (begin < end) {
            int partitionIndex = partition(w, begin, end);

            quickSort(w, begin, partitionIndex - 1);
            quickSort(w, partitionIndex + 1, end);
        }
        return w;
    }

    private static int partition(String[] w, int begin, int end) {
        String pivot = w[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (w[j].compareToIgnoreCase(pivot) <= 0) {
                i++;
                String swapTemp = w[i];
                w[i] = w[j];
                w[j] = swapTemp;
            }
        }
        String swapTemp = w[i + 1];
        w[i + 1] = w[end];
        w[end] = swapTemp;

        return i + 1;
    }


    public static String[] readWords(String fileName) {
        String[] words = new String[MAXSIZE];
        int rowCount = 0;
        int wordCount = 0;
        try {
            BufferedReader myReader = new BufferedReader(new FileReader(fileName));
            String inputLine, thisLine;

            // Read lines until end of file
            while ((inputLine = myReader.readLine()) != null) {
                // Remove punctuation characters and convert to lower case
                //// Note: compound words will have the - removed !!!
                thisLine = inputLine.replaceAll("\\p{Punct}", "").toLowerCase();
                if (thisLine.length() !=0) {         // Skip empty lines
                    // Split the line into separate words on one or more whitespace
                    String[] w = thisLine.split("\\p{IsWhite_Space}+");
                    // Put the words in an array
                    for(String s:w){
                        if (!s.isEmpty()) words[wordCount++] = s;  // Skip empty words, count nr of words
                    }
                    rowCount++;    // Count number of rows
                }
            }
            System.out.println();
            System.out.println("Read " + rowCount + " rows and " + wordCount + " words");
            // Return the words in an array of of length wordCound
            return(java.util.Arrays.copyOfRange(words, 0, wordCount));
        }

        catch (IOException e) { // No file
            System.out.println("Error: " + e.getMessage());
            return (null);
        }
    }


    public static void writeWords(String [] words, String fileName) {
        BufferedWriter bw = null;
        try {
            File outputFile = new File(fileName);
            outputFile.createNewFile();        // Create the output file
            FileWriter fw = new FileWriter(outputFile);
            bw = new BufferedWriter(fw);
            for (String s:words) {       // Write the words to the file
                bw.write(s + " ");     //
            }
            bw.newLine();
            System.out.println("Wrote file " + fileName);

        } catch (IOException e) {
            System.out.println ("No file " + fileName);
            System.exit(0);
        }
        finally {
            try {
                if (bw != null) bw.close();
            }
            catch (Exception ex) {
                System.out.println("Error in closing file " + fileName);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1 ) {
            System.out.println("Please give the file name");
            System.exit(0);
        }
        String fileName = args[0];   // Get the file name from the argument

        // Read the words from the input file
        String[] words = readWords(fileName);
        if (words == null) System.exit(0);     // Quit if file is not found

        // Make copies of the input for each sorting method
        String[] words1 = Arrays.copyOf(words, words.length);
        String[] words2 = Arrays.copyOf(words, words.length);
        String[] words3 = Arrays.copyOf(words, words.length);

        System.out.println();
        System.out.println("Sorting with Insertion sort ");
        // Test the insertion sort method and measure how long it takes
        long startTime = System.nanoTime();
        insertionSort(words1);
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time for InsertionSort: " + estimatedTime/1000000000.0 + " seconds");
        // Write the result of insertion sort to a new file
        writeWords(words1, fileName + ".InsertionSort" );

        System.out.println();
        System.out.println("Sorting with MergeSort ");
        // Test the MergeSort method
        startTime = System.nanoTime();
        mergeSort(words2, words2.length);
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time for MergeSort: " + estimatedTime/1000000000.0 + " seconds");
        writeWords(words2, fileName + ".MergeSort" );


        // Test the quicksort method and measure how long it takes
        System.out.println();
        System.out.println("Sorting with Quicksort ");
        startTime = System.nanoTime();
        quickSort(words3, 0, words3.length-1);
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time for QuickSort: " + estimatedTime/1000000000.0 + " seconds");
        // Write the result of quicksort to a new file
        writeWords(words3, fileName + ".QuickSort" );
        System.out.println();
        System.out.println();

    }
}
