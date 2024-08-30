package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;


public class AddProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		Product product=new Product();
		product.setProdName(request.getParameter("prodName"));//상품명
		product.setProdDetail(request.getParameter("prodDetail"));//상품상세정보
		product.setManuDate(request.getParameter("manuDate"));//제조일자
		product.setPrice(Integer.parseInt(request.getParameter("price")));//가격		
		product.setFileName(request.getParameter("fileName"));//상품이미지

		System.out.println(product);
		
		ProductService service=new ProductServiceImpl();
		service.addProduct(product);
		
		return "redirect:/product/addProductView.jsp";
		//return "redirect:/user/loginView.jsp";
	}
}