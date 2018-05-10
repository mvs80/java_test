package test.web.validators;

import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("test.web.validators.LoginValidator")
public class LoginValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        try {
            String newValue = value.toString();

            if (newValue.length() < 4) {
                throw new IllegalArgumentException("Логин должен быть более 4 символов");
            }

//            if (getTestArray().contains(newValue)) {
//                throw new IllegalArgumentException("used_name");
//            }


        } catch (IllegalArgumentException e) {
            FacesMessage message = new FacesMessage(e.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

    }

    private ArrayList<String> getTestArray() {
        ArrayList<String> list = new ArrayList<String>();// заглушка, желательно делать запрос в базу данных для проверки существующего имени
        list.add("username");
        list.add("login");
        return list;
    }
    
}
