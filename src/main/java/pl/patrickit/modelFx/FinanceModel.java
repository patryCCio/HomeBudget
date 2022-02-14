package pl.patrickit.modelFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import pl.patrickit.database.dao.FinanceDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Finance;
import pl.patrickit.database.models.Money;
import pl.patrickit.utils.conveters.FinanceConveter;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.List;

public class FinanceModel {

    private ObjectProperty<FinanceFx> financeFxObjectProperty = new SimpleObjectProperty<>(new FinanceFx());
    private ObjectProperty<FinanceFx> financeFxObjectPropertyEdit = new SimpleObjectProperty<>(new FinanceFx());

    private ObservableList<FinanceFx> financeFxObservableList = FXCollections.observableArrayList();


    public void init() throws ApplicationException {
        FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
        List<Finance> financesList = financeDao.queryForAll(Finance.class);
        this.financeFxObservableList.clear();
        financesList.forEach(finances -> {
            FinanceFx financeFx = FinanceConveter.convertToFinanceFx(finances);
            String option = financeFx.getType();
            System.out.println(option);
            if(option.equals("plus")){
                financeFx.setPrice("+" + financeFx.getPrice());

            }else if(option.equals("minus")){
                financeFx.setPrice("-" + financeFx.getPrice());

            }else{
                financeFx.setPrice("-> " + financeFx.getPrice());
            }

            this.financeFxObservableList.add(financeFx);
        });
    }

    public void saveFinanceEditInDatabase(){
        saveOrUpdate(this.getFinanceFxObjectPropertyEdit());
    }

    public void saveFinanceInDatabase(){
        saveOrUpdate(this.getFinanceFxObjectPropertyEdit());
    }

    public void deleteFinanceInDatabase(){
        FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
        try {
            financeDao.deleteById(Finance.class, this.getFinanceFxObjectPropertyEdit().getId());
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

    private void saveOrUpdate(FinanceFx financeFxObjectPropertyEdit){
        FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
        Finance finance = FinanceConveter.convertToFinance(financeFxObjectPropertyEdit);
        try {
            financeDao.creatOrUpdate(finance);
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

    public FinanceFx getFinanceFxObjectProperty() {
        return financeFxObjectProperty.get();
    }

    public ObjectProperty<FinanceFx> financeFxObjectPropertyProperty() {
        return financeFxObjectProperty;
    }

    public void setFinanceFxObjectProperty(FinanceFx financeFxObjectProperty) {
        this.financeFxObjectProperty.set(financeFxObjectProperty);
    }

    public ObservableList<FinanceFx> getFinanceFxObservableList() {
        return financeFxObservableList;
    }

    public void setFinanceFxObservableList(ObservableList<FinanceFx> financeFxObservableList) {
        this.financeFxObservableList = financeFxObservableList;
    }

    public FinanceFx getFinanceFxObjectPropertyEdit() {
        return financeFxObjectPropertyEdit.get();
    }

    public ObjectProperty<FinanceFx> financeFxObjectPropertyEditProperty() {
        return financeFxObjectPropertyEdit;
    }

    public void setFinanceFxObjectPropertyEdit(FinanceFx financeFxObjectPropertyEdit) {
        this.financeFxObjectPropertyEdit.set(financeFxObjectPropertyEdit);
    }
}
