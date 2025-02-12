package ohora.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class orderDetailDTO {
	
	private String ord_id;
	private Date ord_orderdate;
	private String ord_name;
	private String opdt_state;
	private int ord_total_amount;
	private int discount;
	private int amount;
	private String ord_pay_option;
	//
	private String opdt_name;
	private int opdt_count;
	private String pdt_img_url;
	private String pdt_name;
	private int pdt_amount;
	private int discount_price;
	//
	private String addr_name;
    private String addr_zipcode;
    private String addr_address_main;
    private String addr_htel;
    private String addr_tel;
	
} // class
