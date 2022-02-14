package pl.patrickit.modelFx;

import javafx.beans.property.*;

public class CreditFx {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty dateFrom = new SimpleStringProperty();
    private StringProperty dateTo = new SimpleStringProperty();
    private StringProperty price = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getDateFrom() {
        return dateFrom.get();
    }

    public StringProperty dateFromProperty() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom.set(dateFrom);
    }

    public String getDateTo() {
        return dateTo.get();
    }

    public StringProperty dateToProperty() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo.set(dateTo);
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }
}
