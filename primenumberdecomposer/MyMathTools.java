package primenumberdecomposer;

import java.math.BigInteger;


/**
 * (C) Oct. 2016
 * @author René Vollmer (except for where otherwise stated)
 */
public class MyMathTools {
    
    /**
    Based on the binary GCD algorithm
    Source: http://stackoverflow.com/questions/30697402/explain-how-coprime-check-works
    * 
     * @param u One number
     * @param v another number
     * @return coprime number u, v
     */
    public static boolean isCoprime(long u, long v) {
        // If both numbers are even, then they are not coprime.
        if (((u | v) & 1) == 0) return false;

        // Now at least one number is odd. Eliminate all the factors of 2 from u.
        while ((u & 1) == 0) u >>= 1;

        // One is coprime with everything else by definition.
        if (u == 1) return true;

        do {
            // Eliminate all the factors of 2 from v, because we know that u and v do not have any 2's in common.
            while ((v & 1) == 0) v >>= 1;

            // One is coprime with everything else by definition.
            if (v == 1) return true;

            // Swap if necessary to ensure that v >= u.
            if (u > v) {
                long t = v;
                v = u;
                u = t;
            }

            // We know that GCD(u, v) = GCD(u, v - u).
            v -= u;
        } while (v != 0);

        // When we reach here, we have v = 0 and GCD(u, v) = current value of u, which is greater than 1.
        return false;
    }
    
    /**
     * Calculates the logarithms to base two very nicely and effiently.
     * Source: http://stackoverflow.com/questions/3305059/how-do-you-calculate-log-base-2-in-java-for-integers
     * 
     * @param n n=2^x
     * @return x=log2(n)
     */
    public static int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }
    
    /**
     * Search for the periodicity of a^i mod N
     * This is the only clever part of this program:
     * Instead of calculating f(i) = a^i mod N each time from scratch,
     * we can actually use f(i-1) = a^(i-1) mod N to calculate f(i)
     * by using that f(i)=a*(i-1) mod N.
     * 
     * @param a a^i mod N
     * @param N a^i mod N
     * @param maxPeriode a^i mod N, 0 &lt; i &lt; maxPeriode
     * @return period if found, 0 otherwise
     */
    public static int periodicityFinder(int a, int N, int maxPeriode) {
        int r = 0;
        BigInteger m = BigInteger.valueOf(N);
        BigInteger e = BigInteger.valueOf(a);
        BigInteger b = e;
        for (int i = 1; i <= maxPeriode && r == 0; ++i) {
            e = e.multiply(b).mod(m);
            if (e.equals(BigInteger.ONE)) {
                //i might be our periodicity
                r = i;
            }
        }
        return r;
    }
    
    /**
     * Calculates value^power mod m.
     * 
     * Since a^x can be larger then the largest allowed value for integers,
     * a^x mod N, often returns wrong results. This function should return
     * correct results, using BigInteger.
     * 
     * @param value value^power mod n
     * @param power value^power mod n
     * @param m value^power mod n
     * @return = value^power mod n
     */
    public static int modpow(int value, int power, int m){
        int e = BigInteger.valueOf(value).modPow(BigInteger.valueOf(power),
                BigInteger.valueOf(m)).intValue();
        if (e < 0) {
            e = (e + m) % m;
        }
        return e;
    }
    
    /**
     * Calculates value^power mod m.
     * 
     * This works only for small values.
     * 
     * @param value
     * @param power
     * @param m
     * @return 
     */
    public static int oldModpow(int value, int power, int m) {
        int e = 1;
        for (int i = 0; i<power;i++) {
            e = ((e * value) % m);
        }
        
        if (e < 0) {
            e = (e + m) % m;
        }
        return e;
    }
    
    /**
     * Calculates value^power + plus mod m.
     * 
     * @param value value^power + plus mod m
     * @param power value^power + plus mod m
     * @param m value^power + plus mod m
     * @param plus value^power + plus mod m
     * @return value^power + plus mod m
     */
    public static int modpowp(int value , int power, int m, int plus){
        int e = MyMathTools.modpow(value, power, m);
        e = (e + plus) % m;
        if (e < 0) {
            e = (e + m) % m;
        }

        return e;
    }
    
    /**
     * Greatest common divisor
     * 
     * @param a gcd(a,b)
     * @param b gcd(a,b)
     * @return gcd(a,b)
     */
    public static int gcd(int a, int b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        return MyMathTools.gcd(b1,b2);
    }
    
    /**
     * Greatest common divisor
     * 
     * @param b1 gcd(b1,b2)
     * @param b2 gcd(b1,b2)
     * @return gcd(b1,b2)
     */
    public static int gcd(BigInteger b1, BigInteger b2) {
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }
    
    /**
     * Outputs all coprimes to a number N on the console.
     * 
     * @param N 
     */
    public static void listCoprimes(int N) {
        for (int i = 2; i<N; i++) {
            if (MyMathTools.isCoprime(i, N))
                System.out.print(i+", ");
        }
    }

    /**
     * Returns one random coprime between 1 and N-1.
     * 
     * @param N
     * @return 
     */
    public static int findRandomCoprime(int N) {
        if (N>1) {
            while (true) {
                int a = (int) Math.round(1 + (N-1) * Math.random());
                if (a<N && a>1) {
                    if (MyMathTools.isCoprime(a, N)) {
                        return a;
                    }
                }
            }
        } else 
            throw new ArithmeticException("Numbers smaller than 2 have no coprimes!");
    }
}
