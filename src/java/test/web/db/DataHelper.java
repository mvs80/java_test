package test.web.db;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import test.web.entity.Product;
import test.web.entity.Category;
import test.web.entity.HibernateUtil;

public class DataHelper {

    private SessionFactory sessionFactory = null;
    private static DataHelper dataHelper;

    private DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static DataHelper getInstance() {
        return dataHelper == null ? new DataHelper() : dataHelper;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Product> getAllProducts() {
        return getSession().createCriteria(Product.class).list();
    }

    public List<Category> getAllCategorys() {
        return getSession().createCriteria(Category.class).list();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
//        StringBuilder queryBuilder = new StringBuilder();
//        queryBuilder.append("from Product where category.id = 1");
//        
//        Query result = getSession().createQuery(queryBuilder.toString());
//        
//        return result.list();
//        query.setParameter("id", product.getId());

        return getSession().createCriteria(Product.class).add(Restrictions.eq("category.id", categoryId)).list();
    }

    public List<Product> getProductsByName(String bookName) {
        return getProductList("name", bookName, MatchMode.ANYWHERE);
    }

    private List<Product> getProductList(String field, String value, MatchMode matchMode) {
        return getSession().createCriteria(Product.class).add(Restrictions.ilike(field, value, matchMode)).list();
    }

    private Object getFieldValue(String field, Long id) {
        return getSession().createCriteria(Product.class).setProjection(Projections.property(field)).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public boolean isNameExist(String name) {

        Criteria criteria = getSession().createCriteria(Product.class).add(Restrictions.eq("name", name));
        Object result = criteria.setProjection(Projections.rowCount()).uniqueResult();
        int count = Integer.parseInt(result.toString());

        return count >= 1;
    }

    public void updateProduct(Product product) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update Product ");
        queryBuilder.append(" set name = :name, ");
        queryBuilder.append(" category = :category ");
//        queryBuilder.append(" description = :description ");
        queryBuilder.append(" where id = :id");

        Query query = getSession().createQuery(queryBuilder.toString());

        query.setParameter("name", product.getName());
        query.setParameter("category", product.getCategory());
//        query.setParameter("description", product.getDescription());
        query.setParameter("id", product.getId());

        int result = query.executeUpdate();

    }

    public void addProduct(Product product) {
        getSession().save(product);
    }

        
 }
