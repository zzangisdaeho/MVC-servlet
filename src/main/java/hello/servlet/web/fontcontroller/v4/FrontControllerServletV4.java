package hello.servlet.web.fontcontroller.v4;

import hello.servlet.web.fontcontroller.ModelView;
import hello.servlet.web.fontcontroller.MyView;
import hello.servlet.web.fontcontroller.v3.ControllerV3;
import hello.servlet.web.fontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.fontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.fontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.fontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.fontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.fontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    // 좀더 사용의 편의를 높이기 위해 Model을 직접 넘김 - 기존 ModelAndView에서 Model을 직접 넘김. 결과적으로 ModelAndView가 아닌 View값만 리턴하고 Model에 직접 데이터를 넣으면 됨. (ModelAndView의 기능을 분리하여 직접 제공)
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV4.service");

        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);

        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);

        Map<String, Object> modelMap = new HashMap<>();

        String viewName = controller.process(paramMap, modelMap);

        MyView myView = viewResolver(viewName);
        myView.render(modelMap, request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
