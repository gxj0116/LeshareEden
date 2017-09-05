package com.leshare;

import android.content.Context;
import android.util.SparseArray;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.leshare.model.User;

import java.util.List;

/**
 * 作者：gxj on 2017/9/5 12:27
 * 邮箱：jun18735177413@sina.com
 */
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper mInstance;
    private Context mContext;

    public static DBHelper newInstance() {
        if (mInstance == null){
            mInstance = new DBHelper();
        }
        return mInstance;
    }

    public void initDB(Context context) {
        mContext = context;
        Configuration.Builder builder = new Configuration.Builder(context);
        //其他设置，数据库名、版本号也可以在这里设置
        //设置SQL解析器，可以解析跨越多行的SQL语句
        builder.setSqlParser(Configuration.SQL_PARSER_DELIMITED);
        //以新的配置初始化ActiveAndroid
        ActiveAndroid.initialize(builder.create());
    }

    public void dispose(){
        ActiveAndroid.dispose();
    }

    //增
    private void addUser(User user) {
        ActiveAndroid.beginTransaction();
        try {
            user.save();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    //删
    private void deleteStudent(int id) {
        User user = new User();
        user.delete(User.class, 1);
    }

    //改
    private void updateStudent(SparseArray<String> setParams, SparseArray<String> whereParams) {
        Update update = new Update(User.class);
        update.set(setParams.get(0)).where(whereParams.get(0)).execute();
    }

    /**
     * 查询一条用户记录
     * @param user
     * @return
     */
    public static User getRandom(User user) {
        return new Select()
                .from(User.class)
                .where("User = ?", user.getId())
                .orderBy("RANDOM()")
                .executeSingle();
    }

    //查询所有用户信息
    private List<User> queryStudent() {
        Select select = new Select();
        List<User> modelList = null;
        try {
            modelList = select.from(User.class).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }
}
