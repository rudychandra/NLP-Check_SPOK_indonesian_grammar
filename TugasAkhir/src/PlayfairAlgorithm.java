import java.util.Scanner;

/**
 *
 * @author almizfar
 */

public class PlayfairAlgorithm {
    public static void main (String [] args){
        PlayfairAlgorithm x = new PlayfairAlgorithm();
        Scanner input = new Scanner (System.in);
        
        System.out.print("Message: ");
        String message = x.SetMessage(input.nextLine());
        System.out.println(message);
        
        System.out.print("Key: ");
        char[][] key = x.SetKey(input.nextLine());
        
        
        System.out.print("Encrypted Message:");
        long s = System.nanoTime();
        String encryptedmessage = x.Encrypt(message, key);
        long f = System.nanoTime();
        System.out.println("\n"+encryptedmessage);
        System.out.println(f-s+" nanoseconds");
        
        System.out.print("Decrypted Message:");
        s = System.nanoTime();
        String decryptedmessage = x.Decrypt(encryptedmessage, key);
        f = System.nanoTime();
        System.out.println("\n"+decryptedmessage);
        System.out.println(f-s+" nanoseconds");
    }

    public String SetMessage(String message) {
        char[] arraymessage = message.toCharArray();
        char[] filler;
        int flag;   
        
        flag = 0;
        
        while (CheckPairedChars(message)==false){
            //check jika ada karakter sama yg berpasangan
            for (int i=0; i<arraymessage.length; i+=2) {
                if (i == arraymessage.length-1) {
                    break;
                }
                if (arraymessage[i]==arraymessage[i+1]) {
                    message = message + "`";
                    flag = i+1;
                    break;
                }
            }
            //sisip karakter grave accent yg telah ditambahkan 
            //di akhir string sebelumnyapesan 
            arraymessage = message.toCharArray();
            filler = message.toCharArray();
            for (int i=0; i<arraymessage.length; i++) {
                if (i==flag) {
                    arraymessage[flag]=arraymessage[arraymessage.length-1];
                    for (int j=flag+1; j<arraymessage.length; j++) {
                        arraymessage[j]=filler[flag];
                        flag++;
                    }
                    break;
                }
            }
            message = String.copyValueOf(arraymessage);
        }
        
        //check jika jumlah karakter plaintext ganjil dan tambahkan karakter grave accent
        if (message.length()%2!=0) {
            message = message + "`";
        }
        
        return message;
    }

    public char[][] SetKey(String key) {
        char[] arraykey = key.toCharArray();
        char[] realarray = new char[95];
        char[][] thematrix = new char[5][19];
        int indexarray;
        
        indexarray = 0;
        
        //Memasukkan arraykey ke dalam arraycharacters
        //Ubah ASCII decimal 32-126 menjadi character dan gabung ke dalam arraycharacters
        char[] arraycharacters = new char[arraykey.length+95];
        for (int i=0, j=32; i<arraycharacters.length; i++) {
            if (i < arraykey.length) {
                arraycharacters[i] = arraykey[i];
            }
            else{
                arraycharacters[i] = (char) j;
                j++;
            }
        }
        
        //Seleksi yg duplikat dan masukkan ke realarray
        for (int i=0; i<95; i++) {
            if (i==0) {
                realarray[i] = arraycharacters[i];
            }
            else{
                for (int j=0; j<arraycharacters.length; j++) {
                    boolean duplicate = false;
                    for (int k=i-1; k>=0; k--) {
                        if (arraycharacters[j]==realarray[k]) {
                            duplicate = true;
                        }
                    }
                    if (duplicate==false){
                        realarray[i] = arraycharacters[j];
                        break;
                    }
                }
            }  
        }
        
        //Mengubah realarray menjadi thematrix
        for (int i=0; i<5; i++){
            for (int j=0; j<19; j++){
                thematrix[i][j]=realarray[indexarray];
                //System.out.print(thematrix[i][j]+" ");
                indexarray++;
            }
            //System.out.println();
        }
        
        return thematrix;
    }    

    public String Encrypt(String message, char[][] key) {
        int[] char1 = new int[2];
        int[] char2 = new int[2];
        char[] arraymessage = message.toCharArray();
        char[] arrayencryptedmessage = new char[arraymessage.length];
        
        //mulai enkripsi
        for (int i=0; i<arraymessage.length; i+=2){
            //mengambil posisi tiap karakter dalam digraf
            char1 = GetXY(arraymessage[i],key);
            char2 = GetXY(arraymessage[i+1],key);
            
            //enkripsi per 2 karakter
            if (char1[0]==char2[0]){
                if (char1[1]<18){
                    char1[1]++;
                }
                else{
                    char1[1]=0;
                }
                if (char2[1]<18){
                    char2[1]++;
                }
                else{
                    char2[1]=0;
                }
            }
            else if (char1[1]==char2[1]){
                if (char1[0]<4){
                    char1[0]++;
                }
                else{
                    char1[0]=0;
                }
                if (char2[0]<4){
                    char2[0]++;
                }
                else{
                    char2[0]=0;
                }
            }
            else{
                int temp = char1[1];
                char1[1]=char2[1];
                char2[1]=temp;
            }
            arrayencryptedmessage[i]=key[char1[0]][char1[1]];
            arrayencryptedmessage[i+1]=key[char2[0]][char2[1]];
        }
        return String.copyValueOf(arrayencryptedmessage);
    }

    private int[] GetXY(char character, char[][] key) {
        int[] xy = new int[2];

        //mencari posisi tiap karakter
        for (int i=0; i<5; i++) {
            for (int j=0; j<19; j++) {
                if (key[i][j]==character) {
                    xy[0]=i;
                    xy[1]=j;
                    break;
                }
            }
        } 
        
        return xy;
    }

    public String Decrypt(String encryptedmessage, char[][] key) {
        int[] char1 = new int[2];
        int[] char2 = new int[2];
        char[] arraymessage = encryptedmessage.toCharArray();
        char[] arraydecryptedmessage = new char[arraymessage.length];
        char[] filler;
        
        //mulai dekripsi
        for (int i=0; i<arraymessage.length; i+=2){
            //mengambil posisi tiap karakter dalam digraf
            char1 = GetXY(arraymessage[i],key);
            char2 = GetXY(arraymessage[i+1],key);
            
            //dekripsi per 2 karakter
            if (char1[0]==char2[0]){
                if (char1[1]>0){
                    char1[1]--;
                }
                else{
                    char1[1]=18;
                }
                if (char2[1]>0){
                    char2[1]--;
                }
                else{
                    char2[1]=18;
                }
            }
            else if (char1[1]==char2[1]){
                if (char1[0]>0){
                    char1[0]--;
                }
                else{
                    char1[0]=4;
                }
                if (char2[0]>0){
                    char2[0]--;
                }
                else{
                    char2[0]=4;
                }
            }
            else{
                int temp = char1[1];
                char1[1]=char2[1];
                char2[1]=temp;
            }
            arraydecryptedmessage[i]=key[char1[0]][char1[1]];
            arraydecryptedmessage[i+1]=key[char2[0]][char2[1]];
        }
        
        return (String.copyValueOf(arraydecryptedmessage));
    }

    private boolean CheckPairedChars(String message) {
        char[] arraymessage = message.toCharArray();
        
        for (int i=0; i<arraymessage.length; i+=2) {
            if (i == arraymessage.length-1 || i == arraymessage.length) {
                return true;
            }
            if (arraymessage[i]==arraymessage[i+1]) {
                return false;
            }
        }
        //System.out.println(message);
        return true;
    }
}
