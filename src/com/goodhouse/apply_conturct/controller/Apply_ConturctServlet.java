package com.goodhouse.apply_conturct.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodhouse.house.model.*;
import com.goodhouse.member.model.*;
import com.goodhouse.ele_contract.model.*;
import com.goodhouse.landlord.model.*;
import com.goodhouse.apply_conturct.model.*;

public class Apply_ConturctServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		HttpSession session = req.getSession();
		
		
		//房東查看全部的合約處理列表
		if("lanApply_conturctListAll".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***1接收請求參數***********/
				MemVO lanMemVO = (MemVO)session.getAttribute("mVO");
				
				String lanMem_id = lanMemVO.getMem_id();
				
				/****2開始查詢資料****************/
				HouseService houSvc = new HouseService();
				LanService lanSvc = new LanService();
				MemService mSvc = new MemService();
				String lan_id = null;
				//先取出房東編號
				for(LanVO lanVO : lanSvc.getAll()) {
					if(lanVO.getMem_id().equals(lanMem_id)) {
						lan_id = lanVO.getLan_id();
					}
				}
				
				//找到該房東所屬的房屋編號
				List<Apply_ConturctVO> applyConturctList = new ArrayList<Apply_ConturctVO>();
				Apply_ConturctService applyConturctSvc = new Apply_ConturctService();
				String hou_id = null;
				for(HouseVO houVO : houSvc.getAll()) {
					if(houVO.getLan_id().equals(lan_id)) {
						hou_id = houVO.getHou_id();
						System.out.println("hou_id = " + hou_id);
					}
				}
				
				//找到該房屋所屬的申請合約處理編號
				applyConturctList = applyConturctSvc.getApplyListByHou_id(hou_id);
				System.out.println("applyConturctList = " + applyConturctList);
				
				if(applyConturctList.isEmpty()) {
					errorMsgs.add("無資料");
				}
				
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failure = req.getRequestDispatcher("lan_select_page.jsp");
					failure.forward(req, res);
					return;
				}
				
				/****3查詢完成準備轉交****************/
				req.getSession().setAttribute("applyConturctList", applyConturctList);
				RequestDispatcher success = req.getRequestDispatcher("/front/ele_contract/lanApply_conturct_listAll.jsp");
				success.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failure = req.getRequestDispatcher("lan_select_page.jsp");
				failure.forward(req, res);
			}
			
		}
	}
}
