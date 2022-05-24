# WC
WC replicated in java

*To get result pass parameters form of String array(args[]) to the Wc Object method getResult*

# Structure 
Threre are three classes 
Wc , Utils , WcReader
Bat file 
wc.bat

# Wc - 
   * It's the public class that has only one public methode. Either pass an aruguments or String arrya to that methode as parameter (:it is so an command line application)
   * To Only purpose to select the apropriate methode of the utils class.
   * It uses the funtinallity of the utils class by extend the Utils class.
   
# Utils -
   * This class manage the threads creation and allocate the task those threads
   * it has readLargeFile and readAllFile methode (readLargeFile - assign task as read perticular limit of bytes of the file to threads, readAllFile - assign task as read all bytes to one threads)
   * After that all task are over it find the final answer of the all WcReader objects
   * That's the result return to Wc class for the requirements based
   
# WcReader
   * WcReader has the methode reader it uses the fileChannel to read bytes as buffer from filechannel
   * In constructor init the limit , so buffer will read the limit of char only
   * While reading it keep checking how many lines and words are there and stored it in the reference variable
 
# wc.bat -
   * Through this we can acheive compile and run the java program internally
   * It also paasing the arguments to the class



***It is basically on commandline perspective so if any one have to use this instanciate the Wc class and pass Args or array of String to the getResult methode*** 
   
