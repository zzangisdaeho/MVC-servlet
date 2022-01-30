package hello.servlet.web.fontcontroller;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ModelView {

    private final String viewName;

    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName){
        this.viewName = viewName;
    }
}
