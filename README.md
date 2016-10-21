This is a Java program, that decomposes a number N into two prime numbers p and q using a classical variant of <a href="https://en.wikipedia.org/wiki/Shor%27s_algorithm">Shor's algorithm</a> (period finding approach).

Performance
=================
Now it is working deterministically (checking all a's) and takes for a 31 bit N around 15 minutes on my 4x2.00GHz processor. For smaller numbers, the runtime decreases drastically! <br />
If you want to make it faster, you can cheat (at least in the language of Quantum Computers) and set maxPeriode=2;

How to run
=================
Download and compile with javac. Precompiled jar's might be added later.

TODO
=================
<ul>
<li>Make p=q; N=p^2 work!</li>
<li>Use BigInteger for p, q and N (so that we can factor larger N's than 32bit!)</li>
<li>Allow inputs</li>
<li>Set conditions for failing cases (N can not be factorized in only two prime factors)</li>
<li>Better Documentation</li>
<li>Make a nice UI</li>
</ul>


change log
=================

21.10.2016 - inital release<br />
This is the intial working version. Not thoroughly tested yet.
