package com.nursery;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * author:MeiShiQiang
 * Date:2021/3/27 | Time:21:15
 */
public class Stri {

    @Test
    public void test01() {
        String s = "sel-business-1";
        System.out.println(s.substring(13, s.length()));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("Msq123456."));

    }
}
