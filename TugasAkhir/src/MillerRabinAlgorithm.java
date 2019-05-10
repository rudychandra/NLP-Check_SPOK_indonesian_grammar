import java.math.BigInteger;
import java.util.Scanner;

/**
 *
 * @author almizfar
 */

public class MillerRabinAlgorithm {
    public static void main (String[] args){
        MillerRabinAlgorithm x = new MillerRabinAlgorithm();
        Scanner input = new Scanner(System.in);
        
        System.out.print("Number: ");
        Long number = input.nextLong();
        String result = x.CheckPrime(number);
        //System.out.print("\n"+result+"\n");
    }

    public String CheckPrime(Long number) {
        if (number%2==0 && number>2) {
            return "Composite";
        }
        
        if (number==2) {
            return "Probably prime";
        }
        
        long a;
        int t = 0;
        
        String numberbinary = Long.toBinaryString(number-1);
        //System.out.println("(Number-1) in binary: "+numberbinary+" binary length: "+numberbinary.length());
        
        //cari nilai t
        for (int i=numberbinary.length()-1; i>=0; i--){
            if(numberbinary.charAt(i)=='0') {
                t++;
            }
            else {
                break;
            }
        }
        //System.out.println("t: "+t);
        
        //cari nilai u
        String temp_u = numberbinary.substring(0, numberbinary.length()-t);
        long u = Long.parseLong(temp_u,2);
        //System.out.println("temp_u: "+temp_u+" u: "+u);
        
        for (int i=0; i<40; i++) {
            a = (long) ((Math.random()*((number-1)-1)+1))+1;
            BigInteger bigint_a = BigInteger.valueOf(a);
            //System.out.print("Basis "+ a);
            if (Test_a(bigint_a,number,t,u)==true) {
                return "Composite";
            }
            //System.out.print("\n");
        }
        
        return "Probably prime";
    }
    
    private boolean Test_a(BigInteger bigint_a, Long number, int t, long u) {
        BigInteger[] x = new BigInteger[t+1];
        BigInteger bigint_number = BigInteger.valueOf(number);
        BigInteger bigint_u = BigInteger.valueOf(u);
        BigInteger bigint_two = new BigInteger("2");
        long number_minus_one = number-1;
        
        x[0] = bigint_a.modPow(bigint_u, bigint_number);
        //System.out.print(" Xo: "+x[0]);
        for (int i=1; i<=t; i++){
            x[i] = x[i-1].modPow(bigint_two, bigint_number);
            //System.out.print(" X"+i+": "+x[i]);
            if (x[i].equals(BigInteger.ONE) && !x[i-1].equals(BigInteger.ONE) && !x[i-1].equals(BigInteger.valueOf(number_minus_one))){
                return true;
            }
        }
        
        if (!x[t].equals(BigInteger.ONE)) {
            return true;
        }
        
        return false;
    }
}
