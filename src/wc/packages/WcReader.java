package wc.packages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

 class WcReader implements Runnable{
     static private class Totals{
         public static long nBytes;
         public static long nChars;
         public static long nWords;
         public static long nLines;
     }

    private final FileChannel fileChannel;
    private final String fileName;
    private final boolean large;
    private final long limit;
    private final long nBytes;
    private final long nChars;
    private long nWords;
    private long nLines;


    public WcReader(String fileName , FileChannel fileChannel , long start, long limit, boolean large) throws IOException {
        this.fileName = fileName;
        this.fileChannel = fileChannel;
        this.nChars = limit-start;
        this.nBytes = nChars*Character.BYTES;
        this.limit = limit;
        this.large = large;
        if(large) {
            if (start > 0) fileChannel.position(start - 1);
            if (start == 0) nWords++;
            else fileChannel.position(start);
        }
    }

    private void reader() throws IOException{
        int noOfBytesRead = 0;
        while(noOfBytesRead != -1) {
            ByteBuffer buffer = ByteBuffer.allocate(10000);
            noOfBytesRead = fileChannel.read(buffer);
            buffer.flip();
            char x;
            while(buffer.hasRemaining() ){
                x = (char)buffer.get();
                if(x=='\n')
                {
                    nLines++;
                }
                else if(x!= ' ') {
                    while(buffer.hasRemaining() && x!=' '){
                        //  System.out.print(x);
                        x = (char)buffer.get();
                        if(x=='\n') {
                            nLines++;
                            break;
                        }
                    }
                    nWords++;
                    //      System.out.println(",");
                }
            }
        }
        synchronized (this) {
            Totals.nBytes += this.nBytes;
            Totals.nChars += this.nChars;
            Totals.nLines += this.nLines;
            Totals.nWords += this.nWords;
        }
    }
    private void readLarger() throws IOException {
        int noOfBytesRead = 0;
        long start= fileChannel.position();
        boolean precaution = true;
        while(noOfBytesRead != -1 && start<limit) {
            ByteBuffer buffer = ByteBuffer.allocate(10000);
            noOfBytesRead = fileChannel.read(buffer);
            buffer.flip();
            char x;
            if(precaution && buffer.hasRemaining()) {
                x = (char) buffer.get();
                if (x != ' ') nWords--;
                if (x == '\n') nWords++;
                precaution = false;
            }
            while(buffer.hasRemaining() && start<limit){
                x = (char)buffer.get();
                start++;
                if(x=='\n')
                {
                    nLines++;
                }
                else if(x!= ' ') {
                    while(buffer.hasRemaining() && x!=' ' && start<limit){
                      //  System.out.print(x);
                        x = (char)buffer.get();
                        start++;
                        if(x=='\n') {
                            nLines++;
                            break;
                        }
                    }
                    nWords++;
              //      System.out.println(",");
                }
            }
        }
        synchronized (this) {
            Totals.nBytes += this.nBytes;
            Totals.nChars += this.nChars;
            Totals.nLines += this.nLines;
            Totals.nWords += this.nWords;
        }
    }

    public static void totalResult(){
        System.out.println("-Totals : " + " " + Totals.nBytes + " " + Totals.nChars + " "+Totals.nWords +" " + Totals.nLines);
    }

    public static void LargerResult(){
        System.out.println(Totals.nBytes + " " + Totals.nChars + " "+Totals.nWords + " "+ (Totals.nLines+1));
    }

    private void result(){
       // "Bytes Chars Words Lines"
        System.out.println("-" + fileName + " " + nBytes + " " + nChars + " "+nWords + " "+ nLines );
    }

    @Override
    public void run() {
        try {
            if(large) {
                readLarger();
            } else {
                reader();
                result();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected long lines() throws IOException {
        reader();
         return this.nLines;
     }

     protected long words() throws IOException {
        reader();
         return this.nWords;
     }

     protected long chars() {
         return this.nChars;
     }

     protected long bytes() {
         return this.nBytes;
     }

     protected String details() throws IOException {
        reader();
        return nBytes + " " + nChars + " "+nWords + " "+ nLines;
     }
 }
