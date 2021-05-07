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
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("admin"));
    }
}
