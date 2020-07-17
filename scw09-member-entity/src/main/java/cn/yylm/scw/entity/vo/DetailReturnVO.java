package cn.yylm.scw.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {
    private Integer returnId;
    //需要的金额
    private Integer supportMoney;
    //单笔限购
    private Integer signalPurchase;
    //支持数量
    private Integer supportCount;
    //运费
    private Integer freight;
    //发货
    private Integer returnData;
    //回报内容
    private String content;
    //限额的数量
    private Integer purchase;
}
