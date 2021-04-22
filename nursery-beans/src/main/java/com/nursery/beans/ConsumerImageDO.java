package com.nursery.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * author:MeiShiQiang
 * Date:2021/2/7 | Time:17:35
 * `tb_consumerimage`
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerImageDO {
    private String imageId;
    private String image64Base;
    private String imageUrl;
    private String imageType;
    private int imageSize;
}
