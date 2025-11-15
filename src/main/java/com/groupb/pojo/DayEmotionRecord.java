package com.groupb.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 每一天的记录情况
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("day_record")
public class DayEmotionRecord {

    //主键
    @TableId(type = IdType.AUTO)
    private long id;
    //外键（周报）
    @TableField("weekly_report_id")
    private long weeklyReportId;
    //心情
    private String emotion;
    //记录周几，1--周一，2--周二....
    private Integer day;

}
