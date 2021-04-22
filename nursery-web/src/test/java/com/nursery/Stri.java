package com.nursery;

import org.junit.jupiter.api.Test;

/**
 * author:MeiShiQiang
 * Date:2021/3/27 | Time:21:15
 */
public class Stri {

    @Test
    public void test01() {
        String base64 = "data:image/jpeg;base64";
        String suffix = base64.substring(11,15);
        System.out.println(suffix);

    }
}
