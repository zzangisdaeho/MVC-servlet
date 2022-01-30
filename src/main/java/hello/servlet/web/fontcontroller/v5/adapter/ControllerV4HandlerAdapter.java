package hello.servlet.web.fontcontroller.v5.adapter;

import hello.servlet.web.fontcontroller.ModelView;
import hello.servlet.web.fontcontroller.v3.ControllerV3;
import hello.servlet.web.fontcontroller.v4.ControllerV4;
import hello.servlet.web.fontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {


    @Override
    public boolean supports(Object handler) {
        return handler instanceof ControllerV4;
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(request);

        Map<String, Object> model = new HashMap<>();

        String viewPath = controller.process(paramMap, model);

        ModelView mv = new ModelView(viewPath);
        Map<String, Object> mvModel = mv.getModel();

        Set<Map.Entry<String, Object>> entries = model.entrySet();
        entries.forEach(entry -> mvModel.put(entry.getKey(), entry.getValue()));

        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }

}
