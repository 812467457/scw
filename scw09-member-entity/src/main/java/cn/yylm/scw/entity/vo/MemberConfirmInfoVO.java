package cn.yylm.scw.entity.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberConfirmInfoVO implements Serializable {
    // 易付宝账号
    private String paynum;
// 法人身份证号
    private String cardnum;
}
