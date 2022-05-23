package reader.wc;

import java.io.File;
import java.io.IOException;

class Utils {

    private long nLines, nWords,nChars, nBytes, longLine,longLineCount;
    private long sizeOfFile(String path){
        return new File(path).length();
    }

    private WcReader[] readLarger(String path) throws IOException {
        int noOfThreads = Runtime.getRuntime().availableProcessors(); // noOfCore + extra 2 threads
        long fileSize = sizeOfFile(path);
        long noOfBytesPerThread = fileSize/noOfThreads;
        int remainingBytes = (int)fileSize%noOfThreads;
        Thread[] threads = new Thread[noOfThreads+1]; // creating threads
        WcReader[] wcReaders = new WcReader[noOfThreads+1];
        long startPosition =0;
        for(int i=0;i<noOfThreads;i++){
            wcReaders[i] = new WcReader(path , startPosition , noOfBytesPerThread, true); // unique for every threads
            threads[i]= new Thread(wcReaders[i]);
            threads[i].start();
            startPosition+=noOfBytesPerThread;
        }
        // remaining read
        wcReaders[noOfThreads] = new WcReader(path , startPosition , remainingBytes,true);
        threads[noOfThreads] = new Thread(wcReaders[noOfThreads]);
        threads[noOfThreads].start();

        // wait until all threads complete the task
        for(int i=0;i<=noOfThreads;i++){
            while(threads[i].isAlive());
        }

        return wcReaders;
    }

    protected WcReader[] readAllFiles(String[] paths) throws IOException {
        Thread[] threads = new Thread[paths.length];
        WcReader[] wcReaders = new WcReader[paths.length];
        for(int i=0;i<paths.length;i++){
            wcReaders[i] = new WcReader(paths[i],0,sizeOfFile(paths[i]));
            threads[i] = new Thread(wcReaders[i]);
            threads[i].start();
        }

        //wait for all threads to complete the task
        for(int i=0;i<paths.length;i++){
            while (threads[i].isAlive());
        }

        return wcReaders;
    }

    protected long getWords(String path) throws IOException{
        WcReader[] wcReaders = readLarger(path);
        for(WcReader wcReader : wcReaders)
            nWords += wcReader.nWords;
        return nWords;
    }

    protected long getBytes(String path) throws IOException {
        WcReader[] wcReaders = readLarger(path);
        for(WcReader wcReader : wcReaders)
            nBytes += wcReader.nBytes;
        return nBytes;
    }

    protected  long getChars(String path) throws IOException {
        WcReader[] wcReaders = readLarger(path);
        for(WcReader wcReader : wcReaders)
            nChars+= wcReader.nChars;
        return nChars;
    }

    protected long getLines(String path) throws IOException {
        WcReader[] wcReaders = readLarger(path);
        for(WcReader wcReader : wcReaders)
            nLines += wcReader.nLines;
        return nLines;
    }

    protected long getLongLines(String path) throws IOException {
        WcReader[] wcReaders = readLarger(path);
        for(WcReader wcReader : wcReaders){
            if(longLineCount < wcReader.longLineCount){
                longLineCount = wcReader.longLineCount;
                longLine = wcReader.longLine;
            }
        }
        return longLine;
    }

    protected String getInfo(String path, boolean large) throws IOException {
        WcReader[] wcReaders = readLarger(path);
        for(WcReader wcReader : wcReaders){
            nLines += wcReader.nLines;
            nWords += wcReader.nWords;
            nChars += wcReader.nChars;
            nBytes += wcReader.nBytes;
        }
        return nBytes + " " +nChars+ " " + nWords + " " + nLines;
    }

    protected void getInfo(String[] paths) throws IOException {
        WcReader[] wcReaders = readAllFiles(paths);

        for (WcReader wcReader : wcReaders) {
            nLines += wcReader.nLines;
            nWords += wcReader.nWords;
            nChars += wcReader.nChars;
            nBytes += wcReader.nBytes;
            System.out.println(wcReader.fileName + " " + wcReader.nBytes + " " + wcReader.nChars + " " + wcReader.nWords + " " + wcReader.nLines);
        }
        System.out.println("Totals : " + nBytes+ " "+nChars+" "+nWords + " "+nLines);
    }
}