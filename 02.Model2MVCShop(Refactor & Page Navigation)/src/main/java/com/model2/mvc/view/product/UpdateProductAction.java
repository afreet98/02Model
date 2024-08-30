package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;


public class UpdateProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		
		Product product=new Product();
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));//АЁАн
		product.setFileName(request.getParameter("fileName"));
		
		
		ProductService service=new ProductServiceImpl();
		service.updateProduct(product);
		
		/*HttpSession session=request.getSession();
		String sessionId=String.valueOf(((ProductVO)session.getAttribute("product")).getProdNo());
	
		
		
		if(sessionId.equals(prodNo)){
			session.setAttribute("product", productVO);          
		}
		*/
		
		
		
		return "/getProduct.do?prodNo="+prodNo+"&menu=ok";
	}
}