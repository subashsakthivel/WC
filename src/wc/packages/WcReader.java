package wc.packages;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class WcMain {

    private static void version(){
        System.out.println("Version 1.01");
    }

    private  static  void help() {
        System.out.println("""
                            wc file (or) files -> bytes chars words lines
                            wc -l file -> no of lines
                            wc -w file -> no of words
                            wc -m file -> no of characters
                            wc -c file -> no of bytes
                            wc -L file -> no of Longest line
                            Try this""");
    }

    private static void readLargeFile(char cmd , String path) {
        Path of = Path.of(path);
        try(FileChannel fileChannel = FileChannel.open(of)){
            long start =0;
            int noOfThreads = Runtime.getRuntime().availableProcessors() + 1;
            long totalBytes = fileChannel.size();
            long noOfBytesPerThread = totalBytes/noOfThreads;
            long remainingBytes = totalBytes%noOfThreads;
            Thread[] threads = new Thread[noOfThreads + 1];
            int i;
            for(i=0;i<noOfThreads;i++){
                WcReader reader = new WcReader(path , FileChannel.open(of), start , start+noOfBytesPerThread, true);
                threads[i] = new Thread(reader);
                threads[i].start();
                start += noOfBytesPerThread;
            }
            if(remainingBytes > 0) {
                WcReader reader = new WcReader(path, FileChannel.open(of), start-1, start + remainingBytes-1, true);
                threads[noOfThreads] = new Thread(reader);
                threads[noOfThreads].start();
            }
            for(i = i-1;i>=0;i--){
                while (threads[i].isAlive());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.print(path + " ");
        WcReader.LargerResult();
    }

    private static void readFile(char cmd, String path) {

        try(FileChannel fileChannel = FileChannel.open(Path.of(path))) {
            WcReader reader = new WcReader(path, fileChannel, 0, fileChannel.size(), false);
            if(fileChannel.size() > 10000000) {
                readLargeFile(cmd , path);
                return;
            }
            switch (cmd) {
                case 'l' -> System.out.println(reader.lines());
                case 'w' -> System.out.println(reader.words());
                case 'm' -> System.out.println(reader.chars());
                case 'c' -> System.out.println(reader.bytes());
                case 'd' -> System.out.println(reader.details());
                case 'L' -> System.out.println(reader.getLongestLine());
                default -> help();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(path + "File Not Found");
        }
    }

    private static void readAllFile(String[] paths) throws IOException {
        Thread[] threads = new Thread[paths.length];
        int i = 0;
        for(String path : paths) {
            FileChannel fileChannel = FileChannel.open(Path.of(path));
            WcReader reader = new WcReader(path , fileChannel , 0 , fileChannel.size() , false);
            threads[i] = new Thread(reader);
            threads[i++].start();
        }

        for(i = i-1;i>=0;i--){
            while (threads[i].isAlive());
        }
        WcReader.totalResult();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        long time = System.currentTimeMillis();
        int len = args.length;
        switch(len) {
            case 0: 
                help();
                break;
            case 1:
                if(args[0].equals("--help"))
                    help();
                else if(args[0].equals("--version"))
                    version();
                else readLargeFile('d', args[0]);
                break;
            case 2:
                if(args[0].charAt(0)=='-')
                    readFile(args[0].charAt(1), args[1]);
                else readAllFile(args);
                break;
            default:
                readAllFile(args);
        }
        System.out.println("Time taken : " + (System.currentTimeMillis() - time));
    }
}
