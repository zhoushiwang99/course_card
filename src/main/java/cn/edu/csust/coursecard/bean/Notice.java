package cn.edu.csust.coursecard.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
public class Notice implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String target;

    /**
     * 
     */
    private String targetData;

    private static final long serialVersionUID = 1L;
}