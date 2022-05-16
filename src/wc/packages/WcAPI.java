package wc.packages;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WcAPI {

    public  static  void help() {
        System.out.println("""
                            wc file (or) files -> lines bytes chars words
                            wc -l file -> no of lines
                            wc -w file -> no of words
                            wc -m file -> no of characters
                            wc -c file -> no of bytes
                            wc -L file -> no of Longest line
                            Try this""");
    }

    public static void readFile(char cmd, String path) {
        try {
            switch (cmd) {
                case 'l' -> System.out.println(new WcReader(path).lines());
                case 'w' -> System.out.println(new WcReader(path).words());
                case 'm' -> System.out.println(new WcReader(path).chars());
                case 'c' -> System.out.println(new WcReader(path).bytes());
                case 'd' -> System.out.println(new WcReader(path));
                case 'L' -> System.out.println(new WcReader(path).longLineLen());
                default -> System.out.println("try wc --help");
            }
        } catch (Exception e) {
            System.out.println(path + " File not found");
        }
    }

    public static void readAllFile(String[] paths) throws InterruptedException {
        ExecutorService tasks = Executors.newFixedThreadPool(10);
        for(String path : paths) {
            try {
                tasks.submit(new WcReader(path));
            } catch (Exception e) {
                System.out.println(path + " file Not found");
            }
        }
        tasks.shutdown();
        while(!tasks.isTerminated()) {};
        new WcReader().totals();
    }

    public static void main(String[] args) {
        long time = (long) System.currentTimeMillis();
        if(args.length==0) {
            help();
        }
        else if(args.length==1) {
            if (args[0].equals("--help")) {
                // help
                help();
            } else {
                readFile('d' ,args[0]);
            }
        }
        else if(args.length==2) {
            //read single file
                try {
                    if(args[0].charAt(0)=='-' && args[0].length()==2)
                    readFile(args[0].charAt(1), args[1]);
                    else readAllFile(args);
                } catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
                    System.out.println("command not found");
                    help();
                }
        }
        else {
            // read all file
            try {
                readAllFile(args);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Time taken : " + TimeUnit.MILLISECONDS.toMillis(((long)System.currentTimeMillis()) - time));
    }
}
