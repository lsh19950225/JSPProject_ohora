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
public class AddressDTO {
	
	int addr_id; // 주소 id
	int user_id;
	private String addr_nick; // 예) 학원, 집
	private String addr_name; // 이름
	private String addr_htel; // 집 번호
	private String addr_tel; // 휴대폰 번호
    private String addr_address_main;  // 주소
    private String addr_address_detail; // 상세 주소
    private String addr_zipcode; // 우편번호
    private String addr_main; // 메인으로 쓰일 주소 Y / N
	
} // class
