package com.example.rsaalgorithm;


import java.math.BigInteger;
import java.util.Random;

/**
 * @author Abdusselam koç
 */

public class KeyGeneration {
    private final BigInteger e;
    private final BigInteger d;
    private final BigInteger n;

    public KeyGeneration() {
        int specificBitNum = 1024;
        BigInteger p = choosePrime(specificBitNum);
        BigInteger q = choosePrime(specificBitNum);
        this.n = p.multiply(q);
        BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        this.e = CalculateE(n, totient);
        this.d = e.modInverse(totient);
    }

    // This method is chooses p and q values
    private BigInteger choosePrime(int bitNumber) {
        Random random;
        BigInteger chosenPrime;
        boolean prime_or_not;
        do {
            random = new Random();
            chosenPrime = BigInteger.probablePrime(bitNumber, random);
            int iterations = 20;
            prime_or_not = Fermats_Prime_Test(chosenPrime, iterations);
        } while (!prime_or_not); // if chosen prime number IS NOT a prime, then stay in loop

        return chosenPrime;

    }

    // This method check that gcd of the given values is 1 or not
    private boolean CoPrimeCheck(BigInteger i, BigInteger j) {
        BigInteger k = i.gcd(j);
        BigInteger one = BigInteger.ONE;
        int comparevalue = k.compareTo(one);

        if (comparevalue == 0)
            return true;
        else
            return false;

    }

    // Fermat's Primality Test Implementation
    private boolean Fermats_Prime_Test(BigInteger possible_prime, int iteration) {

        if (possible_prime.equals(BigInteger.ONE))
            return false;

        for (int i = 0; i < iteration; i++) {
            BigInteger a = ChooseAppropriateA(possible_prime);

            if (CoPrimeCheck(possible_prime, a)) {
                a = a.modPow(possible_prime.subtract(BigInteger.ONE), possible_prime);
                if (!a.equals(BigInteger.ONE))
                    return false;
            }
        }

        return true;

    }

    // A helper method for chosen random "a" value for using it in Fermat's
    // Primality Test. It check the random "a" value is smaller than p (OR q) OR
    // not.
    private static BigInteger ChooseAppropriateA(BigInteger pvalue) {
        Random aRandom = new Random();

        while (true) {
            final BigInteger a = new BigInteger(pvalue.bitLength(), aRandom);
            // must have 1 <= a < n
            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(pvalue) < 0) {
                return a;
            }
        }
    }

    // chosing random numbers to find a sutiable value for E. 
    private BigInteger CalculateE(BigInteger n, BigInteger totient_n) {

        Random rand = new Random();
        BigInteger possible_e = new BigInteger(1024, rand);
        boolean check = false;

        while (!check) {
            if (totient_n.compareTo(possible_e) < 0 && possible_e.compareTo(BigInteger.ONE) < 0) {
                possible_e = new BigInteger(1024, rand);
                continue;
            } else if (!(possible_e.gcd(n).equals(BigInteger.ONE))) {
                possible_e = new BigInteger(1024, rand);
                continue;

            } else if (!(possible_e.gcd(totient_n).equals(BigInteger.ONE))) {
                possible_e = new BigInteger(1024, rand);
                continue;
            }

            check = true;
        }

        return possible_e;

    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getN() {
        return n;
    }


}