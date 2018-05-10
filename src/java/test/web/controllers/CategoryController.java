package test.web.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import test.web.db.DataHelper;
import test.web.entity.Category;

@ManagedBean(eager = false)
@ApplicationScoped
public class CategoryController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();
    private Map<Long, Category> categoryMap;
    private List<Category> categoryList;
    
    public CategoryController() {

        categoryMap = new HashMap<Long, Category>();
        categoryList = DataHelper.getInstance().getAllCategorys();

        for (Category category : categoryList) {
            categoryMap.put(category.getId(), category);
            selectItems.add(new SelectItem(category, category.getName()));
        }

    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    // 
    public List<Category> getCategoryList() {
        return categoryList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return categoryMap.get(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Category) value).getId().toString();
    }
}
