package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer fromId;

    /**
     * 
     */
    private Integer toId;

    /**
     * 
     */
    private String conversationId;

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
    private int status;

    private static final long serialVersionUID = 1L;
}