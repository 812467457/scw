package cn.yylm.scw.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {
    //项目id
    private Integer projectId;
    //项目名称
    private String projectName;
    //头图路径
    private String headerPicturePath;
    //项目众筹金额
    private Integer money;
    //项目发布日期
    private String deployDate;
    //众筹百分比
    private Integer percentage;
    //支持人数
    private Integer supporter;
}
