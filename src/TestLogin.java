import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodhouse.member.model.MemService;
import com.goodhouse.member.model.MemVO;

public class TestLogin extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		HttpSession session = req.getSession();
		
		List<String> errorMsgs = new LinkedList<String>();
		req.setAttribute("errorMsgs", errorMsgs);
		MemService mSvc = new MemService();
		
		//測試會員登入後跳轉頁面
		if("test_login".equals(action)) {
			
			try {
				/**1接收請求參數*****/
				
				String mem_name = req.getParameter("mem_name");
//				String mem_email = req.getParameter("mem_email");
//				if(mem_email == null || mem_email.trim().length() == 0) {
//					errorMsgs.add("e-mail不能空白，請重新登入");
//				}
//				
//				String emailReq = "";
//				
//				/**2查詢**/
//				String mem_id = null;
//				
//				for(MemVO mVO : mSvc.getAll()) {
//					if(mem_email.equals(mVO.getMem_email())) {
//						mem_id = mVO.getMem_id();
//						session.setAttribute("mVO",mVO);
//					}
//				}
				
				/**3查詢完成準備轉交****/
				String url = "/front/ele_contract/mem_select_page.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/ele_contract/mem_select_page.jsp");
				failureView.forward(req, res);
			}
		}
	}
}
