package pl.patrickit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import pl.patrickit.database.dao.*;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.*;
import pl.patrickit.modelFx.CreditFx;
import pl.patrickit.modelFx.CreditModel;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreditController {

    @FXML
    private Label budgetLabel;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private ComboBox<String> infoComboBox;

    @FXML
    private Label moneyAsideLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TextField valueTextField;

    @FXML
    private Button addCreditButton;

    @FXML
    private TextField infoTextField;

    @FXML
    private TableView<CreditFx> creditTableView;

    @FXML
    private TableColumn<CreditFx, String> fromTableColumn;

    @FXML
    private TableColumn<CreditFx, String> toTableColumn;

    @FXML
    private TableColumn<CreditFx, String> descriptionTableColumn;

    @FXML
    private TableColumn<CreditFx, String> valueTableColumn;

    @FXML
    private MenuItem payOffMenuItem;

    @FXML
    private ToggleGroup toggleGroupChoice;

    @FXML
    private StackPane addStackPane;

    @FXML
    private StackPane addForSomeoneStackPane;



    private CreditModel creditModel;

    MoneyAside moneyAside;
    Money actualMoney;
    Salary actualSalary;
    private ObservableList<String> List;
    ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public void initialize(){
        initBindings();
        showInfo();
        this.creditModel = new CreditModel();
        try {
            this.creditModel.init();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        bindingsTableView();
        this.descriptionTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.toTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.fromTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.valueTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.creditTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.creditModel.setCreditFxObjectPropertyEdit(newValue);
        });
    }

    private void initBindings(){
        if(toggleGroupChoice.getSelectedToggle()==null){
            this.addStackPane.setVisible(false);
            this.addForSomeoneStackPane.setVisible(false);
        }

        this.addCreditButton.disableProperty().bind(valueTextField.textProperty().isEmpty().or(descriptionTextField.textProperty().isEmpty().or(this.fromDatePicker.getEditor().textProperty().isEmpty().or(toDatePicker.getEditor().textProperty().isEmpty()))));
    }

    private void bindingsTableView() {
        this.creditTableView.setItems(this.creditModel.getCreditFxObservableList());
        this.descriptionTableColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        this.toTableColumn.setCellValueFactory(cellData -> cellData.getValue().dateToProperty());
        this.fromTableColumn.setCellValueFactory(cellData -> cellData.getValue().dateFromProperty());
        this.valueTableColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

    }

    private void showInfo(){
        ArrayList<String> arrayList = new ArrayList<>();
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        arrayList.add(bundle.getString("budget"));
        arrayList.add(bundle.getString("money.aside"));
        List = FXCollections.observableArrayList(arrayList);
        infoComboBox.setItems(List);

        Money money = new Money();
        this.actualMoney = ConstantMethod.showInfoBudget(money);
        ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);

        MoneyAside moneyAside = new MoneyAside();
        this.moneyAside = ConstantMethod.showInfoMoneyAside(moneyAside);
        ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);

        Salary actualSalary = new Salary();
        this.actualSalary = ConstantMethod.showInfoSalary(actualSalary);
        ConstantMethod.setLabel(this.actualSalary.getSalary(), salaryLabel);
    }

    @FXML
    void addForSomeone(){
        this.addStackPane.setVisible(false);
        this.addForSomeoneStackPane.setVisible(true);
        initBindings();
    }

    @FXML
    void addCredit() {
        String helper = valueTextField.getText();
        double price = ConstantMethod.getMoney(helper);

        if(price != 0){
            CreditDao creditDao = new CreditDao(DbManager.getConnectionSource());
            MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
            this.actualMoney.setMoney(this.actualMoney.getMoney()+price);
            this.actualMoney.setId(this.actualMoney.getId());
            try {
                moneyDao.creatOrUpdate(this.actualMoney);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            Credit credit = new Credit();
            credit.setId(credit.getId());
            credit.setPrice(price);
            credit.setDataFrom(fromDatePicker.getEditor().getText());
            credit.setDataTo(toDatePicker.getEditor().getText());
            credit.setDescription(descriptionTextField.getText());

            try {
                FinanceDao financeDao = new FinanceDao(DbManager.getConnectionSource());
                Finance finance = new Finance();
                finance.setData(ConstantMethod.getData());
                finance.setDescription(bundle.getString("loan.from.someone") + " - " + credit.getDescription());
                finance.setCategory(bundle.getString("credits"));
                finance.setType("plus");
                finance.setPrice(price);
                creditDao.creatOrUpdate(credit);
                financeDao.creatOrUpdate(finance);
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
            try {
                this.creditModel.init();
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            DbManager.closeConnectionSource();
        }

        descriptionTextField.clear();
        valueTextField.clear();
    }

    @FXML
    void onActionComboBox() {
        String info = infoComboBox.getValue();
        this.infoComboBox.getEditor().textProperty().set(info);
    }

    @FXML
    void onActionPayOffMenuItem(){
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        if((infoComboBox.getEditor().getText().isEmpty())||(infoTextField.getText().isEmpty())){
            DialogUtils.dialogEmpty();
        }else if(infoComboBox.getValue().equals(bundle.getString("budget"))){
            String helper = infoTextField.getText();
            double price = ConstantMethod.getMoney(helper);
            if(price != 0){
                this.creditModel.deleteCreditInDatabase(price);
            }

        }else{
            String helper = infoTextField.getText();
            double price = ConstantMethod.getMoney(helper);
            if(price != 0){
                this.creditModel.deleteCreditInDatabaseOther(price);
            }
        }
        this.actualMoney = ConstantMethod.showInfoBudget(actualMoney);
        ConstantMethod.setLabel(actualMoney.getMoney(), budgetLabel);

        this.moneyAside = ConstantMethod.showInfoMoneyAside(moneyAside);
        ConstantMethod.setLabel(moneyAside.getMoneyAside(), moneyAsideLabel);

    }

    public void onEditCommitFrom(TableColumn.CellEditEvent<CreditFx, String> creditFxStringCellEditEvent) {
        this.creditModel.getCreditFxObjectPropertyEdit().setDateFrom(creditFxStringCellEditEvent.getNewValue());
        this.creditModel.saveCreditEditInDatabase();
    }

    public void onEditCommitTo(TableColumn.CellEditEvent<CreditFx, String> creditFxStringCellEditEvent) {
        this.creditModel.getCreditFxObjectPropertyEdit().setDateTo(creditFxStringCellEditEvent.getNewValue());
        this.creditModel.saveCreditEditInDatabase();
    }

    public void onEditCommitDescription(TableColumn.CellEditEvent<CreditFx, String> creditFxStringCellEditEvent) {
        this.creditModel.getCreditFxObjectPropertyEdit().setDescription(creditFxStringCellEditEvent.getNewValue());
        this.creditModel.saveCreditEditInDatabase();
    }

    public void onEditCommitValue(TableColumn.CellEditEvent<CreditFx, String> creditFxStringCellEditEvent) {
        this.creditModel.getCreditFxObjectPropertyEdit().setPrice(creditFxStringCellEditEvent.getNewValue());
        this.creditModel.saveCreditEditInDatabase();
    }

    public void setToday() {
        this.fromDatePicker.setValue(LocalDate.now());
    }

    public void addStack() {
        this.addStackPane.setVisible(true);
        this.addForSomeoneStackPane.setVisible(false);
        initBindings();
    }


}
