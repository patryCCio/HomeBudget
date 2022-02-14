package pl.patrickit.modelFx;

import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Money;
import pl.patrickit.utils.exceptions.ApplicationException;

public class MoneyModel {
    public void addMoney(double money){
        MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());

        Money actualMoney = new Money();
        double money2 = actualMoney.getMoney();
        money2 = money2 + money;
        actualMoney.setMoney(money2);
        try {
            moneyDao.creatOrUpdate(actualMoney);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
    }
}
