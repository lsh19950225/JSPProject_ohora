package ohora.persistence;

import java.util.ArrayList;

import ohora.domain.AddressDTO;
import ohora.domain.MyPageDTO;

public interface AddressDAO {
	
	// address 이름 부분 처리 / myPageDTO 쓴 이유 : 똑같아서
	ArrayList<MyPageDTO> addressName(Integer userId);
	
	// address 주소 부분 동적 select
	ArrayList<AddressDTO> addressSel(Integer userId);
	
	// address 주소 메인 처리
	boolean addressMain(int addrId, int userId);
	
	// address 삭제 버튼 처리
	boolean addressDelBtn(String[] addrIdsStr);
	
} // interface
