package com.egoonet.lighting.egoo_iam_plus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RolePojo implements Serializable {
    //角色id
    private String id;

    //角色名称
    private String name;

    //角色类型,1:静态，2:动态
    private Integer type;

    //角色状态,1:启用，2:停用
    private Integer status;

    //说明
    private String remark;
}
