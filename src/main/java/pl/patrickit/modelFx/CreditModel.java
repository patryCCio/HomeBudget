package pl.patrickit.modelFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.patrickit.database.dao.CreditDao;
import pl.patrickit.database.dao.FinanceDao;
import pl.patrickit.database.dao.MoneyAsideDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Credit;
import pl.patrickit.database.models.Finance;
import pl.patrickit.database.models.Money;
import pl.patrickit.database.models.MoneyAside;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.conveters.CreditConveter;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.List;
import java.util.ResourceBundle;

public class CreditModel {
    private ObjectProperty<CreditFx> creditFxObjectProperty = new SimpleObjectProperty<>(new CreditFx());
    private ObjectProperty<CreditFx> creditFxObjectPropertyEdit = new SimpleObjectProperty<>(new CreditFx());

    private ObservableList<CreditFx> creditFxObservableList = FXCollections.observableArrayList();

    ResourceBundle bundle = FxmlUtils.getResourceBundle();


    public void init() throws ApplicationException {
        CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
        List<Credit> creditList = creditDao.queryForAll(Credit.class);
        this.creditFxObservableList.clear();
        creditList.forEach(credits -> {
            CreditFx creditFx = CreditConveter.convertToCreditFx(credits);
            this.creditFxObservableList.add(creditFx);
        });
    }

    public void saveCreditEditInDatabase() {
        saveOrUpdate(this.getCreditFxObjectPropertyEdit());
    }

    public void saveCreditInDatabase() {
        saveOrUpdate(this.getCreditFxObjectPropertyEdit());
    }

    public void deleteCreditInDatabase(double price) {
            try{

                Credit credit = CreditConveter.convertToCredit(getCreditFxObjectPropertyEdit());
            Money money = new Money();
            money = ConstantMethod.showInfoBudget(money);
            double actualMoney = money.getMoney();
            double helperMoney = actualMoney;

            if(price>actualMoney){
                DialogUtils.dialogErrorBudget();
            }else{
                if(credit.getPrice()<price){
                    DialogUtils.errorDialog("Za duzo!");
                } else{
                    actualMoney = actualMoney - price;
                    money.setMoney(actualMoney);
                    money.setId(money.getId());
                    MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
                    try {
                        moneyDao.creatOrUpdate(money);
                        FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                        Finance finance = new Finance();
                        finance.setPrice(price);
                        finance.setType("minus");
                        finance.setCategory(bundle.getString("credits") + " - " + bundle.getString("budget"));
                        finance.setData(ConstantMethod.getData());
                        finance.setDescription(bundle.getString("loan.from.someone") + " - " + credit.getDescription());
                        financeDao.creatOrUpdate(finance);
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    }
                    if(credit.getPrice()==price){
                    CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
                    try {
                        creditDao.deleteById(Credit.class, this.getCreditFxObjectPropertyEdit().getId());
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    }
                    }else{
                        CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
                        credit.setPrice(credit.getPrice()-price);
                        try {
                            creditDao.creatOrUpdate(credit);
                        } catch (ApplicationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                this.init();
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            DbManager.closeConnectionSource();

            }catch(RuntimeException e){
                DialogUtils.dialogEmptyList();
            }
    }

    public void deleteCreditInDatabaseOther(double price) {

        try{
            Credit credit = CreditConveter.convertToCredit(getCreditFxObjectPropertyEdit());
            MoneyAside moneyAside = new MoneyAside();
            moneyAside = ConstantMethod.showInfoMoneyAside(moneyAside);
            double actualMoneyAside = moneyAside.getMoneyAside();
            double helperMoney = actualMoneyAside;

            if(price>actualMoneyAside){
                DialogUtils.dialogErrorMoneyAside();
            }else{
                if(credit.getPrice()<price){
                    DialogUtils.errorDialog("Za duzo!");
                } else{
                    actualMoneyAside = actualMoneyAside - price;
                    moneyAside.setMoneyAside(actualMoneyAside);
                    moneyAside.setId(moneyAside.getId());
                    MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());
                    try {
                        moneyAsideDao.creatOrUpdate(moneyAside);
                        FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                        Finance finance = new Finance();
                        finance.setPrice(price);
                        finance.setType("minus");
                        finance.setCategory(bundle.getString("credits") + " - " + bundle.getString("money.aside"));
                        finance.setData(ConstantMethod.getData());
                        finance.setDescription(bundle.getString("loan.from.someone") + " - " + credit.getDescription());
                        financeDao.creatOrUpdate(finance);
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    }
                    if(credit.getPrice()==price){
                    CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
                    try {
                        creditDao.deleteById(Credit.class, this.getCreditFxObjectPropertyEdit().getId());
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    }
                    }else{
                        CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
                        credit.setPrice(credit.getPrice()-price);
                        try {
                            creditDao.creatOrUpdate(credit);
                        } catch (ApplicationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                this.init();
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            DbManager.closeConnectionSource();
        }catch (RuntimeException e){
            DialogUtils.dialogEmptyList();
        }


    }

    private void saveOrUpdate(CreditFx creditFxObjectPropertyEdit) {
        CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
        Credit credit = CreditConveter.convertToCredit(creditFxObjectPropertyEdit);
        try {
            creditDao.creatOrUpdate(credit);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        try {
            this.init();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    public CreditFx getCreditFxObjectProperty() {
        return creditFxObjectProperty.get();
    }

    public ObjectProperty<CreditFx> creditFxObjectPropertyProperty() {
        return creditFxObjectProperty;
    }

    public void setCreditFxObjectProperty(CreditFx creditFxObjectProperty) {
        this.creditFxObjectProperty.set(creditFxObjectProperty);
    }

    public CreditFx getCreditFxObjectPropertyEdit() {
        return creditFxObjectPropertyEdit.get();
    }

    public ObjectProperty<CreditFx> creditFxObjectPropertyEditProperty() {
        return creditFxObjectPropertyEdit;
    }

    public void setCreditFxObjectPropertyEdit(CreditFx creditFxObjectPropertyEdit) {
        this.creditFxObjectPropertyEdit.set(creditFxObjectPropertyEdit);
    }

    public ObservableList<CreditFx> getCreditFxObservableList() {
        return creditFxObservableList;
    }

    public void setCreditFxObservableList(ObservableList<CreditFx> creditFxObservableList) {
        this.creditFxObservableList = creditFxObservableList;
    }
}
