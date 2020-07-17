package cn.yylm.scw.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVo implements Serializable{
    private Integer id;
    private String email;
    private String username;
}
