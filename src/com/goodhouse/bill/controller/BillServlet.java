package com.goodhouse.bill.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodhouse.apply_conturct.model.Apply_ConturctVO;
import com.goodhouse.bill.model.BillService;
import com.goodhouse.bill.model.BillVO;
import com.goodhouse.ele_contract.model.Ele_ContractService;
import com.goodhouse.ele_contract.model.Ele_ContractVO;
import com.goodhouse.landlord.model.LanService;
import com.goodhouse.landlord.model.LanVO;
import com.goodhouse.member.model.MemService;
import com.goodhouse.member.model.MemVO;

public class BillServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		doPost(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		HttpSession session = req.getSession();
		
		//TODO 前台帳單編號單一查詢(房客)
		if("bill_getOne_mem".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/****1接收請求參數******************/
				//帳單編號
				String bill_id = req.getParameter("bill_id");
				if(bill_id == null || bill_id.trim().length() == 0) {
					errorMsgs.add("帳單編號不能空白，請重新輸入");
				}
				// 如 ~ 帳單編號：20190226-B00001
				String bill_idReq = "^[0-9]{8}[-]{1}[B]{1}[0-9]{5}$";
				if(!bill_id.trim().matches(bill_idReq)) {
					errorMsgs.add("帳單編號輸入格式 ( 如：20190226-B00001，含 - 共15碼 ) 錯誤，請重新輸入");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/mem_select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/******2準備查詢***********************/
				BillService billSvc = new BillService();
				BillVO billVO = billSvc.getOneB(bill_id);
				
				if(billVO == null) {
					errorMsgs.add("查無資料");
				}
				System.out.println("billVO = " +billVO);
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/mem_select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				
				/******3查詢完成準備轉交************************/
				req.getSession().setAttribute("billVO", billVO);
				String url = "/front/bill/mem_listOne.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料");
				RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/mem_select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		//TODO 前台使用者(房東)：輸入姓名查詢該會員的所有帳單
		if("bill_getBy_mem_name".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/****1接收請求參數**********************/
				String mem_id = null;
				String mem_name = req.getParameter("mem_name");
				if(mem_name == null || mem_name.trim().length() == 0) {
					errorMsgs.add("名字不能空白，請重新輸入");
				}
				
				MemService mSvc = new MemService();
				for(MemVO mVO : mSvc.getAll()) {
					if(mem_name.equals(mVO.getMem_name())){
						mem_id = mVO.getMem_id();
					}
				}
				
				if(mem_id == null) {
					errorMsgs.add("姓名輸入錯誤或無此資料，請重新輸入");
				}
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/lan_select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/*****2開始查詢資料*******************************/
				BillService billSvc = new BillService();
				Ele_ContractService eleConSvc = new Ele_ContractService();
				List<BillVO> billVOlist = new ArrayList();
				String ele_con_id = null;
				String bill_id = null;
				
				//利用mem_id找出該會員的電子合約
				//eleConSvc.getOneEC( billVO.getEle_con_id() ).getMem_id().equals( mSvc.getOneMem(mem_id).mem_id )
//				for(Ele_ContractVO eleConVO : eleConSvc.getAll()) {
//					//把每筆所取出的資料跟使用者做比對
//					if(mem_id.equals(eleConVO.getMem_id())) {
//						//利用找到的電子合約物件與帳單所有物件比對電子合約編號
//						for(BillVO billVO : billSvc.getAll()) {
//							//符合電子合約編號的帳單物件才取出
//							if(eleConVO.getEle_con_id().equals(billVO.getEle_con_id())) {
//								bill_id = billVO.getBill_id();
//								list.add(billVO);
//							}
//						}
//					}
//				}
				//從session取出已登入的該房東會員名稱
				String lan_id = null;
				LanService lanSvc = new LanService();
				String lan_name = session.getAttribute("mem_name").toString();
				
				for(MemVO mVO : mSvc.getAll()) {
					if(lan_name.equals(mVO.getMem_name())) {
						String lan_mem_id = mVO.getMem_id();
						for(LanVO lanVO : lanSvc.getAll()) {
							if(lan_mem_id.equals(lanVO.getMem_id())) {
								lan_id = lanVO.getLan_id();
							}
						}
					}
				}
				
				
				for(BillVO billVO : billSvc.getAll()) {
					//把mem_id跟電子合約的mem_id 及 lan_id 跟電子合約的lan_id 做比對取出該房東底下查的會員的帳單
					if( (eleConSvc.getOneEC( billVO.getEle_con_id() ).getMem_id() ).equals(mem_id) &&  (eleConSvc.getOneEC( billVO.getEle_con_id()).getLan_id()).equals(lan_id) ){
						bill_id = billVO.getBill_id();
						billVOlist.add(billVO);
					}
				}
				
				if(bill_id == null) {
					errorMsgs.add("查無資料");
				}
				
				/*****3查詢完成準備轉交***************************/
				req.setAttribute("lastPage", true);
				req.setAttribute("billVOlist", billVOlist);
				String url = "/front/bill/billList_for_oneMem.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/mem_select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		//TODO 後臺使用者：單一編號查詢
		if("back_getBy_bill_id".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/****1接收請求參數******************/
				//帳單編號
				String bill_id = req.getParameter("bill_id");
				if(bill_id == null || bill_id.trim().length() == 0) {
					errorMsgs.add("帳單編號不能空白，請重新輸入");
				}
				// 如 ~ 帳單編號：20190226-B00001
				String bill_idReq = "^[0-9]{8}[-]{1}[B]{1}[0-9]{5}$";
				if(!bill_id.trim().matches(bill_idReq)) {
					errorMsgs.add("帳單編號輸入格式 ( 如：20190226-B00001，含 - 共15碼 ) 錯誤，請重新輸入");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/bill/back_select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/******2準備查詢***********************/
				BillService billSvc = new BillService();
				BillVO billVO = billSvc.getOneB(bill_id);
				
				if(billVO == null) {
					errorMsgs.add("查無資料");
				}
				
				/******3查詢完成準備轉交************************/
				req.setAttribute("billVO", billVO);
				String url = "/back/bill/back_listOne.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/bill/back_select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料");
				RequestDispatcher failureView = req.getRequestDispatcher("/back/bill/back_select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		//TODO 後台使用者：單一姓名查詢
		if("back_getBy_mem_name".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/****1接收請求參數**********************/
				String mem_id = null;
				String mem_name = req.getParameter("mem_name");
				if(mem_name == null || mem_name.trim().length() == 0) {
					errorMsgs.add("名字不能空白，請重新輸入");
				}
				
				MemService mSvc = new MemService();
				for(MemVO mVO : mSvc.getAll()) {
					if(mem_name.equals(mVO.getMem_name())){
						mem_id = mVO.getMem_id();
					}
				}
				
				if(mem_id == null) {
					errorMsgs.add("姓名輸入錯誤或無此資料，請重新輸入");
				}
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back/bill/back_select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/*****2開始查詢資料*******************************/
				BillService billSvc = new BillService();
				Ele_ContractService eleConSvc = new Ele_ContractService();
				List<BillVO> list = new ArrayList();
				String ele_con_id = null;
				String bill_id = null;
				
				//利用mem_id找出該會員的電子合約
				//eleConSvc.getOneEC( billVO.getEle_con_id() ).getMem_id().equals( mSvc.getOneMem(mem_id).mem_id )
//				for(Ele_ContractVO eleConVO : eleConSvc.getAll()) {
//					//把每筆所取出的資料跟使用者做比對
//					if(mem_id.equals(eleConVO.getMem_id())) {
//						//利用找到的電子合約物件與帳單所有物件比對電子合約編號
//						for(BillVO billVO : billSvc.getAll()) {
//							//符合電子合約編號的帳單物件才取出
//							if(eleConVO.getEle_con_id().equals(billVO.getEle_con_id())) {
//								bill_id = billVO.getBill_id();
//								list.add(billVO);
//							}
//						}
//					}
//				}
				for(BillVO billVO : billSvc.getAll()) {
					//把mem_id跟電子合約的mem_id做比對取出所屬的帳單
					if( (eleConSvc.getOneEC( billVO.getEle_con_id() ).getMem_id() ).equals(mem_id) ){
						bill_id = billVO.getBill_id();
						list.add(billVO);
					}
				}
				
				if(bill_id == null) {
					errorMsgs.add("查無資料");
				}
				
				/*****3查詢完成準備轉交***************************/
				req.setAttribute("billList", list);
				String url = "/back/bill/back_billList_for_oneMem.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back/bill/back_select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		//TODO 房客的全部帳單列表
		if("billForMemListAll".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***1接收請求參數************************/
				MemVO mVO = (MemVO) session.getAttribute("mVO");
				String mem_id = mVO.getMem_id();
				
				/****2準備查詢**********************/
				BillService billSvc = new BillService();
				Ele_ContractService eleConSvc = new Ele_ContractService();
				List<Ele_ContractVO> eleConVOList = (List<Ele_ContractVO>) eleConSvc.getAllForEle_ConByMem_id(mem_id);
				List<BillVO> billVOList = new ArrayList<BillVO>();
				String bill_id = null;
				
				Iterator obj = eleConVOList.iterator();
				while(obj.hasNext()) {
					Ele_ContractVO eleConVO = (Ele_ContractVO)obj.next();
					for(BillVO billVO : billSvc.getAll()) {
						if(eleConVO.getEle_con_id().equals(billVO.getEle_con_id())) {
							bill_id = billVO.getBill_id();
							billVOList.add(billVO);
						}
					}
				}
				
				if(billVOList.isEmpty()) {
					errorMsgs.add("無資料");
				}
				/****3查詢完成準備轉交***************/
				req.setAttribute("lastPage", true);
				req.getSession().setAttribute("billVOList", billVOList);
				String url = "/front/bill/mem_listAll_bill.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/mem_select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		//TODO (房東)所有房租帳單
		if("billForLanListAll".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			
			try {
				/****1接收請求參數****************/
				String mem_name = req.getParameter("mem_name");
				
				/******2開始查詢資料*****************/
				String mem_id = null;
				String lan_id = null;
				
				MemService mSvc = new MemService();
				LanService lanSvc = new LanService();
				//取得lan_id
				for(MemVO mVO : mSvc.getAll()) {
					if(mem_name.equals(mVO.getMem_name())) {
						mem_id = mVO.getMem_id();
						for(LanVO lanVO : lanSvc.getAll()) {
							if(mem_id.equals(lanVO.getMem_id())) {
								lan_id = lanVO.getLan_id();
							}
						}
					}
				}
				
				BillService billSvc = new BillService();
				Ele_ContractService eleConSvc = new Ele_ContractService();
				List<Ele_ContractVO> ele_contractForLanList = eleConSvc.getAllForEle_ConByLan_id(lan_id);
				List<BillVO> billVOList = new ArrayList<BillVO>();
				String bill_id = null;
				
				Iterator obj = ele_contractForLanList.iterator();
				while(obj.hasNext()) {
					Ele_ContractVO eleConVO = (Ele_ContractVO)obj.next();
					for(BillVO billVO : billSvc.getAll()) {
						if(eleConVO.getEle_con_id().equals(billVO.getEle_con_id())) {
							bill_id = billVO.getBill_id();
							billVOList.add(billVO);
						}
					}
				}
				
				if(billVOList.isEmpty()) {
					errorMsgs.add("無資料");
				}
				
				/****3查詢完成準備轉交***************/
				req.setAttribute("lastPage", true);
				req.getSession().setAttribute("billVOList", billVOList);
				String url = "/front/bill/lan_listAll_bill.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch(Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front/bill/lan_select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		//新增第一筆帳單
		if("creatBill".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***1接收請求參數********/
				//電子合約編號
				String ele_con_id = req.getParameter("ele_con_id");
				System.out.println(ele_con_id);
				//繳交費用
				Integer bill_pay = Integer.parseInt(req.getParameter("bill_pay"));
				//信用卡錯誤驗證
				try {
					
					String phovisa = null;
					String phovisa0 = req.getParameter("phovisa0");
					String phovisa1 = req.getParameter("phovisa1");
					String phovisa2 = req.getParameter("phovisa2");
					String phovisa3 = req.getParameter("phovisa3");
					Integer test = null;
					test = new Integer(phovisa0);
					phovisa = phovisa0;
					test = new Integer(phovisa1);
					phovisa += phovisa1 ;
					test = new Integer(phovisa2);
					phovisa += phovisa2;
					test = new Integer(phovisa3);
					phovisa += phovisa3;
					if(phovisa.length() != 16) {
						errorMsgs.add("信用卡號碼長度錯誤");
					}
				} catch (Exception e) {
					errorMsgs.add("信用卡號碼為數字");
				}
				//繳交日期
				java.sql.Date bill_date = Date.valueOf(req.getParameter("bill_date"));
				//帳單產生時間
				java.sql.Date bill_producetime = Date.valueOf(req.getParameter("bill_producetime"));
				//帳單繳費狀態
				String bill_status = req.getParameter("bill_status");
				//付款方式
				String bill_paymethod = req.getParameter("bill_paymethod");
				//繳費型態
				String bill_paymenttype = req.getParameter("bill_paymenttype");
				
				
				BillVO billVO = new BillVO();
				billVO.setEle_con_id(ele_con_id);
				billVO.setBill_pay(bill_pay);
				billVO.setBill_date(bill_date);
				billVO.setBill_producetime(bill_producetime);
				billVO.setBill_status(bill_status);
				billVO.setBill_paymethod(bill_paymethod);
				billVO.setBill_paymenttype(bill_paymenttype);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("billVO", billVO); // 含有輸入格式錯誤的billVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front/bill/creatFirstBill.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/**2準備新增**********/
				BillService billSvc = new BillService();
				billSvc.addB(billVO);
				
				//繳費完改變電子合約狀態
				Ele_ContractService eleConSvc = new Ele_ContractService();
				Ele_ContractVO eleConVO = eleConSvc.getOneEC(ele_con_id);
				eleConVO.setEle_con_id(eleConVO.getEle_con_id());
				eleConVO.setCon_id(eleConVO.getCon_id());
				eleConVO.setMem_idnumber(eleConVO.getMem_idnumber());
				eleConVO.setLan_id(eleConVO.getLan_id());
				eleConVO.setLan_idnumber(eleConVO.getLan_idnumber());
				eleConVO.setHou_id(eleConVO.getHou_id());
				eleConVO.setEle_rent_money(eleConVO.getEle_rent_money());
				eleConVO.setEle_deposit_money(eleConVO.getEle_deposit_money());
				eleConVO.setEle_rent_time(eleConVO.getEle_rent_time());
				eleConVO.setEle_rent_f_day(eleConVO.getEle_rent_f_day());
				eleConVO.setEle_rent_l_day(eleConVO.getEle_rent_l_day());
				eleConVO.setEle_singdate(eleConVO.getEle_singdate());
				
				if(bill_status.equals("s3")) {
					eleConVO.setEle_con_status("s2");
				}
				
				eleConVO.setBill_paymenttype(eleConVO.getBill_paymenttype());
				eleConVO.setEle_con_note(eleConVO.getEle_con_note());
				req.setAttribute("lastPage", true);
				

				/*******3新增成功，準備轉交****************************/
				MemVO mVO = (MemVO) session.getAttribute("mVO");
				List<Ele_ContractVO> eleConVOList = (List<Ele_ContractVO>) eleConSvc.getAllForEle_ConByMem_id(mVO.getMem_id());
				List<BillVO> billVOList = new ArrayList<BillVO>();
				String bill_id = null;
				Iterator obj = eleConVOList.iterator();
				while(obj.hasNext()) {
					Ele_ContractVO eleConVO2 = (Ele_ContractVO)obj.next();
					for(BillVO billVO2 : billSvc.getAll()) {
						if(eleConVO2.getEle_con_id().equals(billVO2.getEle_con_id())) {
							bill_id = billVO2.getBill_id();
							billVOList.add(billVO);
						}
					}
				}
				/****3查詢完成準備轉交***************/
				req.setAttribute("lastPage", true);
				req.setAttribute("ele_contractForMemList", billVOList);
				String url = "/front/ele_contract/mem_listAll_ele_contract.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
				
			} catch(Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failure = req.getRequestDispatcher("/front/bill/creatFirstBill.jsp");
				failure.forward(req, res);
			}
		}
	}
}
