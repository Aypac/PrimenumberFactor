/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primenumberdecomposer;


/**
 *
 * @author rene
 */
public class Reporter implements Runnable {

    private int frequency = 1000;

    Reporter(int f) {
        frequency = f;
    }

    
    @Override
    public void run() {
        while(!this.isInterrupted()) {
            
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException ex) {
                //Oh, well... Who cares!
                Thread.currentThread().interrupt();
            }
            
            
            if (!this.isInterrupted()) {
                //Make all Threads report!
                for (int i=0;i<PrimeNumberDecomposer.numthreads;i++) {
                    PrimeNumberDecomposer.checker2[i].overallReport();
                }
            }
        }
    }
    
    private boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }
}
