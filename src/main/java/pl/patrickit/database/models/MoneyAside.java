package pl.patrickit.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MONEY_ASIDE")
public class MoneyAside implements BaseModel{
    public MoneyAside() {
    }

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "MONEY_ASIDE", defaultValue = "0")
    private double moneyAside;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoneyAside() {
        return moneyAside;
    }

    public void setMoneyAside(double moneyAside) {
        this.moneyAside = moneyAside;
    }
}
