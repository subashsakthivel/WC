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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void duck(){
        System.out.println(nWords + " " + Thread.currentThread().getName());
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
        this.precaution = false;
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
        char prev = '\0';
        char x, y='\0';
        while(limit!=0) {
            if(limit <= 10000) capacity = (int)limit;
            else capacity =10000;
            limit -= capacity;
            ByteBuffer buffer = ByteBuffer.allocate(capacity);
            noOfBytesRead = fileChannel.read(buffer);
            buffer.flip();
            if(precaution && start!=0){
                prev = (char)buffer.get();
                precaution = false;
            }
            if(buffer.hasRemaining()) {
                x = (char) buffer.get();
                if (x == '\n')
                    nLines++;
                else if ((prev == ' ' || prev == '\n') && x != ' ')
                    nWords++;
            } else {
                break;
            }
            while ( buffer.hasRemaining()) {
                y = (char) buffer.get();
                if(y=='\n')
                    nLines++;
                else if((x==' ' || x=='\n') && y!=' ')
                    nWords++;
                x = y;
            }
            prev = y;
        }
    }
}

