package com.egoonet.lighting.egoo_iam_plus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserPojo implements Serializable {
    //用户id
    private int id;

    //姓名
    private String name;

    //用户名
    private String userName;

    //性别，1:男，2:女
    private Integer gender;

    //员工编号
    private String employeeId;

    //具体职位编号
    private String jobId;

    //具体职位名称
    private String jobName;

    //联系电话
    private String phone;

    //电子邮件
    private String email;

    //上级主管编号
    private String parentId;

    //上级主管称谓
    private String parentName;

    //入职时间
    private Date hireDate;

    //所属机构id
    private Integer orgId;

    /**
     * 用工形式
     * 120	编外合同工
     * 200	劳务派遣工
     * 301	聘用离退休员工
     * 302	聘用下岗员工
     * 303	借用外单位（中国银行以外单位）人员
     * 304	中国银行以外单位人
     * 401	在校学生实习
     * 402	进站博士后
     */
    private Integer employeeType;
    /**
     * 员工状态
     * 0	未入司
     * 1	试用期中
     * 2	在岗工作
     * 3	离职
     * 4	退休
     * 5	内退
     * 6	离休
     * 7	待岗
     * 8	外派
     * 9	退职
     * 10	亲属随任
     * 11	借用
     * 12	脱钩
     * 13	挂职
     */
    private Integer status;

    //数据日期，格式‘yyyyMMdd HH:mm:ss’
    private Date date;

    //说明
    private String remark;
}
