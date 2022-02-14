package pl.patrickit.modelFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.patrickit.database.dao.CategoryDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Category;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.List;

public class CategoryModel {

    private ObservableList<CategoryFx> categoryList = FXCollections.observableArrayList();
    private ObjectProperty<CategoryFx> category = new SimpleObjectProperty<>();

    public void init(){
        CategoryDao categoryDao = new CategoryDao(DbManager.getConnectionSource());
        List<Category> categories = null;
        this.categoryList.clear();
        try {
            categories = categoryDao.queryForAll(Category.class);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        categories.forEach(c ->{
            CategoryFx categoryFx = new CategoryFx();
            categoryFx.setId(c.getId());
            categoryFx.setName(c.getName());
            this.categoryList.add(categoryFx);
        });
        DbManager.closeConnectionSource();
    }

    public ObservableList<CategoryFx> getCategoryList() {
        return categoryList;
    }

    public void deleteCategoryById(){
        CategoryDao categoryDao = new CategoryDao(DbManager.getConnectionSource());
        try {
            categoryDao.deleteById(Category.class, category.getValue().getId());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        init();
    }

    public void setCategoryList(ObservableList<CategoryFx> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryFx getCategory() {
        return category.get();
    }

    public ObjectProperty<CategoryFx> categoryProperty() {
        return category;
    }

    public void setCategory(CategoryFx category) {
        this.category.set(category);
    }

    public void saveCategory(String name) throws ApplicationException {
        CategoryDao categoryDao = new CategoryDao(DbManager.getConnectionSource());
        Category category = new Category();
        category.setName(name);
        categoryDao.creatOrUpdate(category);
        DbManager.closeConnectionSource();
        init();
    }

}
