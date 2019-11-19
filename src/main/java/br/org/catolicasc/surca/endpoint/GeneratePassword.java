package br.org.catolicasc.surca.endpoint;

import java.util.Random;

class GeneratePassword {

    static String getPassword() {
        StringBuilder password = new StringBuilder();
        Random generator = new Random();
        int size;
        char letra;
        int number;
        for(int i = 0; i < 8; i++){
            int random = generator.nextInt(10);
            if(random % 2 == 0)
                size = 65;
            else
                size = 97;

            if(random > 4){
                letra = (char) (generator.nextInt(25) + size);
                password.append(letra);
            } else{
                number = generator.nextInt(9);
                password.append(number);
            }
        }
        return password.toString();
    }
}
