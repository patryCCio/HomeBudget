package pl.patrickit.controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import pl.patrickit.database.dao.FinanceDao;
import pl.patrickit.database.dao.MoneyAsideDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dao.SalaryDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Finance;
import pl.patrickit.database.models.Money;
import pl.patrickit.database.models.MoneyAside;
import pl.patrickit.database.models.Salary;
import pl.patrickit.modelFx.CategoryFx;
import pl.patrickit.modelFx.CategoryModel;
import pl.patrickit.modelFx.MoneyModel;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagementController {

    @FXML
    private Button addMoneyButton;

    @FXML
    private TextField addMoneyTextField;

    @FXML
    private Button changeSalaryButton;

    @FXML
    private Button addWithoutLossButton;

    @FXML
    private Button addToBudgetButton;

    @FXML
    private Button addAllBudgetButton;

    @FXML
    private TextField changeSalaryTextField;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button deleteCategoryButton;

    @FXML
    private Label budgetLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Label moneyAsideLabel;

    @FXML
    private ComboBox<CategoryFx> categoryComboBox;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField addWithoutLossTextField;

    @FXML
    private TextField addToBudgetTextField;

    @FXML
    private Button addSavingsButton;

    @FXML
    private TextField addSavingsTextField;

    @FXML
    private StackPane addStackPane;

    @FXML
    private ToggleGroup toggleGroupChoice;

    private CategoryModel categoryModel;

    ResourceBundle bundle = FxmlUtils.getResourceBundle();
    Money actualMoney = new Money();
    Salary actualSalary = new Salary();
    MoneyAside moneyAside = new MoneyAside();
    MainController mainController = new MainController();

    @FXML
    void addStack(){
        this.addStackPane.setVisible(true);
        initBindigs();
    }



    @FXML
    public void initialize() {
        showInfo();
        this.categoryModel = new CategoryModel();
        this.categoryModel.init();
        this.categoryComboBox.setItems(this.categoryModel.getCategoryList());
        initBindigs();
    }


    private void initBindigs(){
        if(toggleGroupChoice.getSelectedToggle()==null){
            this.addStackPane.setVisible(false);
        }

        this.addCategoryButton.disableProperty().bind(categoryTextField.textProperty().isEmpty());
        this.deleteCategoryButton.disableProperty().bind(categoryModel.categoryProperty().isNull());
        this.addMoneyButton.disableProperty().bind(addMoneyTextField.textProperty().isEmpty());
        this.changeSalaryButton.disableProperty().bind(changeSalaryTextField.textProperty().isEmpty());
        this.addWithoutLossButton.disableProperty().bind(addWithoutLossTextField.textProperty().isEmpty());
        this.addToBudgetButton.disableProperty().bind(addToBudgetTextField.textProperty().isEmpty());
        this.addSavingsButton.disableProperty().bind(addSavingsTextField.textProperty().isEmpty());
    }

    @FXML
    void addCategory(){
        if(categoryTextField.getText()!="") {
            try {
                categoryModel.saveCategory(categoryTextField.getText());
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            this.categoryModel.init();
            this.categoryComboBox.setItems(this.categoryModel.getCategoryList());
            categoryTextField.clear();
        }
    }

    @FXML
    void onActionComboBox(){
        this.categoryModel.setCategory(this.categoryComboBox.getSelectionModel().getSelectedItem());
    }

    private void showInfo(){
        this.actualMoney = ConstantMethod.showInfoBudget(this.actualMoney);
        ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
        this.moneyAside = ConstantMethod.showInfoMoneyAside(this.moneyAside);
        ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
        this.actualSalary = ConstantMethod.showInfoSalary(this.actualSalary);
        ConstantMethod.setLabel(this.actualSalary.getSalary(), salaryLabel);
    }

    @FXML
    void addWithoutLoss(){
        String helper = addWithoutLossTextField.getText();
        double money = ConstantMethod.getMoney(helper);
        if(money != 0){
            MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());
            FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
            try {
                Finance finance = new Finance();
                finance.setType("plus");
                finance.setDescription(bundle.getString("add.without.loss.description"));
                finance.setPrice(money);
                finance.setCategory(bundle.getString("savings"));
                finance.setData(ConstantMethod.getData());
                moneyAsideDao.findById(MoneyAside.class, 1);
                this.moneyAside.setId(this.moneyAside.getId());
                this.moneyAside.setMoneyAside(this.moneyAside.getMoneyAside()+money);
                moneyAsideDao.creatOrUpdate(moneyAside);
                financeDao.creatOrUpdate(finance);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            ConstantMethod.setLabel(moneyAside.getMoneyAside(), moneyAsideLabel);
        }
        addWithoutLossTextField.clear();
        DbManager.closeConnectionSource();
    }

    @FXML
    void addToBudget(){
        String helper = addToBudgetTextField.getText();
        double subMoney = ConstantMethod.getMoney(helper);
        if(subMoney != 0){
            MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());
            double money = 0;
            double setMoney = 0;
            try {
            this.moneyAside = moneyAsideDao.findById(MoneyAside.class, 1);
            money = this.moneyAside.getMoneyAside();

            } catch (ApplicationException e) {
            e.printStackTrace();
            }

            if(subMoney>money){
            DialogUtils.dialogErrorMoneyAside();
            }else{
            setMoney = money - subMoney;
            this.moneyAside.setMoneyAside(setMoney);
            this.moneyAside.setId(this.moneyAside.getId());
            try {
                moneyAsideDao.creatOrUpdate(this.moneyAside);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }

            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            try {
                this.actualMoney = moneyDao.findById(Money.class, 1);
                this.actualMoney.setMoney(this.actualMoney.getMoney()+subMoney);
                this.actualMoney.setId(this.actualMoney.getId());
                moneyDao.creatOrUpdate(this.actualMoney);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }

            FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
            Finance finance = new Finance();
            finance.setData(ConstantMethod.getData());
            finance.setCategory(bundle.getString("management"));
            finance.setDescription(bundle.getString("transfer.from.savings.to.budget"));
            finance.setPrice(subMoney);
            finance.setType("transfer");
                try {
                    financeDao.creatOrUpdate(finance);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }
                ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
                ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
            }
        }
        addToBudgetTextField.clear();
        DbManager.closeConnectionSource();
    }

    @FXML
    void addSavings() {
        String helper = addSavingsTextField.getText();
        double subMoney = ConstantMethod.getMoney(helper);
        if(subMoney != 0){
            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            double money = 0;
            double setMoney = 0;
            try {
            this.actualMoney = moneyDao.findById(Money.class, 1);
            money = this.actualMoney.getMoney();

            } catch (ApplicationException e) {
            e.printStackTrace();
            }

            if(subMoney>money){
            DialogUtils.dialogErrorMoneyAside();
            }else{
            setMoney = money - subMoney;
            this.actualMoney.setMoney(setMoney);
            this.actualMoney.setId(this.actualMoney.getId());
            try {
                moneyDao.creatOrUpdate(this.actualMoney);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }

            MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());
            try {
                this.moneyAside = moneyAsideDao.findById(MoneyAside.class, 1);
                this.moneyAside.setMoneyAside(this.moneyAside.getMoneyAside()+subMoney);
                this.moneyAside.setId(this.moneyAside.getId());
                moneyAsideDao.creatOrUpdate(this.moneyAside);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }

            FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
            Finance finance = new Finance();
            finance.setData(ConstantMethod.getData());
            finance.setCategory(bundle.getString("management"));
            finance.setDescription(bundle.getString("transfer.from.budget.to.savings"));
            finance.setPrice(subMoney);
            finance.setType("transfer");
                try {
                    financeDao.creatOrUpdate(finance);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }
                ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
                ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
            }
            DbManager.closeConnectionSource();
        }
        addSavingsTextField.clear();

        }


    @FXML
    void addAllBudget(){
        Optional<ButtonType> result = DialogUtils.additionalSalary();
        if(result.get()==ButtonType.OK){
            double money = 0;
            MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());
            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            try {
                this.moneyAside = moneyAsideDao.findById(MoneyAside.class, 1);
                money = this.moneyAside.getMoneyAside();
                if(money!=0){
                    this.moneyAside.setId(this.moneyAside.getId());
                    this.moneyAside.setMoneyAside(0);
                    moneyAsideDao.creatOrUpdate(this.moneyAside);
                    this.actualMoney = moneyDao.findById(Money.class, 1);
                    this.actualMoney.setMoney(this.actualMoney.getMoney()+money);
                    this.actualMoney.setId(this.actualMoney.getId());
                    moneyDao.creatOrUpdate(this.actualMoney);
                    ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
                    ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);

                    FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                    Finance finance = new Finance();
                    finance.setType("transfer");
                    finance.setPrice(money);
                    finance.setDescription(bundle.getString("all.savings.to.budget"));
                    finance.setCategory(bundle.getString("management"));
                    finance.setData(ConstantMethod.getData());
                    financeDao.creatOrUpdate(finance);

                    DbManager.closeConnectionSource();
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void addSalary() {
        Optional<ButtonType> result = DialogUtils.additionalSalary();
        if(result.get()==ButtonType.OK){
            double salary = 0;
            SalaryDao salaryDao = new SalaryDao(DbManager.getConnectionSource());
            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            try {
                this.actualSalary = salaryDao.findById(Salary.class, 1);
                salary = this.actualSalary.getSalary();
                if(salary!=0){
                    this.actualMoney = moneyDao.findById(Money.class, 1);
                    this.actualMoney.setMoney(this.actualMoney.getMoney()+salary);
                    this.actualMoney.setId(this.actualMoney.getId());
                    moneyDao.creatOrUpdate(this.actualMoney);

                    FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                    Finance finance = new Finance();
                    finance.setData(ConstantMethod.getData());
                    finance.setCategory(bundle.getString("management"));
                    finance.setDescription(bundle.getString("salary"));
                    finance.setType("plus");
                    finance.setPrice(salary);
                    financeDao.creatOrUpdate(finance);
                    ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            }

            DbManager.closeConnectionSource();
        }
    }

    @FXML
    public void addMoney(){
        String helper = addMoneyTextField.getText();
        double moneyAdd = ConstantMethod.getMoney(helper);

        if(moneyAdd != 0){
            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            try {
            this.actualMoney = moneyDao.findById(Money.class, 1);
            this.actualMoney.setMoney(this.actualMoney.getMoney()+moneyAdd);
            this.actualMoney.setId(this.actualMoney.getId());
            moneyDao.creatOrUpdate(this.actualMoney);

            FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
            Finance finance = new Finance();
            finance.setData(ConstantMethod.getData());
            finance.setCategory(bundle.getString("management"));
            finance.setDescription(bundle.getString("add.money"));
            finance.setType("plus");
            finance.setPrice(moneyAdd);
            financeDao.creatOrUpdate(finance);

            ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
            DbManager.closeConnectionSource();

            } catch (ApplicationException e) {
            e.printStackTrace();
            }
        }

        addMoneyTextField.clear();
    }

    @FXML
    public void deleteCategory() {
        this.categoryModel.deleteCategoryById();
    }

    @FXML
    public void changeSalary(){
        String helper = changeSalaryTextField.getText();
        double moneyAdd = ConstantMethod.getMoney(helper);

        SalaryDao salaryDao = new SalaryDao(DbManager.getConnectionSource());
        try {
            this.actualSalary = salaryDao.findById(Salary.class, 1);
            this.actualSalary.setSalary(moneyAdd);
            this.actualSalary.setId(this.actualSalary.getId());
            salaryDao.creatOrUpdate(this.actualSalary);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        ConstantMethod.setLabel(this.actualSalary.getSalary(), salaryLabel);
        DbManager.closeConnectionSource();
        changeSalaryTextField.clear();
    }


}
