package primenumberdecomposer;

import java.math.BigInteger;



/**
 * (C) Oct. 2016
 * @author René Vollmer
 */
public class PrimeNumberDecomposer {
    
    
    /**
     * How many threads should be running at all times?
     * I recommend for maximum speed an even multiple of your CPU count.
     */
    static int numthreads=8;
    /**
     * These are the arrays with the threads.
     */
    static Thread[] checker = new Thread[numthreads];
    static CheckBaseValue[] checker2 = new CheckBaseValue[numthreads];

    
    static int ap = 2;
    
    
    /**
     * Is the factorization found yet?
     */
    static boolean found=false;
    /**
     * How many threads have ended, since we found the solution?
     */
    static int end=0;
    /**
     * In this variable the winner-thread will be written!
     */
    static CheckBaseValue winner = null;
    
    
    static final int p = 65521; //Put your p here
    static final int q = 32749; //Put your q here
    static final int N = p*q; //Or put your N here
        

    static int numberOfCoprimes = 5;//((p-1)*(q-1));
    
    
    
    /**
     * Length of N in bits.
     */
    static final int n = MyMathTools.log2(N)+1;
        
    /**
     * Maximum number to expect as a periodicity - this
     * choice is somewhat random right now and could
     * certainly be improved. I does not seem very important though,
     * since this algorithm usually finds a periodicity long before
     * reaching this value.
     */
    static final int maxPeriode = (int) Math.sqrt(N) + 1;//(int) Math.pow(2, n);
    private static Thread reporter;
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        System.out.println("> p=" + p);
        System.out.println("  q=" + q);
        System.out.println("  N=p*q=" + N + " (#bits: " + n + ")");


        //If N<100, output all coprimes to N [ (p-1)*(q-1) in total ].
        if (N<100) {
            System.out.println("  The coprimes of "+N+" are:");
            MyMathTools.listCoprimes(N);
            System.out.println("");
        }
        System.out.println("  There is a total of " + numberOfCoprimes + " coprimes of " + N);
        System.out.println("> --------------------------------------");
        
        
        reporter = new Thread(new Reporter(2000));
        /*
         * Okay, let's start some Threads!
         */
        for (int i=0; i < numthreads; i=(i+1)) {            
            /*
             * Split up the work. Create a Thread for each i for
             * checking all a_n = (i+1) + n*numthreads < N.
             */
            checker2[i] = new CheckBaseValue(i, (i+1), numthreads, N, maxPeriode);
            checker[i] = new Thread(checker2[i]);
        }
        
        reporter.start();
        
        for (int i=0; i < numthreads; i=(i+1)) {
            checker[i].start();
        }
        
    }
    
    static void notifyFound(int threadId) {
        reporter.interrupt();
        winner = checker2[threadId];
        int checkedNumbers = 0;
        for (int i=0;i<numthreads;i++) {
            checkedNumbers += checker2[i].getCheckedNumbers();
            checker2[i].shutdown();
        }
        /*
         * Output the found solution!
         */
        System.out.println("\n\n\nAfter checking " + checkedNumbers + "/" + numberOfCoprimes + " different a's, I announce the final results:");
        winner.overallReport();
    }
}
