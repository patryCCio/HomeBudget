package pl.patrickit.modelFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import pl.patrickit.controllers.PermanentController;
import pl.patrickit.database.dao.BalanceDao;
import pl.patrickit.database.dao.PermanentDao;
import pl.patrickit.database.dao.SalaryDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Balance;
import pl.patrickit.database.models.Permanent;
import pl.patrickit.database.models.Salary;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.conveters.PermanentConveter;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.List;
import java.util.ResourceBundle;

public class PermanentModel {
    private ObjectProperty<PermanentFx> permanentFxObjectProperty = new SimpleObjectProperty<>(new PermanentFx());
    private ObjectProperty<PermanentFx> permanentFxObjectPropertyEdit = new SimpleObjectProperty<>(new PermanentFx());

    private ObservableList<PermanentFx> permanentFxObservableList = FXCollections.observableArrayList();

    ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public void init(String option){
        PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
        List<Permanent> permanentList = null;
        try {
            permanentList = permanentDao.queryForAll(Permanent.class);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        this.permanentFxObservableList.clear();
        permanentList.forEach(permanents -> {
            PermanentFx permanentFx = PermanentConveter.convertToPermanentFx(permanents);
            if((option.equals(permanentFx.getFrequency()))||option.equals(bundle.getString("all"))){
             this.permanentFxObservableList.add(permanentFx);
            }
        });
        DbManager.closeConnectionSource();
    }



    public void getBalance(String option) {
        PermanentController.BALANCE = 0;
        PermanentController.BALANCE_PER_MONTH=0;
        PermanentController.BALANCE_PER_DAY=0;
        PermanentController.BALANCE_PER_WEEK=0;
        PermanentController.BALANCE_PER_ONE_MONTH=0;
        PermanentController.BALANCE_PER_QUARTER=0;
        PermanentController.BALANCE_PER_SIX_MONTHS=0;
        PermanentController.BALANCE_PER_YEAR=0;
        PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
        List<Permanent> permanentList = null;
        try {
            permanentList = permanentDao.queryForAll(Permanent.class);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        this.permanentFxObservableList.clear();
        permanentList.forEach(permanents -> {
            PermanentFx permanentFx = PermanentConveter.convertToPermanentFxBalance(permanents, option);
            if((option.equals(permanentFx.getFrequency()))){
             this.permanentFxObservableList.add(permanentFx);
            }
        });
        DbManager.closeConnectionSource();
        System.out.println(PermanentController.BALANCE);
    }

    public void getBalanceAllInfo(String option){
        PermanentController.BALANCE = 0;
        PermanentController.BALANCE_PER_MONTH=0;
        PermanentController.BALANCE_PER_DAY=0;
        PermanentController.BALANCE_PER_WEEK=0;
        PermanentController.BALANCE_PER_ONE_MONTH=0;
        PermanentController.BALANCE_PER_QUARTER=0;
        PermanentController.BALANCE_PER_SIX_MONTHS=0;
        PermanentController.BALANCE_PER_YEAR=0;
        PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
        List<Permanent> permanentList = null;
        try {
            permanentList = permanentDao.queryForAll(Permanent.class);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        permanentList.forEach(permanents -> {
            PermanentFx permanentFx = PermanentConveter.convertToPermanentFxBalanceAll(permanents, option);
        });
        DbManager.closeConnectionSource();
        System.out.println(PermanentController.BALANCE);
    }

    public void getBalanceAll(String option) {
        PermanentController.BALANCE = 0;
        PermanentController.BALANCE_PER_MONTH=0;
        PermanentController.BALANCE_PER_DAY=0;
        PermanentController.BALANCE_PER_WEEK=0;
        PermanentController.BALANCE_PER_ONE_MONTH=0;
        PermanentController.BALANCE_PER_QUARTER=0;
        PermanentController.BALANCE_PER_SIX_MONTHS=0;
        PermanentController.BALANCE_PER_YEAR=0;
        PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
        List<Permanent> permanentList = null;
        try {
            permanentList = permanentDao.queryForAll(Permanent.class);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        this.permanentFxObservableList.clear();
        permanentList.forEach(permanents -> {
            PermanentFx permanentFx = PermanentConveter.convertToPermanentFxBalanceAll(permanents, option);
            this.permanentFxObservableList.add(permanentFx);
        });
        DbManager.closeConnectionSource();
        System.out.println(PermanentController.BALANCE);
    }

    public void savePermanentEditInDatabase(String helper){
        saveOrUpdate(this.getPermanentFxObjectPropertyEdit(), helper);
    }

    public void savePermanentInDatabase(String helper){
        saveOrUpdate(this.getPermanentFxObjectPropertyEdit(), helper);
    }

    public void deletePermanentInDatabase(String helper){
        PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
        try {
            permanentDao.deleteById(Permanent.class, this.getPermanentFxObjectPropertyEdit().getId());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        this.getBalance(helper);
    }

    private void saveOrUpdate(PermanentFx permanentFxObjectPropertyEdit, String helper){
        PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
        Permanent permanent = PermanentConveter.convertToPermanent(permanentFxObjectPropertyEdit);
        try {
            permanentDao.creatOrUpdate(permanent);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        this.getBalance(helper);
    }

    public PermanentFx getPermanentFxObjectProperty() {
        return permanentFxObjectProperty.get();
    }

    public ObjectProperty<PermanentFx> permanentFxObjectPropertyProperty() {
        return permanentFxObjectProperty;
    }

    public void setPermanentFxObjectProperty(PermanentFx permanentFxObjectProperty) {
        this.permanentFxObjectProperty.set(permanentFxObjectProperty);
    }

    public PermanentFx getPermanentFxObjectPropertyEdit() {
        return permanentFxObjectPropertyEdit.get();
    }

    public ObjectProperty<PermanentFx> permanentFxObjectPropertyEditProperty() {
        return permanentFxObjectPropertyEdit;
    }

    public void setPermanentFxObjectPropertyEdit(PermanentFx permanentFxObjectPropertyEdit) {
        this.permanentFxObjectPropertyEdit.set(permanentFxObjectPropertyEdit);
    }

    public ObservableList<PermanentFx> getPermanentFxObservableList() {
        return permanentFxObservableList;
    }

    public void setPermanentFxObservableList(ObservableList<PermanentFx> permanentFxObservableList) {
        this.permanentFxObservableList = permanentFxObservableList;
    }


}
