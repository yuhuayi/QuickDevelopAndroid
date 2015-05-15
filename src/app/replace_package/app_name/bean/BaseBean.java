package app.zhengbang.teme.bean;

import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by victor on 2014/10/15 0015.
 */
@Table(name = "BaseBean")  // 建议加上注解， 混淆后表名不受影响
public class BaseBean implements Serializable {
    private static final long serialVersionUID = 1 << 2L;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
