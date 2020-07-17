package cn.yylm.scw.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo {
    private String loginacct;
    private String userpswd;
    private String email;
    private String username;
    private String phoneNum;
    private String code;
}
