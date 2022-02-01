package hello.servlet.web.fontcontroller.v1;

import hello.servlet.web.fontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.fontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.fontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    // FrontController 첫 도입 - 프론트 컨트롤러에서 각 컨트롤러를 호출
    // 모든 컨트롤러를 프론트 컨트롤러에서 갖고있기 위해 인터페이스를 활용하여 다형성으로 받는다.
    // 상식 : 부모가 다형성으로 자식을 받아도 실행시 오버라이드 된 자식의 기능을 사용한다.
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String requestURI = request.getRequestURI();
        ControllerV1 controller = controllerMap.get(requestURI);

        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request, response);
    }
}
