package fadcorp.mprice;

public class ProductElement {
    Long prodId;
    String name;
    String BarCode;
    String modifiedOn;
    float price;

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getName() {
        return name;
    }

    public String getBarCode() {return BarCode;}

    public void setName(String name) {
        this.name = name;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }
}