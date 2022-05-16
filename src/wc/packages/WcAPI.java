package wc.packages;

import wc.packages.WcReader;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WcAPI {

    public  static  void help() {
        System.out.println("""
                            wc -l file
                            wc -w file
                            wc -m file
                            wc -c file
                            wc -L file
                            wc files
                            Try this""");
    }

    public static void readFile(char cmd, String path) {
        try {
            switch (cmd) {
                case 'l' -> System.out.println(" lines " + new WcReader(path).lines());
                case 'w' -> System.out.println(" words " + new WcReader(path).words());
                case 'm' -> System.out.println(" chars " + new WcReader(path).chars());
                case 'c' -> System.out.println(" bytes " + new WcReader(path).bytes());
                case 'd' -> System.out.println(new WcReader(path));
                case 'L' -> System.out.println(" Longest Line" + new WcReader(path).longLineLen());
                default -> System.out.println("try wc --help");
            }
        } catch (Exception e) {
            System.out.println(path + " File not found");
        }
    }

    public static void readAllFile(String[] paths) throws InterruptedException {
        ExecutorService tasks = Executors.newFixedThreadPool(paths.length);
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
            //--help
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
                    if(args[0].charAt(0)=='-')
                    readFile(args[0].charAt(1), args[1]);
                    else {
                        readAllFile(args);
                    }
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
        //System.out.println("Time taken : " + TimeUnit.MILLISECONDS.toMicros(((long)System.currentTimeMillis()) - time));
    }
}