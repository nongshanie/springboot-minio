package com.nongshanie.minio.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 元数据，目前只包含数据记录数
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Meta implements Serializable {
    private Integer count;

    public Meta(Integer count) {
        this.count = count;
    }

    public Meta(String count) {
        this.count = Integer.valueOf(count);
    }

    public static Meta buildMeta(Integer count) {
        return new Meta(count == null ? 0 : count);
    }

}
