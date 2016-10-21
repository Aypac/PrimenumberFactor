package primenumberdecomposer;


/**
 * (C) Oct. 2016
 * @author René Vollmer
 */
public class CheckBaseValue implements Runnable {
    private final int id;
    public final int startA;
    public final int step;
    public final int N;
    private int p;
    private int q;
    public final int maxPeriode;
    private int runtime = -1; //in millis
    private long starttime = -1;
    private int winningR;
    private int winningA;
    private boolean shutdown = false;
    private int a;
    private int r;
    private int n = -1;
    

    CheckBaseValue(int id, int startA, int step, int N, int maxPeriode) {
        this.id = id;
        this.startA = this.a = startA;
        this.step = step;
        this.N = N;
        this.maxPeriode = maxPeriode;
    }
    
    @Override
    public void run() {
        starttime = System.currentTimeMillis();
        for (n = 0; a <= N && a >= 1 && !shutdown; n++) {
            a = startA + n*step;
            if (MyMathTools.isCoprime(a, N)) {

                //long m = System.currentTimeMillis();
                
                //Search the periodicity of a^x mod N
                r = MyMathTools.periodicityFinder(a, N, maxPeriode);

                //If a period was found
                if (r > 0) {
                    //and is even
                    if (r%2 != 1) {
                        /* then follow the procedure to find p and q as
                         * decribed in the lecture:
                         */
                        int r2 = (int) r/2;
                        this.calculatePQ(a,r2);

                    } else {
                        /* We can check if sqrt(a) is an integer, if so, we can do
                         * the same as above...
                         * I guess it is a good thing to do, since it only takes
                         * around 0.2µs to test it, which will amount to around 2ms
                         * for a two-hour run :D (determined with testPerformanceOfOddR)
                         */
                        if (Math.pow(((int) Math.sqrt(a)),2) == a) {
                            this.calculatePQ((int) Math.sqrt(a), r);
                        }
                    }
                }
                
                if (this.solutionFound()) {
                    winningR = r;
                    winningA = a;
                    PrimeNumberDecomposer.notifyFound(id);
                }
                
                //this.reportRun(a, r, (int) (System.currentTimeMillis() - m));
            }
        }
        
        runtime = this.getRuntime();
    }
    
    /**
     * Will try to calculate a p and q based on an a and r.
     * 
     * @param testA testA^testR mod N = 1
     * @param testR testA^testR mod N = 1
     */
    private void calculatePQ(int testA, int testR) {
        int p1 = (int) MyMathTools.modpowp(testA, testR, N, +1);
        int p1gcd = MyMathTools.gcd(p1, N);

        int p1gcdC = N/p1gcd; //If this is not integer, then it will be cut to an integer
        if (p1gcd > 1 && p1gcdC > 1 && p1gcdC*p1gcd == N) { //So this would not return N and is thus actually a valid test
            //To be 100% certain we would have to check if these Numbers are actually prime, but experience shows, that this works fine.
            p=p1gcd; q=p1gcdC;
        }

        /* I guess stricly speaking we don't need to do this,
         * but since this code will only be called in very few
         * cases I'll put it here anyway.
         */
        int m1 = (int) MyMathTools.modpowp(testA, testR, N, -1);
        int m1gcd = MyMathTools.gcd(m1, N);


        int m1gcdC = N/m1gcd; //If this is not integer, then it will be cut to an integer
        if (m1gcd > 1 && m1gcdC > 1 && m1gcdC*m1gcd == N) { //So this would not return N and is thus actually a valid test
            //To be 100% certain we would have to check if these Numbers are actually prime, but experience shows, that this works fine.
            p=m1gcd; q=m1gcdC;
        }
    }
    
    /**
     * Output on the console, what the results of one run were.
     * @param a
     * @param r
     * @param t
     */
    public void reportRun(int a, int r, int t) {
        System.out.println("> Thread ("+id+") for a="+a+" ("+t+"ms) results:");
        if (starttime == -1) {
            System.out.println("  I didn't even start!");
        } else {
            if (r>0) {
                System.out.println("  We found the period of a^x mod N = 1 to be r="+r);
                if (r%2 == 0) {
                    System.out.println("  Periodicity is even!");
                } else {
                    System.out.println("  Periodicity is odd!");
                }
                if (this.solutionFound()) {
                    System.out.println("  "+p+" and "+q+" are our p and q!");
                } else {
                    System.out.println("  But we could not find p and q");
                }
            } else {
                System.out.println("  No periodicity found!");
            }
            System.out.println("> --------------------------------------");
        }
    }
    
    
    public static void testPerformanceOfOddR(int N) {
        long m = System.currentTimeMillis();
        int c=200000000;
        int b=0;
        for (int i=0; i<c;++i) {
            int a = MyMathTools.findRandomCoprime(N);
            if (Math.pow(((int) Math.sqrt(a)),2) == a) {
                //System.out.println(a);
                ++b;
            }
        }
        System.out.println("> "+b+"/"+c+"="+(((0.0+b)/c)*100.)+"%");
        System.out.println("> less than "+(0.+System.currentTimeMillis()-m)/c*1000.+"µs per check.");
    }

    public boolean solutionFound() {
        return (p>0 && q>0);
    }
    
    public int getRuntime() {
        if (this.ended()) {
            return runtime;
        } else if (this.started()) {
            return (int) (System.currentTimeMillis() - starttime);
        } else {
            return 0;
        }
    }

    void overallReport() {
        System.out.println("> Thread ("+id+") status report:");
        if (!this.started()) {
            System.out.println("  I didn't even start!");
        } else {
            if (this.running()) {
                System.out.println("  My mission is to check all numbers a_n="+startA+"+ n*"+step+" < N.");
                System.out.println("  I have been working on this for " + (this.getRuntime()/1000) + "s and I am at a=" + a);
            } else {
                System.out.print("  I checked all numbers a_n="+startA+"+ n*"+step+"<N");
            
            
                if (this.solutionFound()) {
                    System.out.println(", until I found p="+p+" and q="+q+"!");
                    System.out.print("  It took me "+(this.getRuntime()/1000)+"s and finally I used a="+winningA
                            +" for which I found the period to be r="+winningR+".");
                } else {
                    System.out.println(", but even after "+(this.getRuntime()/1000)+"s I could not find p and q.");
                }
            }
        }
    }
    
    public void shutdown() {
        shutdown = true;
    }
    
    public int getCheckedNumbers() {
        if (this.started()) {
            return n;
        } else {
            return 0;
        }
    }

    private boolean started() {
        return (starttime > 0);
    }

    public boolean ended() {
        return (shutdown || runtime != -1);
    }
    
    public boolean running() {
        return (this.started() && !this.ended());
    }
}
