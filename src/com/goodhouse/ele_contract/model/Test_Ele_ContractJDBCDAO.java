package com.goodhouse.ele_contract.model;

public class Test_Ele_ContractJDBCDAO {

	public static void main(String[] args) {
		
		Ele_ContractJDBCDAO dao = new Ele_ContractJDBCDAO();
		
		Ele_ContractVO ecVO = new Ele_ContractVO();
		
		//新增
		ecVO.setCon_id("CON0000001");
		ecVO.setMem_id("M000000008");
		ecVO.setMem_idnumber("P123556789");
		ecVO.setLan_id("L000000008");
		ecVO.setLan_idnumber("Q123556789");
		ecVO.setHou_id("HOU0000008");
		ecVO.setEle_rent_money(5000);
		ecVO.setEle_deposit_money(10000);
		ecVO.setEle_rent_time(12);
		ecVO.setEle_rent_f_day(new java.sql.Date(System.currentTimeMillis()));
		ecVO.setEle_rent_l_day(new java.sql.Date(System.currentTimeMillis()));
		ecVO.setEle_singdate(new java.sql.Date(System.currentTimeMillis()));
		ecVO.setEle_con_status("S2已簽約");
		ecVO.setBill_paymenttype("P1月繳");
		ecVO.setEle_con_note("");
		
		dao.insert(ecVO);
	}

}
