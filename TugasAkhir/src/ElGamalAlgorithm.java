import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author almizfar
 */

public class ElGamalAlgorithm {
    public static void main (String [] args){
        ElGamalAlgorithm x = new ElGamalAlgorithm();
        Scanner input = new Scanner (System.in);
        
        System.out.print("Prime number: ");
        Long number = input.nextLong();
        BigInteger[][] key = x.GenerateKey(number);
        
        System.out.print("Plaintext: ");
        int p_text = input.nextInt();
        long s = System.nanoTime();
        BigInteger[] ciphertext= x.Encrypt(p_text, key[0][0], key[0][1], key[0][2]);
        System.out.print("Ciphertext: "+ciphertext[0]+" "+ciphertext[1]);
        
        BigInteger plaintext= x.Decrypt(ciphertext, key[1][0], number);
        System.out.print("\nPlaintext: "+plaintext);
        long f = System.nanoTime();
        System.out.println(f-s);
    }

    public BigInteger[][] GenerateKey(Long number) {
        MillerRabinAlgorithm MR = new MillerRabinAlgorithm();
        Random rdm = new Random();
        
        BigInteger[] publickey = new BigInteger[3];
        BigInteger[] privatekey = new BigInteger[1];
        BigInteger[][] key = {publickey,privatekey};
        BigInteger alpha = BigInteger.ZERO;
        
        publickey[0]=BigInteger.valueOf(number);
        //System.out.println("prime : "+publickey[0]);
        long n = number-1;
        ArrayList<Long> list_factors = new ArrayList();
        long test_factors = 2;
                                               
        while (test_factors<=(long) Math.sqrt(n)) {
            if (n%test_factors==0) {
                n=n/test_factors;
                if (!list_factors.contains(test_factors)) {
                    //System.out.println(test_factors);
                    list_factors.add(test_factors);
                }
            }
            else {
                test_factors++;
            }
        }
        list_factors.add(n);
        //System.out.println(n);
        //System.out.println("Size "+list_factors.size());
                        
        long[] thepowers = new long[list_factors.size()];
        for (int k=0; k<thepowers.length; k++) {
            thepowers[k]=(number-1)/list_factors.get(k);
            //System.out.println(thepowers[k]);
        }
        
        boolean flag=true;
        for (long l=2; l<number-1; l++) {   
            alpha=BigInteger.valueOf(l);
            for (int k=0; k<thepowers.length; k++) {
                BigInteger checktest= (BigInteger.valueOf(l).modPow(BigInteger.valueOf(thepowers[k]), BigInteger.valueOf(number)));
                //System.out.println(l+" "+checktest);
                if (checktest.equals(BigInteger.ONE)) {
                    alpha=BigInteger.ZERO;
                    break;
                }
            }
            if (alpha.equals(BigInteger.ZERO)) {
                continue;
            }
            else {
                break;
            }
        }
                        
        long[] primitiveroots = new long[9];
        BigInteger test, gcd;
        for (int l=0; l<primitiveroots.length; l++) {
            do {
                test = (BigInteger.valueOf((long) (Math.random()*(((number-1)-alpha.longValue())+1))+alpha.longValue()));
                gcd = (BigInteger.valueOf(number-1)).gcd(test);
            } while (!gcd.equals(BigInteger.ONE));
            primitiveroots[l]=(alpha.modPow(test, BigInteger.valueOf(number))).longValue();
            //System.out.print(" "+primitiveroots[l]+" ");
        }
        publickey[1]=BigInteger.valueOf(primitiveroots[rdm.nextInt(9)]);
        //System.out.print("\nalpha : ");
        //System.out.print(publickey[1]);
        
        privatekey[0] = BigInteger.valueOf((long) (Math.random()*((((number-2)-1)+1)+1)));
        //System.out.println(privatekey[0]+" "+BigInteger.valueOf(number));
        publickey[2] = publickey[1].modPow(privatekey[0], BigInteger.valueOf(number));
        //System.out.println("\nbeta : "+publickey[2]);
        
        return key;
    }

    public BigInteger[] Encrypt(int p_text, BigInteger prime, BigInteger alpha, BigInteger beta) {
        BigInteger[] ciphertext = new BigInteger[2];
        long r = (long) (Math.random()*((((prime.longValue()-2)-1)+1)+1));
        ciphertext[0] = alpha.modPow(BigInteger.valueOf(r), prime);
        //System.out.println(ciphertext[0]);
        ciphertext[1] = beta.modPow(BigInteger.valueOf(r), prime);
        ciphertext[1] = (ciphertext[1].multiply(BigInteger.valueOf((long)p_text))).mod(prime);
        //System.out.println(ciphertext[1]);
        
        return ciphertext;
    }

    public BigInteger Decrypt(BigInteger[] ciphertext, BigInteger d, long number) {
        BigInteger plaintext;
        plaintext = ciphertext[0].modPow((BigInteger.valueOf(number).subtract(BigInteger.ONE)).subtract(d), BigInteger.valueOf(number));
        plaintext = (ciphertext[1].multiply(plaintext)).mod(BigInteger.valueOf(number));
        
        return plaintext;
    }
}
