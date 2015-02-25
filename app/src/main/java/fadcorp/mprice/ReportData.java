package fadcorp.mprice;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Date;

public class ReportData extends SugarRecord {

    @Ignore
    private static final String TAG = Constants.getLogTag("ReportData");

    private String name;
    private Date modifiedOn;
    private float price;

    public ReportData() {
    }

    public ReportData(Date modifiedOn, String name, float price) {
        this.modifiedOn = modifiedOn;
        this.name = name;
        this.price = price;
    }

    public void setModifiedOn(Date date) {
        this.modifiedOn = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    public Date getModifiedOn() {return this.modifiedOn; }

}