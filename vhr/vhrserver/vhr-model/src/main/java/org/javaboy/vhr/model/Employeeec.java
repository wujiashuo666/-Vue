package org.javaboy.vhr.model;

import java.util.Date;

/**
 * 员工奖惩记录，对应 employeeec 表。
 * 属性名与表字段/前端 JSON 保持一致（ecDate、ecReason、ecPoint、ecType），
 * 避免 Jackson 反序列化时字段名不匹配导致只有 remark 能写入的问题。
 */
public class Employeeec {
    private Integer id;

    private Integer eid;

    private Date ecDate;

    private String ecReason;

    private Integer ecPoint;

    private Integer ecType;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Date getEcDate() {
        return ecDate;
    }

    public void setEcDate(Date ecDate) {
        this.ecDate = ecDate;
    }

    public String getEcReason() {
        return ecReason;
    }

    public void setEcReason(String ecReason) {
        this.ecReason = ecReason == null ? null : ecReason.trim();
    }

    public Integer getEcPoint() {
        return ecPoint;
    }

    public void setEcPoint(Integer ecPoint) {
        this.ecPoint = ecPoint;
    }

    public Integer getEcType() {
        return ecType;
    }

    public void setEcType(Integer ecType) {
        this.ecType = ecType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return "Employeeec{" +
                "id=" + id +
                ", eid=" + eid +
                ", ecDate=" + ecDate +
                ", ecReason='" + ecReason + '\'' +
                ", ecPoint=" + ecPoint +
                ", ecType=" + ecType +
                ", remark='" + remark + '\'' +
                '}';
    }
}
