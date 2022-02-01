package hello.servlet.web.fontcontroller.v5;

import hello.servlet.web.fontcontroller.ModelView;
import hello.servlet.web.fontcontroller.MyView;
import hello.servlet.web.fontcontroller.v3.ControllerV3;
import hello.servlet.web.fontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.fontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.fontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.fontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.fontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.fontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.fontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.fontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();

    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();

        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    // 어뎁터 기능을 활용하여 FrontController(어뎁터를 적용하였으므로 핸들러라 명칭 변경)의 버전을 사용자가 자유롭게 선택하여 사용 가능
    // 어뎁터를 활용하기 위해 다형성으로 어뎁터 리스트를 추가로 받는다.
    // 핸들러 어댑터 실행 -> 핸들러 어댑터를 통해 핸들러 실행 -> ModelAndView 반환

    /**
     * 동작 순서
     * 1. 핸들러 조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
     * 2. 핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
     * 3. 핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
     * 4. 핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
     * 5. ModelAndView 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서
     * 반환한다.
     * 6. viewResolver 호출: 뷰 리졸버를 찾고 실행한다.
     * JSP의 경우: InternalResourceViewResolver 가 자동 등록되고, 사용된다.
     * 7. View 반환: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를
     * 반환한다.
     * JSP의 경우 InternalResourceView(JstlView) 를 반환하는데, 내부에 forward() 로직이 있다.
     * 8. 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);

        if(handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView mv = adapter.handle(request, response, handler);

        MyView myView = viewResolver(mv.getViewName());
        myView.render(mv.getModel(), request, response);

    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream().filter(adapter -> adapter.supports(handler)).findFirst().orElseThrow(() -> new IllegalArgumentException("adaptor not found"));
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
