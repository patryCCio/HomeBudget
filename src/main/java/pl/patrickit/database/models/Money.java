package pl.patrickit.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ACTUAL_MONEY")
public class Money implements BaseModel{
    public Money() {
    }

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "MONEY", defaultValue = "0")
    private double money;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
