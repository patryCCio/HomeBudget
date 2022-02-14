package pl.patrickit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXHamburger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import pl.patrickit.database.dao.FinanceDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Finance;
import pl.patrickit.database.models.Money;
import pl.patrickit.database.models.MoneyAside;
import pl.patrickit.database.models.Salary;
import pl.patrickit.modelFx.CategoryFx;
import pl.patrickit.modelFx.CategoryModel;
import pl.patrickit.modelFx.FinanceFx;
import pl.patrickit.modelFx.FinanceModel;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.conveters.FinanceConveter;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancesController {

    @FXML
    private Label budgetLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Label moneyAsideLabel;

    @FXML
    private StackPane addStackPane;

    @FXML
    private TableColumn<FinanceFx, String> categoryTableColumn;

    @FXML
    private TableColumn<FinanceFx, String> dateTableColumn;

    @FXML
    private TableColumn<FinanceFx, String> descriptionTableColumn;

    @FXML
    private TableView<FinanceFx> financesTableView;

    @FXML
    private TableColumn<FinanceFx, String> priceTableColumn;

    @FXML
    private ToggleGroup toggleGroupChoice;

    @FXML
    private Button addButton;

    @FXML
    private Button todayButton;

    @FXML
    private ComboBox<CategoryFx> categoryComboBox;

    @FXML
    private DatePicker calendarDatePicker;

    @FXML
    private TextField valueTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private MenuItem deleteMenuItem;

    private CategoryModel categoryModel;
    private FinanceModel financeModel;

    Money actualMoney = new Money();
    MoneyAside moneyAside = new MoneyAside();
    Salary actualSalary = new Salary();
    MainController mainController = new MainController();

    public void initialize(){
        this.financeModel = new FinanceModel();
        this.categoryModel = new CategoryModel();
        try {
            this.financeModel.init();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        this.categoryModel.init();
        this.categoryComboBox.setItems(this.categoryModel.getCategoryList());
        initBindings();
        bindingsTableView();
        showInfo();
        this.descriptionTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.dateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.categoryTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.priceTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.financesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.financeModel.setFinanceFxObjectPropertyEdit(newValue);
        });
    }

    private void showInfo(){
        this.actualMoney = ConstantMethod.showInfoBudget(actualMoney);
        ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
        this.moneyAside = ConstantMethod.showInfoMoneyAside(moneyAside);
        ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
        this.actualSalary = ConstantMethod.showInfoSalary(actualSalary);
        ConstantMethod.setLabel(this.actualSalary.getSalary(), salaryLabel);
    }

    public void initBindings(){
        if(toggleGroupChoice.getSelectedToggle()==null){
            this.addStackPane.setVisible(false);
        }
        this.addButton.disableProperty().bind(valueTextField.textProperty().isEmpty().or(descriptionTextField.textProperty().isEmpty().or(this.categoryModel.categoryProperty().isNull().or(this.calendarDatePicker.getEditor().textProperty().isEmpty()))));
    }

    private void bindingsTableView() {
        this.financesTableView.setItems(this.financeModel.getFinanceFxObservableList());
        this.descriptionTableColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        this.categoryTableColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        this.dateTableColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        this.priceTableColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
    }

    public void onEditCommitCategory(TableColumn.CellEditEvent<FinanceFx, String> financeFxStringCellEditEvent) {
        this.financeModel.getFinanceFxObjectPropertyEdit().setCategory(financeFxStringCellEditEvent.getNewValue());
        this.financeModel.saveFinanceEditInDatabase();
    }


    public void onEditCommitDate(TableColumn.CellEditEvent<FinanceFx, String> financeFxStringCellEditEvent) {
        this.financeModel.getFinanceFxObjectPropertyEdit().setData(financeFxStringCellEditEvent.getNewValue());
        this.financeModel.saveFinanceEditInDatabase();
    }

    public void onEditCommitDescription(TableColumn.CellEditEvent<FinanceFx, String> financeFxStringCellEditEvent) {
        this.financeModel.getFinanceFxObjectPropertyEdit().setDescription(financeFxStringCellEditEvent.getNewValue());
        this.financeModel.saveFinanceEditInDatabase();
    }

    public void addStack(){
        this.addStackPane.setVisible(true);
        initBindings();
    }

    public void setToday() {
        this.calendarDatePicker.setValue(LocalDate.now());
    }

    public void onActionComboBox() {
        this.categoryModel.setCategory(this.categoryComboBox.getSelectionModel().getSelectedItem());
    }

    public void addBill() {
        String helper = valueTextField.getText();
        double price = ConstantMethod.tryAndCatchValue(helper);
        double budget = 0;

        if (price != 0) {
            FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            try {
                this.actualMoney = moneyDao.findById(Money.class, 1);
                budget = this.actualMoney.getMoney();
                if (budget < price) {
                    DialogUtils.dialogErrorBudget();
                } else {
                    budget = budget - price;
                    this.actualMoney.setMoney(budget);
                    this.actualMoney.setId(this.actualMoney.getId());
                    moneyDao.creatOrUpdate(this.actualMoney);
                    Finance finance = new Finance();
                    finance.setId(finance.getId());
                    finance.setPrice(price);
                    finance.setData(calendarDatePicker.getEditor().getText());
                    finance.setType("minus");
                    finance.setDescription(descriptionTextField.getText());
                    finance.setCategory(this.categoryModel.categoryProperty().getValue().getName());
                    try {
                        financeDao.creatOrUpdate(finance);
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    }
                    ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
                    try {
                        this.financeModel.init();
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            DbManager.closeConnectionSource();
        }
        descriptionTextField.clear();
        valueTextField.clear();
    }

    public void deleteBill() {
        this.financeModel.deleteFinanceInDatabase();
    }

}
