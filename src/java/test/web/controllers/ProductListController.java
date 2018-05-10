package test.web.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import test.web.db.DataHelper;
import test.web.entity.Product;

@ManagedBean(eager = true)
@SessionScoped
public class ProductListController implements Serializable {

    private List<Product> currentProductList; // текущий список продуктов для отображения

    private Product selectedProduct;  // выбранный продукт
    private Product newProduct;
//    private transient DataHelper dataHelper;
    // критерии поиска
    private long selectedCategoryId; // выбранная категория
    private String currentSearchString; // хранит поисковую строку

    private boolean editMode;// отображение режима редактирования
    private boolean addMode;// отображение режима добавление

    
    public ProductListController() {
        fillProductsAll();
    }


    //<editor-fold defaultstate="collapsed" desc="запросы в базу">
    private void fillProductsAll() {
        currentProductList = DataHelper.getInstance().getAllProducts();
    }

    public String fillProductsByCategory() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedCategoryId = Long.valueOf(params.get("category_id"));
        currentProductList = DataHelper.getInstance().getProductsByCategory(selectedCategoryId);
        return "products";
    }

    public String fillProductsBySearch() {
        if (currentSearchString == null || currentSearchString.trim().length() == 0) {
            fillProductsAll();
            return "products";
        }
        currentProductList = DataHelper.getInstance().getProductsByName(currentSearchString);
        return "products";
    }

    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="режим редактирования">
    public String switchEditMode() {
        editMode = true;
        addMode = false;
//        RequestContext.getCurrentInstance().execute("dlgEditProduct.show()");
        return "edit";
    }

    public String switchAddMode() {
        addMode = true;
        editMode = false;
        selectedProduct = new Product();
        return "edit";
    }

    public String cancelModes() {
        if (addMode) {
            addMode = false;
        }
        if (editMode) {
            editMode = false;
        }
        return "cancel";
    }

    public String saveProduct() {
        if (!validateFields()) {
            return "error";
        }
        if (editMode) {
            DataHelper.getInstance().updateProduct(selectedProduct);
        }
        else if (addMode) {
            DataHelper.getInstance().addProduct(selectedProduct); //.getProduct()
        }
        fillProductsAll();
        return cancelModes();

    }
    
    public String saveBook() {
        if (!validateFields()) {
            return "error";
        }
        if (editMode) {
            DataHelper.getInstance().updateProduct(selectedProduct);
        }
        else if (addMode) {
            DataHelper.getInstance().addProduct(selectedProduct); //.getProduct()
        }
        fillProductsAll();
        return cancelModes();

    }

    //</editor-fold>

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    //<editor-fold defaultstate="collapsed" desc="гетеры сетеры">
    public String getSearchString() {
        return currentSearchString;
    }

    public void setSearchString(String searchString) {
        this.currentSearchString = searchString;
    }

    public List<Product> getCurrentProductList() {
        return currentProductList;
    }

    public long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public Product getNewProduct() {
        if (newProduct == null) {
            newProduct = new Product();
        }
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }

    //</editor-fold>
    private boolean validateFields() {

        if (isNullOrEmpty(selectedProduct.getCategory()) || isNullOrEmpty(selectedProduct.getName())) {
            failValidation("Поля название и категория должны быть заполнены");
            return false;
        }
        if ((DataHelper.getInstance().isNameExist(selectedProduct.getName())) & addMode){
            failValidation("Продукт с ткаим именем уже существует");
            return false;            
        }
        return true;

    }

    private boolean isNullOrEmpty(Object obj) {
        if (obj == null || obj.toString().equals("")) {
            return true;
        }
        return false;
    }

    private void failValidation(String message) {
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "error"));
    }
}
