/*
 * Andrew Peklar
 * Assingment #4
 * 10 May, 2017
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Hw4 {
	
    public static void main(String[] args) throws FileNotFoundException {
        //set filepath directory and scanner object
        File file = new File("input.txt"); 
        Scanner scan = new Scanner(file);
        
        //change out to place output to text file
        PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out);
        
        //first 3 numbers of the header
        int numPages    = scan.nextInt(); 
        int numFrames   = scan.nextInt();
        int numAccReq   = scan.nextInt();
        
        //create and fill List of page access requests
        ArrayList<Integer> list = new ArrayList<>(numAccReq);
        while(scan.hasNextInt())    list.add(scan.nextInt());
             
        //list.forEach(System.out::println);  

        //Replacement Policy Calls
        FIFO(list, numPages, numFrames, numAccReq);
        System.out.println();
        System.out.println();
        Optimal(list, numPages, numFrames, numAccReq);
        System.out.println();
        System.out.println();
        LRU(list, numPages, numFrames, numAccReq);
        
        scan.close();
        out.close();      
    }
    
    public static void FIFO(ArrayList<Integer> list, int numPages, int numFrames, int numAccReq) {
        System.out.println("FIFO");
        ArrayList<Integer> newFrames = new ArrayList<>(numFrames);

        int count       = -1;
        int faults      =  0;
        int frameIdx    =  0;
        int curPage;
         
        while(++count != numAccReq) {
            //next input from list of page requests
            curPage = list.get(count);
            
            //reset Frame index -- FIFO Exclusively
            if(frameIdx == numFrames) frameIdx = 0;
            
            //If page not already loaded and there's still empty space
            if(!newFrames.contains(curPage) && count <= numFrames) {
                newFrames.add(curPage);
                System.out.println("Page " + curPage + " loaded in Frame " + frameIdx);
                frameIdx++;
                faults++;
            }
            //If page not aleady loaded and no empty space -> toss out first in and replace
            else if (!newFrames.contains(curPage) && count > numFrames) {
                System.out.println("Page " + newFrames.get(frameIdx) + " unloaded from Frame " + frameIdx + ", Page " + curPage + " loaded in Frame " + frameIdx);
                newFrames.set(frameIdx, curPage);
                frameIdx++;
                faults++;
            }
            //Otherwise, page is already occupying a frame
            else    
                System.out.println("Page " + curPage + " already in Frame " + newFrames.indexOf(curPage));
        }
        System.out.println("Number of page faults: " + faults);
    }//end of FIFO    
    
    public static void LRU(ArrayList<Integer> list, int numPages, int numFrames, int numAccReq) {
        System.out.println("LRU");

        ArrayList<Integer> newFrames    = new ArrayList<>(numFrames);
        ArrayList<Integer> frameFreq    = new ArrayList<>(numFrames);
        ArrayList<Integer> rangeArray   = new ArrayList<>();
        
        int count       = -1;
        int faults      =  0;
        int frameIdx    =  0;
        int curPage, minIdx;
        
        while(++count != numAccReq) {
            //next input from list of page requests
            curPage = list.get(count);
            //consumed input from stream
            rangeArray.add(curPage);
            
            //reset Frame index
            if(frameIdx == numFrames) frameIdx = 0;
            
            //If page not already loaded and there's still empty space
            if(!newFrames.contains(curPage) && count <= numFrames) {
                newFrames.add(curPage);
                System.out.println("Page " + curPage + " loaded in Frame " + frameIdx);
                frameIdx++;
                faults++;
            }
            //If page not aleady loaded and no empty space
            else if (!newFrames.contains(curPage) && count > numFrames) {
                for(int frame : newFrames) frameFreq.add(rangeArray.lastIndexOf(frame));
                minIdx = frameFreq.indexOf(Collections.min(frameFreq));             
                System.out.println("Page " + newFrames.get(minIdx) + " unloaded from Frame " + minIdx + ", Page " + curPage + " loaded in Frame " + minIdx);
                newFrames.set(minIdx, curPage);
                frameFreq.clear();
                faults++;
            }
            //Otherwise, page is already occupying a frame
            else    
                System.out.println("Page " + curPage + " already in Frame " + newFrames.indexOf(curPage));
        }
        System.out.println("Number of page faults: " + faults); 
    }//End of LRU
     
    
    public static void Optimal(ArrayList<Integer> list, int numPages, int numFrames, int numAccReq) {
        System.out.println("Optimal");

        ArrayList<Integer> newFrames    = new ArrayList<>(numFrames);
        ArrayList<Integer> frameFreq    = new ArrayList<>(numFrames);
        ArrayList<Integer> rangeArray   = new ArrayList<>();

        int count       = -1;
        int faults      =  0;
        int frameIdx    =  0;
        int curPage,  minIdx;
        
        while(++count != numAccReq) {
            //next input from list of page requests
            curPage = list.get(count);
           
            //consumed input from stream
            for(int i = count; i != numAccReq; i++)
              rangeArray.add(list.get(i));

            if(frameIdx == numFrames) frameIdx = 0;
            
            //If page not already loaded and there's still empty space
            if(!newFrames.contains(curPage) && count <= numFrames) {
                newFrames.add(curPage);
                System.out.println("Page " + curPage + " loaded in Frame " + frameIdx);
                frameIdx++;
                faults++;
            }
            //If page not aleady loaded and no empty space
            else if (!newFrames.contains(curPage) && count > numFrames) {
                for(int frame : newFrames) frameFreq.add(rangeArray.lastIndexOf(frame));
                minIdx = frameFreq.indexOf(Collections.min(frameFreq));             
                System.out.println("Page " + newFrames.get(minIdx) + " unloaded from Frame " + minIdx + ", Page " + curPage + " loaded in Frame " + minIdx);
                newFrames.set(minIdx, curPage);
                frameFreq.clear();
                faults++;
            }
            //Otherwise, page is already occupying a frame
            else    
                System.out.println("Page " + curPage + " already in Frame " + newFrames.indexOf(curPage));
            rangeArray.clear();
        }
        System.out.println("Number of page faults: " + faults);
    }       
}
