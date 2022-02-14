package pl.patrickit.modelFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.patrickit.database.dao.FinanceDao;
import pl.patrickit.database.dao.MoneyAsideDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dao.PlanningDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Finance;
import pl.patrickit.database.models.Money;
import pl.patrickit.database.models.MoneyAside;
import pl.patrickit.database.models.Planning;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.conveters.PlanningConveter;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.List;
import java.util.ResourceBundle;

public class PlanningModel {
    private ObjectProperty<PlanningFx> planningFxObjectProperty = new SimpleObjectProperty<>(new PlanningFx());
    private ObjectProperty<PlanningFx> planningFxObjectPropertyEdit = new SimpleObjectProperty<>(new PlanningFx());

    private ObservableList<PlanningFx> planningFxObservableList = FXCollections.observableArrayList();


    public void init() throws ApplicationException {
        PlanningDao planningDao = new PlanningDao(DbManager.getConnectionSource());
        List<Planning> planningList = planningDao.queryForAll(Planning.class);
        this.planningFxObservableList.clear();
        planningList.forEach(plans -> {
            PlanningFx planningFx = PlanningConveter.convertToPlanningFx(plans);
            this.planningFxObservableList.add(planningFx);
        });
    }

    public void savePlanningEditInDatabase() {
        saveOrUpdate(this.getPlanningFxObjectPropertyEdit());
    }

    public void saveCreditInDatabase() {
        saveOrUpdate(this.getPlanningFxObjectPropertyEdit());
    }

    private void saveOrUpdate(PlanningFx planningFxObjectPropertyEdit) {
        PlanningDao planningDao = new PlanningDao(DbManager.getConnectionSource());
        Planning planning = PlanningConveter.convertToPlanning(planningFxObjectPropertyEdit);
        try {
            planningDao.creatOrUpdate(planning);
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

    public PlanningFx getPlanningFxObjectProperty() {
        return planningFxObjectProperty.get();
    }

    public ObjectProperty<PlanningFx> planningFxObjectPropertyProperty() {
        return planningFxObjectProperty;
    }

    public void setPlanningFxObjectProperty(PlanningFx planningFxObjectProperty) {
        this.planningFxObjectProperty.set(planningFxObjectProperty);
    }

    public PlanningFx getPlanningFxObjectPropertyEdit() {
        return planningFxObjectPropertyEdit.get();
    }

    public ObjectProperty<PlanningFx> planningFxObjectPropertyEditProperty() {
        return planningFxObjectPropertyEdit;
    }

    public void setPlanningFxObjectPropertyEdit(PlanningFx planningFxObjectPropertyEdit) {
        this.planningFxObjectPropertyEdit.set(planningFxObjectPropertyEdit);
    }

    public ObservableList<PlanningFx> getPlanningFxObservableList() {
        return planningFxObservableList;
    }

    public void setPlanningFxObservableList(ObservableList<PlanningFx> planningFxObservableList) {
        this.planningFxObservableList = planningFxObservableList;
    }

    public void deletePlanningInDatabase() {
        PlanningDao planningDao = new PlanningDao(DbManager.getConnectionSource());
        try {
            planningDao.deleteById(Planning.class, this.getPlanningFxObjectPropertyEdit().getId());
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

    public void payOffPlan(int choice) {
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        try{
            Planning planning = PlanningConveter.convertToPlanning(getPlanningFxObjectPropertyEdit());
            double check = planning.getPrice();
            if(choice == 1){
            Money money1 = new Money();
            Money actualMoney = ConstantMethod.showInfoBudget(money1);
            if(actualMoney.getMoney()<check){
                DialogUtils.dialogErrorBudget();
            }else{
                actualMoney.setMoney(actualMoney.getMoney()-check);
                actualMoney.setId(actualMoney.getId());
                MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
                try {
                    moneyDao.creatOrUpdate(actualMoney);
                    FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                    Finance finance = new Finance();
                    finance.setPrice(check);
                    finance.setType("minus");
                    finance.setCategory(planning.getCategory());
                    finance.setDescription(bundle.getString("budget") + " - " + planning.getDescription());
                    finance.setData(ConstantMethod.getData());
                    financeDao.creatOrUpdate(finance);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }
                deletePlanningInDatabase();
                }
            }else{
            MoneyAside moneyAside = new MoneyAside();
            MoneyAside actualMoneyAside = ConstantMethod.showInfoMoneyAside(moneyAside);
            if(actualMoneyAside.getMoneyAside()<check){
                DialogUtils.dialogErrorMoneyAside();
            }else{
                actualMoneyAside.setMoneyAside(actualMoneyAside.getMoneyAside()-check);
                actualMoneyAside.setId(actualMoneyAside.getId());
                MoneyAsideDao moneyDao = new MoneyAsideDao(DbManager.getConnectionSource());
                try {
                    moneyDao.creatOrUpdate(actualMoneyAside);
                    FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                    Finance finance = new Finance();
                    finance.setPrice(check);
                    finance.setType("minus");
                    finance.setCategory(planning.getCategory());
                    finance.setDescription(bundle.getString("money.aside") + " - " + planning.getDescription());
                    finance.setData(ConstantMethod.getData());
                    financeDao.creatOrUpdate(finance);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }
                deletePlanningInDatabase();
            }
        }
        DbManager.closeConnectionSource();

        }catch (RuntimeException e){
            DialogUtils.dialogEmptyList();
        }


    }
}
