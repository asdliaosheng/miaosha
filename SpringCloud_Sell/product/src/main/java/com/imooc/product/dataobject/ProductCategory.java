package com.imooc.product.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * create by ASheng
 * 2019/3/20 17:14
 */
@Entity
@Data
public class ProductCategory {

    //类目Id
    @Id
    private Integer categoryId;

    //父类Id,id=0则为一级类目
    private Integer parentId;

    //类目名称
    private String categoryName;

    //类目状态,1-正常,2-已废弃
    private Integer categoryStatus;

    private Date createTime;

    private Date updateTime;
}
