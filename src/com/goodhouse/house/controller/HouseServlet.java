package com.goodhouse.house.controller;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import javax.sql.DataSource;

import com.goodhouse.house.model.*;

@MultipartConfig
public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HouseServlet() {
		super();
	}

	Connection con;
	DataSource ds = null;
	public static final String photo1 = "SELECT hou_f_picture FROM HOUSE WHERE hou_id='";
	public static final String photo2 = "SELECT hou_s_picture FROM HOUSE WHERE hou_id='";
	public static final String photo3 = "SELECT hou_t_picture FROM HOUSE WHERE hou_id='";

	public void init() throws ServletException {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/goodhouse");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("image/jpeg");
		ServletOutputStream out = res.getOutputStream();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			String hou_id = req.getParameter("hou_id").trim();
			if (Integer.parseInt(req.getParameter("photo")) == 1) {
				rs = stmt.executeQuery(photo1 + hou_id + "'");
			} else if (Integer.parseInt(req.getParameter("photo")) == 2) {
				rs = stmt.executeQuery(photo2 + hou_id + "'");
			} else if (Integer.parseInt(req.getParameter("photo")) == 3) {
				rs = stmt.executeQuery(photo3 + hou_id + "'");
			} else {
				InputStream in = getServletContext().getResourceAsStream("/back/house/image/test.jpg");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
			}

			if (rs.next()) {
				BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream(1));
				byte[] buf = new byte[4 * 1024];
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				in.close();
			} else {
				InputStream in = getServletContext().getResourceAsStream("/back/house/image/test.jpg");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			InputStream in = getServletContext().getResourceAsStream("/back/house/image/test.jpg");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			out.write(buf);
			in.close();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception se) {
					se.printStackTrace(System.err);
				}
			}
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

//**********************insert		
		if ("insert".equals(action)) {

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				String hou_name = req.getParameter("hou_name").trim();
				if (hou_name == null || hou_name.trim().length() == 0) {
					errorMsgs.put("hou_name", "房屋名稱不可空白");
				}
				String hou_type = req.getParameter("hou_type").trim();
				if (hou_type == null || hou_type.trim().length() == 0) {
					errorMsgs.put("hou_type", "房屋型別不可空白");
				}

				String hou_size = req.getParameter("hou_size").trim();
				String hou_sizeReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{1,50}$";
				if (hou_size == null || hou_size.trim().length() == 0) {
					hou_size = "無填寫";
				} else if (!hou_size.trim().matches(hou_sizeReg)) {
					errorMsgs.put("hou_size", "格式不正確");
				}

				String hou_property = req.getParameter("hou_property").trim();
				if (hou_property == null || hou_property.trim().length() == 0) {
					errorMsgs.put("hou_property", "產權不可空白");
				}
				String hou_parkspace = req.getParameter("hou_parkspace").trim();
				if (hou_parkspace == null || hou_parkspace.trim().length() == 0) {
					errorMsgs.put("hou_parkspace", "車位不可空白");
				}
				String hou_cook = req.getParameter("hou_cook").trim();
				if (hou_cook == null || hou_cook.trim().length() == 0) {
					errorMsgs.put("hou_cook", "開火不可空白");
				}
				String hou_managefee = req.getParameter("hou_managefee").trim();
				if (hou_managefee == null || hou_managefee.trim().length() == 0) {
					errorMsgs.put("hou_managefee", "管理費不可空白");
				}
				String hou_address = req.getParameter("hou_address").trim();
				String hou_addressReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,200}$";
				if (hou_address == null || hou_address.trim().length() == 0) {
					errorMsgs.put("hou_address", "地址必須填寫(不然怎麼看房)");
				} else if (!hou_address.trim().matches(hou_addressReg)) {
					errorMsgs.put("hou_address", "格式不正確");
				}
				Integer hou_rent = null;
				try {
					hou_rent = new Integer(req.getParameter("hou_rent").trim());
				} catch (NumberFormatException es) {
					errorMsgs.put("hou_rent", "租金請填寫數字");
				}
				String lan_id = new String(req.getParameter("lan_id").trim());

				/**********************************/
				// 圖片一
				Part hou_f_picture = req.getPart("hou_f_picture");
				BufferedInputStream buf = new BufferedInputStream(hou_f_picture.getInputStream());
				ByteArrayOutputStream baf = new ByteArrayOutputStream();
				int f;
				byte bf[] = new byte[8192];
				while ((f = buf.read(bf)) != -1) {
					baf.write(bf);
				}
				baf.toByteArray();
				/************** 圖片一end ********************/
				/**********************************/
				// 圖片二
				Part hou_s_picture = req.getPart("hou_s_picture");
				BufferedInputStream bus = new BufferedInputStream(hou_s_picture.getInputStream());
				ByteArrayOutputStream bas = new ByteArrayOutputStream();
				int s;
				byte bs[] = new byte[8192];
				while ((s = bus.read(bs)) != -1) {
					bas.write(bs);
				}
				bas.toByteArray();
				/************** 圖片二end ********************/
				/**********************************/
				// 圖片三
				Part hou_t_picture = req.getPart("hou_t_picture");
				BufferedInputStream but = new BufferedInputStream(hou_t_picture.getInputStream());
				ByteArrayOutputStream bat = new ByteArrayOutputStream();
				int t;
				byte bt[] = new byte[8192];
				while ((t = but.read(bt)) != -1) {
					bat.write(bt);
				}
				bat.toByteArray();
				/**********************************/
				String hou_note = new String(req.getParameter("hou_note").trim());

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/house/addHouse.jsp");
					failureView.forward(req, res);
					return;
				}

				HouseService houSvc = new HouseService();
				houSvc.addHouse(hou_name, hou_type, hou_size, hou_property, hou_parkspace, hou_cook, hou_managefee,
						hou_address, lan_id, hou_rent, baf.toByteArray(), bas.toByteArray(), bat.toByteArray(),
						hou_note);

				String url = "/back/house/listAllHouse.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

			} catch (Exception e) {
				errorMsgs.put("insert失敗", e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back/house/addHouse.jsp");
				failureView.forward(req, res);
			}

		}
		// **********************insert end

		// **********************delete
		if ("delete".equals(action)) {// 來自listAllEmp.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 ***************************************/
				String hou_id = new String(req.getParameter("hou_id"));
				/*************************** 2.開始刪除資料 ***************************************/
				HouseService houSvc = new HouseService();
				houSvc.deleteHouse(hou_id);

				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				String url = "/back/house/listAllHouse.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back/house/listAllHouse.jsp");
				failureView.forward(req, res);
			}
		}
		// **********************delete end

		// **********************getOne_For_Display
		if ("getOne_For_Display".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);
			try {
				String str = req.getParameter("hou_id");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入房屋編號");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/house/select_page.jsp");
					failureView.forward(req, res);
					return;
				}
				String hou_id = null;
				try {
					hou_id = new String(str);
				} catch (Exception e) {
					errorMsgs.add("房屋編號格式錯誤");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/house/select_page.jsp");
					failureView.forward(req, res);
					return;
				}
				HouseService houSvc = new HouseService();
				HouseVO houVO = houSvc.getOneHouse(hou_id);
				if (houVO == null) {
					errorMsgs.add("查無資料");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/house/select_page.jsp");
					failureView.forward(req, res);
					return;
				}
				req.setAttribute("houVO", houVO);
				String url = "/back/house/listOneHouse.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

			} catch (Exception e) {
				errorMsgs.add("尋找資料失敗" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back/house/select_page.jsp");
				failureView.forward(req, res);
			}

		}

		// **********************getOne_For_Display end

		// **********************getOne_For_Update
		if ("getOne_For_Update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);
			try {
				String hou_id = new String(req.getParameter("hou_id"));

				HouseService houSvc = new HouseService();
				HouseVO houVO = houSvc.getOneHouse(hou_id);

				req.setAttribute("houVO", houVO);
				String url = "/back/house/update_hou_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("無法取得修改的資料....." + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back/house/listAllHouse.jsp");
				failureView.forward(req, res);
			}
		}
		// **********************getOne_For_Update end

		// **********************update
		if ("update".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();

			req.setAttribute("errorMsgs", errorMsgs);
			try {
				String hou_id = new String(req.getParameter("hou_id").trim());

				String hou_name = req.getParameter("hou_name").trim();
				if (hou_name == null || hou_name.trim().length() == 0) {
					errorMsgs.add("房屋名稱不可空白");
				}
				String hou_type = req.getParameter("hou_type").trim();
				if (hou_type == null || hou_type.trim().length() == 0) {
					errorMsgs.add("房屋型別不可空白");
				}

				String hou_size = req.getParameter("hou_size").trim();
				if (hou_size == null || hou_size.trim().length() == 0) {
					hou_size = "無填寫";
				}

				String hou_property = req.getParameter("hou_property").trim();
				if (hou_property == null || hou_property.trim().length() == 0) {
					errorMsgs.add("產權不可空白");
				}
				String hou_parkspace = req.getParameter("hou_parkspace").trim();
				if (hou_parkspace == null || hou_parkspace.trim().length() == 0) {
					errorMsgs.add("車位不可空白");
				}
				String hou_cook = req.getParameter("hou_cook").trim();
				if (hou_cook == null || hou_cook.trim().length() == 0) {
					errorMsgs.add("開火不可空白");
				}
				String hou_managefee = req.getParameter("hou_managefee").trim();
				if (hou_managefee == null || hou_managefee.trim().length() == 0) {
					errorMsgs.add("管理費不可空白");
				}
				String hou_address = req.getParameter("hou_address").trim();
				String hou_addressReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,200}$";
				if (hou_address == null || hou_address.trim().length() == 0) {
					errorMsgs.add("地址必須填寫(不然怎麼看房)");
				} else if (!hou_address.trim().matches(hou_addressReg)) {
					errorMsgs.add("格式不正確");
				}
				Integer hou_rent = null;
				try {
					hou_rent = new Integer(req.getParameter("hou_rent").trim());
				} catch (NumberFormatException es) {
					errorMsgs.add("租金請填寫數字");
				}
				String hou_note = new String(req.getParameter("hou_note").trim());

				/**********************************/
				// 圖片一
//				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");//除錯標記
				Part hou_f_picture = req.getPart("hou_f_picture");
				byte[] pictf;
				if (hou_f_picture.getSize() == 0) {
					HouseService houSvc = new HouseService();
					HouseVO houVO = houSvc.getOneHouse(hou_id);
					pictf = houVO.getHou_f_picture();
				} else {
//				System.out.println(hou_f_picture);//除錯確認是否hou_f_picture有抓到記憶體位置	
//				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");//除錯標記
					BufferedInputStream buf = new BufferedInputStream(hou_f_picture.getInputStream());
					ByteArrayOutputStream baf = new ByteArrayOutputStream();
					int f;
					byte bf[] = new byte[8192];
					while ((f = buf.read(bf)) != -1) {
						baf.write(bf);
					}
					pictf = baf.toByteArray();
				}
				/************** 圖片一end ********************/
				/**********************************/
				// 圖片二
				Part hou_s_picture = req.getPart("hou_s_picture");
				byte[] picts;
				if (hou_s_picture.getSize() == 0) {
					HouseService houSvc = new HouseService();
					HouseVO houVO = houSvc.getOneHouse(hou_id);
					picts = houVO.getHou_s_picture();
				} else {
					BufferedInputStream bus = new BufferedInputStream(hou_s_picture.getInputStream());
					ByteArrayOutputStream bas = new ByteArrayOutputStream();
					int s;
					byte bs[] = new byte[8192];
					while ((s = bus.read(bs)) != -1) {
						bas.write(bs);
					}
					picts = bas.toByteArray();
				}
				/************** 圖片二end ********************/
				/**********************************/
				// 圖片三
				Part hou_t_picture = req.getPart("hou_t_picture");
				byte[] pictt;
				if (hou_t_picture.getSize() == 0) {
					HouseService houSvc = new HouseService();
					HouseVO houVO = houSvc.getOneHouse(hou_id);
					pictt = houVO.getHou_s_picture();
				} else {
					BufferedInputStream but = new BufferedInputStream(hou_t_picture.getInputStream());
					ByteArrayOutputStream bat = new ByteArrayOutputStream();
					int t;
					byte bt[] = new byte[8192];
					while ((t = but.read(bt)) != -1) {
						bat.write(bt);
					}
					pictt = bat.toByteArray();
				}
				/*************** 圖片三 end *******************/

				HouseVO houVO = new HouseVO();
				houVO.setHou_id(hou_id);
				houVO.setHou_name(hou_name);
				houVO.setHou_type(hou_type);
				houVO.setHou_size(hou_size);
				houVO.setHou_property(hou_property);
				houVO.setHou_parkspace(hou_parkspace);
				houVO.setHou_cook(hou_cook);
				houVO.setHou_managefee(hou_managefee);
				houVO.setHou_address(hou_address);
				houVO.setHou_rent(hou_rent);
				houVO.setHou_note(hou_note);
				houVO.setHou_f_picture(pictf);
				houVO.setHou_t_picture(picts);
				houVO.setHou_s_picture(pictt);
				req.setAttribute("houVO", houVO);

				if (!errorMsgs.isEmpty()) {
					// req.setAttribute("houVO", houVO);

					RequestDispatcher failureView = req.getRequestDispatcher("/back/house/update_hou_input.jsp");
					failureView.forward(req, res);
					return;
				}
				HouseService housSvc = new HouseService();
				houVO = housSvc.updateHouse(hou_id, hou_name, hou_type, hou_size, hou_property, hou_parkspace, hou_cook,
						hou_managefee, hou_address, hou_rent, hou_note// ,null,null,null);
						, pictf, picts, pictt);//

				System.out.println(houVO);
				req.setAttribute("houVO", houVO);
				RequestDispatcher successView = req.getRequestDispatcher("/back/house/listOneHouse.jsp");
				successView.forward(req, res);
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.add("update失敗" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back/house/update_hou_input.jsp");
				failureView.forward(req, res);
			}
		}
		// **********************update end

	}
}
