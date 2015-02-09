package fadcorp.mprice;

/**
 * Created by fad on 07/12/14.
 */

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class UserData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("ReportData");

    private String userName;
    private String passWord;

    public UserData() {
    }

    public UserData(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public void setUsername(String name) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

}