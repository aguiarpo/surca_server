package br.org.catolicasc.surca.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCrypt {
        public static void main(String[] args) {
            BCryptPasswordEncoder oi = new BCryptPasswordEncoder();
            System.out.println(oi.encode("12345678aS"));
        }
}
