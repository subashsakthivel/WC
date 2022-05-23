package reader.wc;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

class WcReader implements Runnable {

    long nLines,nWords,nChars,nBytes,longLine,longLineCount,start;
    final String fileName;
    private final FileChannel fileChannel;
    private long limit;
    private boolean precaution;

    @Override
    public void run() {
        try {
            reader();
            debug();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void debug() {
        System.out.println(nWords + " " + nLines + " " + Thread.currentThread().getName());
    }

    public WcReader(String path , long start , long limit) throws IOException {
        fileName = path;
        fileChannel = FileChannel.open(Path.of(path));
        fileChannel.position(start);
        this.limit = limit;
        this.nChars = limit;
        this.nBytes = this.nChars*Character.BYTES;
        this.nLines = 1;
        this.start = start;
    }

    public WcReader(String path , long start , long limit, boolean precaution) throws IOException {
        fileName = path;
        fileChannel = FileChannel.open(Path.of(path));
        if(start!=0) {
            fileChannel.position(start - 1);
            this.limit = limit+1;
        } else {
            fileChannel.position(start);
            this.limit = limit;
            this.nLines = 1;
        }
        this.precaution = precaution;
        this.nChars = limit;
        this.nBytes = this.nChars*Character.BYTES;
        this.start = start;
    }

    private void reader() throws IOException {
        int noOfBytesRead = 0;
        int capacity;
        long lineCount = 0;
        while(limit!=0) {
            if(limit <= 10000) capacity = (int)limit;
            else capacity =10000;
            limit -= capacity;
            ByteBuffer buffer = ByteBuffer.allocate(capacity);
            noOfBytesRead = fileChannel.read(buffer);
            buffer.flip();
            char x;
            if(precaution && start!=0){
                x = (char) buffer.get();
                //if(x!=' ' && x!='\n') nWords--;
                while(buffer.hasRemaining() &&  x!=' '){
                    x = (char) buffer.get();
                    if(x=='\n') {
                        nLines++;
                        break;
                    }
                }
                precaution = false;
            }

            while(buffer.hasRemaining()){
                x = (char)buffer.get();
                //System.out.print(x);
                lineCount++;
                if(x=='\n')
                {
                    nLines++;
                    if(longLineCount < lineCount){
                        longLineCount = lineCount;
                        longLine = nLines;
                    }
                    lineCount = 0;
                }
                else if(x!= ' ') {
                    while(buffer.hasRemaining() && x!=' '){
                        x = (char)buffer.get();
                        lineCount++;
                        //System.out.print(x);
                        if(x=='\n') {
                            nLines++;
                            if(longLineCount < lineCount){
                                longLineCount = lineCount;
                                longLine = nLines;
                            }
                            lineCount = 0;
                            break;
                        }
                    }
                    nWords++;
                }
            }
        }
    }
}

