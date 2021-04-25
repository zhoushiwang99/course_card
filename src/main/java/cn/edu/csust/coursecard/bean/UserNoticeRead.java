package cn.edu.csust.coursecard.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_notice_read
 */
@Data
@Builder
public class UserNoticeRead implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer noticeId;

    /**
     * 
     */
    private Date readTime;

    private static final long serialVersionUID = 1L;
}