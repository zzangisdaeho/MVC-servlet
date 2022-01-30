package hello.servlet.web.fontcontroller.v3;

import hello.servlet.web.fontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
