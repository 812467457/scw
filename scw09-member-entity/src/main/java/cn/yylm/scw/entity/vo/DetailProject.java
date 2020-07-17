package cn.yylm.scw.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProject {
    private Integer projectId;
    private String projectName;                         //项目名称
    private String projectDesc;                         //项目介绍
    private Integer followerCount;                      //项目关注人数
    private Integer status;                             //项目状态记号
    private Integer day;                                //众筹需要的天数
    private String statusTest;                          //项目状态
    private Integer money;                              //项目所需资金
    private Integer supportMoney;                       //支持的金额
    private Integer percentage;                         //所占百分比
    private String deployDate;                          //项目发布日期
    private Integer lastDay;                            //还有多少结束
    private Integer supporterCount;                     //支持的人数
    private String headerPicturePath;                   //头图路径
    private List<String> detailPicturePath;             //详情图路径
    private List<DetailReturnVO> detailReturnVOList;    //回报信息
}
